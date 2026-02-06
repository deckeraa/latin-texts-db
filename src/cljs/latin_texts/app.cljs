(ns latin-texts.app
  (:require [reagent.core :as r]
            [reagent.dom.client :as rd-client]
            [cljs.core.async :refer [go <! chan put!]]
            [cljs-bean.core :refer [->clj]]
            [promesa.core :as p]
            [cljs.reader :as reader]
            [cognitect.transit :as t]
            [latin-texts.lexeme-editor :as lexeme-editor]
            [latin-texts.bulk-insert :refer [bulk-insert]]
            [latin-texts.ui-components :refer [labeled-field labeled-checkbox]]
            [latin-texts.text-selector :refer [text-selector] :as text-selector]))

(defonce app-state (r/atom {:mode :text
                            :text-id 15
                            :auto-advance? true
                            :selection-start-token-id nil
                            :selection-end-token-id nil
                            :texts []
                            }))

(def text-id-cursor
  (r/cursor app-state [:text-id]))

(def mode-cursor
  (r/cursor app-state [:mode]))

(defn set-mode [mode]
  ;; modes are :text, :lexeme-editor, :bulk-insert
  (swap! app-state assoc :mode mode))

(def auto-advance?-cursor
  (r/cursor app-state [:auto-advance?]))

(def texts-cursor
  (r/cursor app-state [:texts]))

(def current-token-id
  (r/cursor app-state [:current-token-id]))

(defn set-current-token! [token]
  (swap! app-state assoc :current-token-id (:tokens/token_id token)))

(def get-text-as-list
  (r/cursor app-state [:text-as-list]))

(def current-text-tokens-by-id
  (r/cursor app-state [:current-text-tokens-by-id]))

(defn current-token []
  (get-in @current-text-tokens-by-id [@current-token-id]))

(defn token-by-id [id]
  (get-in @current-text-tokens-by-id [id]))

(defn advance-token []
  (println @auto-advance?-cursor)
  (let [next-token-id (-> (current-token) :tokens/next_token_id)
        next-token (get @current-text-tokens-by-id next-token-id)]
    (set-current-token! next-token)
    (when (:tokens/meaning_id (current-token))
      (advance-token))))

(defn update-token [new-token]
  (swap! app-state assoc-in
         [:current-text-tokens-by-id
          (:tokens/token_id new-token)]
         new-token))

(def selection-start-cursor
  (r/cursor app-state [:selection-start-token-id]))

(def selection-end-cursor
  (r/cursor app-state [:selection-end-token-id]))

(defn selection-start-token []
  (get @current-text-tokens-by-id @selection-start-cursor))

(defn selection-end-token []
  (get @current-text-tokens-by-id @selection-end-cursor))

(defn set-selection-start-token [token-or-token-id]
  (let [token-id (if (map? token-or-token-id)
                   (:tokens/token_id token-or-token-id)
                   token-or-token-id)]
    (println "set-selection-start-token" token-id)
    (swap! app-state assoc
           :selection-start-token-id token-id
           :selection-end-token-id nil
           )))

(defn set-selection-end-token [token-or-token-id]
  (let [token-id (if (map? token-or-token-id)
                   (:tokens/token_id token-or-token-id)
                   token-or-token-id)]
    (println "set-selection-end-token" token-id)
    (swap! app-state assoc :selection-end-token-id token-id)))

(defn selected-tokens-seq
  [start-id end-id]
  (when (and start-id end-id)
    (let [step (fn step [id]
                 (if id
                   (when-let [token (token-by-id id)]
                     (cons token
                           (when-not (= id end-id)
                             (step (:tokens/next_token_id token)))))
                   []))]
      (step start-id))))

(def selected-tokens
  (r/reaction
    (into [] (selected-tokens-seq 
               @selection-start-cursor
               @selection-end-cursor))))

(def selected-tokens-ids
  (r/reaction
   (into #{} (map :tokens/token_id
                  (selected-tokens-seq 
                   @selection-start-cursor
                   @selection-end-cursor)))))

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
  (let [id (:tokens/token_id token)]
    (cond
      (= id @current-token-id)
      "orange"
      ;;
      (@selected-tokens-ids id)
      "pink")))

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

