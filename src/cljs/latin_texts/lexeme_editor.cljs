(ns latin-texts.lexeme-editor
  (:require [reagent.core :as r]
            [reagent.dom.client :as rd-client]
            [cljs.core.async :refer [go <! chan put!]]
            [cljs-bean.core :refer [->clj]]
            [promesa.core :as p]
            [cljs.reader :as reader]
            [cognitect.transit :as t]
            [latin-texts.autocomplete :refer [autocomplete]]
            ))

(defonce lexeme-editor-state
  (r/atom {:lexeme nil
           :lexeme-dictionary-form-in-search ""
           :known-lexemes []
           :meanings []
           :collapsed {}
           :selected-pos nil
           }))

(def lexeme-cursor
  (r/cursor lexeme-editor-state [:lexeme]))

(def lexeme-id-cursor
  (r/cursor lexeme-editor-state [:lexeme :lexemes/lexeme_id]))

(def lexeme-dictionary-form-in-search
  (r/cursor lexeme-editor-state [:lexeme-dictionary-form-in-search]))

(def known-lexemes
  (r/cursor lexeme-editor-state [:known-lexemes]))

(def collapsed-cursor
  (r/cursor lexeme-editor-state [:collapsed]))

(def selected-pos-cursor
  (r/cursor lexeme-editor-state [:selected-pos]))

;; input to type in lexical form
;; search-as-you-type would be nice
;; hitting the search button runs ll, creating the lexical form if not found
;; the lexical form is returned along with all meanings associated with that lexeme
;; there's then a function called meaning-lookup that takes characteristics and filters all meanings that have those
;; then I have bunch of boxes for nouns, verbs, etc. that all use that function
;; each box has a little edit star
;; one button at the bottom to save down all changes at one and reload the page

(defn fetch-lexemes []
  (-> (js/fetch (str "/lexemes"))
      (.then (fn [v]
               (.json v)))
      (p/then (fn [result]
                (let [r (->clj result)]
                  (reset! known-lexemes r))))))

(defonce _ (fetch-lexemes)) ;; pull them once on page load ;; TODO make this update when proper

(defn fetch-lexeme-with-meanings [dictionary-form]
  (-> (js/fetch (str "/lexeme-with-meanings?dictionary-form=" dictionary-form))
      (.then (fn [v]
               (.json v)
               ))
      (p/then (fn [result]
                (let [r (->clj result)]
                  (swap! lexeme-editor-state assoc
                         :lexeme (:lexeme r)
                         :meanings (:meanings r)
                         :selected-pos (first (map :meanings/part_of_speech (:meanings r)))))))))

(def meanings-cursor (r/cursor lexeme-editor-state [:meanings]))

(defn filter-meanings [filter-map]
  (filter (fn [meaning]
            (empty? (remove (fn [k] (= (get meaning k)
                                       (get filter-map k)))
                            (keys filter-map))))
          @meanings-cursor))

(defn insert-meaning [meaning]
  (swap! lexeme-editor-state update-in [:meanings] conj meaning))

(defn update-meanings [meaning]
  ;; linear time efficiency -- not the best; but works within the scale of likely # of meanings within a single lexeme
  (swap! lexeme-editor-state update-in [:meanings]
         (fn [meanings]
           (mapv
            (fn [m]
              (if (= (:meanings/meaning_id meaning)
                     (:meanings/meaning_id m))
                meaning
                m))
            meanings)))
  )

(defn lexeme-box []
  (let [select-fn (fn [] (fetch-lexeme-with-meanings @lexeme-dictionary-form-in-search))]
    [:div {}
     [autocomplete
      {:value       lexeme-dictionary-form-in-search
       :on-change   (fn [v] (reset! lexeme-dictionary-form-in-search v))
       :suggestions (doall (remove nil? (mapv :lexemes/dictionary_form @known-lexemes)))
       :on-select   select-fn
       :placeholder "porcus, porcī"}]
     [:button {:on-click select-fn} "Search"]
     [:button {:on-click
               (fn [e]
                 (swap! lexeme-editor-state
                         assoc :lexeme nil
                         :lexeme-dictionary-form-in-search ""
                         :meanings [])
                 )}
      "Clear"]
     [:span {} (str @lexeme-id-cursor)]]))

