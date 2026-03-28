(ns latin-texts.text-insert
  (:require [reagent.core :as r]
            [reagent.dom.client :as rd-client]
            [cljs.core.async :refer [go <! chan put!]]
            [cljs-bean.core :refer [->clj]]
            [cljs.reader :as reader]
            [latin-texts.ui-components :refer [labeled-field labeled-checkbox labeled-dropdown gender-dropdown]]
            [latin-texts.utils :as u]))

(defn call-text-insert-endpoint [{:keys [text callback-fn] :as m}]
  (->
   (js/fetch
    "/text/insert-new-text"
    #js {:method "POST"
         :headers #js {"Content-Type" "application/json"}
         :body (js/JSON.stringify
                (clj->js m))})
   (.then (fn [v] (.json v)))
   (.then (fn [v]
            (let [v (->clj v)]
              (println v)
              (when callback-fn (callback-fn v)))
            ;; TODO a toast message for success would be nice
            ;;(.json v)
            ))))

(defn new-text-inserter []
  (r/with-let [title-atom (r/atom "")
               text-atom (r/atom "")
               doing-insertion? (r/atom false)]
    [:<>
     [:h1 "Insert a new text"]
     [:div {:style {:display :flex
                    :flex-direction :column
                    :width "400px"}}
      [:input
       {:type :text
        :value (str @title-atom)
        :placeholder "title"
        :style {:min-width "300px"}
        :on-change #(reset! title-atom (.. % -target -value))
        }]
      [:textarea
       {:value (str @text-atom)
        :placeholder "text"
        :style {:min-width "300px"}
        :on-change #(reset! text-atom (.. % -target -value))}]
      [:button
       {:disabled @doing-insertion?
        :on-click (fn [e]
                    (call-text-insert-endpoint
                     {:title @title-atom
                      :text @text-atom
                      :callback-fn
                      (fn [v]
                        (println "v: " v (type v))
                        (println (:text-id v))
                        (reset! doing-insertion? false)
                        (when (:success v)
                          (reset! title-atom "")
                          (reset! text-atom ""))
                        (js/alert (str "Inserted text #" (:text-id v)))
                        )}))}
       (if @doing-insertion?
         "Inserting text..."
         "Insert")]]]))

(defn text-insert []
  [:div
   [new-text-inserter]
   ])
