(ns latin-texts-db.texts
  (:require [next.jdbc :as jdbc]
            [clojure.java.io :as io]
            [migratus.core :as migratus]
            [honey.sql :as sql]
            [honey.sql.helpers :as h]
            [latin-texts-db.db :as db :refer [ds do! insert-token-into-db* get-potential-meanings-of-wordform]]))

(defn insert-text! [text-title text-contents-as-string]
  ;; TODO you need to split the string in such a way that "Cave Canem\nIānua vīllae" splits correctly and the \n is retained as a trailing puncutation on Canem.
  (let [tokens (remove empty? (clojure.string/split text-contents-as-string #"\s+" -1))
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
        first-token (-> (do! {:select [:*]
                              :from :tokens
                              :where [:and
                                      [:= :text-id text-id]
                                      [:= :prev-token-id nil]]})
                        first)
        next-token-id (atom (:tokens/next_token_id first-token))]
    (swap! fetched-tokens conj (db/decorate-token first-token))
    (doall
     (for [x (range 1 n)
           :when @next-token-id]
       (do
         (let [new-token (-> (do! {:select [:*]
                                   :from :tokens
                                   :where [:= :token_id @next-token-id]})
                             first)]
           (swap! fetched-tokens
                  conj (db/decorate-token new-token))
           (reset! next-token-id (:tokens/next_token_id new-token))))))
    @fetched-tokens))

;; (defn map-meanings-by-wordforms [meanings]
;;   (let [wordforms->meanings (atom {})]
;;     (doseq [meaning meanings]
;;       (let [w* (clojure.string/lower-case (:meanings/wordform meaning))]
;;         (if (nil? (get @wordforms->meanings w*))
;;           (swap! wordforms->meanings assoc w* [meaning])
;;           (swap! wordforms->meanings update w* conj meaning))))
;;     @wordforms->meanings))

(defn map-tokens-by-wordforms [tokens]
  (let [wordforms->tokens (atom {})]
    (doseq [token tokens]
      (let [w* (clojure.string/lower-case (:tokens/wordform token))]
        (if (nil? (get @wordforms->tokens w*))
          (swap! wordforms->tokens assoc w* [token])
          (swap! wordforms->tokens update w* conj token))))
    @wordforms->tokens))

(defn parsed-entry-for-noun [meaning skip-from?]
  (str (:meanings/number meaning)
       " "
       (:meanings/gender meaning)
       " "
       (:meanings/case_ meaning)
       (when-not skip-from?
         (str
          " from "
          (get-in meaning [:lexeme :lexemes/dictionary_form])))))

(defn pretty-person [person]
  (str
   ({1 "1st"
     "1" "1st"
     2 "2nd"
     "2" "2nd"
     3 "3rd"
     "3" "3rd"} person)
   " person"))

(defn parsed-entry-for-verb-imperative [meaning skip-from?]
  (str (clojure.string/join
        " " [(:meanings/number meaning)
             (:meanings/voice meaning)
             "imperative"])
       (when-not skip-from?
         (str
          " from "
          (get-in meaning [:lexeme :lexemes/dictionary_form])))))

(defn parsed-entry-for-verb-infinitive [meaning skip-from?]
  (str (clojure.string/join
        " " [(:meanings/tense meaning)
             (:meanings/voice meaning)
             "infinitive"])
       (when-not skip-from?
         (str
          " from "
          (get-in meaning [:lexeme :lexemes/dictionary_form])))))

(defn parsed-entry-for-verb [meaning skip-from?]
  (cond
    (= (:meanings/mood meaning) "imperative")
    (parsed-entry-for-verb-imperative meaning skip-from?)
    ;;
    (= (:meanings/mood meaning) "infinitive")
    (parsed-entry-for-verb-infinitive meaning skip-from?)
    ;;
    :else
    (str (clojure.string/join
          " "
          (remove
           nil?
           [(pretty-person (:meanings/person meaning))
            (:meanings/number meaning)
            (:meanings/tense meaning)
            (when-not (= (:meanings/voice "active"))
              (:meanings/voice meaning))
            (when-not (= (:meanings/mood "indicative"))
              (:meanings/mood meaning))]))
         (when-not skip-from?
           (str
            " from "
            (get-in meaning [:lexeme :lexemes/dictionary_form]))))))

(defn parsed-entry-for-participle [meaning skip-from?]
  (str (clojure.string/join
        " "
        (remove
         nil?
         [(:meanings/number meaning)
          (:meanings/gender meaning)
          (:meanings/case_ meaning)
          (:meanings/tense meaning)
          (when-not (= (:meanings/voice "active"))
            (:meanings/voice meaning))
          "participle"
          ]))
       (when-not skip-from?
         (str
          " from "
          (get-in meaning [:lexeme :lexemes/dictionary_form])))))

(defn parsed-entry-for-adjective [meaning skip-from?]
  (str (clojure.string/join
        " "
        (remove
         nil?
         [(:meanings/number meaning)
          (:meanings/gender meaning)
          (:meanings/case_ meaning)]))
       (when-not skip-from?
         (str
          " from "
          (get-in meaning [:lexeme :lexemes/dictionary_form])))))

(defn parsed-entry-for-conjunction [meaning skip-from?])

(defn parsed-entry [meaning skip-from?]
  (println "About to case " (:meanings/part_of_speech meaning))
  (case (:meanings/part_of_speech meaning)
    "noun" (parsed-entry-for-noun meaning skip-from?)
    "verb" (parsed-entry-for-verb meaning skip-from?)
    "conjunction" nil
    "particle" nil
    "interjection" nil
    "participle" (parsed-entry-for-participle meaning skip-from?)
    "adjective" (parsed-entry-for-adjective meaning skip-from?)
    "TODO" nil))

;; (defn generate-single-glossary-entry-using-meanings [meanings]
;;   (when (not (= 1 (count (distinct (map :meanings/wordfrom meanings)))))
;;     (throw {:message (str "generate-single-glossary-entry-using-meanings failed because more than one wordform was passed: " (distinct (map :meanings/wordfrom meanings)))
;;             :meanings meanings}))
;;   (let [parsed-section
;;         (if (= 1 (count (distinct (map (fn [m] (get-in m [:lexeme :lexemes/dictionary_form])) meanings))))
;;          ;; only list the dictionary entry once
;;          (str
;;           (clojure.string/join
;;            " or "
;;            (remove nil?
;;                    (conj (mapv #(parsed-entry % true) (butlast meanings))
;;                          (parsed-entry (last meanings) false)))))
;;          ;; list each separately
;;          (clojure.string/join " or " (map #(parsed-entry % false) meanings)))]
;;     (str (:meanings/wordform (first meanings))
;;          ": "
;;          (clojure.string/join " or " (map :meanings/gloss meanings))
;;          (when (not-empty parsed-section)
;;            (str "; " parsed-section)))))

(defn tokens->meanings-with-overrides [tokens]
  (map (fn [token]
         (let [meaning (db/id->meaning (:tokens/meaning_id token))]
           (assoc meaning :meanings/gloss
                  (or (:tokens/gloss_override token)
                      (:meanings/gloss meaning)))))
       (remove
        (fn [token] (nil? (:tokens/meaning_id token)))
        tokens)))

(defn contains-dissimilar-wordforms [tokens]
  (let [non-nil-wordforms (remove nil? (map :tokens/wordform tokens))
        normalized-wordforms (map clojure.string/lower-case non-nil-wordforms)]
    (> (count (distinct normalized-wordforms))
       1)))

(defn generate-single-glossary-entry-using-tokens [tokens]
  (when (contains-dissimilar-wordforms tokens)
    (throw 
     (ex-info 
      (format "generate-single-glossary-entry-using-tokens failed because more than one wordform was passed: %s"
              (->> tokens
                   (map :tokens/wordform)
                   distinct
                   (clojure.string/join ", ")))
      {:wordforms (->> tokens (map :tokens/wordform) distinct)}))
    ;; (throw (Exception. (str "generate-single-glossary-entry-using-tokens failed because more than one wordform was passed: " (doall (distinct (map :tokens/wordform tokens))))))
    )
  (let [meanings (tokens->meanings-with-overrides tokens)]
    (when (not (empty? meanings))
      (let [parsed-section
            (if (= 1 (count (distinct (map (fn [m] (get-in m [:lexeme :lexemes/dictionary_form])) meanings))))
              ;; only list the dictionary entry once
              (str
               (clojure.string/join
                " or "
                (remove nil?
                        (conj (mapv #(parsed-entry % true) (butlast meanings))
                              (parsed-entry (last meanings) false)))))
              ;; list each separately
              (clojure.string/join " or " (map #(parsed-entry % false) meanings)))]
        (str (:meanings/wordform (first meanings))
             ": "
             (clojure.string/join " or " (map :meanings/gloss meanings))
             (when (not-empty parsed-section)
               (str "; " parsed-section)))))))

;; (defn generate-glossary-entry-using-meanings [meanings]
;;   (let [wordforms->meanings (map-meanings-by-wordforms meanings)
;;         ks (sort (keys wordforms->meanings))]
;;     (clojure.string/join
;;      "\n"
;;      (map (fn [k]
;;             (generate-single-glossary-entry-using-meanings (wordforms->meanings k)))
;;           ks))))

(defn generate-glossary-entry-using-tokens [tokens]
  (let [wordforms->tokens (map-tokens-by-wordforms tokens)
        ks (sort (keys wordforms->tokens))]
    (clojure.string/join
     "\n"
     (remove nil?
             (map (fn [k]
                    (generate-single-glossary-entry-using-tokens (wordforms->tokens k)))
                  ks)))))

(defn generate-glossary-for-tokens [tokens]
  (generate-glossary-entry-using-tokens tokens))

(defn generate-glossary-for-token-range [first-token-id last-token-id]
  :todo)

(defn generate-glossary-for-text [text-id]
  (generate-glossary-for-tokens (get-text-edn text-id 5000)))


