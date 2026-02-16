(ns latin-texts.texts-test
  (:require [clojure.test :refer [deftest is testing]]
            [latin-texts.texts :refer :all]
            [latin-texts.verb-tests :refer [find-form s=]]
            ))

(deftest tokenize-text-test
  (is (= (tokenize-text "Ubi est mīles?")
         ["Ubi" "est" "mīles?"]))
  (is (= (tokenize-text "Ubi\nest\n mīles?")
         ["Ubi\n" "est\n" "mīles?"]))
  (is (= (tokenize-text "Epistula Magistrī\n\n\t Iūlius")
         ["Epistula" "Magistrī\n" "\tIūlius"])))
