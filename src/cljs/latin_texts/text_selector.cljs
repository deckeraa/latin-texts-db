(ns latin-texts.text-selector
  (:require [reagent.core :as r]
            [reagent.dom.client :as rd-client]
            [cljs.reader :as reader]
            [cljs.core.async :refer [go <! chan put!]]
            [cljs-bean.core :refer [->clj]]
            [latin-texts.cursors :as c]))

(defn set-autostart-text! [text-id]
  (js/fetch
    "/preferences/set-autostart-text"
    #js {:method "POST"
         :headers #js {"Content-Type" "application/json"}
         :body (js/JSON.stringify
                #js {:text-id text-id
                     ;; TODO also pass start_token_id to bookmark the specific place
                     })}))

(defn fetch-texts!
  ([]
   (fetch-texts! c/texts-cursor))
  ([texts-cursor]
   (-> (js/fetch (str "/texts"))
       (.then (fn [v]
                (println v)
                (.json v)))
       (.then (fn [result]
                (let [r (->clj result)]
                  (reset! texts-cursor r)))))))

(defn set-text! [app-state text-id tokens mode]
  (let [id-token-map (into {} 
                           (map (fn [token]
                                  {(:tokens/token_id token) token})
                                tokens))
        tokens-as-list (mapv :tokens/token_id tokens)
        mode (or mode :overwrite)]
    (println "In set-text! text-id=" text-id)
    (case mode
      :prepend
      (do
        (swap! app-state update
               :current-text-tokens-by-id #(merge % id-token-map))
        (swap! app-state update
               :text-as-list #(concat tokens-as-list %))
        (swap! app-state assoc :text-id text-id))
      :append
      (do
        (swap! app-state update
               :current-text-tokens-by-id #(merge % id-token-map))
        (swap! app-state update
               :text-as-list #(concat % tokens-as-list))
        (swap! app-state assoc :text-id text-id))
      :overwrite
      (do
        (swap! app-state assoc
               :current-text-tokens-by-id id-token-map
               :text-as-list tokens-as-list
               :text-id text-id)))))

(defn get-text [{:keys [text-id start-id end-id n callback-fn]}]
  (-> (js/fetch (str "/text?text-id=" text-id
                     (when n 
                       (str "&n=" n))
                     (when (and (not n) (nil? end-id))
                       (str "&n=" 2000))
                     (when start-id
                       (str "&start-id=" start-id))
                     (when end-id
                       (str "&end-id=" end-id))))
      (.then (fn [v]
               (println v)
               (.text v)))
      (.then (fn [result]
               (callback-fn result)))))

(defn fetch-text! [{:keys [text-id app-state mode] :as args}]
  (get-text
   (merge
    {:n 400
     :callback-fn
     (fn [result]
       (let [result (reader/read-string result)
             result (if (= mode :prepend)
                      (reverse result)
                      result)]
         (set-text!
          app-state
          text-id
          result
          mode
          )))}
    args)))

(defn load-more! [{:keys [app-state]}]
  (let [last-token-id (last (:text-as-list @app-state))
        start-id (get-in @app-state [:current-text-tokens-by-id last-token-id :tokens/next_token_id])
        text-id (:text-id @app-state)]
    (fetch-text!
     {:text-id text-id
      :app-state app-state
      :start-id start-id
      :mode :append})))

(defn load-more-button [{:keys [app-state]}]
  (let [last-token-id (last (:text-as-list @app-state))
        start-id (get-in @app-state [:current-text-tokens-by-id last-token-id :tokens/next_token_id])]
    [:button {:style {:margin-right "10px"}
              :disabled (nil? start-id)
              :on-click #(load-more! {:app-state app-state})}
     "Load more"]))

(defn load-prev! [{:keys [app-state]}]
  (let [first-token-id (first (:text-as-list @app-state))
        end-id (get-in @app-state [:current-text-tokens-by-id first-token-id :tokens/prev_token_id])
        text-id (:text-id @app-state)]
    (fetch-text!
     {:text-id text-id
      :app-state app-state
      :end-id end-id
      :mode :prepend})))

(defn load-prev-button [{:keys [app-state]}]
  (let [first-token-id (first (:text-as-list @app-state))
        end-id (get-in @app-state [:current-text-tokens-by-id first-token-id :tokens/prev_token_id])]
    [:button {:style {:margin-right "10px"}
              :disabled (nil? end-id)
              :on-click #(load-prev! {:app-state app-state})}
     "Load prev"]))

(defn text-selector [texts-cursor text-id-cursor app-state]
  (r/with-let [_ (println "texts-cursor: " @texts-cursor)
               selected-text-id-atom
               (r/atom (or @text-id-cursor
                           (:texts/text_id (last @texts-cursor))))
               cached-options-atom (r/atom @texts-cursor)
               cached-text-id-cursor (r/atom @text-id-cursor)]
    (when-not selected-text-id-atom
      ;; if we're here we hit a race condition where this component first drew before the text listed had been fetched from the server
      (println "===")
      (let [text-id (or @text-id-cursor
                        (:texts/text_id (last @texts-cursor)))]
        (println "=====" text-id)
        (reset! selected-text-id-atom text-id)
        (fetch-text!
         {:text-id text-id
          :app-state app-state})))
    (let [options @texts-cursor
          _ (println "options: " options)
          chosen-option (first (filter #(= (:texts/text_id %) @selected-text-id-atom) options))]
      (when (or (not (= @cached-options-atom options))
                (not (= @cached-text-id-cursor @text-id-cursor)))
        (println "Reloading text-selector: " (:text-id @app-state))
        (reset! cached-options-atom options)
        ;;(reset! selected-text-id-atom (:texts/text_id (last @texts-cursor)))
        (reset! selected-text-id-atom
                (or
                 (:text-id @app-state)
                 (:texts/text_id (last @texts-cursor))))
        (reset! cached-text-id-cursor @text-id-cursor)
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
             (set-autostart-text! text-id)
             ))}
        (for [opt options]
          ^{:key opt}
          [:option {:value (:texts/text_id opt)
                    :title (str opt)}
           (str (:texts/title opt) " ("(:texts/text_id opt) ")")])]
       [:div {} @selected-text-id-atom]
       [:button {:style {:margin-right "10px"}
                 :on-click #(fetch-text!
                             {:text-id @selected-text-id-atom
                              :app-state app-state
                              :start-id (first (:text-as-list @app-state))
                              :end-id (last (:text-as-list @app-state))
                              :mode :overwrite})
                 }
        "Reload"]
       [:button {:style {:margin-right "10px"}
                 :on-click #(fetch-text!
                             {:text-id @selected-text-id-atom
                              :app-state app-state
                              :start-id @c/selection-start-cursor
                              :mode :overwrite})
                 }
        "Reload from selection"]
       [load-prev-button {:app-state app-state}]
       [load-more-button {:app-state app-state}]])))

(defn fetch-autostart-text [app-state]
  (->
   (js/fetch (str "/preferences/autostart-text"))
   (.then (fn [v]
            (.json v)))
   (.then (fn [v]
            (let [v (->clj v)
                  text-id (:text-id v)]
              (println "autostart-text: " v)
              (fetch-text!
               {:text-id text-id
                :app-state app-state}))))))
