(ns latin-texts.app
  (:require [reagent.core :as r]
            [reagent.dom.client :as rd-client]
            [cljs.core.async :refer [go <! chan put!]]
            [cljs-bean.core :refer [->clj]]
            [promesa.core :as p]
            ))

;; (defn fetch-text [text-id]
;;   (let [ch (chan)]
;;     (-> (js/fetch (str "/text-as-string?text-id=" text-id))
;;         (.then #(.json %))                    ;; or .text()
;;         (.then #(put! ch (->clj %)))          ;; ← nice with js→cljs
;;         (.catch #(put! ch {:error %})))
;;     ch)
;;   )

;; (defn fetch-text [text-id]
;;   (p/-> (js/fetch (str "/text-as-string?text-id=" text-id))
;;         .json
;;         ->clj))


(defn fetch-text [text-id]
  (-> (js/fetch (str "/text-as-string?text-id=" text-id))
      (.then (fn [v]
               (println v)
               (.text v)
               ;;(.json v)
               ))                    ;; returns another promise
      ;(.then ->clj)
      ))     

(defn text-fetcher-component []
  (r/with-let [text-edn (r/atom "Initial text")]
    [:div
     [:div {} @text-edn]
     [:button {:on-click
               (fn []
                 (-> (fetch-text 1)
                     (p/then (fn [result]
                               (println "Got result: " result)
                               (reset! text-edn result)))
                     (p/catch (fn [err]
                                (println err)))))}
      "Fetch"]]))

(defn root-component []
  [:div
   [:h1 "Latin Texts DB"]
   [text-fetcher-component]
   ])

(defn ^:export init []
  (let [root-el (js/document.getElementById "app")
        react-root (rd-client/create-root root-el)]
    (rd-client/render react-root [root-component])))
