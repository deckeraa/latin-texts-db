(ns latin-texts.text-selector
  (:require [reagent.core :as r]
            [reagent.dom.client :as rd-client]
            [cljs.reader :as reader]
            [cljs.core.async :refer [go <! chan put!]]
            [cljs-bean.core :refer [->clj]]))


(defn fetch-texts! [texts-cursor]
  (-> (js/fetch (str "/texts"))
      (.then (fn [v]
               (println v)
               (.json v)))
      (.then (fn [result]
               (let [r (->clj result)]
                 (reset! texts-cursor r))))))

(defn set-text! [app-state tokens]
  (swap! app-state assoc :current-text-tokens-by-id
         (into {} 
               (map (fn [token]
                      {(:tokens/token_id token) token})
                    tokens)))
  (swap! app-state assoc :text-as-list
         (mapv :tokens/token_id tokens))
  (swap! app-state assoc :text tokens))

(defn fetch-text! [text-id app-state]
  (-> (js/fetch (str "/text?text-id=" text-id))
      (.then (fn [v]
               (println v)
               (.text v)))
      (.then (fn [result]
               (println "Fetched text: " result)
               (set-text! app-state (reader/read-string result))
               ))))

(defn text-selector [texts-cursor app-state]
  (r/with-let [selected-text-id-atom (r/atom (or (:texts/text_id (last @texts-cursor)) ""))
               cached-options-atom (r/atom @texts-cursor)]
    (let [options @texts-cursor
          chosen-option (first (filter #(= (:texts/text_id %) @selected-text-id-atom) options))]
      (when (not (= @cached-options-atom options))
        (reset! cached-options-atom options)
        (reset! selected-text-id-atom (:texts/text_id (last @texts-cursor)))
        )
      [:div
       ^{:key chosen-option}
       [:select
        {:value @selected-text-id-atom
         :on-change
         (fn [v]
           (let [text-id (.. v -target -value)]
             (reset! selected-text-id-atom text-id)
             (fetch-text! text-id app-state)
             ))}
        (for [opt options]
          ^{:key opt}
          [:option {:value (:texts/text_id opt)}
           (:texts/title opt)])]
       [:button {:style {:margin-right "10px"}
                 :on-click #(fetch-text! @selected-text-id-atom app-state)
                 }
        "Reload"]])))
