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
  )

(deftest pretty-person-test
  (is (= "3rd person" (pretty-person 3))))
