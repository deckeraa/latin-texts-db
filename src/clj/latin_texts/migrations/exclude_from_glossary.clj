(ns latin-texts.migrations.exclude-from-glossary
  (:require
   [next.jdbc :as jdbc]
   [honey.sql :as sql]
   [honey.sql.helpers :as h]
   [latin-texts.migrations.basic-tables :refer [ds run-statements!]]))

(defn migrate-up
  ([] (migrate-up ds))
  ([conn]
   (run-statements!
    conn
    [(-> (h/alter-table :tokens)
         (h/add-column :exclude_from_glossary :boolean))])))
