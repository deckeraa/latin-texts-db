(ns latin-texts.glossary-test
  (:require [clojure.test :refer [deftest is testing]]
            [latin-texts.texts :refer :all]
            [latin-texts.verb-tests :refer [find-form s=]]
            ))

(def ab-tokens
  [{:tokens/prev_token_id 7157,
    :footnotes [],
    :tokens/witness_id nil,
    :tokens/wordform "ab",
    :tokens/meaning_id nil,
    :lexeme nil,
    :potential-meanings
    [{
      :meanings/updated_at nil,
      :meanings/wordform "ab",
      :meanings/gloss "from, out of, by",
      :meanings/meaning_id 20470,
      :meanings/degree nil,
      :meanings/conjugation nil,
      :meanings/usage_note nil,
      :meanings/gender nil,
      :meanings/mood nil,
      :meanings/case_ nil,
      :meanings/confidence nil,
      :lexeme {
               :lexemes/lexeme_id 258,
               :lexemes/dictionary_form "ab"},
      :meanings/voice nil,
      :meanings/part_of_speech "preposition",
      :meanings/number nil,
      :meanings/tense nil,
      :meanings/created_at "2026-02-03 21
:41
:12",
      :meanings/source nil,
      :meanings/definition nil,
      :meanings/lexeme_id 258,
      :meanings/declension nil,
      :meanings/person nil}],
    :tokens/punctuation_trailing "",
    :tokens/token_id 7158,
    :tokens/punctuation_preceding "",
    :tokens/gloss_override nil,
    :tokens/next_token_id 7159,
    :tokens/text_id 18}
   {:tokens/prev_token_id 7326,
    :footnotes [],
    :tokens/witness_id nil,
    :tokens/wordform "ab",
    :tokens/meaning_id nil,
    :lexeme nil,
    :potential-meanings [{
                          :meanings/updated_at nil,
                          :meanings/wordform "ab",
                          :meanings/gloss "from, out of, by",
                          :meanings/meaning_id 20470,
                          :meanings/degree nil,
                          :meanings/conjugation nil,
                          :meanings/usage_note nil,
                          :meanings/gender nil,
                          :meanings/mood nil,
                          :meanings/case_ nil,
                          :meanings/confidence nil,
                          :lexeme {
                                   :lexemes/lexeme_id 258,
                                   :lexemes/dictionary_form "ab"},
                          :meanings/voice nil,
                          :meanings/part_of_speech "preposition",
                          :meanings/number nil,
                          :meanings/tense nil,
                          :meanings/created_at "2026-02-03 21
:41
:12",
                          :meanings/source nil,
                          :meanings/definition nil,
                          :meanings/lexeme_id 258,
                          :meanings/declension nil,
                          :meanings/person nil}],
    :tokens/punctuation_trailing "",
    :tokens/token_id 7327,
    :tokens/punctuation_preceding "",
    :tokens/gloss_override nil,
    :tokens/next_token_id 7328,
    :tokens/text_id 18}])

(deftest generate-single-glossary-entry-using-tokens-test
  (testing "no selected meanings"
    (is (= nil (generate-single-glossary-entry-using-tokens "ab" ab-tokens))))
  (testing "one selected meaning"
    (is (= "ab: from, out of, by" (generate-single-glossary-entry-using-tokens "ab" (update-in ab-tokens [0] assoc :tokens/meaning_id 20470)))))
  (testing "one selected meaning with gloss override"
    (is (= "ab: from"
           (generate-single-glossary-entry-using-tokens
            "ab"
            (-> ab-tokens
                (update-in [0] assoc :tokens/meaning_id 20470)
                (update-in [0] assoc :tokens/gloss_override "from"))))))
  (testing "no selected meaning with gloss override"
    (is (= "ab: from"
           (generate-single-glossary-entry-using-tokens
            "ab"
            (-> ab-tokens
                (update-in [0] assoc :tokens/gloss_override "from"))))))
  (testing "one selected meaning with gloss override on other"
    (is (= "ab: from, out of, by or test"
           (generate-single-glossary-entry-using-tokens
            "ab"
            (-> ab-tokens
                (update-in [0] assoc :tokens/meaning_id 20470)
                (update-in [1] assoc :tokens/gloss_override "test"))))))
  (testing "one selected meaning with gloss override on other, alphabetization enforced"
    (is (= "ab: from, out of, by or test"
           (generate-single-glossary-entry-using-tokens
            "ab"
            (-> ab-tokens
                (update-in [1] assoc :tokens/meaning_id 20470)
                (update-in [0] assoc :tokens/gloss_override "test")))))))

