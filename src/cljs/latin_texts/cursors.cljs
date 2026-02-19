(ns latin-texts.cursors
  (:require [reagent.core :as r]))

(defonce app-state
  (r/atom {:mode :text
           :text-id nil
           :auto-advance? true
           :selection-start-token-id nil
           :selection-end-token-id nil
           :texts []
           }))
