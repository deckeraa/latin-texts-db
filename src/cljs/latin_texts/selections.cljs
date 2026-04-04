(ns latin-texts.selections
  (:require [reagent.core :as r]
            [reagent.dom.client :as rd-client]
            [cljs.core.async :refer [go <! chan put!]]
            [cljs-bean.core :refer [->clj]]
            [promesa.core :as p]
            [cljs.reader :as reader]
            [cognitect.transit :as t]
            [latin-texts.cursors :as c :refer [app-state selections-cursor set-selections text-id-cursor]]
            [latin-texts.text-selector :as text-selector]
            [latin-texts.ui-components :as ui]
            ))

(defn update-selection [new-selection]
  (swap! c/selections-cursor
         (fn [old-selections]
           (map (fn [selection]
                  (if (= (:selections/selection_id selection)
                         (:selections/selection_id new-selection))
                    new-selection
                    selection))
                old-selections))))

(defn update-selection-field [selection-id field value]
  (->
   (js/fetch
    "/selection/update-field"
    #js {:method "POST"
         :headers #js {"Content-Type" "application/json"}
         :body (js/JSON.stringify
                #js {:selection-id selection-id
                     :field (name field)
                     :value value})})
   (.then (fn [v]
            (println v)
            (.json v)))
   (.then (fn [v]
            (update-selection (reader/read-string (:data (->clj v))))))))

(defn get-text-range [text-id start-id end-id callback-fn]
  (-> (js/fetch (str "/text/range?text-id=" text-id
                     "&start-id=" start-id
                     "&end-id=" end-id))
      (.then #(.text %))
      (.then callback-fn)))

(defn get-footnotes-range [text-id start-id end-id callback-fn]
  (-> (js/fetch (str "/text/footnotes/range?text-id=" text-id
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
                     :color "#00ff00"}))

(defn selection-viewer []
  [:span
   ;; (str @selected-tokens-ids)
   ;; (str (doall (map :tokens/wordform @selected-tokens)))
   (str (:tokens/wordform (c/selection-start-token)))
   "->"
   (str (:tokens/wordform (c/selection-end-token)))
   " ("
   (str @c/selected-tokens-distinct-count)
   ")"
   [:button {:on-click
               (fn [e]
                 (get-text-range
                  @c/text-id-cursor
                  @c/selection-start-cursor
                  @c/selection-end-cursor
                  (fn [v] (println "selection text: " v)
                    (-> (js/navigator.clipboard.writeText v)
                        (.catch #(js/console.error "Clipboard copy failed:" %)))
                    )))}
      "Copy to clipboard"]
   ])

(defn selection-component [selection]
  (let [color (or (:selections/color selection) "black")]
    [:li {:style {:color color}
          :title (str selection)
          :on-mouse-over
          #(c/set-hover-selection
            (:selections/start_token_id selection)
            (:selections/end_token_id selection)
            (:selections/color selection))
          :on-mouse-out #(c/clear-hover-selection)}
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
                 (text-selector/fetch-text!
                  {:text-id (:selections/text_id selection)
                   :app-state app-state
                   :start-id (:selections/start_token_id selection)
                   :end-id (:selections/end_token_id selection)
                   :mode :overwrite})
                 )}
      "Go"]
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
                 (get-footnotes-range
                  (:selections/text_id selection)
                  (:selections/start_token_id selection)
                  (:selections/end_token_id selection)
                  (fn [v] (println "selection text: " v)
                    (-> (js/navigator.clipboard.writeText v)
                        (.catch #(js/console.error "Clipboard copy failed:" %)))
                    )))}
      "Footnotes"]
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

(defn selection-component2 [selection expanded-cursor]
  (r/with-let [selection-state (r/atom selection)]
    (let [color (or (:selections/color selection) "black")]
      [:details.my-2
       {:style
        (merge {:color color
                :padding-left :2px}
               (when @expanded-cursor
                 {:border-left-style :solid
                  :border-left-width :2px}))}
       [:summary
        {:title (str selection)
         :on-mouse-over
         #(c/set-hover-selection
           (:selections/start_token_id selection)
           (:selections/end_token_id selection)
           (:selections/color selection))
         :on-mouse-out #(c/clear-hover-selection)
         :on-click #(swap! expanded-cursor not)}
        (str
         (:selections/label selection)
         " "
         ;; (-> selection
         ;;     :selections/start_token_id
         ;;     c/token-by-id
         ;;     :tokens/wordform)
         ;; " -> "
         ;; (-> selection
         ;;     :selections/end_token_id
         ;;     c/token-by-id
         ;;     :tokens/wordform)
         )
        [:button {:on-click
                  (fn [e]
                    (text-selector/fetch-text!
                     {:text-id (:selections/text_id selection)
                      :app-state app-state
                      :start-id (:selections/start_token_id selection)
                      :end-id (:selections/end_token_id selection)
                      :mode :overwrite})
                    )}
         "Go"]
        ]
       [:div.flex.flex-col.pl-2
        [:div.flex
         [ui/labeled-field selection-state :selections/label "" "label for this selection"]
         [:button
          {:on-click #(update-selection-field
                       (:selections/selection_id selection)
                       :selections/label
                       (:selections/label @selection-state))}
          "Update label"]]
        [:div
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
                     (get-footnotes-range
                      (:selections/text_id selection)
                      (:selections/start_token_id selection)
                      (:selections/end_token_id selection)
                      (fn [v] (println "selection text: " v)
                        (-> (js/navigator.clipboard.writeText v)
                            (.catch #(js/console.error "Clipboard copy failed:" %)))
                        )))}
          "Footnotes"]
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
          "Glossary"]]]])))

(defn selections-component []
  (r/with-let [expanded-atom (r/atom {})]
    [:div
     [:h4 "Selections"]
     ;; [:div {} (str @selections-cursor)]
     (into [:div {}]
           (map (fn [selection]
                  (let [id (:selections/selection_id selection)
                        expanded-cursor (r/cursor expanded-atom [id])]
                    ^{:key id}
                    [selection-component2 selection expanded-cursor]))
                @selections-cursor))
     [:button {:on-click create-selection-using-current-selection}
      "Save current selection"]
     [:button {:on-click fetch-selections}
      "Fetch selections"]]))