(deftest pretty-person-test
  (is (= "3rd person" (pretty-person 3))))

(def tenetur-token
  {:tokens/prev_token_id 10894,
   :footnotes [],
   :tokens/witness_id nil,
   :tokens/wordform "tenētur",
   :tokens/meaning_id 37873,
   :lexeme
   #:lexemes{:lexeme_id 598,
             :dictionary_form "teneō, tenēre, tenuī, tentum"},
   :tokens/punctuation_trailing ".",
   :tokens/token_id 10895,
   :meaning
   {:meanings/updated_at nil,
    :meanings/wordform "tenētur",
    :meanings/gloss "he/she/it is held",
    :meanings/meaning_id 37873,
    :meanings/degree nil,
    :meanings/conjugation nil,
    :meanings/usage_note nil,
    :meanings/gender nil,
    :meanings/mood "indicative",
    :meanings/case_ nil,
    :meanings/confidence nil,
    :lexeme
    #:lexemes{:lexeme_id 598,
              :dictionary_form "teneō, tenēre, tenuī, tentum"},
    :meanings/voice "passive",
    :meanings/part_of_speech "verb",
    :meanings/number "singular",
    :meanings/tense "present",
    :meanings/created_at "2026-02-11 04:56:44",
    :meanings/source nil,
    :meanings/definition nil,
    :meanings/lexeme_id 598,
    :meanings/declension nil,
    :meanings/person 3},
   :tokens/punctuation_preceding "",
   :tokens/gloss_override nil,
   :tokens/next_token_id 10896,
   :tokens/text_id 22})

(def tenetur-token-with-override
  (assoc tenetur-token :tokens/gloss_override "foo"))

(def tenetur-token-with-m-gender
  (assoc tenetur-token :tokens/antecedent_english_gender "masculine"))

(def tenetur-token-with-f-gender
  (assoc tenetur-token :tokens/antecedent_english_gender "feminine"))

(def tenetur-token-with-n-gender
  (assoc tenetur-token :tokens/antecedent_english_gender "neuter"))

(deftest gender-specificity-test
  (testing "helper code"
    (is (= (token->gloss tenetur-token) "he/she/it is held"))
    (is (= (token->gloss tenetur-token-with-override) "foo"))
    (is (= (tokens->gloss-to-token-map [tenetur-token])
           {"he/she/it is held" [tenetur-token]}))
    (is (= (tokens->gloss-to-token-map [tenetur-token tenetur-token])
           {"he/she/it is held" [tenetur-token tenetur-token]}))
    (is (= (tokens->gloss-to-token-map
            [tenetur-token
             tenetur-token-with-override])
           {"he/she/it is held" [tenetur-token]
            "foo" [tenetur-token-with-override]})))
  (testing "tokens->glosses"
    (is (= (tokens->glosses [tenetur-token])
           ["he/she/it is held"]))
    (is (= (tokens->glosses [tenetur-token-with-m-gender])
           ["he is held"]))
    (is (= (tokens->glosses [tenetur-token-with-m-gender tenetur-token-with-f-gender])
           ["he/she is held"]))
    (is (= (tokens->glosses [tenetur-token tenetur-token-with-f-gender])
           ["he/she/it is held"]))
    (is (= (tokens->glosses [tenetur-token-with-n-gender tenetur-token-with-f-gender])
           ["she/it is held"])))
  (is (= (generate-single-glossary-entry-using-tokens "tenētur" [tenetur-token])
         "tenētur: he/she/it is held; 3rd person singular present from teneō, tenēre, tenuī, tentum"))
  (is (= (generate-single-glossary-entry-using-tokens
          "tenētur"
          [tenetur-token-with-m-gender])
         "tenētur: he is held; 3rd person singular present from teneō, tenēre, tenuī, tentum"))
  )
