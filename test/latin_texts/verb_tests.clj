(ns latin-texts.verb-tests
  (:require [clojure.test :refer [deftest is testing]]
            [latin-texts.bulk-verb-insert :refer :all]
            ))

(deftest conjugation-detection
  (testing "normal verbs"
    (is (= "1" (get-conjugation "ambulō" "ambulāre")))))
