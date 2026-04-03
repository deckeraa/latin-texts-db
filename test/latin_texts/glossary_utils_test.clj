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
