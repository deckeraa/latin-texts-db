(ns latin-texts.app
  (:require [reagent.core :as r]
            [reagent.dom.client :as rd-client]
            [cljs.core.async :refer [go <! chan put!]]
            [cljs-bean.core :refer [->clj]]
            [promesa.core :as p]
            [cljs.reader :as reader]
            [latin-texts.lexeme-editor :as lexeme-editor]
            ))

(defonce app-state (r/atom {:mode :text
                            :text-id 13}))

(def text-id-cursor
  (r/cursor app-state [:text-id]))

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

(defn progress-component []
  (let [tokens (vals @current-text-tokens-by-id)
        token-count (count tokens)
        num-blue (count (filter (fn [token]
                                 (and (nil? (:tokens/meaning_id token))
                                      (not (empty? (:potential-meanings token)))))
                               tokens))
        num-green (count (filter (fn [token] (:meaning token)) tokens))
        num-red (- token-count num-blue num-green)
        fmt (fn [a b] (str (Math/round (/ (* 100 a) b)) "%"))]
    (when (> token-count 0)
      [:div
       [:span {:style {:color "red"}} num-red]
       "/"
       [:span {:style {:color "blue"}} num-blue]
       "/"
       [:span {:style {:color "green"}} num-green]
       "  "
       [:span {:style {:color "red"}} (fmt num-red token-count)]
       "/"
       [:span {:style {:color "blue"}} (fmt num-blue token-count)]
       "/"
       [:span {:style {:color "green"}} (fmt num-green token-count)]])))

(defn text-fetcher-component []
  [:div {:style {:width "50%"}}
   [:div {:style {:display :flex}}
    [:button {:style {:margin-right "10px"}
              :on-click
              (fn []
                (-> (fetch-text @text-id-cursor)
                    (p/then (fn [result]
                              (set-text! (reader/read-string result))
                              ;; (swap! app-state assoc :text (reader/read-string result))
                              ))
                    (p/catch (fn [err]
                               (println err)))))}
     "Fetch"]
    [progress-component]]
   (into [:div {:style {:display :flex
                        :flex-wrap :wrap
                        :height "500px" ;; TODO better measurement
                        :overflow-y :scroll
                        }}]
         (map (fn [token-id]
                (let [token (token-by-id token-id)]
                  ^{:key (:tokens/token_id token)}
                  [:<>
                   [:span
                    {:style {:color (token-color token)
                             :background-color (token-bg-color token)
                             :margin-left (when (clojure.string/includes? (:tokens/punctuation_preceding token) "\t") "20px")
                             :margin-right "6px"}
                     :on-click #(set-current-token! token)}
                    (str
                     (:tokens/punctuation_preceding token)
                     (:tokens/wordform token)
                     (:tokens/punctuation_trailing token))]
                   (when (clojure.string/includes?
                          (:tokens/punctuation_trailing token)
                          "\n") [:div {:style {:width "100%" :height "0px"}}])]))
              @get-text-as-list
                                        ;(:text @app-state)
              ))
   ])

(defn vocab-str-for-noun
  ([meaning]
   (vocab-str-for-noun meaning nil))
  ([meaning override-gloss]
   (str (:meanings/wordform meaning)
        ": "
        (or override-gloss (:meanings/gloss meaning))
        "; "
        (:meanings/number meaning)
        " "
        (:meanings/gender meaning)
        " "
        (:meanings/case_ meaning)
        " from "
        (get-in meaning [:lexeme :lexemes/dictionary_form])
        ;; (:meanings/case meaning)
        ))
  )

(defn update-token-field [token-id field value]
  (->
   (js/fetch
    "/token/update"
    #js {:method "POST"
         :headers #js {"Content-Type" "application/json"}
         :body (js/JSON.stringify
                #js {:token-id token-id
                     :field (name field)
                     :value value})})
   (.then (fn [v]
            (println v)
            (.json v)))
   (.then (fn [v]
            (update-token (reader/read-string (:data (->clj v))))))))

