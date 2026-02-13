(ns latin-texts.glossary-test
  (:require [clojure.test :refer [deftest is testing]]
            [latin-texts.texts :refer :all]
            [latin-texts.verb-tests :refer [find-form s=]]
            ))

(deftest pretty-person-test
  (is (= "3rd person" (pretty-person 3))))
