(ns latin-texts.app
  (:require [reagent.core :as r]
            [reagent.dom.client :as rd-client]
            [cljs.core.async :refer [go <! chan put!]]
            [cljs-bean.core :refer [->clj]]
            [promesa.core :as p]
            [cljs.reader :as reader]
            ))

(defonce app-state (r/atom {}))

(defn fetch-text [text-id]
  (-> (js/fetch (str "/text?text-id=" text-id))
      (.then (fn [v]
               (println v)
               (.text v)
               ))))

(defn current-token []
  (:current-token @app-state))

(defn current-token-id []
  (:tokens/token_id (current-token)))

(defn set-current-token! [token]
  (swap! app-state assoc :current-token token))

(defn set-text! [tokens]
  (swap! app-state assoc :current-text-tokens-by-id
         (into {} 
               (map (fn [token]
                      {(:tokens/token_id token) token})
                    tokens)))
  (swap! app-state assoc :text-as-list
         (mapv :tokens/token_id tokens))
  (swap! app-state assoc :text tokens))

(defn get-text-as-list []
  (:text-as-list @app-state))

(defn current-text-tokens-by-id []
  (:current-text-tokens-by-id @app-state))

(defn token-by-id [id]
  (get (current-text-tokens-by-id) id))

(defn update-token [new-token]
  (swap! app-state assoc-in [:current-text-tokens-by-id (:tokens/token_id new-token)] new-token))

(defn set-meaning [token-id meaning-id]
  (->
   (js/fetch
    "/token/set-meaning"
    #js {:method "POST"
         :headers #js {"Content-Type" "application/json"}
         :body (js/JSON.stringify
                #js {:token-id token-id
                     :meaning-id meaning-id})})
   (.then (fn [v]
            (println v)
            (.json v)))
   (.then (fn [v]
            (println "2nd then: " v (type v))
            (println "2.5: " (->clj v))
            (println "3rd: " (reader/read-string (get v "data")))
            (update-token (reader/read-string (:data (->clj v))))))))

(defn unset-meaning [token-id]
  (->
   (js/fetch
    "/token/unset-meaning"
    #js {:method "POST"
         :headers #js {"Content-Type" "application/json"}
         :body (js/JSON.stringify
                #js {:token-id token-id})})
   (.then (fn [v]
            (println v)
            (.json v)))
   (.then (fn [v]
            (println "2nd then: " v (type v))
            (println "2.5: " (->clj v))
            (println "3rd: " (reader/read-string (get v "data")))
            (update-token (reader/read-string (:data (->clj v))))
            ))))

(defn token-color [token]
  "purple")

(defn token-bg-color [token]
  (when-let [id (:tokens/token_id token)]
    (when (= id (current-token-id))
      "yellow")))

(defn text-fetcher-component []
  [:div
   [:button {:on-click
             (fn []
               (-> (fetch-text 1)
                   (p/then (fn [result]
                             (set-text! (reader/read-string result))
                             ;; (swap! app-state assoc :text (reader/read-string result))
                             ))
                   (p/catch (fn [err]
                              (println err)))))}
    "Fetch"]
   (into [:<>]
         (map (fn [token-id]
                (let [token (token-by-id token-id)]
                  ^{:key (:tokens/token_id token)}
                  [:span
                   {:style {:color (token-color token)
                            :background-color (token-bg-color token)
                            :margin-right "6px"}
                    :on-click #(set-current-token! token)}
                   (str
                    (:tokens/punctuation_preceding token)
                    (:tokens/wordform token)
                    (:tokens/punctuation_trailing token))]))
              (get-text-as-list)
                                        ;(:text @app-state)
              ))
   ])

(defn vocab-str-for-noun [meaning]
  (str (:meanings/wordform meaning)
       ": "
       (:meanings/gloss meaning)
       "; "
       (:meanings/number meaning)
       " "
       (:meanings/gender meaning)
       " "
       (:meanings/case_ meaning)
       " from "
       "TODO lexeme"
       ;; (:meanings/case meaning)
       )
  )

(defn potential-meaning [meaning]
  [:div {}
   (vocab-str-for-noun meaning)])

(defn potential-meanings-picker [token]
  (r/with-let [selection-atom (r/atom nil)]
    (if (:tokens/meaning_id token)
      [:div "Selected meaning: " (:tokens/meaning_id token)
       [:button {:on-click #(unset-meaning
                             (:tokens/token_id token))
                 ;; (-> (unset-meaning 1)
                 ;;   (p/then (fn [result]
                 ;;             (set-text! (reader/read-string result))
                 ;;             ;; (swap! app-state assoc :text (reader/read-string result))
                 ;;             ))
                 ;;   (p/catch (fn [err]
                 ;;              (println err))))
                 }
        "Unset"]]
      [:div {} "Potential meanings"
       (into [:ul]
             (map (fn [meaning]
                    [:li {} [potential-meaning meaning]
                     [:button {:on-click
                               #(set-meaning
                                 (:tokens/token_id token)
                                 (:meanings/meaning_id meaning))
                               ;; (fn []
                                 
                               ;;   (->
                               ;;    (js/fetch
                               ;;     "/token/set-meaning"
                               ;;     #js {:method "POST"
                               ;;          :headers #js {"Content-Type" "application/json"}
                               ;;          :body (js/JSON.stringify
                               ;;                 #js {:token-id   (:tokens/token_id token)
                               ;;                      :meaning-id (:meanings/meaning_id meaning)})})
                               ;;       (.then (fn [v]
                               ;;                (println (.text v))
                               ;;                ;(update-token (reader/read-string v))
                               ;;                ))))
                               }
                      "Set"]])
                  (:potential-meanings token)))
       ])))

(defn current-token-component []
  (let [token (current-token)]
    [:div {} "Current token: " (:tokens/wordform token)
     [potential-meanings-picker token]
     [:div {} token]]))

(defn root-component []
  [:div
   [:h1 "Latin Texts DB"]
   [text-fetcher-component]
   [current-token-component]
   ;; [:div {} (current-text-tokens-by-id)]
   [:div {} @app-state]
   ])

(defonce react-root (atom nil))

(defn render! []
  (let [root-el (js/document.getElementById "app")]
    (when-not @react-root
      (reset! react-root (rd-client/create-root root-el)))
    (rd-client/render @react-root [root-component])))

(defn ^:export init []
  (render!))

(defn ^:dev/before-load-async stop [done]
  (js/console.log "stop")
  (js/setTimeout
    (fn []
      (js/console.log "stop complete")
      (render!)
      (done))))

(defn ^:dev/after-load-async start [done]
  (js/console.log "start")
  (js/setTimeout
    (fn []
      (js/console.log "start complete")
      (render!)
      (done))))
