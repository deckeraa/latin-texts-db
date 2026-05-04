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
      (is (s= (find-form forms {:number "plural" :gender "feminine" :case_ "nominative" :degree "superlative"}) :wordform #{"fortissimae"})))))

;; (deftest one-termination-third-declension
;;   (testing "absēns"
;;     (let [forms (get-adj-forms
;;                  {:dictionary-form "absēns"
;;                   :pos-gloss "absent"
;;                   :sg-gen "absentis"
;;                   :include-comparative? true
;;                   :sup-m "absentissimus"})]
;;       (is (s= (find-form forms {:number "singular" :gender "masculine" :case_ "nominative" :degree "positive"}) :wordform #{"absēns"}))
;;       ;; about 15-20 more tests here
;;       )))

(deftest one-termination-third-declension
  (testing "absēns — third-declension one-termination adjective (positive degree)"
    (let [forms (get-adj-forms
                 {:dictionary-form "absēns"
                  :pos-gloss "absent"
                  :sg-gen "absentis"
                  :include-comparative? true
                  :sup-m "absentissimus"
                  :pl-gen-ium? true})]

      ;; Singular Masculine
      (is (s= (find-form forms {:number "singular" :gender "masculine" :case_ "nominative" :degree "positive"})
              :wordform #{"absēns"}))
      (is (s= (find-form forms {:number "singular" :gender "masculine" :case_ "nominative" :degree "positive"})
              :gloss #{"absent"}))

      (is (s= (find-form forms {:number "singular" :gender "masculine" :case_ "genitive" :degree "positive"})
              :wordform #{"absentis"}))
      (is (s= (find-form forms {:number "singular" :gender "masculine" :case_ "genitive" :degree "positive"})
              :gloss #{"absent"}))

      (is (s= (find-form forms {:number "singular" :gender "masculine" :case_ "dative" :degree "positive"})
              :wordform #{"absentī"}))
      (is (s= (find-form forms {:number "singular" :gender "masculine" :case_ "dative" :degree "positive"})
              :gloss #{"absent"}))

      (is (s= (find-form forms {:number "singular" :gender "masculine" :case_ "accusative" :degree "positive"})
              :wordform #{"absentem"}))
      (is (s= (find-form forms {:number "singular" :gender "masculine" :case_ "accusative" :degree "positive"})
              :gloss #{"absent"}))

      (is (s= (find-form forms {:number "singular" :gender "masculine" :case_ "ablative" :degree "positive"})
              :wordform #{"absentī"}))
      (is (s= (find-form forms {:number "singular" :gender "masculine" :case_ "ablative" :degree "positive"})
              :gloss #{"absent"}))

      ;; Singular Feminine (identical to masculine except acc sg -em)
      (is (s= (find-form forms {:number "singular" :gender "feminine" :case_ "nominative" :degree "positive"})
              :wordform #{"absēns"}))
      (is (s= (find-form forms {:number "singular" :gender "feminine" :case_ "accusative" :degree "positive"})
              :wordform #{"absentem"}))
      (is (s= (find-form forms {:number "singular" :gender "feminine" :case_ "ablative" :degree "positive"})
              :wordform #{"absentī"}))

      ;; Singular Neuter
      (is (s= (find-form forms {:number "singular" :gender "neuter" :case_ "nominative" :degree "positive"})
              :wordform #{"absēns"}))
      (is (s= (find-form forms {:number "singular" :gender "neuter" :case_ "accusative" :degree "positive"})
              :wordform #{"absēns"}))
      (is (s= (find-form forms {:number "singular" :gender "neuter" :case_ "genitive" :degree "positive"})
              :wordform #{"absentis"}))
      (is (s= (find-form forms {:number "singular" :gender "neuter" :case_ "dative" :degree "positive"})
              :wordform #{"absentī"}))
      (is (s= (find-form forms {:number "singular" :gender "neuter" :case_ "ablative" :degree "positive"})
              :wordform #{"absentī"}))

      ;; Plural Masculine
      (is (s= (find-form forms {:number "plural" :gender "masculine" :case_ "nominative" :degree "positive"})
              :wordform #{"absentēs"}))
      (is (s= (find-form forms {:number "plural" :gender "masculine" :case_ "accusative" :degree "positive"})
              :wordform #{"absentēs"}))
      (is (s= (find-form forms {:number "plural" :gender "masculine" :case_ "genitive" :degree "positive"})
              :wordform #{"absentium"}))
      (is (s= (find-form forms {:number "plural" :gender "masculine" :case_ "dative" :degree "positive"})
              :wordform #{"absentibus"}))
      (is (s= (find-form forms {:number "plural" :gender "masculine" :case_ "ablative" :degree "positive"})
              :wordform #{"absentibus"}))

      ;; Plural Neuter (key test for one-termination adjectives)
      (is (s= (find-form forms {:number "plural" :gender "neuter" :case_ "nominative" :degree "positive"})
              :wordform #{"absentia"}))
      (is (s= (find-form forms {:number "plural" :gender "neuter" :case_ "accusative" :degree "positive"})
              :wordform #{"absentia"}))
      (is (s= (find-form forms {:number "plural" :gender "neuter" :case_ "genitive" :degree "positive"})
              :wordform #{"absentium"})))))

(deftest diligens
  (testing "dīligēns"
    (let [forms (get-adj-forms
                 {:dictionary-form "dīligēns"
                  :pos-gloss "diligent"
                  :sg-gen "dīligentis"
                  :pl-gen-ium? true})]

      ;; Singular Masculine
      (is (s= (find-form forms {:number "singular" :gender "masculine" :case_ "nominative" :degree "positive"})
              :wordform #{"dīligēns"}))
      (is (s= (find-form forms {:number "singular" :gender "masculine" :case_ "nominative" :degree "positive"})
              :gloss #{"diligent"}))

      (is (s= (find-form forms {:number "singular" :gender "masculine" :case_ "genitive" :degree "positive"})
              :wordform #{"dīligentis"}))
      (is (s= (find-form forms {:number "singular" :gender "masculine" :case_ "genitive" :degree "positive"})
              :gloss #{"diligent"}))

      (is (s= (find-form forms {:number "singular" :gender "masculine" :case_ "dative" :degree "positive"})
              :wordform #{"dīligentī"}))
      (is (s= (find-form forms {:number "singular" :gender "masculine" :case_ "dative" :degree "positive"})
              :gloss #{"diligent"}))

      (is (s= (find-form forms {:number "singular" :gender "masculine" :case_ "accusative" :degree "positive"})
              :wordform #{"dīligentem"}))
      (is (s= (find-form forms {:number "singular" :gender "masculine" :case_ "accusative" :degree "positive"})
              :gloss #{"diligent"}))

      (is (s= (find-form forms {:number "singular" :gender "masculine" :case_ "ablative" :degree "positive"})
              :wordform #{"dīligentī"}))
      (is (s= (find-form forms {:number "singular" :gender "masculine" :case_ "ablative" :degree "positive"})
              :gloss #{"diligent"}))

      ;; Singular Feminine (identical to masculine)
      (is (s= (find-form forms {:number "singular" :gender "feminine" :case_ "nominative" :degree "positive"})
              :wordform #{"dīligēns"}))
      (is (s= (find-form forms {:number "singular" :gender "feminine" :case_ "accusative" :degree "positive"})
              :wordform #{"dīligentem"}))
      (is (s= (find-form forms {:number "singular" :gender "feminine" :case_ "ablative" :degree "positive"})
              :wordform #{"dīligentī"}))

      ;; Singular Neuter
      (is (s= (find-form forms {:number "singular" :gender "neuter" :case_ "nominative" :degree "positive"})
              :wordform #{"dīligēns"}))
      (is (s= (find-form forms {:number "singular" :gender "neuter" :case_ "accusative" :degree "positive"})
              :wordform #{"dīligēns"}))
      (is (s= (find-form forms {:number "singular" :gender "neuter" :case_ "genitive" :degree "positive"})
              :wordform #{"dīligentis"}))
      (is (s= (find-form forms {:number "singular" :gender "neuter" :case_ "dative" :degree "positive"})
              :wordform #{"dīligentī"}))
      (is (s= (find-form forms {:number "singular" :gender "neuter" :case_ "ablative" :degree "positive"})
              :wordform #{"dīligentī"}))

      ;; Plural Masculine
      (is (s= (find-form forms {:number "plural" :gender "masculine" :case_ "nominative" :degree "positive"})
              :wordform #{"dīligentēs"}))
      (is (s= (find-form forms {:number "plural" :gender "masculine" :case_ "accusative" :degree "positive"})
              :wordform #{"dīligentēs"}))
      (is (s= (find-form forms {:number "plural" :gender "masculine" :case_ "genitive" :degree "positive"})
              :wordform #{"dīligentium"}))
      (is (s= (find-form forms {:number "plural" :gender "masculine" :case_ "dative" :degree "positive"})
              :wordform #{"dīligentibus"}))
      (is (s= (find-form forms {:number "plural" :gender "masculine" :case_ "ablative" :degree "positive"})
              :wordform #{"dīligentibus"}))

      ;; Plural Neuter (key test for one-termination adjectives)
      (is (s= (find-form forms {:number "plural" :gender "neuter" :case_ "nominative" :degree "positive"})
              :wordform #{"dīligentia"}))
      (is (s= (find-form forms {:number "plural" :gender "neuter" :case_ "accusative" :degree "positive"})
              :wordform #{"dīligentia"}))
      (is (s= (find-form forms {:number "plural" :gender "neuter" :case_ "genitive" :degree "positive"})
              :wordform #{"dīligentium"})))))

