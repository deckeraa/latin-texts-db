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
  (filter
   (fn [form]
     (empty?
      (remove
       true?
       (map (fn [k]
              (= (get form k) (get filters k)))
            (keys filters)))))
   forms))

(deftest find-form-test
  (let [forms [{:a 1 :b 2} {:a 3 :b 2}]]
    (is (= (find-form forms {:a 1})
           '({:a 1 :b 2})))
    (is (= (find-form forms {:b 2})
           forms))
    (is (= (find-form forms {:c 1})
           '()))))

;;   "Single equals -- check that the forms only has one entry"
;; (defn s=
;;   [forms k v]
;;   (and (= 1 (count forms))
;;        (= (-> forms first k) v)))

;; (deftest s=-test
;;   (is (= true (s= '({:a 1 :b 2}) :a 1)))
;;   (is (false? (s= '({:a 1 :b 2}) :a 2)))
;;   (is (false? (s= '({:a 1} {:a 1}) :a 1))))

(defn s=
  [forms k vs]
  (= (->> forms (map k) set) (set vs)))

(deftest s=-test
  (is (= true (s= '({:a 1 :b 2}) :a #{1})))
  (is (false? (s= '({:a 1 :b 2}) :a #{2})))
  (is (false? (s= '({:a 1} {:a 2}) :a #{1}))))


;; (defn p=
;;   [forms k v-set]
;;   (and (= (count v-set) (count forms))
;;        (= (-> forms first k) v)))

;; (deftest p=-test
;;   (is (= true (p= '({:a 1 :b 2}) :a #{1})))
;;   (is (true? (p= '({:a 1} {:a 2}) :a #{1 2}))))

(deftest āre-generation
  (testing "ambulāre"
    (let [forms
          (get-verb-forms*
           {:dictionary-form "ambulō, ambulāre, ambulāvī, ambulātum"
            :first-person-present-sg-gloss "walk"
            :third-person-present-sg-gloss "walks"
            :first-person-perfect-sg-gloss "walked"
            :present-participle "walking"
            :perfect-participle "walked"}
           )]
      (is (s= (find-form forms {:person 3 :number "singular" :tense "present" :voice "active" :mood "indicative"}) :wordform #{"ambulat"}))
      (is (s= (find-form forms {:person 3 :number "singular" :tense "present" :voice "active" :mood "indicative"}) :gloss #{"he/she/it walks"}))
      (is (s= (find-form forms {:person 3 :number "plural" :tense "present" :voice "active" :mood "indicative"}) :wordform #{"ambulant"}))
      (is (s= (find-form forms {:person 3 :number "plural" :tense "present" :voice "active" :mood "indicative"}) :gloss #{"they walk"}))
      (is (s= (find-form forms {:person 2 :number "plural" :tense "perfect" :voice "active" :mood "indicative"}) :wordform #{"ambulāvistis"}))
      (is (s= (find-form forms {:person 2 :number "plural" :tense "perfect" :voice "active" :mood "indicative"}) :gloss #{"you walked"}))
      (is (s= (find-form forms {:person 1 :number "plural" :tense "imperfect" :voice "active" :mood "indicative"}) :wordform #{"ambulābāmus"}))
      (is (s= (find-form forms {:person 1 :number "plural" :tense "imperfect" :voice "active" :mood "indicative"}) :gloss #{"we were walking"}))
      (is (s= (find-form forms {:person 1 :number "singular" :tense "future" :voice "active" :mood "indicative"}) :wordform #{"ambulābō"}))
      (is (s= (find-form forms {:person 1 :number "singular" :tense "future" :voice "active" :mood "indicative"}) :gloss #{"I will walk"}))
      (is (s= (find-form forms {:person 1 :number "singular" :tense "future-perfect" :voice "active" :mood "indicative"}) :wordform #{"ambulāverō" "ambulārō"}))
      (is (s= (find-form forms {:person 1 :number "singular" :tense "future-perfect" :voice "active" :mood "indicative"}) :gloss #{"I will have walked"}))
      (is (s= (find-form forms {:person 3 :number "plural" :tense "present" :voice "passive" :mood "indicative"}) :wordform #{"ambulantur"}))
      (is (s= (find-form forms {:person 3 :number "plural" :tense "present" :voice "passive" :mood "indicative"}) :gloss #{"they are walked"}))
      )))
