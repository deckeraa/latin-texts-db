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

(defn insert-token-into-db* [text-id prev-id tokens]
  (println "On " (first tokens))
  (let [ ;; TODO handle puncutation
        insert-result (do! {:insert-into [:tokens]
                            :values [{:text_id text-id
                                      :wordform (first tokens)
                                      :prev_token_id prev-id
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
