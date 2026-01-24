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

(defn get-potential-meanings-of-wordform [wordform]
  (let [potential-meanings (do! {:select [:*]
                                 :from :meanings
                                 :where [:= :wordform (clojure.string/lower-case wordform)]})]
    potential-meanings))

(defn get-token [token-id]
  (let [token (-> (do! {:select [:*]
                        :from :tokens
                        :where [:= :token_id token-id]})
                  first)]
    (if (nil? (:tokens/meaning_id token))
      (assoc token :potential-meanings
             (get-potential-meanings-of-wordform
              (:tokens/wordform token)))
      token)))

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
