(ns latin-texts-db.texts
  (:require [next.jdbc :as jdbc]
            [migratus.core :as migratus]
            [honey.sql :as sql]
            [honey.sql.helpers :as h]
            [latin-texts-db.db :as db :refer [ds do! insert-token-into-db* get-potential-meanings-of-wordform]]))

(defn insert-text! [text-title text-contents-as-string]
  (let [tokens (clojure.string/split text-contents-as-string #" ")
        text-insert-result (do! {:insert-into [:texts]
                                 :values [{:title text-title}]
                                 :returning :text_id})
        text-id (:texts/text_id (first text-insert-result))]
    (when (nil? text-id)
      (throw (new Exception "text-id not found after attempted insert")))
    (println "text-id: " text-id tokens)
    (insert-token-into-db* text-id nil tokens)
    ))

(defn get-text-as-string [text-id n]
  (let [fetched-tokens (atom [])
        first-token (-> (do! {:select [:token_id :wordform :next_token_id :punctuation_preceding :punctuation_trailing]
                              :from :tokens
                              :where [:and
                                      [:= :text-id text-id]
                                      [:= :prev-token-id nil]]})
                        first)
        next-token-id (atom (:tokens/next_token_id first-token))]
    (swap! fetched-tokens conj (str (:tokens/punctuation_preceding first-token)
                                    (:tokens/wordform first-token)
                                    (:tokens/punctuation_trailing first-token)))
    (doall
     (for [x (range 1 n)
           :when @next-token-id]
       (do
         (let [new-token (-> (do! {:select [:token_id :wordform :next_token_id :punctuation_preceding :punctuation_trailing]
                                   :from :tokens
                                   :where [:= :token_id @next-token-id]})
                             first)]
           (swap! fetched-tokens
                  conj
                  (str (:tokens/punctuation_preceding new-token)
                       (:tokens/wordform new-token)
                       (:tokens/punctuation_trailing new-token)))
           (reset! next-token-id (:tokens/next_token_id new-token)))
         )))
    (clojure.string/join " " @fetched-tokens)))

(defn get-text-edn [text-id n]
  (let [fetched-tokens (atom [])
        ;;
        get-token-data
        (fn [token]
          ;; todo check for :meaning
          (as-> token $
            (if-let [v (:tokens/meaning_id $)]
              (let [meaning (db/id->meaning v)]
                (assoc $ :meaning
                       (assoc meaning
                              :lexeme
                              (db/get-lexeme-for-meaning meaning))))
              $)
            (assoc $ :potential-meanings
                   (get-potential-meanings-of-wordform
                    (:tokens/wordform token)))
            (assoc $ :lexeme (db/get-lexeme-for-token token))))
        ;;
        first-token (-> (do! {:select [:*]
                              :from :tokens
                              :where [:and
                                      [:= :text-id text-id]
                                      [:= :prev-token-id nil]]})
                        first)
        next-token-id (atom (:tokens/next_token_id first-token))]
    (swap! fetched-tokens conj (get-token-data first-token))
    (doall
     (for [x (range 1 n)
           :when @next-token-id]
       (do
         (let [new-token (-> (do! {:select [:*]
                                   :from :tokens
                                   :where [:= :token_id @next-token-id]})
                             first)]
           (swap! fetched-tokens
                  conj (get-token-data new-token))
           (reset! next-token-id (:tokens/next_token_id new-token))))))
    @fetched-tokens))
