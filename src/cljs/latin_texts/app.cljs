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

(defn current-token-id []
  (:tokens/token_id (:current-token @app-state)))

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

(defn root-component []
  [:div
   [:h1 "Latin Texts DB"]
   [text-fetcher-component]
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
