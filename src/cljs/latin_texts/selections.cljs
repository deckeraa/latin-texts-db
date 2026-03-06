(ns latin-texts.selections
  (:require [reagent.core :as r]
            [reagent.dom.client :as rd-client]
            [cljs.core.async :refer [go <! chan put!]]
            [cljs-bean.core :refer [->clj]]
            [promesa.core :as p]
            [cljs.reader :as reader]
            [cognitect.transit :as t]
            [latin-texts.cursors :as c :refer [app-state selections-cursor set-selections text-id-cursor]]
            [latin-texts.text-selector :as text-selectoro]
            ))

(defn fetch-selections []
  (-> (js/fetch (str "/text/selections?text-id=" @text-id-cursor))
      (.then #(.text %))
      (.then (fn [v]
               (let [selections (reader/read-string v)]
                 (set-selections selections))
               ))))

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
            (let [selection (reader/read-string (:data (->clj v)))]
              (println "selection: " selection)
              (c/append-selection selection))
            )))
  )

(defn create-selection-using-current-selection []
  (create-selection {:text-id @c/text-id-cursor
                     :start-token-id @c/selection-start-cursor
                     :end-token-id @c/selection-end-cursor
                     :label "A selection"
                     :color "00ff00"}))

(defn selection-viewer []
  [:span
   ;; (str @selected-tokens-ids)
   ;; (str (doall (map :tokens/wordform @selected-tokens)))
   (str (:tokens/wordform (c/selection-start-token)))
   "->"
   (str (:tokens/wordform (c/selection-end-token)))
   ])

(defn get-text-range [text-id start-id end-id callback-fn]
  (-> (js/fetch (str "/text/range?text-id=" text-id
                     "&start-id=" start-id
                     "&end-id=" end-id))
      (.then #(.text %))
      (.then callback-fn)))

(defn get-glossary-range [text-id start-id end-id callback-fn]
  (-> (js/fetch (str "/text/glossary/range?text-id=" text-id
                     "&start-id=" start-id
                     "&end-id=" end-id))
      (.then #(.text %))
      (.then callback-fn)))

(defn selection-component [selection]
  (let [color (or (:selections/color selection) "black")]
    [:li {:style {:color color}
          :title (str selection)}
     (str
      (:selections/label selection)
      " "
      (-> selection
          :selections/start_token_id
          c/token-by-id
          :tokens/wordform)
      " -> "
      (-> selection
          :selections/end_token_id
          c/token-by-id
          :tokens/wordform))
     [:button {:on-click
               (fn [e]
                 (get-text-range
                  (:selections/text_id selection)
                  (:selections/start_token_id selection)
                  (:selections/end_token_id selection)
                  (fn [v] (println "selection text: " v)
                    (-> (js/navigator.clipboard.writeText v)
                        (.catch #(js/console.error "Clipboard copy failed:" %)))
                    )))}
      "Text"]
     [:button {:on-click
               (fn [e]
                 (get-glossary-range
                  (:selections/text_id selection)
                  (:selections/start_token_id selection)
                  (:selections/end_token_id selection)
                  (fn [v] (println "glossary text: " v)
                    (-> (js/navigator.clipboard.writeText v)
                        (.catch #(js/console.error "Clipboard copy failed:" %)))
                    )))}
      "Glossary"]]))

(defn selections-component []
  [:div
   [:h4 "Selections"]
   ;; [:div {} (str @selections-cursor)]
   (into [:ul {}]
         (map (fn [selection]
                ^{:key (:selection/selection_id selection)}
                [selection-component selection])
              @selections-cursor))
   [:button {:on-click create-selection-using-current-selection}
    "Save current selection"]
   [:button {:on-click fetch-selections}
    "Fetch selections"]])
