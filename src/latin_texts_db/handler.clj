(ns latin-texts-db.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [ring.util.response :as resp]
            [latin-texts-db.texts :as texts]
            [latin-texts-db.db :as db]))

(defn get-text-as-string [text-id]
  (let [s (texts/get-text-as-string text-id 5000)]
    (if s s
        (str "Text not found: " text-id " " (type text-id) "\n"))))

(defn get-text-as-edn [text-id]
  (let [v (texts/get-text-edn text-id 5000)]
    (if v (str v)
        (str "Text not found: " text-id " " (type text-id) "\n"))))

(defroutes app-routes
  (GET "/" [] (ring.util.response/resource-response "index.html" {:root "public"}))
  (route/resources "/") ;; serves /js/compiled/main.js etc.
  (GET "/text-as-string" [text-id] (get-text-as-string text-id))
  (GET "/text" [text-id] (get-text-as-edn text-id))
  (POST "/token/set-meaning" {body :body}
  (let [{:keys [token-id meaning-id]} body]
    (db/set-meaning-for-token! token-id meaning-id)
    (resp/response {:status :ok})))
  (route/not-found "Not Found"))

(def app
  (-> app-routes
      (wrap-json-body {:keywords? true})
      wrap-json-response
      (wrap-defaults (assoc-in site-defaults [:security :anti-forgery] false))))


