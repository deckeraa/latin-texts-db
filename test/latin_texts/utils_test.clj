(ns latin-texts.utils_test
  (:require [clojure.test :refer [deftest is testing]]
            [latin-texts.utils :refer :all]
            ))

(deftest remove-macrons-test
  (is (= (remove-macrons "mīles") "miles")))

(deftest sg-gloss-guess-test
  (is (= (sg-gloss-guess "pig") "pig's"))
  (is (= (sg-gloss-guess "ring, signet ring") "ring's, signet ring's")))

(deftest pn-gloss-guess-test
  (is (= (pn-gloss-guess "pig") "pigs"))
  (is (= (pn-gloss-guess "ring, signet ring") "rings, signet rings")))

(deftest pg-gloss-guess-test
  (is (= (pg-gloss-guess "pig") "of the pigs"))
  (is (= (pg-gloss-guess "ring, signet ring") "of the rings, signet rings")))
