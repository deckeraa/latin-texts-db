(ns latin-texts.app
  (:require [reagent.core :as r]
            [reagent.dom.client :as rd-client]))

(defn root-component []
  [:div
   [:h1 "Hello from shadow-cljs + Reagent 2.0.1"]
   [:p "Backend served this page from Compojure."]])

(defn ^:export init []
  (let [root-el (js/document.getElementById "app")
        react-root (rd-client/create-root root-el)]
    (rd-client/render react-root [root-component])))
