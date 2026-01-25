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
  (r/with-let [lexeme-atom (r/atom "pāx, pācis")]
    [:div {}
     [:input {:value (str @lexeme-atom)
              :on-change #(reset! lexeme-atom (.. % -target -value))}]
     [:button {:on-click #(fetch-lexeme-with-meanings @lexeme-atom)} "Search"]
     ])
  )

(defn wordform-editor [filters]
  (r/with-let [sa (r/atom "")]
    [:div {}
     [:input {:value (str @sa)
              :on-change #(reset! sa (.. % -target -value))}]
     [:div (str (vals filters))]
     ]))

(defn lexeme-editor []
  [:div
   [:h2 "Lexeme Editor"]
   [lexeme-box]
   [wordform-editor {:meanings/part_of_speech "noun" :meanings/number "singular" :meanings/case_ "nominative"}]
   [:div {} (str "filtered: " (filter-meanings {:meanings/part_of_speech "noun" :meanings/number "singular" :meanings/case_ "nominative"}))]
   ])
