(ns latin-texts.db
  (:require [next.jdbc :as jdbc]
            [migratus.core :as migratus]
            [honey.sql :as sql]
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

;; Example insert
;; (defn insert-token! [token-map]
;;   (sql/insert! ds :tokens token-map))
