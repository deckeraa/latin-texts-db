(ns latin-texts.cursors
  (:require [reagent.core :as r]))

(defonce app-state
  (r/atom {:mode :text
           :text-id nil
           :auto-advance? true
           :texts []
           :current-token-id nil
           :current-text-tokens-by-id {}
           :selection-start-token-id nil
           :selection-end-token-id nil
           :selections [] ;; all selections for the current text
           }))

;; :mode
(def mode-cursor
  (r/cursor app-state [:mode]))

(defn set-mode [mode]
  ;; modes are :text, :lexeme-editor, :bulk-insert
  (swap! app-state assoc :mode mode))

;; :text-id
(def text-id-cursor
  (r/cursor app-state [:text-id]))

;; :auto-advance?
(def auto-advance?-cursor
  (r/cursor app-state [:auto-advance?]))

;; :texts
(def texts-cursor
  (r/cursor app-state [:texts]))

;; :current-token-id
(def current-token-id
  (r/cursor app-state [:current-token-id]))

(defn set-current-token! [token]
  (swap! app-state assoc :current-token-id (:tokens/token_id token)))

;; :current-text-tokens-by-id
(def current-text-tokens-by-id
  (r/cursor app-state [:current-text-tokens-by-id]))

(defn current-token []
  (get-in @current-text-tokens-by-id [@current-token-id]))

(defn token-by-id [id]
  (get-in @current-text-tokens-by-id [id]))

;; :selections
(def selections-cursor
  (r/cursor app-state [:selections]))

(defn set-selections [selections]
  (reset! selections-cursor selections))
