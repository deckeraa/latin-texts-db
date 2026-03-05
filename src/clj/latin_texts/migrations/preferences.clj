(ns latin-texts.migrations.preferences
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
     :users
     (h/with-columns
       [:user_id   :integer :primary-key :autoincrement]
       [:user_name :text]
       ))
     (h/create-table
     :preference_autostart_text
     (h/with-columns
       [:preference_id  :integer :primary-key :autoincrement]
       [:user_id        :integer :references [:users  :user_id]]
       [:text_id :integer :references [:texts :text_id]]
       [:start_token_id   :integer :references [:tokens :token_id]]
       ))])))
