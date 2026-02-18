(ns latin-texts.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [ring.util.response :as resp]
            [cognitect.transit :as transit]
            [ring.middleware.format :refer [wrap-restful-format]]
            [latin-texts.texts :as texts]
            [latin-texts.db :as db]
            [latin-texts.bulk-verb-insert :as bulk-verb-insert]
            [latin-texts.bulk-noun-insert :as bulk-noun-insert]
            [latin-texts.bulk-adj-insert :as bulk-adj-insert]))

(defn get-text-as-string [text-id]
  (let [s (texts/get-text-as-string text-id 5000)]
    (if s s
        (str "Text not found: " text-id " " (type text-id) "\n"))))

(defn get-text-as-edn [{:keys [text-id n start-id] :as args}]
  (let [v (texts/get-text-edn args)]
    (if v (str v)
        (str "Text not found: " text-id " " (type text-id) "\n"))))

(defroutes app-routes
  (GET "/" [] (ring.util.response/resource-response "index.html" {:root "public"}))
  (route/resources "/") ;; serves /js/compiled/main.js etc.
  (GET "/text-as-string" [text-id] (get-text-as-string text-id))
  (GET "/text" [text-id start-id n]
    (let [n (or (int (parse-double n))
                5000)]
      (get-text-as-edn {:text-id text-id :n n :start-id start-id})))
  (GET "/text/glossary" [text-id] (texts/generate-glossary-for-text text-id))
  (POST "/token/update-field" {body :body}
    (let [{:keys [token-id field value]} body]
      (db/update-token-field! token-id field value)
      (resp/response {:data (str (db/get-token token-id))})))
  (POST "/token/set-meaning" {body :body}
    (let [{:keys [token-id meaning-id]} body]
      (println "set: " token-id meaning-id)
      (db/set-meaning-for-token! token-id meaning-id)
      (resp/response {:data (str (db/get-token token-id))})))
  (POST "/token/set-antecedent-english-gender" {body :body}
    (let [{:keys [token-id gender]} body
          gender (if (= "nil" gender) nil gender)]
      (db/update-token-field! token-id :antecedent_english_gender gender)
      (resp/response {:data (str (db/get-token token-id))})))
  (POST "/token/create-footnote" {body :body}
    (let [{:keys [token-id text]} body]
      (db/do! {:insert-into [:footnotes]
               :values [{:token_id token-id
                         :text text}]})
      (resp/response {:data (str (db/get-token token-id))})))
  (POST "/token/update-footnote" {body :body}
    (let [footnote (clojure.edn/read-string body) ;; TODO use transit instead
          token-id (:footnotes/token_id footnote)]
      (println "footnote: " footnote)
      (println "token-id: " token-id)
      (db/do! {:update :footnotes
               :set footnote
               :where [:= :footnote_id (:footnotes/footnote_id footnote)]})
      (resp/response {:data (str (db/get-token token-id))})))
  (POST "/token/delete-footnote" {body :body}
    (let [{:keys [footnote-id]} body
          footnote (db/id->footnote footnote-id)
          token-id (:footnotes/token_id footnote)]
      (db/do! {:delete-from [:footnotes]
               :where [:= :footnote_id footnote-id]})
      (resp/response {:data (str (db/get-token token-id))})))
  (POST "/token/unset-meaning" {body :body}
    (let [{:keys [token-id]} body]
      (println "unset: " token-id)
      (db/unset-meaning-for-token! token-id)
      (resp/response {:data (str (db/get-token token-id))})))
  (POST "/token/update" {body :body}
    (println "/token/update body: " (type body))
    (let [token (clojure.edn/read-string body)
          token-update (select-keys token [:tokens/punctuation_preceding :tokens/wordform :tokens/punctuation_trailing])
          token-id (:tokens/token_id token)]
      (db/do! {:update :tokens
               :set token-update
               :where [:= :token_id token-id]})
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
  (POST "/meaning/update" {body :body}
    (println "/meaning/update body: " (type body))
    (let [meaning (clojure.edn/read-string body) ;; TODO use transit instead
          _ (println meaning)
          meaning-id (:meanings/meaning_id meaning)]
      (db/do! {:update :meanings
               :set meaning
               :where [:= :meaning_id meaning-id]})
      (resp/response {:data "todo"})))
  (POST "/bulk-insert/verb" {body :body}
    ;; TODO check body for validity
    (let [{:keys [principal-parts first-person-present-gloss third-person-present-gloss third-person-perfect-gloss present-participle-gloss perfect-passive-participle-gloss] :as args} body]
      (bulk-verb-insert/insert-single-verb-from-args!
       {:dictionary-form principal-parts
        :first-person-present-sg-gloss first-person-present-gloss
        :third-person-present-sg-gloss third-person-present-gloss
        :first-person-perfect-sg-gloss third-person-perfect-gloss
        :present-participle present-participle-gloss
        :perfect-participle perfect-passive-participle-gloss})
      (resp/response "success")))
  (POST "/bulk-insert/noun" {body :body}
    ;; TODO check body for validity
    (let [args body]
      (bulk-noun-insert/insert-noun-meanings! args) 
      (resp/response "success")))
  (POST "/bulk-insert/adjective" {body :body}
    ;; TODO check body for validity
    (let [args body]
      (bulk-adj-insert/insert-adj-meanings! args) 
      (resp/response "success")))
  (GET "/texts" []
    (resp/response (db/get-texts)))

  (route/not-found "Not Found"))

(def app
  (-> app-routes
      (wrap-json-body
       {:keywords? true
        :predicate
        (fn [req]
          (let [ct (get-in req [:headers "content-type"] "")]
            (println "ct: " ct)
            (and (string? ct)
                 (re-find #"(?i)application/(.*\+)?json" ct)
                 (not (re-find #"(?i)transit" ct)))))
        })
      ;; (wrap-restful-format :formats [:transit-json])
      wrap-json-response
      (wrap-defaults (assoc-in site-defaults [:security :anti-forgery] false))))