(defn token-edit [token field]
  (r/with-let [initial-value-atom (r/atom token)
               temp-state-atom (r/atom (get token field))]
    (when (not (= token @initial-value-atom))
      (reset! initial-value-atom token)
      (reset! temp-state-atom (get token field)))
    [:div
     [:span (str field)]
     [:input {:value (str @temp-state-atom)
              :on-change #(reset! temp-state-atom (.. % -target -value))}]
     [:button {:on-click #(update-token-field (:tokens/token_id token) field @temp-state-atom)}
      "Save"]]))

(defn potential-meaning [meaning]
  [:div {}
   (vocab-str-for-noun meaning)])

(defn potential-meanings-picker [token]
  (r/with-let [selection-atom (r/atom nil)]
    (if (:tokens/meaning_id token)
      [:div "Selected meaning: "
       ;; (:tokens/meaning_id token)
       (vocab-str-for-noun (:meaning token) (:tokens/gloss_override token))
       [:button {:on-click #(unset-meaning
                             (:tokens/token_id token))}
        "Unset"]
       [:div {:style {:display :flex}}
;;        [:div {} "Note: gloss_override does not yet affect glossary output"]
        [token-edit token :tokens/gloss_override]
        [:button {:on-click
                  (fn []
                    (update-token-field (:tokens/token_id token) :tokens/gloss_override nil))}
         "Unset"]]]
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
    [:div {:style {:margin-bottom "20px"}}
     [:div {} "Current token: " (:tokens/token_id token)]
     [potential-meanings-picker token]
     [:div {:style {:margin-top "20px"}}
      [:button {:on-click #(update-token-field
                            (:tokens/token_id token)
                            :tokens/punctuation_preceding
                            (str (:tokens/punctuation_preceding token)
                                 "\t"))} "Insert tab"]
      [token-edit token :tokens/punctuation_preceding]
      [token-edit token :tokens/wordform]
      [token-edit token :tokens/punctuation_trailing]
      [:button {:on-click #(update-token-field
                            (:tokens/token_id token)
                            :tokens/punctuation_trailing
                            (str (:tokens/punctuation_trailing token)
                                 "\n"))} "Add newline"]]
     ;; [:div {} token]
     ;; [:div {} (current-token)]
     ;; [:div {:style {:margin "10px"}} (str (keys @app-state))]
     ;; [:div {:style {:margin "10px"}} (str "app-state 2 meaning: "(get-in @app-state [:current-text-tokens-by-id 2 :tokens/meaning_id]))]
     ;; [:div {:style {:margin "10px"}} (token-by-id 2)]
     ]))

(defn glossary-component []
  (r/with-let [glossary-atom (r/atom "")]
    [:div
     [:div {:style {:display :flex}}
      [:button
       {:on-click
        (fn []
          (-> (js/fetch (str "/text/glossary?text-id=" @text-id-cursor))
              (.then (fn [v]
                       (.text v)))
              (.then #(reset! glossary-atom %))))}
       "Generate glossary"]
      [:button {:on-click
                (fn []
                  (when-not (empty? @glossary-atom)
                    (-> (js/navigator.clipboard.writeText @glossary-atom)
                        (.catch #(js/console.error "Clipboard copy failed:" %)))))} "Copy to clipboard"]]
     [:textarea {:value @glossary-atom
                 :style {:height "100px" :width "100%"}}]
     ]))

(defn mode-switcher []
  [:span {:style {:margin-left "20px"}}
   [:button {:on-click #(set-mode :text)} "Text"]
   [:button {:on-click #(set-mode :lexeme-editor)} "Lexeme Editor"]])

(defn text-component []
  [:div {:style {:display :flex}}
   [text-fetcher-component]
   [:div {:style {:width "49%"}}
    [current-token-component]
    [glossary-component]]])

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
