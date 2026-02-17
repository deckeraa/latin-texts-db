(ns latin-texts.migrations.antecedent-english-gender
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
         (h/add-column :antecedent_english_gender :text))])))


