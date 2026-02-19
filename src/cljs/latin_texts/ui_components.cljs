(ns latin-texts.ui-components
  (:require [reagent.core :as r]
            [reagent.dom.client :as rd-client]
            [cljs.core.async :refer [go <! chan put!]]
            [cljs-bean.core :refer [->clj]]
            [promesa.core :as p]
            [cljs.reader :as reader]))

(defn labeled-field
  ([state-atom k label placeholder]
   (labeled-field state-atom k label placeholder {:input-attrs {}}))
  ([state-atom k label placeholder {:keys [input-attrs on-blur]}]
   (let [on-change
         (fn [v]
           (let [s (.. v -target -value)
                 v* (-> s
                        clojure.string/triml
                        (clojure.string/replace #"\t" ", "))]
             (swap! state-atom assoc k v*)))]
     [:div {}
      [:label {} label]
      [:input
       (merge {:value (or (get @state-atom k) "")
               :on-change on-change
               :on-blur #(when on-blur (on-blur (.. % -target -value)))
               :placeholder placeholder}
              input-attrs)]])))

(defn labeled-checkbox [state-atom k label]
  [:div {}
   [:label {} label]
   [:input {:type "checkbox"
            :checked (get @state-atom k false)
            :on-change #(swap! state-atom assoc k (.. % -target -checked))}]])

(defn labeled-dropdown
  [state-atom k label options]
  [:div {}
   [:label {} label]
   [:select
    {:value (get @state-atom k "")
     :on-change #(swap! state-atom assoc k (.. % -target -value))}
    (for [opt options]
      ^{:key opt}
      [:option {:value opt} opt])]])

(defn gender-dropdown
  [state-atom k]
  (labeled-dropdown state-atom
                    k
                    "Gender: "
                    ["masculine" "feminine" "neuter" "common"]))
