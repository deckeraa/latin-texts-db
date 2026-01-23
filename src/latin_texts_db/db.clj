(ns latin-texts.db
  (:require [next.jdbc :as jdbc]
            [migratus.core :as migratus]
            [honey.sql :as sql]
            [honey.sql.helpers :as h]
            [latin-texts.migrations.basic-tables]))

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
  (let [insert-result {:text_id text-id
                       :wordform (first tokens)
                       :prev_token_id prev-id
                       :next_token_id nil ;; TODO
                       }
        new-id :todo]
    (when (not (empty (rest tokens)))
      (insert-token-into-db* new-id (rest tokens))
      ))
  )

(defn insert-text! [text-title text-contents-as-string]
  (let [tokens (clojure.string/split text-contents-as-string #" ")
        text-insert-result (do! {:insert-into [:texts]
                                 :values [{:title text-title}]
                                 :returning :text_id})
        text-id (:texts/text_id (first text-insert-result))]
    (when (nil? text-id)
      (throw (new Exception "text-id not found after attempted insert")))
    (println "text-id: " text-id)
    ;; (insert-token-into-db* nil tokens)
    ))
