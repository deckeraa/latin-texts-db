(ns latin-texts.adj-test
  (:require [clojure.test :refer [deftest is testing]]
            [latin-texts.bulk-adj-insert :refer :all]
            [latin-texts.verb-tests :refer [find-form s=]]
            ))

(deftest get-adj-forms-test
  (testing "trimming on Iānuārius , Iānuāria , Iānuārium"
    (let [forms (get-adj-forms
                 {:dictionary-form "Iānuārius , Iānuāria , Iānuārium"
                  :pos-gloss "January"})]
      (is (s= (find-form forms {:number "plural" :gender "feminine" :case_ "nominative" :degree "positive"}) :wordform #{"Iānuāriae"}))))
  (testing "trimming on fortis , forte"
    (let [forms (get-adj-forms
                 {:dictionary-form "fortis , forte"
                  :sup-m "fortissimus"
                  :include-superlative? true
                  :pos-gloss "strong"
                  :sup-gloss "strongest"})]
      (is (s= (find-form forms {:number "plural" :gender "feminine" :case_ "nominative" :degree "positive"}) :wordform #{"fortēs"}))
      (is (s= (find-form forms {:number "plural" :gender "feminine" :case_ "nominative" :degree "superlative"}) :wordform #{"fortissimae"}))))
  )
