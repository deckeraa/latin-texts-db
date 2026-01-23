(ns latin-texts.app
  (:require [reagent.core :as r]
            [reagent.dom.client :as rd-client]))

(defn text-fetcher-component []
  (let [text-edn (r/atom nil)]
    [:button {} "Fetch"]))

(defn root-component []
  [:div
   [:h1 "Latin Texts DB"]
   [text-fetcher-component]
   ])

(defn ^:export init []
  (let [root-el (js/document.getElementById "app")
        react-root (rd-client/create-root root-el)]
    (rd-client/render react-root [root-component])))
