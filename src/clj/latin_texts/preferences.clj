(ns latin-texts.preferences
  (:require [latin-texts.db :as db]))

(defn autostart-text []
  (let [user-id 1 ;; TODO pull from login cookie
        pref-row
        (->
         (db/do! {:select [:*]
                  :from :preference_autostart_text
                  :where [:= user-id :preference_autostart_text/user_id]})
         first)]
    (if-let [start-token-id (:preference_autostart_text/start_token_id pref-row)]
      {:text-id (db/token->text start-token-id)
       :start-token-id start-token-id}
      {:text-id (:preference_autostart_text/text_id pref-row)})))

(defn set-autostart-text [text-id]
  (let [user-id 1 ;; TODO pull from login cookie
        ]
    (db/do! {:update :preference_autostart_text
             :set {:text_id text-id}
             :where [:= :user_id user-id]})))
