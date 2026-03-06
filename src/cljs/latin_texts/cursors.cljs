(ns latin-texts.cursors
  (:require [reagent.core :as r]))

(defonce app-state
  (r/atom {:mode :text
           :text-id nil
           :auto-advance? true
           :texts []
           :text-as-list []
           :current-token-id nil
           :current-text-tokens-by-id {}
           :selection-start-token-id nil
           :selection-end-token-id nil
           :selections [] ;; all selections for the current text
           :hover-selection-start-token-id nil
           :hover-selection-end-token-id nil
           :hover-selection-color ""
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

(defn set-text-id! [text-id]
  (reset! text-id-cursor text-id))

;; :auto-advance?
(def auto-advance?-cursor
  (r/cursor app-state [:auto-advance?]))

;; :texts
(def texts-cursor
  (r/cursor app-state [:texts]))

;; :get-text-as-list
(def get-text-as-list
  (r/cursor app-state [:text-as-list]))

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

;; :selection-start-token-id
(def selection-start-cursor
  (r/cursor app-state [:selection-start-token-id]))

(defn selection-start-token []
  (get @current-text-tokens-by-id @selection-start-cursor))

(defn set-selection-start-token [token-or-token-id]
  (let [token-id (if (map? token-or-token-id)
                   (:tokens/token_id token-or-token-id)
                   token-or-token-id)]
    (println "set-selection-start-token" token-id)
    (swap! app-state assoc
           :selection-start-token-id token-id
           :selection-end-token-id nil
           )))

;; :selection-end-token-id
(def selection-end-cursor
  (r/cursor app-state [:selection-end-token-id]))

(defn selection-end-token []
  (get @current-text-tokens-by-id @selection-end-cursor))

(defn set-selection-end-token [token-or-token-id]
  (let [token-id (if (map? token-or-token-id)
                   (:tokens/token_id token-or-token-id)
                   token-or-token-id)]
    (println "set-selection-end-token" token-id)
    (swap! app-state assoc :selection-end-token-id token-id)))

;; :selections
(def selections-cursor
  (r/cursor app-state [:selections]))

(defn append-selection [selection]
  (swap! selections-cursor conj selection))

(defn set-selections [selections]
  (reset! selections-cursor selections))

(defn selected-tokens-seq
  [start-id end-id]
  (when (and start-id end-id)
    (let [step (fn step [id]
                 (if id
                   (when-let [token (token-by-id id)]
                     (cons token
                           (when-not (= id end-id)
                             (step (:tokens/next_token_id token)))))
                   []))]
      (step start-id))))

(def selected-tokens
  (r/reaction
   (into [] (selected-tokens-seq 
             @selection-start-cursor
             @selection-end-cursor))))

(def selected-tokens-ids
  (r/reaction
   (into #{} (map :tokens/token_id
                  (selected-tokens-seq 
                   @selection-start-cursor
                   @selection-end-cursor)))))

(def selected-tokens-distinct-count
  (r/reaction
   (count
    (distinct (map
               (fn [token]
                 (when-let [s (:tokens/wordform token)]
                   (clojure.string/lower-case s)))
               (selected-tokens-seq 
                @selection-start-cursor
                @selection-end-cursor))))))

;; hover selection
(def hover-selection-start-cursor
  (r/cursor app-state [:hover-selection-start-token-id]))

(def hover-selection-end-cursor
  (r/cursor app-state [:hover-selection-end-token-id]))

(def hover-selection-color
  (r/cursor app-state [:hover-selection-color]))

(defn set-hover-selection [start end color]
  (let [start-id (if (map? start) (:tokens/token_id start) start)
        end-id (if (map? end) (:tokens/token_id end) end)]
    (swap! app-state assoc
           :hover-selection-start-token-id start-id
           :hover-selection-end-token-id end-id
           :hover-selection-color color)))

(defn clear-hover-selection []
  (swap! app-state assoc
           :hover-selection-start-token-id nil
           :hover-selection-end-token-id nil
           :hover-selection-color ""))

(def hover-selected-tokens
  (r/reaction
   (into [] (selected-tokens-seq 
             @hover-selection-start-cursor
             @hover-selection-end-cursor))))

(def hover-selected-tokens-ids
  (r/reaction
   (into #{} (map :tokens/token_id
                  (selected-tokens-seq 
                   @hover-selection-start-cursor
                   @hover-selection-end-cursor)))))