(defn create-meaning [filters wordform gloss]
  (->
   (js/fetch
    "/meaning/create"
    #js {:method "POST"
         :headers #js {"Content-Type" "application/json"}
         :body (js/JSON.stringify
                (clj->js
                 {:lexeme-dictionary-form @lexeme-dictionary-form-in-search
                  :meaning
                  (merge filters
                         {:wordform wordform
                          :gloss gloss})}))})
   (.then (fn [v]
            (.json v)))
   (.then (fn [v]
            (insert-meaning (:data (->clj v)))
            ))))

;; (defn update-meaning [meaning]
;;   (->
;;    (js/fetch
;;     "/meaning/update"
;;     #js {:method "POST"
;;          :headers #js {"Content-Type" "application/json"}
;;          :body (js/JSON.stringify
;;                 (clj->js meaning))})
;;    (.then (fn [v]
;;             (println v)
;;             (.json v)))
;;    (.then (fn [v]
;;             (println "Got meaning: " v)
;;             ;; (update-token (reader/read-string (:data (->clj v))))
;;             ))))

(defn update-meaning! [meaning]
  (let [writer (t/writer :json)]
    (->
     (js/fetch
      "/meaning/update"
      #js {:method "POST"
           :headers #js {"Content-Type" "application/json"}
           ;;:body (t/write writer meaning) ;; TODO switch to transit
           :body (js/JSON.stringify (pr-str meaning))
           })
     (.then (fn [v]
              (println v)
              (.json v)))
     (.then (fn [v]
              (println v)
              ;; (update-token (reader/read-string (:data (->clj v))))
              )))))

