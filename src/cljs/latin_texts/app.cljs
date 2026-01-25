(ns latin-texts.app
  (:require [reagent.core :as r]
            [reagent.dom.client :as rd-client]
            [cljs.core.async :refer [go <! chan put!]]
            [cljs-bean.core :refer [->clj]]
            [promesa.core :as p]
            [cljs.reader :as reader]
            [latin-texts.lexeme-editor :as lexeme-editor]
            ))

(defonce app-state (r/atom {:mode :text}))

(def mode-cursor
  (r/cursor app-state [:mode]))

(defn set-mode [mode]
  ;; modes are :text or :lexeme-editor
  (swap! app-state assoc :mode mode))

(defn fetch-text [text-id]
  (-> (js/fetch (str "/text?text-id=" text-id))
      (.then (fn [v]
               (println v)
               (.text v)
               ))))

(def current-token-id
  (r/cursor app-state [:current-token-id]))

(defn set-current-token! [token]
  (swap! app-state assoc :current-token-id (:tokens/token_id token)))

(defn set-text! [tokens]
  (swap! app-state assoc :current-text-tokens-by-id
         (into {} 
               (map (fn [token]
                      {(:tokens/token_id token) token})
                    tokens)))
  (swap! app-state assoc :text-as-list
         (mapv :tokens/token_id tokens))
  (swap! app-state assoc :text tokens))

(def get-text-as-list
  (r/cursor app-state [:text-as-list]))

(def current-text-tokens-by-id
  (r/cursor app-state [:current-text-tokens-by-id]))

(defn current-token []
  (get-in @current-text-tokens-by-id [@current-token-id]))

(defn token-by-id [id]
  (get-in @current-text-tokens-by-id [id]))

(defn update-token [new-token]
  (swap! app-state assoc-in
         [:current-text-tokens-by-id
          (:tokens/token_id new-token)]
         new-token))

(defn set-meaning [token-id meaning-id]
  (->
   (js/fetch
    "/token/set-meaning"
    #js {:method "POST"
         :headers #js {"Content-Type" "application/json"}
         :body (js/JSON.stringify
                #js {:token-id token-id
                     :meaning-id meaning-id})})
   (.then (fn [v]
            (println v)
            (.json v)))
   (.then (fn [v]
            (update-token (reader/read-string (:data (->clj v))))))))

(defn unset-meaning [token-id]
  (->
   (js/fetch
    "/token/unset-meaning"
    #js {:method "POST"
         :headers #js {"Content-Type" "application/json"}
         :body (js/JSON.stringify
                #js {:token-id token-id})})
   (.then (fn [v]
            (println v)
            (.json v)))
   (.then (fn [v]
            (update-token (reader/read-string (:data (->clj v))))
            ))))

(defn token-color [token]
  (cond
    (:tokens/meaning_id token) "green"
    (not (empty? (:potential-meanings token))) "blue"
    true "red"))

(defn token-bg-color [token]
  (when-let [id (:tokens/token_id token)]
    (when (= id @current-token-id)
      "orange")))

(defn text-fetcher-component []
  [:div {:style {:width "50%"}}
   [:button {:on-click
             (fn []
               (-> (fetch-text 13)
                   (p/then (fn [result]
                             (set-text! (reader/read-string result))
                             ;; (swap! app-state assoc :text (reader/read-string result))
                             ))
                   (p/catch (fn [err]
                              (println err)))))}
    "Fetch"]
   (into [:div {:style {:display :flex
                        :flex-wrap :wrap
                        }}]
         (map (fn [token-id]
                (let [token (token-by-id token-id)]
                  ^{:key (:tokens/token_id token)}
                  [:<>
                   [:span
                    {:style {:color (token-color token)
                             :background-color (token-bg-color token)
                             :margin-right "6px"}
                     :on-click #(set-current-token! token)}
                    (str
                     (:tokens/punctuation_preceding token)
                     (:tokens/wordform token)
                     (:tokens/punctuation_trailing token))]
                   (when (clojure.string/includes?
                          (:tokens/punctuation_trailing token)
                          "\n") [:br])]))
              @get-text-as-list
                                        ;(:text @app-state)
              ))
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
       (get-in meaning [:lexeme :lexemes/dictionary_form])
       ;; (:meanings/case meaning)
       )
  )

(defn potential-meaning [meaning]
  [:div {}
   (vocab-str-for-noun meaning)])

(defn potential-meanings-picker [token]
  (r/with-let [selection-atom (r/atom nil)]
    (if (:tokens/meaning_id token)
      [:div "Selected meaning: "
       ;; (:tokens/meaning_id token)
       (vocab-str-for-noun (:meaning token))
       [:button {:on-click #(unset-meaning
                             (:tokens/token_id token))}
        "Unset"]]
      [:div {} "Potential meanings"
       (into [:ul]
             (map (fn [meaning]
                    [:li {} [potential-meaning meaning]
                     [:button {:on-click
                               #(set-meaning
                                 (:tokens/token_id token)
                                 (:meanings/meaning_id meaning))
                               }
                      "Set"]])
                  (:potential-meanings token)))
       ])))

(defn current-token-component []
  (let [token (current-token)]
    [:div {:style {:width "49%"
                   :margin-bottom "20px"}} ;; "Current token: " (:tokens/wordform token)
     [potential-meanings-picker token]
     ;; [:div {} token]
     ;; [:div {} (current-token)]
     ;; [:div {:style {:margin "10px"}} (str (keys @app-state))]
     ;; [:div {:style {:margin "10px"}} (str "app-state 2 meaning: "(get-in @app-state [:current-text-tokens-by-id 2 :tokens/meaning_id]))]
     ;; [:div {:style {:margin "10px"}} (token-by-id 2)]
     ]))

(defn mode-switcher []
  [:span {:style {:margin-left "20px"}}
   [:button {:on-click #(set-mode :text)} "Text"]
   [:button {:on-click #(set-mode :lexeme-editor)} "Lexeme Editor"]])

(defn text-component []
  [:div {:style {:display :flex}}
    [text-fetcher-component]
    [current-token-component]])

(defn root-component []
  [:div
   [:div {:style {:display :flex
                  :align-items :center}}
    [:h1 "Latin Texts DB"]
    [mode-switcher]]
   (case @mode-cursor
     :text [text-component]
     :lexeme-editor [lexeme-editor/lexeme-editor])
   ;; [:div {} @current-text-tokens-by-id]
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
