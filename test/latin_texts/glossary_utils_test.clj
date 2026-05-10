(ns latin-texts.glossary_utils_test
  (:require [clojure.test :refer [deftest is testing]]
            [latin-texts.glossary-utils :refer :all]
            ))

(deftest parsed-entry-test
  (let [porcus-meaning
        {:meanings/updated_at nil,
         :meanings/wordform "porcus",
         :meanings/gloss "pig",
         :meanings/meaning_id 64324,
         :meanings/degree nil,
         :meanings/conjugation nil,
         :meanings/usage_note nil,
         :meanings/gender "masculine",
         :meanings/mood nil,
         :meanings/case_ "nominative",
         :meanings/confidence nil,
         :lexeme #:lexemes{:lexeme_id 937, :dictionary_form "porcus, porcī"},
         :meanings/voice nil,
         :meanings/part_of_speech "noun",
         :meanings/number "singular",
         :meanings/tense nil,
         :meanings/created_at "2026-02-19 18:54:56",
         :meanings/source nil,
         :meanings/definition nil,
         :meanings/lexeme_id 937,
         :meanings/declension nil,
         :meanings/person nil}]
    (is (= (parsed-entry porcus-meaning false)
           "singular masculine nominative from porcus, porcī"))
    (is (= (parsed-entry porcus-meaning true)
           "singular masculine nominative"))))

(deftest parsed-entry-for-participle-test
  (let [vendendum-meaning
        {:meanings/updated_at nil,
         :meanings/wordform "vēndendum",
         :meanings/gloss "selling",
         :meanings/meaning_id 32666,
         :meanings/degree nil,
         :meanings/conjugation nil,
         :meanings/usage_note nil,
         :meanings/gender "masculine",
         :meanings/mood "indicative",
         :meanings/case_ "accusative",
         :meanings/confidence nil,
         :lexeme
         #:lexemes{:lexeme_id 525,
                   :dictionary_form "vēndō, vēndere, vēndidī, vēnditum"},
         :meanings/voice "passive",
         :meanings/part_of_speech "participle",
         :meanings/number "singular",
         :meanings/tense "future",
         :meanings/created_at "2026-02-08 02:07:09",
         :meanings/source nil,
         :meanings/definition nil,
         :meanings/lexeme_id 525,
         :meanings/declension nil,
         :meanings/person nil}]

    (is (= (parsed-entry-for-participle vendendum-meaning false)
           "singular masculine accusative future participle from vēndō, vēndere, vēndidī, vēnditum"))
    (is (= (parsed-entry-for-participle vendendum-meaning false {:is-gerund-set #{true}})
           "singular masculine accusative gerund from vēndō, vēndere, vēndidī, vēnditum"))
    (is (= (parsed-entry-for-participle vendendum-meaning false {:is-gerund-set #{true false}})
           "singular masculine accusative gerund or future participle from vēndō, vēndere, vēndidī, vēnditum"))
    (is (= (parsed-entry-for-participle vendendum-meaning false {:is-gerund-set #{false}})
           "singular masculine accusative future participle from vēndō, vēndere, vēndidī, vēnditum"))
    ))

;; (deftest is-gerund-test
;;   (let [vendendum-token
;;         #:tokens{:prev_token_id 28746,
;;                  :witness_id nil,
;;                  :wordform "vēndendum",
;;                  :meaning_id nil,
;;                  :antecedent_english_gender nil,
;;                  :exclude_from_glossary nil,
;;                  :is_gerund 1,
;;                  :punctuation_trailing "",
;;                  :token_id 28747,
;;                  :punctuation_preceding "",
;;                  :gloss_override nil,
;;                  :next_token_id 28748,
;;                  :text_id 36}]
;;     (is (= (update-gloss "singular masculine accusative future participle from vēndō, vēndere, vēndidī, vēnditum" [vendendum-token])
;;            "singular masculine accusative gerund from vēndō, vēndere, vēndidī, vēnditum"))
;;     ))