;; (defn wordform-editor [filters]
;;   (r/with-let [wordform-atom (r/atom "")
;;                gloss-atom (r/atom "")
;;                initial-meanings-atom (r/atom nil)]
;;     (let [meanings (filter-meanings filters)]
;;       (when (not (= meanings @initial-meanings-atom))
;;         (reset! wordform-atom (:meanings/wordform (first meanings)))
;;         (reset! gloss-atom    (:meanings/gloss    (first meanings)))
;;         (reset! initial-meanings-atom meanings))
;;       [:div
;;        (into [:<>]
;;              (map (fn [meaning]
;;                     ^{:key meaning}
;;                     [:div {:style {:background-color (when (> (count meanings) 1) "red")}}
;;                      [:input {:value (str @wordform-atom)
;;                               :title (vals filters)
;;                               :on-change #(reset! wordform-atom (.. % -target -value))}]
;;                      [:input {:value (str @gloss-atom)
;;                               :title (str meaning)
;;                               :on-change #(reset! gloss-atom (.. % -target -value))}]
;;                      (when (empty? @initial-meanings-atom)
;;                        [:button {:on-click #(create-meaning filters @wordform-atom @gloss-atom)} "Create"])
;;                      ;; [:div (str (vals filters))]
;;                      ;; [:div {} (str "filter: " (filter-meanings filters))]
;;                      ])
;;                   meanings))
;;        (when (seq meanings)
;;          [:button {} "+"])
;;        ])))

;; (defn wordform-editor [filters]
;;   (r/with-let [wordform-atom (r/atom "")
;;                gloss-atom (r/atom "")
;;                initial-meanings-atom (r/atom nil)]
;;     (let [meanings (filter-meanings filters)
;;           on-change (fn [atm event]
;;                       (let [v (.. event -target -value)]
;;                         (reset! atm (clojure.string/triml v)))
;;                       )]
;;       (when (not (= meanings @initial-meanings-atom))
;;         (reset! wordform-atom (:meanings/wordform (first meanings)))
;;         (reset! gloss-atom    (:meanings/gloss    (first meanings)))
;;         (reset! initial-meanings-atom meanings))
;;       [:div {:style {:background-color (when (> (count meanings) 1) "red")}}
;;        [:input {:value (str @wordform-atom)
;;                 :title (vals filters)
;;                 :on-change #(on-change wordform-atom %)}]
;;        [:input {:value (str @gloss-atom)
;;                 :on-change #(on-change gloss-atom %)}]
;;        (when (empty? @initial-meanings-atom)
;;          [:button {:on-click #(create-meaning filters @wordform-atom @gloss-atom)} "Create"])
;;        (when (= 1 (count meanings))
;;          (let [meaning (first meanings)]
;;            [:button {:on-click #(update-meaning! (assoc meaning :wordform @wordform-atom :gloss @gloss-atom))} "Update"]))
;;        ;; [:div (str (vals filters))]
;;        ;; [:div {} (str "filter: " (filter-meanings filters))]
;;        ])))

(defn wordform-editor [filters]
  (r/with-let [wordform-atom (r/atom "")
               gloss-atom (r/atom "")
               initial-meanings-atom (r/atom nil)
               ;; meaning-id->wordform (r/atom {})
               ;; meaning-id->gloss (r/atom {})
               id->meanings-atom (r/atom {})
               ]
    (let [meanings (filter-meanings filters)
          on-change (fn [atm event]
                      (let [v (.. event -target -value)]
                        (reset! atm (clojure.string/triml v)))
                      )]
      (when (not (= meanings @initial-meanings-atom))
        (reset! wordform-atom (:meanings/wordform (first meanings)))
        (reset! gloss-atom    (:meanings/gloss    (first meanings)))
        (reset! initial-meanings-atom meanings)
        (reset! id->meanings-atom
                (into {} (map (fn [meaning] {(:meanings/meaning_id meaning) meaning}) meanings))
                ;; (clojure.set/index meanings [:meanings/meaning_id])
                )
        )
      [:div {:class "border-l-2 mb-1"}
       (when (empty? @initial-meanings-atom)
         [:div
          [:input {:value (str @wordform-atom)
                   :title (vals filters)
                   :on-change #(on-change wordform-atom %)}]
          [:input {:value (str @gloss-atom)
                   :on-change #(on-change gloss-atom %)}]
          [:button {:on-click #(create-meaning filters @wordform-atom @gloss-atom)} "Create"]])
       (when (> (count meanings) 0)
         (doall (map (fn [meaning]
                       (let [id (:meanings/meaning_id meaning)
                             wordform-cursor (r/cursor id->meanings-atom [id :meanings/wordform])
                             gloss-cursor (r/cursor id->meanings-atom [id :meanings/gloss])]
                         [:div
                          ;; [:div {} id]
                          ;; [:div {} (str "id->meanings: " @id->meanings-atom)]
                          ;; [:div {} (str "meaning: " @wordform-cursor)]
                          [:input {:value (str @wordform-cursor)
                                   :title (vals filters)
                                   :on-change #(on-change wordform-cursor %)}]
                          [:input {:value (str @gloss-cursor)
                                   :on-change #(on-change gloss-cursor %)}]
                          [:button {:on-click #(update-meaning! (assoc meaning :meanings/wordform @wordform-cursor :meanings/gloss @gloss-cursor))} "Update"]
                          ])
                       )
                     meanings)))
       ;; [:div (str (vals filters))]
       ;; [:div {} (str "filter: " (filter-meanings filters))]
       ])))

(defn header-with-collapse [k title]
  (let [collapsed? (k @collapsed-cursor)]
    [:div {:style {:display :flex :align-items :center}}
     [:h2 title]
     [:button {:style {:height "30px" :margin-left "10px"}
               :on-click #(swap! collapsed-cursor assoc k (not collapsed?))}
      (if collapsed? "+" "-")]]))

(defn adverb-editor []
  [:div
   [:h2 "Adverb"]
   [wordform-editor {:meanings/part_of_speech "adverb"}]])

(defn conjunction-editor []
  [:div
   [:h2 "Conjunction"]
   [wordform-editor {:meanings/part_of_speech "conjunction"}]])

(defn preposition-editor []
  [:div
   [:h2 "Preposition"]
   [wordform-editor {:meanings/part_of_speech "preposition"}]])

(defn interjection-editor []
  [:div
   [:h2 "Interjection"]
   [wordform-editor {:meanings/part_of_speech "interjection"}]])

;; (defn noun-editor []
;;   (let [collapsed? (:noun-editor @collapsed-cursor)]
;;     [:div {:style {:margin "10px"}}
;;      [header-with-collapse :noun-editor "Noun"]
;;      (when (not collapsed?)
;;        [:<>
;;         [:div {:style {:display :flex}}
;;          [:div {:style {:width "50%"}}
;;           [wordform-editor {:meanings/part_of_speech "noun" :meanings/number "singular" :meanings/case_ "nominative"}]
;;           [wordform-editor {:meanings/part_of_speech "noun" :meanings/number "singular" :meanings/case_ "genitive"}]
;;           [wordform-editor {:meanings/part_of_speech "noun" :meanings/number "singular" :meanings/case_ "dative"}]
;;           [wordform-editor {:meanings/part_of_speech "noun" :meanings/number "singular" :meanings/case_ "accusative"}]
;;           [wordform-editor {:meanings/part_of_speech "noun" :meanings/number "singular" :meanings/case_ "ablative"}]
;;           [wordform-editor {:meanings/part_of_speech "noun" :meanings/number "singular" :meanings/case_ "vocative"}]]
;;          [:div {:style {:width "50%"}}
;;           [wordform-editor {:meanings/part_of_speech "noun" :meanings/number "plural" :meanings/case_ "nominative"}]
;;           [wordform-editor {:meanings/part_of_speech "noun" :meanings/number "plural" :meanings/case_ "genitive"}]
;;           [wordform-editor {:meanings/part_of_speech "noun" :meanings/number "plural" :meanings/case_ "dative"}]
;;           [wordform-editor {:meanings/part_of_speech "noun" :meanings/number "plural" :meanings/case_ "accusative"}]
;;           [wordform-editor {:meanings/part_of_speech "noun" :meanings/number "plural" :meanings/case_ "ablative"}]
;;           [wordform-editor {:meanings/part_of_speech "noun" :meanings/number "plural" :meanings/case_ "vocative"}]]]
;;         [wordform-editor {:meanings/part_of_speech "noun" :meanings/case_ "locative"}]])]))

(defn noun-editor []
  (r/with-let [selected-gender (r/atom "masculine")]
    (let [collapsed? (:noun-editor @collapsed-cursor)
          gender-options ["masculine" "feminine" "neuter" "common"]
          no-nouns-yet? (empty? (filter-meanings {:meanings/part_of_speech "noun"}))]
      [:div {:style {:margin "10px"}}
       [header-with-collapse :noun-editor "Noun"]
       (when no-nouns-yet?
         [:div {}
          [:label {} "Show gender: "]
          [:select
           {:value  @selected-gender 
            :on-change #(reset! selected-gender (.. % -target -value))}
           (for [opt gender-options]
             ^{:key opt}
             [:option {:value opt} opt])]])
       (when (not collapsed?)
         (doall
          (map (fn [gender]
                 (when (or (and no-nouns-yet? (= gender @selected-gender))
                           (not (empty?
                                 (filter-meanings {:meanings/part_of_speech "noun" :meanings/gender gender}))))
                   ^{:key (str"noun-editor_" gender)}
                   [:<>
                    [:h4 (clojure.string/upper-case gender)]
                    [:div {:style {:display :flex}}
                     [:div {:style {:width "50%"}}
                      [wordform-editor {:meanings/part_of_speech "noun" :meanings/number "singular" :meanings/case_ "nominative" :meanings/gender gender}]
                      [wordform-editor {:meanings/part_of_speech "noun" :meanings/number "singular" :meanings/case_ "genitive" :meanings/gender gender}]
                      [wordform-editor {:meanings/part_of_speech "noun" :meanings/number "singular" :meanings/case_ "dative" :meanings/gender gender}]
                      [wordform-editor {:meanings/part_of_speech "noun" :meanings/number "singular" :meanings/case_ "accusative" :meanings/gender gender}]
                      [wordform-editor {:meanings/part_of_speech "noun" :meanings/number "singular" :meanings/case_ "ablative" :meanings/gender gender}]
                      [wordform-editor {:meanings/part_of_speech "noun" :meanings/number "singular" :meanings/case_ "vocative" :meanings/gender gender}]]
                     [:div {:style {:width "50%"}}
                      [wordform-editor {:meanings/part_of_speech "noun" :meanings/number "plural" :meanings/case_ "nominative" :meanings/gender gender}]
                      [wordform-editor {:meanings/part_of_speech "noun" :meanings/number "plural" :meanings/case_ "genitive" :meanings/gender gender}]
                      [wordform-editor {:meanings/part_of_speech "noun" :meanings/number "plural" :meanings/case_ "dative" :meanings/gender gender}]
                      [wordform-editor {:meanings/part_of_speech "noun" :meanings/number "plural" :meanings/case_ "accusative" :meanings/gender gender}]
                      [wordform-editor {:meanings/part_of_speech "noun" :meanings/number "plural" :meanings/case_ "ablative" :meanings/gender gender}]
                      [wordform-editor {:meanings/part_of_speech "noun" :meanings/number "plural" :meanings/case_ "vocative" :meanings/gender gender}]]]
                    [wordform-editor {:meanings/part_of_speech "noun" :meanings/case_ "locative"}]]))
               gender-options)))])))

(defn five-by-two [filters title]
  [:div {}
   (when title [:h3 title])
   [:div {:style {:display :flex}}
    [:div {:style {:width "50%"}}
     [wordform-editor (merge filters {:meanings/number "singular" :meanings/case_ "nominative"})]
     [wordform-editor (merge filters {:meanings/number "singular" :meanings/case_ "genitive"})]
     [wordform-editor (merge filters {:meanings/number "singular" :meanings/case_ "dative"})]
     [wordform-editor (merge filters {:meanings/number "singular" :meanings/case_ "accusative"})]
     [wordform-editor (merge filters {:meanings/number "singular" :meanings/case_ "ablative"})]
     ]
    [:div {:style {:width "50%"}}
     [wordform-editor (merge filters {:meanings/number "plural" :meanings/case_ "nominative"})]
     [wordform-editor (merge filters {:meanings/number "plural" :meanings/case_ "genitive"})]
     [wordform-editor (merge filters {:meanings/number "plural" :meanings/case_ "dative"})]
     [wordform-editor (merge filters {:meanings/number "plural" :meanings/case_ "accusative"})]
     [wordform-editor (merge filters {:meanings/number "plural" :meanings/case_ "ablative"})]
     ]]])

;; (defn three-by-two [filters title]
;;   [:div {}
;;    (when title [:h3 title])
;;    [:div {:style {:display :flex}}
;;     [:div {:style {:width "50%"}}
;;      [wordform-editor (merge filters {:meanings/number "singular" :meanings/person 1})]
;;      [wordform-editor (merge filters {:meanings/number "singular" :meanings/person 2})]
;;      [wordform-editor (merge filters {:meanings/number "singular" :meanings/person 3})]
;;      ]
;;     [:div {:style {:width "50%"}}
;;      [wordform-editor (merge filters {:meanings/number "plural" :meanings/person 1})]
;;      [wordform-editor (merge filters {:meanings/number "plural" :meanings/person 2})]
;;      [wordform-editor (merge filters {:meanings/number "plural" :meanings/person 3})]]]])

(defn filters->title [filters]
  (let [cap (fn [s] (when s (clojure.string/capitalize s)))]
    (clojure.string/join
     " "
     [(-> filters :meanings/voice cap)
      (-> filters :meanings/tense cap)
      (-> filters :meanings/mood  cap)])))

(defn three-by-two
  ([filters]
   [three-by-two filters (filters->title filters)])
  ([filters title]
   (let [style {:vertical-align :top}]
     [:div {}
      (when title [:h3 title])
      [:table
       [:tr 
        [:td {:style style} [wordform-editor (merge filters {:meanings/number "singular" :meanings/person 1})]]
        [:td {:style style} [wordform-editor (merge filters {:meanings/number "plural" :meanings/person 1})]]]
       [:tr 
        [:td {:style style} [wordform-editor (merge filters {:meanings/number "singular" :meanings/person 2})]]
        [:td {:style style} [wordform-editor (merge filters {:meanings/number "plural" :meanings/person 2})]]]
       [:tr 
        [:td {:style style} [wordform-editor (merge filters {:meanings/number "singular" :meanings/person 3})]]
        [:td {:style style} [wordform-editor (merge filters {:meanings/number "plural" :meanings/person 3})]]]]])))

(defn adjective-editor []
  (let [collapsed? (:adjective-editor @collapsed-cursor)]
    [:div {:style {:margin "10px"}}
     [header-with-collapse :adjective-editor "Adjective"]
     (when (not collapsed?)
       [:<>
        [five-by-two {:meanings/part_of_speech "adjective"
                      :meanings/degree "positive"
                      :meanings/gender "masculine"} "Positive Masculine"]
        [five-by-two {:meanings/part_of_speech "adjective"
                      :meanings/degree "positive"
                      :meanings/gender "feminine"} "Positive Feminine"]
        [five-by-two {:meanings/part_of_speech "adjective"
                      :meanings/degree "positive"
                      :meanings/gender "neuter"} "Positive Neuter"]
        [five-by-two {:meanings/part_of_speech "adjective"
                      :meanings/degree "positive"
                      :meanings/gender "common"} "Positive Common"]
        [five-by-two {:meanings/part_of_speech "adjective"
                      :meanings/degree "comparative"
                      :meanings/gender "masculine"} "Comparative Masculine"]
        [five-by-two {:meanings/part_of_speech "adjective"
                      :meanings/degree "comparative"
                      :meanings/gender "feminine"} "Comparative Feminine"]
        [five-by-two {:meanings/part_of_speech "adjective"
                      :meanings/degree "comparative"
                      :meanings/gender "neuter"} "Comparative Neuter"]
        [five-by-two {:meanings/part_of_speech "adjective"
                      :meanings/degree "superlative"
                      :meanings/gender "masculine"} "Superlative Masculine"]
        [five-by-two {:meanings/part_of_speech "adjective"
                      :meanings/degree "superlative"
                      :meanings/gender "feminine"} "Superlative Feminine"]
        [five-by-two {:meanings/part_of_speech "adjective"
                      :meanings/degree "superlative"
                      :meanings/gender "neuter"} "Superlative Neuter"]])
     ]))

(defn pronoun-editor []
  (let [collapsed? (:pronoun-editor @collapsed-cursor)]
    [:div {:style {:margin "10px"}}
     [header-with-collapse :pronoun-editor "Pronoun"]
     (when (not collapsed?)
       [:<>
        [five-by-two {:meanings/part_of_speech "pronoun"
                      :meanings/gender "masculine"} "Masculine"]
        [five-by-two {:meanings/part_of_speech "pronoun"
                      :meanings/gender "feminine"} "Feminine"]
        [five-by-two {:meanings/part_of_speech "pronoun"
                      :meanings/gender "neuter"} "Neuter"]])]))

(defn participle-editors [gender]
  [:<>
   [five-by-two {:meanings/part_of_speech "participle"
                 :meanings/voice "active"
                 :meanings/gender gender
                 :meanings/tense "present"
                 }
    (str "Present Active " (clojure.string/capitalize gender) " Participle")]
   [five-by-two {:meanings/part_of_speech "participle"
                      :meanings/voice "passive"
                      :meanings/gender gender
                      :meanings/tense "perfect"
                      }
    (str "Perfect Passive " (clojure.string/capitalize gender) " Participle")]
   [five-by-two {:meanings/part_of_speech "participle"
                      :meanings/voice "active"
                      :meanings/gender gender
                      :meanings/tense "future"
                      }
    (str "Future Active " (clojure.string/capitalize gender) " Participle")]])

(defn verb-editor []
  (r/with-let [noun-tab-atom (r/atom "active")]
    [:div {:style {:margin "10px"}}
     [:h2 "Verb"]
     [:div
      (doall
       (map (fn [tab-name]
              [:button
               {:style
                {:font-weight (if (= tab-name @noun-tab-atom)
                                :bold :normal)}
                :on-click (fn [e] (reset! noun-tab-atom tab-name))}
               tab-name])
            ["active" "passive" "masculine participle" "feminine participle" "neuter participle"]))]
     (case @noun-tab-atom
       "masculine participle" [participle-editors "masculine"]
       "feminine participle"  [participle-editors "feminine"]
       "neuter participle"    [participle-editors "neuter"]
       ;;
       "passive"
       [:<>
        [three-by-two {:meanings/part_of_speech "verb"
                       :meanings/voice "passive"
                       :meanings/tense "present"
                       :meanings/mood "indicative"
                       }]
        [three-by-two {:meanings/part_of_speech "verb"
                       :meanings/voice "passive"
                       :meanings/tense "imperfect"
                       :meanings/mood "indicative"
                       }]
        [three-by-two {:meanings/part_of_speech "verb"
                       :meanings/voice "passive"
                       :meanings/tense "future"
                       :meanings/mood "indicative"
                       }]
        ]
       ;; default is active
       [:<>
        [three-by-two {:meanings/part_of_speech "verb"
                       :meanings/voice "active"
                       :meanings/tense "present"
                       :meanings/mood "indicative"}]
        [three-by-two {:meanings/part_of_speech "verb"
                       :meanings/voice "active"
                       :meanings/tense "imperfect"
                       :meanings/mood "indicative"}]
        [three-by-two {:meanings/part_of_speech "verb"
                       :meanings/voice "active"
                       :meanings/tense "perfect"
                       :meanings/mood "indicative"}]
        [three-by-two {:meanings/part_of_speech "verb"
                       :meanings/voice "active"
                       :meanings/tense "future"
                       :meanings/mood "indicative"}]
        [three-by-two {:meanings/part_of_speech "verb"
                       :meanings/voice "active"
                       :meanings/tense "pluperfect"
                       :meanings/mood "indicative"}]
        [three-by-two {:meanings/part_of_speech "verb"
                       :meanings/voice "active"
                       :meanings/tense "present"
                       :meanings/mood "subjunctive"}]
        [three-by-two {:meanings/part_of_speech "verb"
                       :meanings/voice "active"
                       :meanings/tense "imperfect"
                       :meanings/mood "subjunctive"}]
        [:div
         [:h3 "Imperative"
          [wordform-editor
           {:meanings/part_of_speech "verb"
            :meanings/mood "imperative"
            :meanings/voice "active"
            :meanings/number "singular"}]
          [wordform-editor
           {:meanings/part_of_speech "verb"
            :meanings/mood "imperative"
            :meanings/voice "active"
            :meanings/number "plural"}]]]
        [:div
         [:h3 "Infinitive"
          [wordform-editor
           {:meanings/part_of_speech "verb"
            :meanings/mood "infinitive"
            :meanings/voice "active"
            :meanings/tense "present"}]
          [wordform-editor
           {:meanings/part_of_speech "verb"
            :meanings/mood "infinitive"
            :meanings/voice "active"
            :meanings/number "perfect"}]
          [wordform-editor
           {:meanings/part_of_speech "verb"
            :meanings/mood "infinitive"
            :meanings/voice "passive"
            :meanings/tense "present"}]]]]

       )]))
(defn pos-tabs []
  [:div
   (doall
    (map (fn [pos]
           (let [has-entries? (not (empty? (filter-meanings {:meanings/part_of_speech pos})))]
             ^{:key pos}
             [:button {:style {:font-weight (if (= pos @selected-pos-cursor) :bold :normal)
                               :color (if has-entries? "green" "inherit")}
                       :on-click #(reset! selected-pos-cursor pos)}
              pos]))
         ["adjective" "adverb" "conjunction" "interjection" "noun" "preposition" "pronoun" "verb"]))])

(defn create-lexeme [lexeme]
  (->
   (js/fetch
    "/lexeme/create"
    #js {:method "POST"
         :headers #js {"Content-Type" "application/json"}
         :body (js/JSON.stringify
                (clj->js
                 {:lexeme-dictionary-form @lexeme-dictionary-form-in-search}))})
   (.then (fn [v]
            (.json v)))
   (.then (fn [v]
            ;; (insert-meaning (:data (->clj v)))
            (reset! lexeme-cursor (:data (->clj v)))
            (println (:data (->clj v)))
            ))))

(defn lexeme-editor []
  [:div
   [:h2 "Lexeme Editor"]
   [lexeme-box]
   (if @lexeme-id-cursor
     [:<>
      [pos-tabs]
      (case @selected-pos-cursor
        "adjective" [adjective-editor]
        "adverb" [adverb-editor]
        "conjunction" [conjunction-editor]
        "interjection" [interjection-editor]
        "noun" [noun-editor]
        "preposition" [preposition-editor]
        "pronoun" [pronoun-editor]
        "verb"    [verb-editor]
        [:div {} "Select a part of speech"])]
     [:button {:on-click create-lexeme
               :disabled (empty? @lexeme-dictionary-form-in-search)}
      "Create lexeme"])
   ])