(defn text-display-component []
  [:div {:style {:width "50%"}}
   [:div {:style {:display :flex}}
    [progress-component]]
   (into [:div {:style {:display :flex
                        :flex-wrap :wrap
                        :height "500px" ;; TODO better measurement
                        :overflow-y :scroll
                        }}]
         (map (fn [token-id]
                (let [token (token-by-id token-id)
                      token-margin "6px"]
                  ^{:key (:tokens/token_id token)}
                  [:<>
                   [:span
                    {:style {:color (token-color token)
                             :background-color (token-bg-color token)
                             :margin-left (when (clojure.string/includes? (:tokens/punctuation_preceding token) "\t") "20px")
                             :margin-right token-margin
                             :user-select :none
                             }
                     :on-click #(set-current-token! token)
                     :on-mouse-down #(set-selection-start-token token)
                     :on-mouse-up #(set-selection-end-token token)
                     }
                    
                    (str
                     (:tokens/punctuation_preceding token)
                     (:tokens/wordform token)
                     (:tokens/punctuation_trailing token))]
                   (when (not (empty? (:footnotes token)))
                     [:div {:style {:font-size "12px"
                                    :margin-left (str "-" token-margin)
                                    :margin-right token-margin}} "f"])
                   (when (clojure.string/includes?
                          (:tokens/punctuation_trailing token)
                          "\n") [:div {:style {:width "100%" :height "0px"}}])
                   
                   ]))
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
       ;;(:tokens/meaning_id token)
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
                               (fn []
                                 (set-meaning
                                  (:tokens/token_id token)
                                  (:meanings/meaning_id meaning))
                                 (when @auto-advance?-cursor
                                   (advance-token)))
                               }
                      "Set"]])
                  (:potential-meanings token)))
       ])))

(defn create-footnote [token-id text]
  (->
   (js/fetch
    "/token/create-footnote"
    #js {:method "POST"
         :headers #js {"Content-Type" "application/json"}
         :body (js/JSON.stringify
                #js {:token-id token-id
                     :text text})})
   (.then (fn [v]
            (println v)
            (.json v)))
   (.then (fn [v]
            (update-token (reader/read-string (:data (->clj v))))))))

(defn update-footnote! [footnote]
  (let [writer (t/writer :json)]
    (->
     (js/fetch
      "/token/update-footnote"
      #js {:method "POST"
           :headers #js {"Content-Type" "application/json"}
           ;;:body (t/write writer footnote) ;; TODO switch to transit
           :body (js/JSON.stringify (pr-str footnote))
           })
     (.then (fn [v]
              (.json v)))
     (.then (fn [v]
              (update-token (reader/read-string (:data (->clj v)))))))))

(defn single-footnote-component [footnote]
  (r/with-let [footnote-atom (r/atom footnote)]
    [:li {}
     ;; @footnote-atom
     ;;(:footnotes/text footnote)
     [labeled-field footnote-atom :footnotes/text "Text" "footnote text goes here" {:input-attrs {:style {:width "100%"}}}]
     [:div
      ;; TODO add a cool hover effect to show the current selection
      (str "Selection: "
           (-> @footnote-atom
               :footnotes/start_token_id
               token-by-id
               :tokens/wordform)
           "->"
           (-> @footnote-atom
               :footnotes/end_token_id
               token-by-id
               :tokens/wordform))]
     [:button
      {:on-click
       #(swap! footnote-atom assoc
               :footnotes/start_token_id @selection-start-cursor
               :footnotes/end_token_id @selection-end-cursor)}
      "Use current selection"]
     [:button {:on-click #(update-footnote! @footnote-atom)}
      "Update"]
     ]))

(defn footnote-component [token]
  (r/with-let [footnote-atom (r/atom "")]
    [:div
     (into [:ul]
           (map single-footnote-component (:footnotes token)))
     [:input {:value (str @footnote-atom)
              :on-change #(reset! footnote-atom (.. % -target -value))}]
     [:button {:on-click #(create-footnote
                           (:tokens/token_id token)
                           @footnote-atom)}
      "Create footnote"]]))

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

     [footnote-component token]

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
                 :read-only true
                 :style {:height "100px" :width "100%"}}]
     ]))

(defn mode-switcher []
  [:span {:style {:margin-left "20px"}}
   [:button {:on-click #(set-mode :text)} "Text"]
   [:button {:on-click #(set-mode :lexeme-editor)} "Lexeme Editor"]
   [:button {:on-click #(set-mode :bulk-insert)} "Bulk Inserter"]])

(defn selection-viewer []
  [:span
   ;; (str @selected-tokens-ids)
   ;; (str (doall (map :tokens/wordform @selected-tokens)))
   (str (:tokens/wordform (selection-start-token)))
   "->"
   (str (:tokens/wordform (selection-end-token)))
   ])

(defn text-component []
  [:div
   [text-selector texts-cursor app-state]
   [:div {:style {:display :flex}}
    [:button {:on-click advance-token} "Advance"]
    [labeled-checkbox app-state :auto-advance? "Auto-advance?"]
    [selection-viewer]]
   [:div {:style {:display :flex}}
    [text-display-component]
    [:div {:style {:width "49%"}}
     [current-token-component]
     [glossary-component]]]])

(defn root-component []
  [:div
   [:div {:style {:display :flex
                  :align-items :center}}
    [:h1 "Latin Texts DB"]
    [mode-switcher]]
   (case @mode-cursor
     :text [text-component]
     :lexeme-editor [lexeme-editor/lexeme-editor]
     :bulk-insert [bulk-insert]
     [:div {} @app-state])
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

(defonce _ (text-selector/fetch-texts! texts-cursor))
