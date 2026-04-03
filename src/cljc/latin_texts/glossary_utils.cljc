(ns latin-texts.glossary-utils
  (:require 
   [clojure.string :as str]))

(defn contains-dissimilar-wordforms [tokens]
  (let [non-nil-wordforms (remove nil? (map :tokens/wordform tokens))
        normalized-wordforms (map clojure.string/lower-case non-nil-wordforms)]
    (> (count (distinct normalized-wordforms))
       1)))

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

(defn parsed-entry-for-pronoun [meaning skip-from?]
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

(defn parsed-entry [meaning skip-from?]
  (case (:meanings/part_of_speech meaning)
    "noun" (parsed-entry-for-noun meaning skip-from?)
    "pronoun" (parsed-entry-for-pronoun meaning skip-from?)
    "verb" (parsed-entry-for-verb meaning skip-from?)
    "conjunction" nil
    "particle" nil
    "interjection" nil
    "adverb" nil
    "preposition" nil
    "participle" (parsed-entry-for-participle meaning skip-from?)
    "adjective" (parsed-entry-for-adjective meaning skip-from?)
    "TODO" nil))

;; (defn generate-single-glossary-entry-using-tokens [wordform tokens]
;;   (when (contains-dissimilar-wordforms tokens)
;;     (throw 
;;      (ex-info 
;;       (format "generate-single-glossary-entry-using-tokens failed because more than one wordform was passed: %s"
;;               (->> tokens
;;                    (map :tokens/wordform)
;;                    distinct
;;                    (clojure.string/join ", ")))
;;       {:wordforms (->> tokens (map :tokens/wordform) distinct)}))
;;     ;; (throw (Exception. (str "generate-single-glossary-entry-using-tokens failed because more than one wordform was passed: " (doall (distinct (map :tokens/wordform tokens))))))
;;     )
;;   (let [tokens (map db/decorate-token tokens)
;;         meanings (distinct (remove nil? (map :meaning tokens)))
;;         first-meaning-wordform (:meanings/wordform (first meanings))
;;         ne? (enclitic-ne? wordform first-meaning-wordform)
;;         ine? (enclitic-ine? wordform first-meaning-wordform)
;;         que? (enclitic-que? wordform first-meaning-wordform)
;;         nam? (enclitic-nam? wordform first-meaning-wordform)
;;         capitalize-wordform? (capitalized? first-meaning-wordform)
;;         ;;
;;         distinct-glosses (tokens->glosses tokens)
;;         ]
;;     (when (not (empty? distinct-glosses))
;;       (let [distinct-meanings (distinct (map (fn [m] (get-in m [:lexeme :lexemes/dictionary_form])) meanings))
;;             parsed-section
;;             (if (= 1 (count distinct-meanings))
;;               ;; only list the dictionary entry once
;;               (let [last-meaning (last meanings)
;;                     parsed-last-without-end (parsed-entry last-meaning true)]
;;                 (str
;;                  (clojure.string/join
;;                   " or "
;;                   (distinct
;;                    (remove
;;                     (fn [entry]
;;                       (or (nil? entry)
;;                           (= entry parsed-last-without-end)))
;;                     (conj (mapv #(parsed-entry % true)
;;                                 (butlast meanings))
;;                           (parsed-entry (last meanings) false)))))))
;;               ;; list each separately
;;               (clojure.string/join " or " (distinct (map #(parsed-entry % false) meanings))))
;;             ;;
;;             ]
;;         (str (if capitalize-wordform?
;;                (clojure.string/capitalize wordform)
;;                wordform)
;;              ": "
;;              (clojure.string/join " or " (sort distinct-glosses))
;;              (when (not-empty parsed-section)
;;                (str "; " parsed-section))
;;              ;; TODO consider making more complete handling for enclitic  -ne
;;              (when ne? "; -ne makes something a question")
;;              (when ine? "; -ne makes something a question, with an i interposing for certain words")
;;              (when que? "; -que adds 'and' in front of a word")
;;              (when nam? "; -nam makes something a question"))))))
