(ns latin-texts.verb-tests
  (:require [clojure.test :refer [deftest is testing]]
            [latin-texts.bulk-verb-insert :refer :all]
            ))

(deftest conjugation-detection
  (testing "normal verbs"
    (is (= "1" (get-conjugation "ambulō" "ambulāre")))
    (is (= "2" (get-conjugation "moneō" "monēre")))
    (is (= "3" (get-conjugation "vīvō" "vīvere")))
    (is (= "3i" (get-conjugation "capiō" "capere")))
    (is (= "4" (get-conjugation "audiō" "audīre")))
    )
  (testing "deponent verbs"
    (is (= "1" (get-conjugation-dep "hortor" "hortārī")))
    (is (= "2" (get-conjugation-dep "vereor" "verērī")))
    (is (= "3" (get-conjugation-dep "sequor" "sequī")))
    (is (= "3i" (get-conjugation-dep "patior" "patī")))
    (is (= "4" (get-conjugation-dep "orior" "orīrī")))
    ))

(defn find-form [forms filters]
  :todo
  )

(deftest āre-generation
  (testing "ambulāre"
    (let [forms (get-verb-forms
                 "ambulō, ambulāre, ambulāvī, ambulātum"
                 "walk"
                 "walks"
                 "walked"
                 "walking"
                 "walked")]
      (is (= 1 1))
      )))
