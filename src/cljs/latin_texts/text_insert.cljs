(ns latin-texts.text-insert
  (:require [reagent.core :as r]
            [reagent.dom.client :as rd-client]
            [cljs.core.async :refer [go <! chan put!]]
            [cljs-bean.core :refer [->clj]]
            [cljs.reader :as reader]
            [latin-texts.ui-components :refer [labeled-field labeled-checkbox labeled-dropdown gender-dropdown]]
            [latin-texts.utils :as u]
            [latin-texts.cursors :as c]
            [latin-texts.text-selector]))

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

(defn call-text-delete-endpoint [{:keys [text callback-fn] :as m}]
  (->
   (js/fetch
    "/text/delete"
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
                    (reset! doing-insertion? true)
                    (call-text-insert-endpoint
                     {:title @title-atom
                      :text @text-atom
                      :callback-fn
                      (fn [v]
                        (println "v: " v (type v))
                        (println (:text-id v))
                        (reset! doing-insertion? false)
                        (latin-texts.text-selector/fetch-texts!)
                        (when (:success v)
                          (reset! title-atom "")
                          (reset! text-atom ""))
                        (js/alert (str "Inserted text #" (:text-id v)))
                        )}))}
       (if @doing-insertion?
         "Inserting text..."
         "Insert")]]]))

(defn text-delete []
  (r/with-let [selected-text-id-atom (r/atom nil)
               safety-disengaged? (r/atom false)]
    (let [options @c/texts-cursor]
      [:div
       [:h1 "Delete a text"]
       [:div
        [:select
         {:value @selected-text-id-atom
          :on-change
          (fn [v]
            (let [text-id (.. v -target -value)]
              (reset! selected-text-id-atom text-id)))}
         (for [opt options]
           ^{:key opt}
           [:option {:value (:texts/text_id opt)
                     :title (str opt)}
            (str (:texts/title opt) " ("(:texts/text_id opt) ")")])]]
       [:span
        [:input {:type :checkbox
                 :checked @safety-disengaged?
                 :on-change #(swap! safety-disengaged? not)}]
        "I understand this is irreversible and have performed any needed backups of the database file."]
       [:button
        {:disabled (not @safety-disengaged?)
         :on-click (fn [e]
                    (call-text-delete-endpoint
                     {:text-id @selected-text-id-atom
                      :callback-fn
                      (fn [v]
                        (println (:text-id v))
                        (latin-texts.text-selector/fetch-texts!)
                        (reset! selected-text-id-atom nil)
                        (reset! safety-disengaged? false)
                        (when (:success v)
                          (js/alert (str "Deleted text #" (:text-id v))))
                        )}))}
        "Delete text"]
       ])))

(defn text-insert []
  [:div
   [new-text-inserter]
   [text-delete]
   ])
