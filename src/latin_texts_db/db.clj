(ns latin-texts-db.db
  (:require [next.jdbc :as jdbc]
            [migratus.core :as migratus]
            [honey.sql :as sql]
            [honey.sql.helpers :as h]
            [latin-texts-db.migrations.basic-tables]))

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

(defn ll "lookup/load lexeme" [dictionary-form]
  (let [lexeme-id (-> (do! {:select [:lexeme_id]
                            :from :lexemes
                            :where [:= :dictionary-form dictionary-form]})
                      first
                      :lexemes/lexeme_id)]
    (if lexeme-id
      lexeme-id
      ;; else insert a new lexeme
      (-> (do! {:insert-into [:lexemes]
                :values [{:dictionary-form dictionary-form}]
                :returning :lexeme_id})
          first
          :lexemes/lexeme_id))))

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

(defn id->meaning* [meaning-or-meaning-id]
  (if (map? meaning-or-meaning-id)
    meaning-or-meaning-id
    (-> (do! {:select [:*]
              :from :meanings
              :where [:= :meaning_id meaning-or-meaning-id]})
        first)))

(defn id->token [token-or-token-id]
  (if (map? token-or-token-id)
    token-or-token-id
    (-> (do! {:select [:*]
              :from :tokens
              :where [:= :token_id token-or-token-id]})
        first)))

(defn get-lexeme-for-meaning [meaning-or-meaning-id]
  (let [lexeme-id (:meanings/lexeme_id (id->meaning* meaning-or-meaning-id))
        lexeme (first (do! {:select [:*]
                            :from :lexemes
                            :where [:= :lexeme_id lexeme-id]}))]
    lexeme))

(defn id->meaning [meaning-or-meaning-id]
  (as-> meaning-or-meaning-id $
    (id->meaning* $)
    (assoc $ :lexeme (get-lexeme-for-meaning $))))

(defn token->meaning [token-or-token-id]
  (let [token (id->token token-or-token-id)]
    (when (and token (:tokens/meaning_id token))
      (id->meaning (:tokens/meaning_id token)))))

(defn get-lexeme-for-token [token-or-token-id]
  (let [meaning-id (:tokens/meaning_id (id->token token-or-token-id))
        
        lexeme (get-lexeme-for-meaning meaning-id)]
    lexeme))

(defn get-potential-meanings-of-wordform [wordform]
  (let [potential-meanings (do! {:select [:*]
                                 :from :meanings
                                 :where [:= :wordform (clojure.string/lower-case wordform)]})]
    (mapv (fn [meaning]
            (assoc meaning :lexeme (get-lexeme-for-meaning meaning))) potential-meanings)))

(defn decorate-token [token]
  (as-> token $
    (if-let [v (:tokens/meaning_id $)]
      (let [meaning (id->meaning v)]
        (assoc $ :meaning
               (assoc meaning
                      :lexeme
                      (get-lexeme-for-meaning meaning))))
      $)
    (assoc $ :potential-meanings
           (get-potential-meanings-of-wordform
            (:tokens/wordform token)))
    (assoc $ :lexeme (get-lexeme-for-token token)))
  )

(defn get-token [token-id]
  (let [token (-> (do! {:select [:*]
                        :from :tokens
                        :where [:= :token_id token-id]})
                  first)]
    (decorate-token token)))

(defn set-meaning-for-token! [token-id meaning-id]
  (let [meaning (first (do! {:select [:meaning_id]
                             :from :meanings
                             :where [:= :meaning_id meaning-id]}))]
    (if meaning
      (do! {:update :tokens
            :set {:meaning_id meaning-id}
            :where [:= :token_id token-id]})
      (println "meaning " meaning-id " not found in db."))))

(defn unset-meaning-for-token! [token-id]
  (println "In unset-meaning-for-token!" token-id)
  (do! {:update :tokens
        :set {:meaning_id nil}
        :where [:= :token_id token-id]}))
