(ns latin-texts.bulk-insert
  (:require [reagent.core :as r]
            [reagent.dom.client :as rd-client]
            [cljs.core.async :refer [go <! chan put!]]
            [cljs-bean.core :refer [->clj]]
            [promesa.core :as p]
            [cljs.reader :as reader]))

(defn labeled-field [state-atom k label placeholder]
  [:div {}
   [:label {} label]
   [:input {:value (or (get @state-atom k) "")
            :on-change (fn [v]
                         (swap! state-atom assoc k (.. v -target -value)))
            :placeholder placeholder}]])

(defn labeled-checkbox [state-atom k label]
  [:div {}
   [:label {} label]
   [:input {:type "checkbox"
            :checked (get @state-atom k false)
            :on-change #(swap! state-atom assoc k (.. % -target -checked))}]])

(defn call-bulk-verb-insert-endpoint [m]
  (->
   (js/fetch
    "/bulk-insert/verb"
    #js {:method "POST"
         :headers #js {"Content-Type" "application/json"}
         :body (js/JSON.stringify
                (clj->js m))})
   (.then (fn [v]
            (println v)
            ;; TODO a toast message for success would be nice
            ;;(.json v)
            ))))

(defn call-bulk-noun-insert-endpoint [m]
  (->
   (js/fetch
    "/bulk-insert/noun"
    #js {:method "POST"
         :headers #js {"Content-Type" "application/json"}
         :body (js/JSON.stringify
                (clj->js m))})
   (.then (fn [v]
            (println v)
            ;; TODO a toast message for success would be nice
            ;;(.json v)
            ))))

(defn call-bulk-adjective-insert-endpoint [m]
  (->
   (js/fetch
    "/bulk-insert/adjective"
    #js {:method "POST"
         :headers #js {"Content-Type" "application/json"}
         :body (js/JSON.stringify
                (clj->js m))})
   (.then (fn [v]
            (println v)
            ;; TODO a toast message for success would be nice
            ;;(.json v)
            ))))

(defn verb-bulk-insert []
  (r/with-let [sa (r/atom {})]
    [:div
     [:h2 "Verb Bulk Insert"]
     (str @sa)
     ;; ["dormiō, dormīre, dormīvī, dormītum" "sleep" "sleeps" "slept" "sleeped" "sleeping"]
     [labeled-field sa :principal-parts "Principal parts" "dormiō, dormīre, dormīvī, dormītum"]
     [labeled-field sa :first-person-present-gloss "1st person present ('now I ____')" "sleep"]
     [labeled-field sa :third-person-present-gloss "3rd person present ('now he ____')" "sleeps"]
     [labeled-field sa :third-person-perfect-gloss "3rd person perfect ('yesterday he ____')" "slept"]
     [labeled-field sa :present-participle-gloss "present participle ('-ing')" "sleeping"]
     [labeled-field sa :perfect-passive-participle-gloss "perfect passive participle ('-ed')" "sleeped"]
     [:button
      {:on-click #(call-bulk-verb-insert-endpoint @sa)
       :disabled (not (empty? (filter nil? (map (fn [k] (get @sa k)) [:principal-parts :first-person-present-gloss :third-person-present-gloss :third-person-perfect-gloss :present-participle-gloss :perfect-passive-participle-gloss]))))}
      "Bulk Insert"]]))

(defn noun-bulk-insert []
  (r/with-let [sa (r/atom {})]
    [:div
     [:h2 "Noun Bulk Insert"]
     (str @sa)
     [labeled-field sa :dictionary-form "Dictionary form" "porcus, porcī"]
     [labeled-field sa :gender "gender" "masculine"] ;; TODO make this a drop-down list
     [labeled-field sa :sn-gloss "singular nominative gloss" "pig"]
     [labeled-field sa :sg-gloss "singular genitive gloss" "pig's"]
     [labeled-field sa :pn-gloss "plural nominative gloss" "pigs"]
     [labeled-field sa :pg-gloss "plural genitive gloss" "of the pigs"]
     [:button
      {:on-click #(call-bulk-noun-insert-endpoint @sa)
       :disabled (not (empty? (filter nil? (map (fn [k] (get @sa k)) [:dictionary-form :gender :sn-gloss :sg-gloss :pn-gloss :pg-gloss]))))}
      "Bulk Insert"]]))

(defn adjective-bulk-insert []
  (r/with-let [sa (r/atom {})]
    [:div
     [:h2 "Adjective Bulk Insert"]
     (str @sa)
     [labeled-field sa :dictionary-form "Dictionary form" "laetus, laetī, laetum"]
     [labeled-field sa :pos-gloss "positive gloss" "happy"]
     [labeled-field sa :comp-gloss "comparative gloss" "happier"]
     [labeled-field sa :sup-gloss "superlative gloss" "happiest"]
     ;; TODO auto-check booleans if glosses provided
     [labeled-checkbox sa :include-comparative? "Include comparative?"]
     [labeled-checkbox sa :include-superlative? "Include superlative?"]
     [labeled-checkbox sa :gen-ius? "Genitive -ius?"]
     [labeled-checkbox sa :pl-gen-ium? "Plural genitive -ium?"]
     [:button
      {:on-click #(call-bulk-adjective-insert-endpoint @sa)
       :disabled (not (empty? (filter nil? (map (fn [k] (get @sa k)) [:dictionary-form :pos-gloss]))))}
      "Bulk Insert"]]))

(defn bulk-insert []
  [:div
   [:h1 "Bulk Insert"]
   [verb-bulk-insert]
   [noun-bulk-insert]
   [adjective-bulk-insert]
   ])
