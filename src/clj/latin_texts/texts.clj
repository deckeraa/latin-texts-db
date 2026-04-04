(ns latin-texts.texts
  (:require [next.jdbc :as jdbc]
            [clojure.java.io :as io]
            [migratus.core :as migratus]
            [honey.sql :as sql]
            [honey.sql.helpers :as h]
            [latin-texts.utils :refer [remove-macrons]]
            [latin-texts.db :as db :refer [ds do! insert-token-into-db* get-potential-meanings-of-wordform]]
            [latin-texts.glossary-utils :refer [parsed-entry generate-single-glossary-entry-using-tokens]]))

(defn tokenize-text [text]
  (let [text-normalized-with-spaces
        (-> text
            (clojure.string/replace #"\n+" "\n ")
            (clojure.string/replace #"\t +" "\t"))
        tokens (remove empty? (clojure.string/split text-normalized-with-spaces #" "))]
    tokens))

(defn get-text-first-token [text-id]
  (-> (do! {:select [:*]
            :from :tokens
            :where [:and
                    [:= :text-id text-id]
                    [:= :prev-token-id nil]]})
      first))

(defn get-text-last-token [text-id]
  ;; TODO eventually will need to update this once the doubly-linked list can branch due to textual variants
  (-> (do! {:select [:*]
            :from :tokens
            :where [:and
                    [:= :text-id text-id]
                    [:= :next-token-id nil]]})
      first))

(defn insert-text! [text-title text-contents-as-string]
  (let [tokens (tokenize-text text-contents-as-string)
        text-insert-result (do! {:insert-into [:texts]
                                 :values [{:title text-title}]
                                 :returning :text_id})
        text-id (:texts/text_id (first text-insert-result))]
    (when (nil? text-id)
      (throw (new Exception "text-id not found after attempted insert")))
    (println "text-id: " text-id tokens)
    (insert-token-into-db* text-id nil tokens)
    ))

(defn append-text! [text-id text-contents-as-string]
  (let [tokens (tokenize-text text-contents-as-string)
        last-token (get-text-last-token text-id)]
    (when (nil? text-id)
      (throw (new Exception "text-id is nil")))
        (when (nil? last-token)
      (throw (new Exception "last-token not found")))
    (insert-token-into-db* text-id (:tokens/token_id last-token) tokens)
    ))

(defn delete-text! [text-id]
  (db/do! {:delete-from [:tokens]
           :where [:= :text_id text-id]})
  (db/do! {:delete-from [:texts]
           :where [:= :text_id text-id]})
  ;; TODO what about preference_autostart_text?
  text-id)

(defn walk-tokens [{:keys [f text-id n start-id end-id iv]}]
  (let [acc-atom (atom (or iv []))
        count-atom (atom 0)
        cur-token-atom (atom (or (db/id->token start-id)
                                 (get-text-first-token text-id)))
        should-quit? (fn []
                       (cond
                         (nil? @cur-token-atom)
                         true
                         ;;
                         (and n (>= @count-atom n))
                         true
                         ;;
                         (and end-id
                              (= (str (:tokens/prev_token_id @cur-token-atom))
                                 (str end-id)))
                         true
                         ;;
                         :else
                         false
                         ))]
    (doall
     (while (not (should-quit?))
       (f @cur-token-atom acc-atom)
       (reset! cur-token-atom (db/id->token (:tokens/next_token_id @cur-token-atom)))
       (swap! count-atom inc)
       ))
    @acc-atom))

(defn walk-tokens-backwards [{:keys [f text-id n start-id end-id iv]}]
  (println "walk-tokens-backwards: " end-id)
  (let [acc-atom (atom (or iv []))
        count-atom (atom 0)
        cur-token-atom (atom (or (db/id->token end-id)
                                 (get-text-last-token text-id)))
        _ (println "====" cur-token-atom)
        should-quit? (fn []
                       (cond
                         (nil? @cur-token-atom)
                         true
                         ;;
                         (and n (>= @count-atom n))
                         true
                         ;;
                         (and start-id
                              (= (str (:tokens/next_token_id @cur-token-atom))
                                 (str start-id)))
                         true
                         ;;
                         :else
                         false
                         ))]
    (doall
     (while (not (should-quit?))
       (f @cur-token-atom acc-atom)
       (reset! cur-token-atom (db/id->token (:tokens/prev_token_id @cur-token-atom)))
       (swap! count-atom inc)
       ))
    @acc-atom))

(defn get-text-as-string [text-id n]
  (clojure.string/join
   " "
   (walk-tokens {:f (fn [token acc-atom]
                      (swap! acc-atom conj
                             (str (:tokens/punctuation_preceding token)
                                  (:tokens/wordform token)
                                  (:tokens/punctuation_trailing token))))
                 :text-id text-id :n n
                 :iv []})))

(defn text-id->first-token [text-id]
  (-> (do! {:select [:token_id :wordform :next_token_id :punctuation_preceding :punctuation_trailing]
            :from :tokens
            :where [:and
                    [:= :text-id text-id]
                    [:= :prev-token-id nil]]})
      first))

(defn token-str
  ([token footnote-str]
   (str
    (:tokens/punctuation_preceding token)
    (:tokens/wordform token)
    footnote-str
    (:tokens/punctuation_trailing token)))
  ([token]
   (token-str token "")))

(defn get-text-as-string-for-range [{:keys [text-id start-id end-id include-footnotes?]}]
  (let [footnote-count (atom 0)]
    (clojure.string/join
     " "
     (walk-tokens
      {:f (fn [token acc-atom]
            (if include-footnotes?
              (let [footnotes (db/token->footnotes token)
                    footnote-str
                    (clojure.string/join
                     ","
                     (map (fn [v] (swap! footnote-count inc))
                          footnotes))]
                (swap! acc-atom conj 
                       (token-str token footnote-str)))
              (swap! acc-atom conj (token-str token))))
       :text-id text-id :start-id start-id :end-id end-id
       :iv []}))))

(defn get-text-edn [{:keys [text-id n start-id end-id] :as args}]
  (let [go-backwards? (and (not start-id) end-id)
        walk-fn (if go-backwards?
                  walk-tokens-backwards
                  walk-tokens)]
    (walk-fn {:f (fn [token acc-atom]
                   (swap! acc-atom conj
                          (db/decorate-token token)))
              :text-id text-id :n n :start-id start-id :end-id end-id
              :iv []})))

(defn map-tokens-by-wordforms [tokens]
  (let [wordforms->tokens (atom {})]
    (doseq [token tokens]
      (let [w* (clojure.string/lower-case (:tokens/wordform token))]
        (if (nil? (get @wordforms->tokens w*))
          (swap! wordforms->tokens assoc w* [token])
          (swap! wordforms->tokens update w* conj token))))
    @wordforms->tokens))

(defn generate-glossary-entry-using-tokens [tokens]
  (let [tokens (remove #(= 1 (:tokens/exclude_from_glossary %)) tokens)
        wordforms->tokens (map-tokens-by-wordforms tokens)
        ks (sort-by
            #(-> % clojure.string/lower-case remove-macrons)
            (keys wordforms->tokens))]
    (clojure.string/join
     "\n"
     (remove nil?
             (map (fn [k]
                    (generate-single-glossary-entry-using-tokens k (wordforms->tokens k)))
                  ks)))))

(defn generate-glossary-for-tokens [tokens]
  (generate-glossary-entry-using-tokens tokens))

(defn generate-glossary-for-token-range [first-token-id last-token-id]
  (let [tokens
        (walk-tokens
         {:f (fn [token acc-atom]
               (swap! acc-atom conj token))
          :start-id first-token-id :end-id last-token-id
          :iv []})]
    (generate-glossary-entry-using-tokens tokens)))

(defn generate-glossary-for-text [text-id]
  (generate-glossary-for-tokens (get-text-edn {:text-id text-id :n 5000})))

(defn generate-footnotes-for-token
  ([token number-footnotes? footnote-count-atom]
   (let [footnotes (db/token->footnotes token)]
     (clojure.string/join
      "\n"
      (map (fn [footnote]
             (let [start-id (:footnotes/start_token_id footnote)
                   end-id   (:footnotes/end_token_id footnote)]
               (str
                (when number-footnotes?
                  ;; swap! with inc will return the new value for the footnote counter, which we want to put in the string
                  (swap! footnote-count-atom inc))
                (when (and start-id end-id)
                  (str
                   (clojure.string/join
                    " "
                    (map :tokens/wordform
                         (db/get-token-range start-id end-id)))
                   " = "))
                (:footnotes/text footnote))))
           footnotes))))
  ([token]
   (generate-footnotes-for-token token false nil)))

(defn generate-footnotes-for-tokens [tokens number-footnotes?]
  (let [footnote-count-atom (atom 0)]
    (clojure.string/join
     "\n"
     (remove
      empty?
      (map #(generate-footnotes-for-token % number-footnotes? footnote-count-atom) tokens)))))

(defn generate-footnotes-for-text [text-id]
  (generate-footnotes-for-tokens (db/text->tokens text-id) false))

(defn generate-footnotes-for-range [start-id end-id]
  (generate-footnotes-for-tokens
   (walk-tokens
    {:f (fn [token acc-atom]
          (swap! acc-atom conj token))
     :start-id start-id :end-id end-id
     :iv []})
   true))
