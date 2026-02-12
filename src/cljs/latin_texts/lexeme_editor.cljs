(ns latin-texts.lexeme-editor
  (:require [reagent.core :as r]
            [reagent.dom.client :as rd-client]
            [cljs.core.async :refer [go <! chan put!]]
            [cljs-bean.core :refer [->clj]]
            [promesa.core :as p]
            [cljs.reader :as reader]
            [latin-texts.autocomplete :refer [autocomplete]]
            ))

(defonce lexeme-editor-state
  (r/atom {:lexeme nil
           :lexeme-dictionary-form-in-search ""
           :known-lexemes []
           :meanings []
           :collapsed {}}))

(def lexeme-id-cursor
  (r/cursor lexeme-editor-state [:lexeme :lexemes/lexeme_id]))

(def lexeme-dictionary-form-in-search
  (r/cursor lexeme-editor-state [:lexeme-dictionary-form-in-search]))

(def known-lexemes
  (r/cursor lexeme-editor-state [:known-lexemes]))

(def collapsed-cursor
  (r/cursor lexeme-editor-state [:collapsed]))

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
                         :meanings (:meanings r)))))))

(def meanings-cursor (r/cursor lexeme-editor-state [:meanings]))

(defn filter-meanings [filter-map]
  (filter (fn [meaning]
            (empty? (remove (fn [k] (= (get meaning k)
                                       (get filter-map k)))
                            (keys filter-map))))
          @meanings-cursor))

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
     [:span {} (str @lexeme-id-cursor)]])
  )

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
            (println v)
            (.json v)))
   (.then (fn [v]
            (println "Got meaning: " v)
            ;; (update-token (reader/read-string (:data (->clj v))))
            ))))

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

(defn wordform-editor [filters]
  (r/with-let [wordform-atom (r/atom "")
               gloss-atom (r/atom "")
               initial-meanings-atom (r/atom nil)]
    (let [meanings (filter-meanings filters)]
      (when (not (= meanings @initial-meanings-atom))
        (reset! wordform-atom (:meanings/wordform (first meanings)))
        (reset! gloss-atom    (:meanings/gloss    (first meanings)))
        (reset! initial-meanings-atom meanings))
      [:div {:style {:background-color (when (> (count meanings) 1) "red")}}
       [:input {:value (str @wordform-atom)
                :title (vals filters)
                :on-change #(reset! wordform-atom (.. % -target -value))}]
       [:input {:value (str @gloss-atom)
                :on-change #(reset! gloss-atom (.. % -target -value))}]
       (when (empty? @initial-meanings-atom)
         [:button {:on-click #(create-meaning filters @wordform-atom @gloss-atom)} "Create"])
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

(defn noun-editor []
  (let [collapsed? (:noun-editor @collapsed-cursor)]
    [:div {:style {:margin "10px"}}
     [header-with-collapse :noun-editor "Noun"]
     (when (not collapsed?)
       [:<>
        [:div {:style {:display :flex}}
         [:div {:style {:width "50%"}}
          [wordform-editor {:meanings/part_of_speech "noun" :meanings/number "singular" :meanings/case_ "nominative"}]
          [wordform-editor {:meanings/part_of_speech "noun" :meanings/number "singular" :meanings/case_ "genitive"}]
          [wordform-editor {:meanings/part_of_speech "noun" :meanings/number "singular" :meanings/case_ "dative"}]
          [wordform-editor {:meanings/part_of_speech "noun" :meanings/number "singular" :meanings/case_ "accusative"}]
          [wordform-editor {:meanings/part_of_speech "noun" :meanings/number "singular" :meanings/case_ "ablative"}]
          [wordform-editor {:meanings/part_of_speech "noun" :meanings/number "singular" :meanings/case_ "vocative"}]]
         [:div {:style {:width "50%"}}
          [wordform-editor {:meanings/part_of_speech "noun" :meanings/number "plural" :meanings/case_ "nominative"}]
          [wordform-editor {:meanings/part_of_speech "noun" :meanings/number "plural" :meanings/case_ "genitive"}]
          [wordform-editor {:meanings/part_of_speech "noun" :meanings/number "plural" :meanings/case_ "dative"}]
          [wordform-editor {:meanings/part_of_speech "noun" :meanings/number "plural" :meanings/case_ "accusative"}]
          [wordform-editor {:meanings/part_of_speech "noun" :meanings/number "plural" :meanings/case_ "ablative"}]
          [wordform-editor {:meanings/part_of_speech "noun" :meanings/number "plural" :meanings/case_ "vocative"}]]]
        [wordform-editor {:meanings/part_of_speech "noun" :meanings/case_ "locative"}]])]))

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

(defn three-by-two [filters title]
  [:div {}
   (when title [:h3 title])
   [:div {:style {:display :flex}}
    [:div {:style {:width "50%"}}
     [wordform-editor (merge filters {:meanings/number "singular" :meanings/person 1})]
     [wordform-editor (merge filters {:meanings/number "singular" :meanings/person 2})]
     [wordform-editor (merge filters {:meanings/number "singular" :meanings/person 3})]
     ]
    [:div {:style {:width "50%"}}
     [wordform-editor (merge filters {:meanings/number "plural" :meanings/person 1})]
     [wordform-editor (merge filters {:meanings/number "plural" :meanings/person 2})]
     [wordform-editor (merge filters {:meanings/number "plural" :meanings/person 3})]]]])

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

(defn verb-editor []
  (let [collapsed? (:verb-editor @collapsed-cursor)]
    [:div {:style {:margin "10px"}}
     [header-with-collapse :verb-editor "Verb"]
     (when (not collapsed?)
       [:<>
        [three-by-two {:meanings/part_of_speech "verb"
                       :meanings/voice "active"
                       :meanings/tense "present"}
         "Present Active"]
        [three-by-two {:meanings/part_of_speech "verb"
                       :meanings/voice "active"
                       :meanings/tense "imperfect"}
         "Imperfect Active"]
        [three-by-two {:meanings/part_of_speech "verb"
                       :meanings/voice "passive"
                       :meanings/tense "present"}
         "Present Passive"]
        [five-by-two {:meanings/part_of_speech "participle"
                      :meanings/voice "active"
                      :meanings/gender "masculine"
                      :meanings/tense "present"
                      }
         "Present Active Masculine Participle"]
        [five-by-two {:meanings/part_of_speech "participle"
                      :meanings/voice "active"
                      :meanings/gender "feminine"
                      :meanings/tense "present"
                      }
         "Present Active Feminine Participle"]
        [five-by-two {:meanings/part_of_speech "participle"
                      :meanings/voice "active"
                      :meanings/gender "neuter"
                      :meanings/tense "present"
                      }
         "Present Active Neuter Participle"]
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
            :meanings/tense "present"}]]]])]))


(defn lexeme-editor []
  [:div
   [:h2 "Lexeme Editor"]
   [lexeme-box]
   [conjunction-editor]
   [adverb-editor]
   [noun-editor]
   [adjective-editor]
   [pronoun-editor]
   [verb-editor]
   [preposition-editor]
   [interjection-editor]
   ])
