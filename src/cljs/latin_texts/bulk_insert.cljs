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

(defn bulk-insert []
  [:div
   [:h1 "Bulk Insert"]
   [verb-bulk-insert]
   ])
