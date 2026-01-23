(ns latin-texts.db
  (:require [next.jdbc :as jdbc]
            [migratus.core :as migratus]
            [honey.sql :as sql]
            [honey.sql.helpers :as h]
            [latin-texts.migrations.basic-tables]))

;; useful HoneySQL ref: https://github.com/seancorfield/honeysql/blob/develop/doc/clause-reference.md

(def db-spec
  {:dbtype "sqlite"
   :dbname "resources/db/latin.db"})   ;; relative to project root

(def config {:store         :database
             :migration-dir "migrations"
             :db            db-spec})

(def ds (jdbc/get-datasource db-spec))

(defn migrate! [] (migratus/migrate config))
(defn rollback! [] (migratus/rollback config))

(defn do! [stmt]
  (try 
    (jdbc/execute! ds (sql/format stmt) {:return-keys true})
    (catch Exception e
      (println "Failed to execute stmt: " stmt)
      (println (sql/format stmt))
      (throw e))))

(defn split-preceding-trailing-punctuation [s]
  (let [[_ lead wordform trail]
        (re-matches #"^(\p{P}+)?(.*?)(\p{P}+)?$" s)]
    {:punctuation_preceding lead
     :wordform wordform
     :punctuation_trailing trail}))

(defn insert-token-into-db* [text-id prev-id tokens]
  (println "On " (first tokens))
  (let [{:keys [:punctuation_preceding :wordform :punctuation_trailing]} (split-preceding-trailing-punctuation (first tokens))
        insert-result (do! {:insert-into [:tokens]
                            :values [{:text_id text-id
                                      :wordform wordform
                                      :prev_token_id prev-id
                                      :punctuation_preceding punctuation_preceding
                                      :punctuation_trailing punctuation_trailing
                                      }]
                            :returning :token_id})
        new-token-id (:tokens/token_id (first insert-result))]
    (do! {:update :tokens
          :set {:next_token_id new-token-id}
          :where [:= :token_id prev-id]})
    (println "new-token-id" new-token-id)
    (when (not (empty? (rest tokens)))
      (insert-token-into-db* text-id new-token-id (rest tokens))
      )))

(defn insert-text! [text-title text-contents-as-string]
  (let [tokens (clojure.string/split text-contents-as-string #" ")
        text-insert-result (do! {:insert-into [:texts]
                                 :values [{:title text-title}]
                                 :returning :text_id})
        text-id (:texts/text_id (first text-insert-result))]
    (when (nil? text-id)
      (throw (new Exception "text-id not found after attempted insert")))
    (println "text-id: " text-id tokens)
    (insert-token-into-db* text-id nil tokens)
    ))

(defn get-text-as-string [text-id n]
  (let [fetched-tokens (atom [])
        first-token (-> (do! {:select [:token_id :wordform :next_token_id :punctuation_preceding :punctuation_trailing]
                              :from :tokens
                              :where [:and
                                      [:= :text-id text-id]
                                      [:= :prev-token-id nil]]})
                        first)
        next-token-id (atom (:tokens/next_token_id first-token))]
    (swap! fetched-tokens conj (str (:tokens/punctuation_preceding first-token)
                                    (:tokens/wordform first-token)
                                    (:tokens/punctuation_trailing first-token)))
    (doall
     (for [x (range 1 n)
           :when @next-token-id]
       (do
         (let [new-token (-> (do! {:select [:token_id :wordform :next_token_id :punctuation_preceding :punctuation_trailing]
                                   :from :tokens
                                   :where [:= :token_id @next-token-id]})
                             first)]
           (swap! fetched-tokens
                  conj
                  (str (:tokens/punctuation_preceding new-token)
                       (:tokens/wordform new-token)
                       (:tokens/punctuation_trailing new-token)))
           (reset! next-token-id (:tokens/next_token_id new-token)))
         )))
    (clojure.string/join " " @fetched-tokens)))

(defn get-noun-forms-first-declension [nom gen gender gloss]
  (let [stem (subs nom 0 (dec (count nom)))]
    [{:wordform (str stem "a") :gloss gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "nominative"}
     {:wordform (str stem "ae") :gloss gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "genitive"}
     {:wordform (str stem "ae") :gloss gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "dative"}
     {:wordform (str stem "am") :gloss gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "accusative"}
     {:wordform (str stem "ā") :gloss gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "ablative"}
     {:wordform (str stem "ae") :gloss gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "nominative"}
     {:wordform (str stem "ārum") :gloss gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "genitive"}
     {:wordform (str stem "īs") :gloss gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "dative"}
     {:wordform (str stem "ās") :gloss gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "accusative"}
     {:wordform (str stem "īs") :gloss gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "ablative"}
     ])
  )

(defn insert-noun-meaning! [meaning-values]
  (let [existing-match (do! {:select [:meaning_id]
                             :from :meanings
                             :where [:and
                                     [:= :wordform (:wordform meaning-values)]
                                     [:= :case_ (name (:case_ meaning-values))]
                                     [:= :number (name (:number meaning-values))]
                                     [:= :gender (name (:gender meaning-values))]]})
        match-id (:meanings/meaning_id (first existing-match))]
    (if match-id
      (println "Meaning is already present in the database: " match-id)
      (do! {:insert-into [:meanings]
            :values [meaning-values]}))))

(defn insert-noun-meanings! [nom gen declension gender gloss]
  (let [meanings (case declension
                   1 (get-noun-forms-first-declension nom gen gender gloss))]
    (doseq [meaning meanings]
      (insert-noun-meaning! meaning)))
  )

(defn get-potential-meanings-of-wordform [wordform]
  (let [potential-meanings (do! {:select [:*]
                                 :from :meanings
                                 :where [:= :wordform wordform]})]
    potential-meanings))
