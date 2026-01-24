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
                             (swap! app-state assoc :text (reader/read-string result))))
                   (p/catch (fn [err]
                              (println err)))))}
    "Fetch"]
   (into [:<>]
         (map (fn [token]
                ^{:key (:tokens/token_id token)}
                [:span
                 {:style {:color (token-color token)
                          :background-color (token-bg-color token)
                          :margin-right "6px"}
                  :on-click #(set-current-token! token)}
                 (str
                  (:tokens/punctuation_preceding token)
                  (:tokens/wordform token)
                  (:tokens/punctuation_trailing token))])
              (:text @app-state)))
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
    [:div {} "Potential meanings"
     (into [:ul]
           (map (fn [meaning]
                  [:li {} [potential-meaning meaning]])
                (:potential-meanings token)))
     ]))

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
   ;; [:div {} @app-state]
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
