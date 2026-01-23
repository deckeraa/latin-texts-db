(ns latin-texts-db.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [latin-texts-db.texts :as texts]))

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
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))


