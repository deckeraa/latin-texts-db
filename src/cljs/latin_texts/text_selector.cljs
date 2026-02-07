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

(defn set-text! [app-state text-id tokens append?]
  (let [id-token-map (into {} 
                           (map (fn [token]
                                  {(:tokens/token_id token) token})
                                tokens))
        tokens-as-list (mapv :tokens/token_id tokens)]
    (if append?
      (do
        (swap! app-state update
               :current-text-tokens-by-id #(merge % id-token-map))
        (swap! app-state update
               :text-as-list #(concat % tokens-as-list))
        (swap! app-state assoc :text-id text-id))
      ;; just overwrite what's there
      (do
        (swap! app-state assoc
               :current-text-tokens-by-id id-token-map
               :text-as-list tokens-as-list
               :text-id text-id)))))

(defn fetch-text! [{:keys [text-id app-state start-id]}]
  (-> (js/fetch (str "/text?text-id=" text-id "&n=" 2000
                     (when start-id
                       (str "&start-id=" start-id))))
      (.then (fn [v]
               (println v)
               (.text v)))
      (.then (fn [result]
               (println "Fetched text: " result)
               (set-text!
                app-state
                text-id
                (reader/read-string result)
                (boolean start-id))))))

(defn load-more! [{:keys [app-state]}]
  (let [last-token-id (last (:text-as-list @app-state))
        start-id (get-in @app-state [:current-text-tokens-by-id last-token-id :tokens/next_token_id])
        text-id (:text-id @app-state)]
    (fetch-text!
     {:text-id text-id
      :app-state app-state
      :start-id start-id})))

(defn load-more-button [{:keys [app-state]}]
  (let [last-token-id (last (:text-as-list @app-state))
        start-id (get-in @app-state [:current-text-tokens-by-id last-token-id :tokens/next_token_id])
        text-id (:text-id @app-state)]
    [:button {:style {:margin-right "10px"}
              :disabled (nil? start-id)
              :on-click #(load-more! {:app-state app-state})}
     "Load more"]))

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
             (fetch-text!
              {:text-id text-id
               :app-state app-state})
             ))}
        (for [opt options]
          ^{:key opt}
          [:option {:value (:texts/text_id opt)}
           (:texts/title opt)])]
       [:button {:style {:margin-right "10px"}
                 :on-click #(fetch-text!
                             {:text-id @selected-text-id-atom
                              :app-state app-state})
                 }
        "Reload"]
       [load-more-button {:app-state app-state}]])))
