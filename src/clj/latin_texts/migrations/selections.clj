(ns latin-texts.migrations.selections
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
    [(h/create-table
     :selections
     (h/with-columns
       [:selection_id   :integer :primary-key :autoincrement]
       [:text_id        :integer :references [:texts  :text_id]]
       [:start_token_id :integer :references [:tokens :token_id]]
       [:end_token_id   :integer :references [:tokens :token_id]]
       [:label          :text]
       [:color          :text]
       ))])))
