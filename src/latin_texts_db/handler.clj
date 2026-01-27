(ns latin-texts-db.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [ring.util.response :as resp]
            [latin-texts-db.texts :as texts]
            [latin-texts-db.db :as db]
            [latin-texts-db.bulk-verb-insert :as bulk-verb-insert]))

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
  (GET "/text/glossary" [text-id] (texts/generate-glossary-for-text text-id))
  (POST "/token/update" {body :body}
    (let [{:keys [token-id field value]} body]
      (db/update-token-field! token-id field value)
      (resp/response {:data (str (db/get-token token-id))})))
  (POST "/token/set-meaning" {body :body}
    (let [{:keys [token-id meaning-id]} body]
      (println "set: " token-id meaning-id)
      (db/set-meaning-for-token! token-id meaning-id)
      (resp/response {:data (str (db/get-token token-id))})))
  (POST "/token/unset-meaning" {body :body}
    (let [{:keys [token-id]} body]
      (println "unset: " token-id)
      (db/unset-meaning-for-token! token-id)
      (resp/response {:data (str (db/get-token token-id))})))
  (GET "/lexeme-with-meanings" [dictionary-form]
    (resp/response
     (db/load-lexeme-with-all-associated-meanings dictionary-form)))
  (GET "/lexemes" []
    (resp/response (db/get-lexemes)))
  (POST "/meaning/create" {body :body}
    ;; TODO check body for validity
    ;; (let [meaning-id (db/insert-mearning! body)]
    ;;   (resp/response {:data (str (db/id->meaning meaning-id))}))
    (let [{:keys [lexeme-dictionary-form meaning]} body
          inserted-meaning (db/insert-meaning! lexeme-dictionary-form meaning)]
      (resp/response {:data inserted-meaning})))
  (POST "/bulk-insert/verb" {body :body}
    ;; TODO check body for validity
    (let [{:keys [principal-parts first-person-present-gloss third-person-present-gloss third-person-perfect-gloss present-participle-gloss perfect-passive-participle-gloss]} body]
      (bulk-verb-insert/insert-single-verb-from-args! [principal-parts first-person-present-gloss third-person-present-gloss third-person-perfect-gloss perfect-passive-participle-gloss present-participle-gloss])
      (resp/response "success")))
  (route/not-found "Not Found"))

(def app
  (-> app-routes
      (wrap-json-body {:keywords? true})
      wrap-json-response
      (wrap-defaults (assoc-in site-defaults [:security :anti-forgery] false))))


