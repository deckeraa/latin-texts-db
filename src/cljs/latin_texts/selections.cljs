(ns latin-texts.selections
  (:require [reagent.core :as r]
            [reagent.dom.client :as rd-client]
            [cljs.core.async :refer [go <! chan put!]]
            [cljs-bean.core :refer [->clj]]
            [promesa.core :as p]
            [cljs.reader :as reader]
            [cognitect.transit :as t]
            [latin-texts.cursors :refer [app-state selections-cursor set-selections text-id-cursor]]
            ))

(defn fetch-selections []
  (-> (js/fetch (str "/text/selections?text-id=" @text-id-cursor))
      (.then #(.json %))
      (.then (fn [v]
               (println (type v) v)
               (set-selections v)))))

(defn create-selection [{:keys [text-id start-token-id end-token-id label color]}]
  (->
   (js/fetch
    "/text/create-selection"
    #js {:method "POST"
         :headers #js {"Content-Type" "application/json"}
         :body (js/JSON.stringify
                #js {:text-id text-id
                     :start-token-id start-token-id
                     :end-token-id end-token-id
                     :label label
                     :color color})})
   (.then (fn [v]
            (println v)
            (.json v)))
   (.then (fn [v]
            ;; (let [token (reader/read-string (:data (->clj v)))]
            ;;   (update-token token)
            ;;   (when callback-fn
            ;;     (callback-fn token)))
            )))
  )

(defn create-selection-using-current-selection []
  )

(defn selections-component []
  [:div
   [:h4 "Selections"]
   [:div {} (str @selections-cursor)]
   [:button {:on-click create-selection-using-current-selection}
    "Save current selection"]])
