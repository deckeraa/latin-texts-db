(ns latin-texts.lexeme-editor
  (:require [reagent.core :as r]
            [reagent.dom.client :as rd-client]
            [cljs.core.async :refer [go <! chan put!]]
            [cljs-bean.core :refer [->clj]]
            [promesa.core :as p]
            [cljs.reader :as reader]
            ))

(defonce lexeme-editor-state (r/atom {:lexeme nil
                                      :meanings []}))

;; input to type in lexical form
;; search-as-you-type would be nice
;; hitting the search button runs ll, creating the lexical form if not found
;; the lexical form is returned along with all meanings associated with that lexeme
;; there's then a function called meaning-lookup that takes characteristics and filters all meanings that have those
;; then I have bunch of boxes for nouns, verbs, etc. that all use that function
;; each box has a little edit star
;; one button at the bottom to save down all changes at one and reload the page

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
  (r/with-let [lexeme-atom (r/atom "ruber, rubra, rubrum")]
    [:div {}
     [:input {:value (str @lexeme-atom)
              :on-change #(reset! lexeme-atom (.. % -target -value))}]
     [:button {:on-click #(fetch-lexeme-with-meanings @lexeme-atom)} "Search"]
     ])
  )

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
                :on-change #(reset! wordform-atom (.. % -target -value))}]
       [:input {:value (str @gloss-atom)
                :on-change #(reset! gloss-atom (.. % -target -value))}]
       ;; [:div (str (vals filters))]
       ;; [:div {} (str "filter: " (filter-meanings filters))]
       ])))

(defn noun-editor []
  [:div {:style {:margin "10px"}}
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
   [wordform-editor {:meanings/part_of_speech "noun" :meanings/case_ "locative"}]])

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

(defn adjective-editor []
  [:div {:style {:margin "10px"}}
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
                 :meanings/gender "neuter"} "Superlative Neuter"]
   ])

(defn lexeme-editor []
  [:div
   [:h2 "Lexeme Editor"]
   [lexeme-box]
   [noun-editor]
   [adjective-editor]
   ;; [wordform-editor {:meanings/part_of_speech "noun" :meanings/number "singular" :meanings/case_ "nominative"}]
   ;; [:div {} (str "filtered: " (filter-meanings {:meanings/part_of_speech "noun" :meanings/number "singular" :meanings/case_ "nominative"}))]
   ])
