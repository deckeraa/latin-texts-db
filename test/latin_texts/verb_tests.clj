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

;; (deftest āre-generation
;;   (testing "ambulāre"
;;     (let [forms
;;           (get-verb-forms*
;;            {:dictionary-form "ambulō, ambulāre, ambulāvī, ambulātum"
;;             :first-person-present-sg-gloss "walk"
;;             :third-person-present-sg-gloss "walks"
;;             :first-person-perfect-sg-gloss "walked"
;;             :present-participle "walking"
;;             :perfect-participle "walked"}
;;            )]
;;       (is (s= (find-form forms {:person 3 :number "singular" :tense "present" :voice "active" :mood "indicative"}) :wordform #{"ambulat"}))
;;       (is (s= (find-form forms {:person 3 :number "singular" :tense "present" :voice "active" :mood "indicative"}) :gloss #{"he/she/it walks"}))
;;       (is (s= (find-form forms {:person 3 :number "plural" :tense "present" :voice "active" :mood "indicative"}) :wordform #{"ambulant"}))
;;       (is (s= (find-form forms {:person 3 :number "plural" :tense "present" :voice "active" :mood "indicative"}) :gloss #{"they walk"}))
;;       (is (s= (find-form forms {:person 2 :number "plural" :tense "perfect" :voice "active" :mood "indicative"}) :wordform #{"ambulāvistis"}))
;;       (is (s= (find-form forms {:person 2 :number "plural" :tense "perfect" :voice "active" :mood "indicative"}) :gloss #{"you walked"}))
;;       (is (s= (find-form forms {:person 1 :number "plural" :tense "imperfect" :voice "active" :mood "indicative"}) :wordform #{"ambulābāmus"}))
;;       (is (s= (find-form forms {:person 1 :number "plural" :tense "imperfect" :voice "active" :mood "indicative"}) :gloss #{"we were walking"}))
;;       (is (s= (find-form forms {:person 1 :number "singular" :tense "future" :voice "active" :mood "indicative"}) :wordform #{"ambulābō"}))
;;       (is (s= (find-form forms {:person 1 :number "singular" :tense "future" :voice "active" :mood "indicative"}) :gloss #{"I will walk"}))
;;       (is (s= (find-form forms {:person 1 :number "singular" :tense "future-perfect" :voice "active" :mood "indicative"}) :wordform #{"ambulāverō" "ambulārō"}))
;;       (is (s= (find-form forms {:person 1 :number "singular" :tense "future-perfect" :voice "active" :mood "indicative"}) :gloss #{"I will have walked"}))
;;       (is (s= (find-form forms {:person 3 :number "plural" :tense "present" :voice "passive" :mood "indicative"}) :wordform #{"ambulantur"}))
;;       (is (s= (find-form forms {:person 3 :number "plural" :tense "present" :voice "passive" :mood "indicative"}) :gloss #{"they are walked"}))
;;       )))

(deftest āre-generation
  (testing "ambulāre — first conjugation verb forms (indicative)"
    (let [forms
          (get-verb-forms*
           {:dictionary-form "ambulō, ambulāre, ambulāvī, ambulātum"
            :first-person-present-sg-gloss "walk"
            :third-person-present-sg-gloss "walks"
            :first-person-perfect-sg-gloss "walked"
            :present-participle "walking"
            :perfect-participle "walked"})]
      ;; some of these test cases were Grok-generated and then corrected by hand
      ;; ── Present Active ──────────────────────────────────────────────
      (is (s= (find-form forms {:person 1 :number "singular" :tense "present" :voice "active" :mood "indicative"})
              :wordform #{"ambulō"}))
      (is (s= (find-form forms {:person 1 :number "singular" :tense "present" :voice "active" :mood "indicative"})
              :gloss #{"I walk"}))

      (is (s= (find-form forms {:person 3 :number "singular" :tense "present" :voice "active" :mood "indicative"})
              :wordform #{"ambulat"}))
      (is (s= (find-form forms {:person 3 :number "singular" :tense "present" :voice "active" :mood "indicative"})
              :gloss #{"he/she/it walks"}))

      (is (s= (find-form forms {:person 1 :number "plural" :tense "present" :voice "active" :mood "indicative"})
              :wordform #{"ambulāmus"}))
      (is (s= (find-form forms {:person 1 :number "plural" :tense "present" :voice "active" :mood "indicative"})
              :gloss #{"we walk"}))

      ;; ── Present Passive ─────────────────────────────────────────────
      (is (s= (find-form forms {:person 3 :number "singular" :tense "present" :voice "passive" :mood "indicative"})
              :wordform #{"ambulātur"}))
      (is (s= (find-form forms {:person 3 :number "singular" :tense "present" :voice "passive" :mood "indicative"})
              :gloss #{"he/she/it is walked"}))

      (is (s= (find-form forms {:person 3 :number "plural" :tense "present" :voice "passive" :mood "indicative"})
              :wordform #{"ambulantur"}))
      (is (s= (find-form forms {:person 3 :number "plural" :tense "present" :voice "passive" :mood "indicative"})
              :gloss #{"they are walked"}))

      ;; ── Imperfect Active ────────────────────────────────────────────
      (is (s= (find-form forms {:person 1 :number "singular" :tense "imperfect" :voice "active" :mood "indicative"})
              :wordform #{"ambulābam"}))
      (is (s= (find-form forms {:person 1 :number "singular" :tense "imperfect" :voice "active" :mood "indicative"})
              :gloss #{"I was walking"}))

      (is (s= (find-form forms {:person 3 :number "plural" :tense "imperfect" :voice "active" :mood "indicative"})
              :wordform #{"ambulābant"}))
      (is (s= (find-form forms {:person 3 :number "plural" :tense "imperfect" :voice "active" :mood "indicative"})
              :gloss #{"they were walking"}))

      ;; ── Imperfect Passive ───────────────────────────────────────────
      (is (s= (find-form forms {:person 1 :number "plural" :tense "imperfect" :voice "passive" :mood "indicative"})
              :wordform #{"ambulābāmur"}))
      (is (s= (find-form forms {:person 1 :number "plural" :tense "imperfect" :voice "passive" :mood "indicative"})
              :gloss #{"we were being walked"}))

      ;; ── Future Active ───────────────────────────────────────────────
      (is (s= (find-form forms {:person 1 :number "singular" :tense "future" :voice "active" :mood "indicative"})
              :wordform #{"ambulābō"}))
      (is (s= (find-form forms {:person 1 :number "singular" :tense "future" :voice "active" :mood "indicative"})
              :gloss #{"I will walk"}))

      (is (s= (find-form forms {:person 2 :number "singular" :tense "future" :voice "active" :mood "indicative"})
              :wordform #{"ambulābis"}))
      (is (s= (find-form forms {:person 2 :number "singular" :tense "future" :voice "active" :mood "indicative"})
              :gloss #{"you will walk"}))

      ;; ── Perfect Active ──────────────────────────────────────────────
      (is (s= (find-form forms {:person 1 :number "singular" :tense "perfect" :voice "active" :mood "indicative"})
              :wordform #{"ambulāvī"}))
      (is (s= (find-form forms {:person 1 :number "singular" :tense "perfect" :voice "active" :mood "indicative"})
              :gloss #{"I walked"}))

      (is (s= (find-form forms {:person 2 :number "plural" :tense "perfect" :voice "active" :mood "indicative"})
              :wordform #{"ambulāvistis"}))
      (is (s= (find-form forms {:person 2 :number "plural" :tense "perfect" :voice "active" :mood "indicative"})
              :gloss #{"you walked"}))

      (is (s= (find-form forms {:person 3 :number "plural" :tense "perfect" :voice "active" :mood "indicative"})
              :wordform #{"ambulāvērunt" "ambulāvēre"}))
      (is (s= (find-form forms {:person 3 :number "plural" :tense "perfect" :voice "active" :mood "indicative"})
              :gloss #{"they walked"}))

      ;; ── Pluperfect Active ───────────────────────────────────────────
      (is (s= (find-form forms {:person 1 :number "singular" :tense "pluperfect" :voice "active" :mood "indicative"})
              :wordform #{"ambulāveram" "ambulāram"}))
      (is (s= (find-form forms {:person 1 :number "singular" :tense "pluperfect" :voice "active" :mood "indicative"})
              :gloss #{"I had walked"}))

      ;; ── Future Perfect Active ───────────────────────────────────────
      (is (s= (find-form forms {:person 1 :number "singular" :tense "future-perfect" :voice "active" :mood "indicative"})
              :wordform #{"ambulāverō" "ambulārō"}))
      (is (s= (find-form forms {:person 1 :number "singular" :tense "future-perfect" :voice "active" :mood "indicative"})
              :gloss #{"I will have walked"}))

      (is (s= (find-form forms {:person 2 :number "plural" :tense "future-perfect" :voice "active" :mood "indicative"})
              :wordform #{"ambulāveritis" "ambulāritis"}))
      (is (s= (find-form forms {:person 2 :number "plural" :tense "future-perfect" :voice "active" :mood "indicative"})
              :gloss #{"you will have walked"}))
      )))

(deftest ēre-generation
  (testing "monēre — second conjugation verb forms"
    (let [forms
          (get-verb-forms*
           {:dictionary-form "moneō, monēre, monuī, monitum"
            :first-person-present-sg-gloss "warn"
            :third-person-present-sg-gloss "warns"
            :first-person-perfect-sg-gloss "warned"
            :present-participle "warning"
            :perfect-participle "warned"})]

      ;; ── Present Active Indicative ───────────────────────────────────
      (is (s= (find-form forms {:person 1 :number "singular" :tense "present" :voice "active" :mood "indicative"})
              :wordform #{"moneō"}))
      (is (s= (find-form forms {:person 1 :number "singular" :tense "present" :voice "active" :mood "indicative"})
              :gloss #{"I warn"}))

      (is (s= (find-form forms {:person 3 :number "singular" :tense "present" :voice "active" :mood "indicative"})
              :wordform #{"monet"}))
      (is (s= (find-form forms {:person 3 :number "singular" :tense "present" :voice "active" :mood "indicative"})
              :gloss #{"he/she/it warns"}))

      (is (s= (find-form forms {:person 1 :number "plural" :tense "present" :voice "active" :mood "indicative"})
              :wordform #{"monēmus"}))
      (is (s= (find-form forms {:person 1 :number "plural" :tense "present" :voice "active" :mood "indicative"})
              :gloss #{"we warn"}))

      ;; ── Present Passive Indicative ─────────────────────────────────
      (is (s= (find-form forms {:person 3 :number "singular" :tense "present" :voice "passive" :mood "indicative"})
              :wordform #{"monētur"}))
      (is (s= (find-form forms {:person 3 :number "singular" :tense "present" :voice "passive" :mood "indicative"})
              :gloss #{"he/she/it is warned"}))

      ;; ── Imperfect Active Indicative ────────────────────────────────
      (is (s= (find-form forms {:person 3 :number "plural" :tense "imperfect" :voice "active" :mood "indicative"})
              :wordform #{"monēbant"}))
      (is (s= (find-form forms {:person 3 :number "plural" :tense "imperfect" :voice "active" :mood "indicative"})
              :gloss #{"they were warning"}))

      ;; ── Future Active Indicative ────────────────────────────────────
      (is (s= (find-form forms {:person 1 :number "singular" :tense "future" :voice "active" :mood "indicative"})
              :wordform #{"monēbō"}))
      (is (s= (find-form forms {:person 1 :number "singular" :tense "future" :voice "active" :mood "indicative"})
              :gloss #{"I will warn"}))

      ;; ── Perfect Active Indicative ──────────────────────────────────
      (is (s= (find-form forms {:person 1 :number "singular" :tense "perfect" :voice "active" :mood "indicative"})
              :wordform #{"monuī"}))
      (is (s= (find-form forms {:person 1 :number "singular" :tense "perfect" :voice "active" :mood "indicative"})
              :gloss #{"I warned"}))

      (is (s= (find-form forms {:person 2 :number "plural" :tense "perfect" :voice "active" :mood "indicative"})
              :wordform #{"monuistis"}))
      (is (s= (find-form forms {:person 2 :number "plural" :tense "perfect" :voice "active" :mood "indicative"})
              :gloss #{"you warned"}))

      ;; ── Present Active Subjunctive ─────────────────────────────────
      (is (s= (find-form forms {:person 1 :number "singular" :tense "present" :voice "active" :mood "subjunctive"})
              :wordform #{"moneam"}))
      (is (s= (find-form forms {:person 1 :number "singular" :tense "present" :voice "active" :mood "subjunctive"})
              :gloss #{"I warn"}))

      (is (s= (find-form forms {:person 3 :number "plural" :tense "present" :voice "active" :mood "subjunctive"})
              :wordform #{"moneant"}))
      (is (s= (find-form forms {:person 3 :number "plural" :tense "present" :voice "active" :mood "subjunctive"})
              :gloss #{"they warn"}))

      ;; ── Present Passive Subjunctive ────────────────────────────────
      (is (s= (find-form forms {:person 3 :number "singular" :tense "present" :voice "passive" :mood "subjunctive"})
              :wordform #{"moneātur"}))
      (is (s= (find-form forms {:person 3 :number "singular" :tense "present" :voice "passive" :mood "subjunctive"})
              :gloss #{"he/she/it is warned"}))

      ;; ── Imperfect Active Subjunctive ───────────────────────────────
      (is (s= (find-form forms {:person 1 :number "plural" :tense "imperfect" :voice "active" :mood "subjunctive"})
              :wordform #{"monērēmus"}))
      (is (s= (find-form forms {:person 1 :number "plural" :tense "imperfect" :voice "active" :mood "subjunctive"})
              :gloss #{"we warn"}))

      ;; ── Perfect Active Subjunctive ─────────────────────────────────
      (is (s= (find-form forms {:person 3 :number "singular" :tense "perfect" :voice "active" :mood "subjunctive"})
              :wordform #{"monuerit"}))
      (is (s= (find-form forms {:person 3 :number "singular" :tense "perfect" :voice "active" :mood "subjunctive"})
              :gloss #{"he/she/it warned"}))

      ;; ── Infinitives ─────────────────────────────────────────────────
      (is (s= (find-form forms {:tense "present" :voice "active" :mood "infinitive"})
              :wordform #{"monēre"}))
      (is (s= (find-form forms {:tense "present" :voice "active" :mood "infinitive"})
              :gloss #{"to warn"}))

      (is (s= (find-form forms {:tense "present" :voice "passive" :mood "infinitive"})
              :wordform #{"monērī"}))
      (is (s= (find-form forms {:tense "present" :voice "passive" :mood "infinitive"})
              :gloss #{"to be warned"}))

      (is (s= (find-form forms {:tense "perfect" :voice "active" :mood "infinitive"})
              :wordform #{"monuisse"}))
      (is (s= (find-form forms {:tense "perfect" :voice "active" :mood "infinitive"})
              :gloss #{"to have warned"}))
      )))

(deftest ēre-deponent
  (testing "vereor"
    (let [forms
          (get-verb-forms*-dep
           {:dictionary-form "vereor, verērī, veritum"
            :first-person-present-sg-gloss "fear"
            :third-person-present-sg-gloss "fears"
            :first-person-perfect-sg-gloss "feared"
            :present-participle "fearing"
            :perfect-participle "feared"})]
      (is (s= (find-form forms {:person 1 :number "singular" :tense "present" :voice "active" :mood "indicative"}) :wordform #{"vereor"}))
      (is (s= (find-form forms {:person 1 :number "singular" :tense "present" :voice "active" :mood "indicative"}) :gloss #{"I fear"}))
      (is (s= (find-form forms {:person 2 :number "singular" :tense "present" :voice "active" :mood "indicative"}) :wordform #{"verēris"}))
      (is (s= (find-form forms {:person 3 :number "singular" :tense "imperfect" :voice "active" :mood "indicative"}) :wordform #{"verēbātur"}))
      (is (s= (find-form forms {:person 2 :number "plural" :tense "future" :voice "active" :mood "indicative"}) :wordform #{"verēbiminī"}))
      (is (s= (find-form forms {:person 2 :number "plural" :tense "future" :voice "active" :mood "indicative"}) :gloss #{"you will fear"}))
      (is (s= (find-form forms {:person 1 :number "singular" :tense "present" :voice "active" :mood "subjunctive"}) :wordform #{"verear"}))
      (is (s= (find-form forms {:person 1 :number "singular" :tense "present" :voice "active" :mood "subjunctive"}) :gloss #{"I fear"}))
      (is (s= (find-form forms {:person 3 :number "singular" :tense "imperfect" :voice "active" :mood "subjunctive"}) :wordform #{"verērētur"}))
      (is (s= (find-form forms {:person 3 :number "singular" :tense "imperfect" :voice "active" :mood "subjunctive"}) :gloss #{"he/she/it feared"}))
      ;; participles
      (is (s= (find-form forms {:number "singular" :tense "present" :case_ "nominative" :gender "masculine"}) :wordform #{"verēns"}))
      (is (s= (find-form forms {:number "singular" :tense "present" :case_ "nominative" :gender "masculine"}) :gloss #{"fearing"}))
      (is (s= (find-form forms {:number "singular" :tense "present" :case_ "nominative" :gender "feminine"}) :wordform #{"verēns"}))
      (is (s= (find-form forms {:number "singular" :tense "present" :case_ "nominative" :gender "feminine"}) :gloss #{"fearing"}))
      (is (s= (find-form forms {:number "plural" :tense "present" :case_ "nominative" :gender "neuter"}) :wordform #{"verentia"}))
      (is (s= (find-form forms {:number "plural" :tense "present" :case_ "nominative" :gender "neuter"}) :gloss #{"fearing"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "passive" :case_ "nominative" :gender "masculine"}) :wordform #{"verendī"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "passive" :case_ "nominative" :gender "masculine"}) :gloss #{"fearing"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "passive" :case_ "genitive" :gender "feminine"}) :wordform #{"verendārum"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "passive" :case_ "genitive" :gender "feminine"}) :gloss #{"fearing"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "passive" :case_ "genitive" :gender "neuter"}) :wordform #{"verendōrum"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "passive" :case_ "genitive" :gender "neuter"}) :gloss #{"fearing"}))
      (is (s= (find-form forms {:number "plural" :tense "perfect" :voice "passive" :case_ "dative" :gender "masculine"}) :wordform #{"veritīs"}))
      (is (s= (find-form forms {:number "plural" :tense "perfect" :voice "passive" :case_ "dative" :gender "masculine"}) :gloss #{"was fearing"}))
      (is (s= (find-form forms {:number "plural" :tense "perfect" :voice "passive" :case_ "dative" :gender "feminine"}) :wordform #{"veritīs"}))
      (is (s= (find-form forms {:number "plural" :tense "perfect" :voice "passive" :case_ "dative" :gender "feminine"}) :gloss #{"was fearing"}))
      (is (s= (find-form forms {:number "plural" :tense "perfect" :voice "passive" :case_ "dative" :gender "neuter"}) :wordform #{"veritīs"}))
      (is (s= (find-form forms {:number "plural" :tense "perfect" :voice "passive" :case_ "dative" :gender "neuter"}) :gloss #{"was fearing"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "active" :case_ "accusative" :gender "masculine"}) :wordform #{"veritūrōs"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "active" :case_ "accusative" :gender "masculine"}) :gloss #{"fearing"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "active" :case_ "accusative" :gender "feminine"}) :wordform #{"veritūrās"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "active" :case_ "accusative" :gender "feminine"}) :gloss #{"fearing"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "active" :case_ "accusative" :gender "neuter"}) :wordform #{"veritūra"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "active" :case_ "accusative" :gender "neuter"}) :gloss #{"fearing"}))
      (is (s= (find-form forms {:tense "present" :mood "infinitive"}) :wordform #{"verērī"}))
      (is (s= (find-form forms {:tense "present" :mood "infinitive"}) :gloss #{"to fear"}))
      (is (s= (find-form forms {:number "singular" :mood "imperative"}) :wordform #{"verēre"}))
      (is (s= (find-form forms {:number "singular" :mood "imperative"}) :gloss #{"fear!"}))
      (is (s= (find-form forms {:number "plural" :mood "imperative"}) :wordform #{"verēminī"}))
      (is (s= (find-form forms {:number "plural" :mood "imperative"}) :gloss #{"fear!"}))
      ))
  (testing "deponent detection"
    (let [forms (get-verb-forms*
                 {:dictionary-form "vereor, verērī, veritum"
                  :first-person-present-sg-gloss "fear"
                  :third-person-present-sg-gloss "fears"
                  :first-person-perfect-sg-gloss "feared"
                  :present-participle "fearing"
                  :perfect-participle "feared"})]
      (is (s= (find-form forms {:number "plural" :mood "imperative"}) :wordform #{"verēminī"}))
      (is (s= (find-form forms {:number "plural" :mood "imperative"}) :gloss #{"fear!"})))))

(deftest ārī-deponent
  (testing "hortor"
    (let [forms
          (get-verb-forms*
           {:dictionary-form "hortor, hortārī, hortātum"
            :first-person-present-sg-gloss "exhort"
            :third-person-present-sg-gloss "exhorts"
            :first-person-perfect-sg-gloss "exhorted"
            :present-participle "exhorting"
            :perfect-participle "exhorted"})]
      (is (s= (find-form forms {:person 1 :number "singular" :tense "present" :voice "active" :mood "indicative"}) :wordform #{"hortor"}))
      (is (s= (find-form forms {:person 1 :number "singular" :tense "present" :voice "active" :mood "indicative"}) :gloss #{"I exhort"}))
      (is (s= (find-form forms {:person 2 :number "singular" :tense "present" :voice "active" :mood "indicative"}) :wordform #{"hortāris"}))
      (is (s= (find-form forms {:person 3 :number "singular" :tense "imperfect" :voice "active" :mood "indicative"}) :wordform #{"hortābātur"}))
      (is (s= (find-form forms {:person 2 :number "plural" :tense "future" :voice "active" :mood "indicative"}) :wordform #{"hortābiminī"}))
      (is (s= (find-form forms {:person 2 :number "plural" :tense "future" :voice "active" :mood "indicative"}) :gloss #{"you will exhort"}))
      (is (s= (find-form forms {:person 1 :number "singular" :tense "present" :voice "active" :mood "subjunctive"}) :wordform #{"horter"}))
      (is (s= (find-form forms {:person 1 :number "singular" :tense "present" :voice "active" :mood "subjunctive"}) :gloss #{"I exhort"}))
      (is (s= (find-form forms {:person 3 :number "singular" :tense "imperfect" :voice "active" :mood "subjunctive"}) :wordform #{"hortārētur"}))
      (is (s= (find-form forms {:person 3 :number "singular" :tense "imperfect" :voice "active" :mood "subjunctive"}) :gloss #{"he/she/it exhorted"}))
      ;; participles
      (is (s= (find-form forms {:number "singular" :tense "present" :case_ "nominative" :gender "masculine"}) :wordform #{"hortāns"}))
      (is (s= (find-form forms {:number "singular" :tense "present" :case_ "nominative" :gender "masculine"}) :gloss #{"exhorting"}))
      (is (s= (find-form forms {:number "singular" :tense "present" :case_ "nominative" :gender "feminine"}) :wordform #{"hortāns"}))
      (is (s= (find-form forms {:number "singular" :tense "present" :case_ "nominative" :gender "feminine"}) :gloss #{"exhorting"}))
      (is (s= (find-form forms {:number "plural" :tense "present" :case_ "nominative" :gender "neuter"}) :wordform #{"hortantia"}))
      (is (s= (find-form forms {:number "plural" :tense "present" :case_ "nominative" :gender "neuter"}) :gloss #{"exhorting"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "passive" :case_ "nominative" :gender "masculine"}) :wordform #{"hortandī"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "passive" :case_ "nominative" :gender "masculine"}) :gloss #{"exhorting"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "passive" :case_ "genitive" :gender "feminine"}) :wordform #{"hortandārum"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "passive" :case_ "genitive" :gender "feminine"}) :gloss #{"exhorting"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "passive" :case_ "genitive" :gender "neuter"}) :wordform #{"hortandōrum"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "passive" :case_ "genitive" :gender "neuter"}) :gloss #{"exhorting"}))
      (is (s= (find-form forms {:number "plural" :tense "perfect" :voice "passive" :case_ "dative" :gender "masculine"}) :wordform #{"hortātīs"}))
      (is (s= (find-form forms {:number "plural" :tense "perfect" :voice "passive" :case_ "dative" :gender "masculine"}) :gloss #{"was exhorting"}))
      (is (s= (find-form forms {:number "plural" :tense "perfect" :voice "passive" :case_ "dative" :gender "feminine"}) :wordform #{"hortātīs"}))
      (is (s= (find-form forms {:number "plural" :tense "perfect" :voice "passive" :case_ "dative" :gender "feminine"}) :gloss #{"was exhorting"}))
      (is (s= (find-form forms {:number "plural" :tense "perfect" :voice "passive" :case_ "dative" :gender "neuter"}) :wordform #{"hortātīs"}))
      (is (s= (find-form forms {:number "plural" :tense "perfect" :voice "passive" :case_ "dative" :gender "neuter"}) :gloss #{"was exhorting"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "active" :case_ "accusative" :gender "masculine"}) :wordform #{"hortātūrōs"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "active" :case_ "accusative" :gender "masculine"}) :gloss #{"exhorting"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "active" :case_ "accusative" :gender "feminine"}) :wordform #{"hortātūrās"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "active" :case_ "accusative" :gender "feminine"}) :gloss #{"exhorting"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "active" :case_ "accusative" :gender "neuter"}) :wordform #{"hortātūra"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "active" :case_ "accusative" :gender "neuter"}) :gloss #{"exhorting"}))
      (is (s= (find-form forms {:tense "present" :mood "infinitive"}) :wordform #{"hortārī"}))
      (is (s= (find-form forms {:tense "present" :mood "infinitive"}) :gloss #{"to exhort"}))
      (is (s= (find-form forms {:number "singular" :mood "imperative"}) :wordform #{"hortāre"}))
      (is (s= (find-form forms {:number "singular" :mood "imperative"}) :gloss #{"exhort!"}))
      (is (s= (find-form forms {:number "plural" :mood "imperative"}) :wordform #{"hortāminī"}))
      (is (s= (find-form forms {:number "plural" :mood "imperative"}) :gloss #{"exhort!"}))
      )))

(deftest third-conjugation-generation
  ;; AI disclosure: Grok-generated
  (testing "dūcere — third conjugation verb forms"
    (let [forms
          (get-verb-forms*
           {:dictionary-form "dūcō, dūcere, dūxī, ductum"
            :first-person-present-sg-gloss "lead"
            :third-person-present-sg-gloss "leads"
            :first-person-perfect-sg-gloss "led"
            :present-participle "leading"
            :perfect-participle "led"})]

      ;; ── Present Active Indicative ───────────────────────────────────
      (is (s= (find-form forms {:person 1 :number "singular" :tense "present" :voice "active" :mood "indicative"})
              :wordform #{"dūcō"}))
      (is (s= (find-form forms {:person 1 :number "singular" :tense "present" :voice "active" :mood "indicative"})
              :gloss #{"I lead"}))

      (is (s= (find-form forms {:person 3 :number "singular" :tense "present" :voice "active" :mood "indicative"})
              :wordform #{"dūcit"}))
      (is (s= (find-form forms {:person 3 :number "singular" :tense "present" :voice "active" :mood "indicative"})
              :gloss #{"he/she/it leads"}))

      (is (s= (find-form forms {:person 3 :number "plural" :tense "present" :voice "active" :mood "indicative"})
              :wordform #{"dūcunt"}))
      (is (s= (find-form forms {:person 3 :number "plural" :tense "present" :voice "active" :mood "indicative"})
              :gloss #{"they lead"}))

      ;; ── Present Passive Indicative ─────────────────────────────────
      (is (s= (find-form forms {:person 3 :number "singular" :tense "present" :voice "passive" :mood "indicative"})
              :wordform #{"dūcitur"}))
      (is (s= (find-form forms {:person 3 :number "singular" :tense "present" :voice "passive" :mood "indicative"})
              :gloss #{"he/she/it is led"}))

      (is (s= (find-form forms {:person 3 :number "plural" :tense "present" :voice "passive" :mood "indicative"})
              :wordform #{"dūcuntur"}))
      (is (s= (find-form forms {:person 3 :number "plural" :tense "present" :voice "passive" :mood "indicative"})
              :gloss #{"they are led"}))

      ;; ── Imperfect Active Indicative ────────────────────────────────
      (is (s= (find-form forms {:person 1 :number "singular" :tense "imperfect" :voice "active" :mood "indicative"})
              :wordform #{"dūcēbam"}))
      (is (s= (find-form forms {:person 1 :number "singular" :tense "imperfect" :voice "active" :mood "indicative"})
              :gloss #{"I was leading"}))

      ;; ── Future Active Indicative ───────────────────────────────────
      (is (s= (find-form forms {:person 1 :number "singular" :tense "future" :voice "active" :mood "indicative"})
              :wordform #{"dūcam"}))
      (is (s= (find-form forms {:person 1 :number "singular" :tense "future" :voice "active" :mood "indicative"})
              :gloss #{"I will lead"}))

      (is (s= (find-form forms {:person 2 :number "singular" :tense "future" :voice "active" :mood "indicative"})
              :wordform #{"dūcēs"}))
      (is (s= (find-form forms {:person 2 :number "singular" :tense "future" :voice "active" :mood "indicative"})
              :gloss #{"you will lead"}))

      (is (s= (find-form forms {:person 3 :number "plural" :tense "future" :voice "active" :mood "indicative"})
              :wordform #{"dūcent"}))
      (is (s= (find-form forms {:person 3 :number "plural" :tense "future" :voice "active" :mood "indicative"})
              :gloss #{"they will lead"}))

      ;; ── Perfect Active Indicative ──────────────────────────────────
      (is (s= (find-form forms {:person 1 :number "singular" :tense "perfect" :voice "active" :mood "indicative"})
              :wordform #{"dūxī"}))
      (is (s= (find-form forms {:person 1 :number "singular" :tense "perfect" :voice "active" :mood "indicative"})
              :gloss #{"I led"}))

      (is (s= (find-form forms {:person 2 :number "plural" :tense "perfect" :voice "active" :mood "indicative"})
              :wordform #{"dūxistis"}))
      (is (s= (find-form forms {:person 2 :number "plural" :tense "perfect" :voice "active" :mood "indicative"})
              :gloss #{"you led"}))

      ;; ── Present Active Subjunctive ─────────────────────────────────
      (is (s= (find-form forms {:person 1 :number "singular" :tense "present" :voice "active" :mood "subjunctive"})
              :wordform #{"dūcam"}))
      (is (s= (find-form forms {:person 1 :number "singular" :tense "present" :voice "active" :mood "subjunctive"})
              :gloss #{"I lead"}))

      (is (s= (find-form forms {:person 3 :number "plural" :tense "present" :voice "active" :mood "subjunctive"})
              :wordform #{"dūcant"}))
      (is (s= (find-form forms {:person 3 :number "plural" :tense "present" :voice "active" :mood "subjunctive"})
              :gloss #{"they lead"}))

      ;; ── Present Passive Subjunctive ────────────────────────────────
      (is (s= (find-form forms {:person 3 :number "singular" :tense "present" :voice "passive" :mood "subjunctive"})
              :wordform #{"dūcātur"}))
      (is (s= (find-form forms {:person 3 :number "singular" :tense "present" :voice "passive" :mood "subjunctive"})
              :gloss #{"he/she/it is led"}))

      ;; ── Imperfect Active Subjunctive ───────────────────────────────
      (is (s= (find-form forms {:person 1 :number "plural" :tense "imperfect" :voice "active" :mood "subjunctive"})
              :wordform #{"dūcerēmus"}))
      (is (s= (find-form forms {:person 1 :number "plural" :tense "imperfect" :voice "active" :mood "subjunctive"})
              :gloss #{"we lead"}))

      ;; ── Perfect Active Subjunctive ─────────────────────────────────
      (is (s= (find-form forms {:person 3 :number "singular" :tense "perfect" :voice "active" :mood "subjunctive"})
              :wordform #{"dūxerit"}))
      (is (s= (find-form forms {:person 3 :number "singular" :tense "perfect" :voice "active" :mood "subjunctive"})
              :gloss #{"he/she/it led"}))

      ;; ── Infinitives ─────────────────────────────────────────────────
      (is (s= (find-form forms {:tense "present" :voice "active" :mood "infinitive"})
              :wordform #{"dūcere"}))
      (is (s= (find-form forms {:tense "present" :voice "active" :mood "infinitive"})
              :gloss #{"to lead"}))

      (is (s= (find-form forms {:tense "present" :voice "passive" :mood "infinitive"})
              :wordform #{"dūcī"}))
      (is (s= (find-form forms {:tense "present" :voice "passive" :mood "infinitive"})
              :gloss #{"to be led"}))

      (is (s= (find-form forms {:tense "perfect" :voice "active" :mood "infinitive"})
              :wordform #{"dūxisse"}))
      (is (s= (find-form forms {:tense "perfect" :voice "active" :mood "infinitive"})
              :gloss #{"to have led"})))))

(deftest ī-deponent
  (testing "sequor"
    (let [forms
          (get-verb-forms*
           {:dictionary-form "sequor, sequī, sēcūtum"
            :first-person-present-sg-gloss "follow"
            :third-person-present-sg-gloss "follows"
            :first-person-perfect-sg-gloss "followed"
            :present-participle "following"
            :perfect-participle "followed"})]
      (is (s= (find-form forms {:person 1 :number "singular" :tense "present" :voice "active" :mood "indicative"}) :wordform #{"sequor"}))
      (is (s= (find-form forms {:person 1 :number "singular" :tense "present" :voice "active" :mood "indicative"}) :gloss #{"I follow"}))
      (is (s= (find-form forms {:person 2 :number "singular" :tense "present" :voice "active" :mood "indicative"}) :wordform #{"sequeris"}))
      (is (s= (find-form forms {:person 3 :number "singular" :tense "imperfect" :voice "active" :mood "indicative"}) :wordform #{"sequēbātur"}))
      (is (s= (find-form forms {:person 2 :number "plural" :tense "future" :voice "active" :mood "indicative"}) :wordform #{"sequēminī"}))
      (is (s= (find-form forms {:person 2 :number "plural" :tense "future" :voice "active" :mood "indicative"}) :gloss #{"you will follow"}))
      (is (s= (find-form forms {:person 1 :number "singular" :tense "present" :voice "active" :mood "subjunctive"}) :wordform #{"sequar"}))
      (is (s= (find-form forms {:person 1 :number "singular" :tense "present" :voice "active" :mood "subjunctive"}) :gloss #{"I follow"}))
      (is (s= (find-form forms {:person 3 :number "singular" :tense "imperfect" :voice "active" :mood "subjunctive"}) :wordform #{"sequerētur"}))
      (is (s= (find-form forms {:person 3 :number "singular" :tense "imperfect" :voice "active" :mood "subjunctive"}) :gloss #{"he/she/it followed"}))
      ;; participles
      (is (s= (find-form forms {:number "singular" :tense "present" :case_ "nominative" :gender "masculine"}) :wordform #{"sequēns"}))
      (is (s= (find-form forms {:number "singular" :tense "present" :case_ "nominative" :gender "masculine"}) :gloss #{"following"}))
      (is (s= (find-form forms {:number "singular" :tense "present" :case_ "nominative" :gender "feminine"}) :wordform #{"sequēns"}))
      (is (s= (find-form forms {:number "singular" :tense "present" :case_ "nominative" :gender "feminine"}) :gloss #{"following"}))
      (is (s= (find-form forms {:number "plural" :tense "present" :case_ "nominative" :gender "neuter"}) :wordform #{"sequentia"}))
      (is (s= (find-form forms {:number "plural" :tense "present" :case_ "nominative" :gender "neuter"}) :gloss #{"following"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "passive" :case_ "nominative" :gender "masculine"}) :wordform #{"sequendī"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "passive" :case_ "nominative" :gender "masculine"}) :gloss #{"following"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "passive" :case_ "genitive" :gender "feminine"}) :wordform #{"sequendārum"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "passive" :case_ "genitive" :gender "feminine"}) :gloss #{"following"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "passive" :case_ "genitive" :gender "neuter"}) :wordform #{"sequendōrum"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "passive" :case_ "genitive" :gender "neuter"}) :gloss #{"following"}))
      (is (s= (find-form forms {:number "plural" :tense "perfect" :voice "passive" :case_ "dative" :gender "masculine"}) :wordform #{"sēcūtīs"}))
      (is (s= (find-form forms {:number "plural" :tense "perfect" :voice "passive" :case_ "dative" :gender "masculine"}) :gloss #{"was following"}))
      (is (s= (find-form forms {:number "plural" :tense "perfect" :voice "passive" :case_ "dative" :gender "feminine"}) :wordform #{"sēcūtīs"}))
      (is (s= (find-form forms {:number "plural" :tense "perfect" :voice "passive" :case_ "dative" :gender "feminine"}) :gloss #{"was following"}))
      (is (s= (find-form forms {:number "plural" :tense "perfect" :voice "passive" :case_ "dative" :gender "neuter"}) :wordform #{"sēcūtīs"}))
      (is (s= (find-form forms {:number "plural" :tense "perfect" :voice "passive" :case_ "dative" :gender "neuter"}) :gloss #{"was following"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "active" :case_ "accusative" :gender "masculine"}) :wordform #{"sēcūtūrōs"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "active" :case_ "accusative" :gender "masculine"}) :gloss #{"following"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "active" :case_ "accusative" :gender "feminine"}) :wordform #{"sēcūtūrās"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "active" :case_ "accusative" :gender "feminine"}) :gloss #{"following"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "active" :case_ "accusative" :gender "neuter"}) :wordform #{"sēcūtūra"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "active" :case_ "accusative" :gender "neuter"}) :gloss #{"following"}))
      (is (s= (find-form forms {:tense "present" :mood "infinitive"}) :wordform #{"sequī"}))
      (is (s= (find-form forms {:tense "present" :mood "infinitive"}) :gloss #{"to follow"}))
      (is (s= (find-form forms {:number "singular" :mood "imperative"}) :wordform #{"sequere"}))
      (is (s= (find-form forms {:number "singular" :mood "imperative"}) :gloss #{"follow!"}))
      (is (s= (find-form forms {:number "plural" :mood "imperative"}) :wordform #{"sequiminī"}))
      (is (s= (find-form forms {:number "plural" :mood "imperative"}) :gloss #{"follow!"}))
      )))

(deftest third-io-conjugation-generation
  ;; Grok-generated
  (testing "capere — third conjugation -iō verb forms"
    (let [forms
          (get-verb-forms*
           {:dictionary-form "capiō, capere, cēpī, captum"
            :first-person-present-sg-gloss "take"
            :third-person-present-sg-gloss "takes"
            :first-person-perfect-sg-gloss "took"
            :present-participle "taking"
            :perfect-participle "taken"})]

      ;; ── Present Active Indicative ───────────────────────────────────
      (is (s= (find-form forms {:person 1 :number "singular" :tense "present" :voice "active" :mood "indicative"})
              :wordform #{"capiō"}))
      (is (s= (find-form forms {:person 1 :number "singular" :tense "present" :voice "active" :mood "indicative"})
              :gloss #{"I take"}))

      (is (s= (find-form forms {:person 3 :number "singular" :tense "present" :voice "active" :mood "indicative"})
              :wordform #{"capit"}))
      (is (s= (find-form forms {:person 3 :number "singular" :tense "present" :voice "active" :mood "indicative"})
              :gloss #{"he/she/it takes"}))

      (is (s= (find-form forms {:person 3 :number "plural" :tense "present" :voice "active" :mood "indicative"})
              :wordform #{"capiunt"}))
      (is (s= (find-form forms {:person 3 :number "plural" :tense "present" :voice "active" :mood "indicative"})
              :gloss #{"they take"}))

      ;; ── Present Passive Indicative ─────────────────────────────────
      (is (s= (find-form forms {:person 3 :number "singular" :tense "present" :voice "passive" :mood "indicative"})
              :wordform #{"capitur"}))
      (is (s= (find-form forms {:person 3 :number "singular" :tense "present" :voice "passive" :mood "indicative"})
              :gloss #{"he/she/it is taken"}))

      ;; ── Imperfect Active Indicative ────────────────────────────────
      (is (s= (find-form forms {:person 1 :number "singular" :tense "imperfect" :voice "active" :mood "indicative"})
              :wordform #{"capiēbam"}))
      (is (s= (find-form forms {:person 1 :number "singular" :tense "imperfect" :voice "active" :mood "indicative"})
              :gloss #{"I was taking"}))

      ;; ── Future Active Indicative ───────────────────────────────────
      (is (s= (find-form forms {:person 1 :number "singular" :tense "future" :voice "active" :mood "indicative"})
              :wordform #{"capiam"}))
      (is (s= (find-form forms {:person 1 :number "singular" :tense "future" :voice "active" :mood "indicative"})
              :gloss #{"I will take"}))

      (is (s= (find-form forms {:person 2 :number "singular" :tense "future" :voice "active" :mood "indicative"})
              :wordform #{"capiēs"}))
      (is (s= (find-form forms {:person 2 :number "singular" :tense "future" :voice "active" :mood "indicative"})
              :gloss #{"you will take"}))

      ;; ── Perfect Active Indicative ──────────────────────────────────
      (is (s= (find-form forms {:person 1 :number "singular" :tense "perfect" :voice "active" :mood "indicative"})
              :wordform #{"cēpī"}))
      (is (s= (find-form forms {:person 1 :number "singular" :tense "perfect" :voice "active" :mood "indicative"})
              :gloss #{"I took"}))

      (is (s= (find-form forms {:person 2 :number "plural" :tense "perfect" :voice "active" :mood "indicative"})
              :wordform #{"cēpistis"}))
      (is (s= (find-form forms {:person 2 :number "plural" :tense "perfect" :voice "active" :mood "indicative"})
              :gloss #{"you took"}))

      ;; ── Present Active Subjunctive ─────────────────────────────────
      (is (s= (find-form forms {:person 1 :number "singular" :tense "present" :voice "active" :mood "subjunctive"})
              :wordform #{"capiam"}))
      (is (s= (find-form forms {:person 1 :number "singular" :tense "present" :voice "active" :mood "subjunctive"})
              :gloss #{"I take"}))

      (is (s= (find-form forms {:person 3 :number "plural" :tense "present" :voice "active" :mood "subjunctive"})
              :wordform #{"capiant"}))
      (is (s= (find-form forms {:person 3 :number "plural" :tense "present" :voice "active" :mood "subjunctive"})
              :gloss #{"they take"}))

      ;; ── Present Passive Subjunctive ────────────────────────────────
      (is (s= (find-form forms {:person 3 :number "singular" :tense "present" :voice "passive" :mood "subjunctive"})
              :wordform #{"capiātur"}))
      (is (s= (find-form forms {:person 3 :number "singular" :tense "present" :voice "passive" :mood "subjunctive"})
              :gloss #{"he/she/it is taken"}))

      ;; ── Imperfect Active Subjunctive ───────────────────────────────
      (is (s= (find-form forms {:person 1 :number "plural" :tense "imperfect" :voice "active" :mood "subjunctive"})
              :wordform #{"caperēmus"}))
      (is (s= (find-form forms {:person 1 :number "plural" :tense "imperfect" :voice "active" :mood "subjunctive"})
              :gloss #{"we took"}))

      ;; ── Perfect Active Subjunctive ─────────────────────────────────
      (is (s= (find-form forms {:person 3 :number "singular" :tense "perfect" :voice "active" :mood "subjunctive"})
              :wordform #{"cēperit"}))
      (is (s= (find-form forms {:person 3 :number "singular" :tense "perfect" :voice "active" :mood "subjunctive"})
              :gloss #{"he/she/it took"}))

      ;; ── Infinitives ─────────────────────────────────────────────────
      (is (s= (find-form forms {:tense "present" :voice "active" :mood "infinitive"})
              :wordform #{"capere"}))
      (is (s= (find-form forms {:tense "present" :voice "active" :mood "infinitive"})
              :gloss #{"to take"}))

      (is (s= (find-form forms {:tense "present" :voice "passive" :mood "infinitive"})
              :wordform #{"capī"}))
      (is (s= (find-form forms {:tense "present" :voice "passive" :mood "infinitive"})
              :gloss #{"to be taken"}))

      (is (s= (find-form forms {:tense "perfect" :voice "active" :mood "infinitive"})
              :wordform #{"cēpisse"}))
      (is (s= (find-form forms {:tense "perfect" :voice "active" :mood "infinitive"})
              :gloss #{"to have taken"})))))

(deftest ī-deponent-i-stem
  (testing "patior"
    (let [forms
          (get-verb-forms*
           {:dictionary-form "patior, patī, passum"
            :first-person-present-sg-gloss "suffer"
            :third-person-present-sg-gloss "suffers"
            :first-person-perfect-sg-gloss "suffered"
            :present-participle "suffering"
            :perfect-participle "suffered"})]
      (is (s= (find-form forms {:person 1 :number "singular" :tense "present" :voice "active" :mood "indicative"}) :wordform #{"patior"}))
      (is (s= (find-form forms {:person 1 :number "singular" :tense "present" :voice "active" :mood "indicative"}) :gloss #{"I suffer"}))
      (is (s= (find-form forms {:person 2 :number "singular" :tense "present" :voice "active" :mood "indicative"}) :wordform #{"pateris"}))
      (is (s= (find-form forms {:person 3 :number "singular" :tense "imperfect" :voice "active" :mood "indicative"}) :wordform #{"patiēbātur"}))
      (is (s= (find-form forms {:person 2 :number "plural" :tense "future" :voice "active" :mood "indicative"}) :wordform #{"patiēminī"}))
      (is (s= (find-form forms {:person 2 :number "plural" :tense "future" :voice "active" :mood "indicative"}) :gloss #{"you will suffer"}))
      (is (s= (find-form forms {:person 1 :number "singular" :tense "present" :voice "active" :mood "subjunctive"}) :wordform #{"patiar"}))
      (is (s= (find-form forms {:person 1 :number "singular" :tense "present" :voice "active" :mood "subjunctive"}) :gloss #{"I suffer"}))
      (is (s= (find-form forms {:person 3 :number "singular" :tense "imperfect" :voice "active" :mood "subjunctive"}) :wordform #{"paterētur"}))
      (is (s= (find-form forms {:person 3 :number "singular" :tense "imperfect" :voice "active" :mood "subjunctive"}) :gloss #{"he/she/it suffered"}))
      ;; participles
      (is (s= (find-form forms {:number "singular" :tense "present" :case_ "nominative" :gender "masculine"}) :wordform #{"patiēns"}))
      (is (s= (find-form forms {:number "singular" :tense "present" :case_ "nominative" :gender "masculine"}) :gloss #{"suffering"}))
      (is (s= (find-form forms {:number "singular" :tense "present" :case_ "nominative" :gender "feminine"}) :wordform #{"patiēns"}))
      (is (s= (find-form forms {:number "singular" :tense "present" :case_ "nominative" :gender "feminine"}) :gloss #{"suffering"}))
      (is (s= (find-form forms {:number "plural" :tense "present" :case_ "nominative" :gender "neuter"}) :wordform #{"patientia"}))
      (is (s= (find-form forms {:number "plural" :tense "present" :case_ "nominative" :gender "neuter"}) :gloss #{"suffering"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "passive" :case_ "nominative" :gender "masculine"}) :wordform #{"patiendī"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "passive" :case_ "nominative" :gender "masculine"}) :gloss #{"suffering"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "passive" :case_ "genitive" :gender "feminine"}) :wordform #{"patiendārum"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "passive" :case_ "genitive" :gender "feminine"}) :gloss #{"suffering"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "passive" :case_ "genitive" :gender "neuter"}) :wordform #{"patiendōrum"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "passive" :case_ "genitive" :gender "neuter"}) :gloss #{"suffering"}))
      (is (s= (find-form forms {:number "plural" :tense "perfect" :voice "passive" :case_ "dative" :gender "masculine"}) :wordform #{"passīs"}))
      (is (s= (find-form forms {:number "plural" :tense "perfect" :voice "passive" :case_ "dative" :gender "masculine"}) :gloss #{"was suffering"}))
      (is (s= (find-form forms {:number "plural" :tense "perfect" :voice "passive" :case_ "dative" :gender "feminine"}) :wordform #{"passīs"}))
      (is (s= (find-form forms {:number "plural" :tense "perfect" :voice "passive" :case_ "dative" :gender "feminine"}) :gloss #{"was suffering"}))
      (is (s= (find-form forms {:number "plural" :tense "perfect" :voice "passive" :case_ "dative" :gender "neuter"}) :wordform #{"passīs"}))
      (is (s= (find-form forms {:number "plural" :tense "perfect" :voice "passive" :case_ "dative" :gender "neuter"}) :gloss #{"was suffering"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "active" :case_ "accusative" :gender "masculine"}) :wordform #{"passūrōs"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "active" :case_ "accusative" :gender "masculine"}) :gloss #{"suffering"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "active" :case_ "accusative" :gender "feminine"}) :wordform #{"passūrās"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "active" :case_ "accusative" :gender "feminine"}) :gloss #{"suffering"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "active" :case_ "accusative" :gender "neuter"}) :wordform #{"passūra"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "active" :case_ "accusative" :gender "neuter"}) :gloss #{"suffering"}))
      (is (s= (find-form forms {:tense "present" :mood "infinitive"}) :wordform #{"patī"}))
      (is (s= (find-form forms {:tense "present" :mood "infinitive"}) :gloss #{"to suffer"}))
      (is (s= (find-form forms {:number "singular" :mood "imperative"}) :wordform #{"patere"}))
      (is (s= (find-form forms {:number "singular" :mood "imperative"}) :gloss #{"suffer!"}))
      (is (s= (find-form forms {:number "plural" :mood "imperative"}) :wordform #{"patiminī"}))
      (is (s= (find-form forms {:number "plural" :mood "imperative"}) :gloss #{"suffer!"}))
      )))

(deftest fourth-conjugation-generation
  (testing "audīre — fourth conjugation verb forms"
    (let [forms
          (get-verb-forms*
           {:dictionary-form "audiō, audīre, audīvī, audītum"
            :first-person-present-sg-gloss "hear"
            :third-person-present-sg-gloss "hears"
            :first-person-perfect-sg-gloss "heard"
            :present-participle "hearing"
            :perfect-participle "heard"})]

      ;; ── Present Active Indicative ───────────────────────────────────
      (is (s= (find-form forms {:person 1 :number "singular" :tense "present" :voice "active" :mood "indicative"})
              :wordform #{"audiō"}))
      (is (s= (find-form forms {:person 1 :number "singular" :tense "present" :voice "active" :mood "indicative"})
              :gloss #{"I hear"}))

      (is (s= (find-form forms {:person 3 :number "singular" :tense "present" :voice "active" :mood "indicative"})
              :wordform #{"audit"}))
      (is (s= (find-form forms {:person 3 :number "singular" :tense "present" :voice "active" :mood "indicative"})
              :gloss #{"he/she/it hears"}))

      (is (s= (find-form forms {:person 3 :number "plural" :tense "present" :voice "active" :mood "indicative"})
              :wordform #{"audiunt"}))
      (is (s= (find-form forms {:person 3 :number "plural" :tense "present" :voice "active" :mood "indicative"})
              :gloss #{"they hear"}))

      ;; ── Present Passive Indicative ─────────────────────────────────
      (is (s= (find-form forms {:person 3 :number "singular" :tense "present" :voice "passive" :mood "indicative"})
              :wordform #{"audītur"}))
      (is (s= (find-form forms {:person 3 :number "singular" :tense "present" :voice "passive" :mood "indicative"})
              :gloss #{"he/she/it is heard"}))

      ;; ── Imperfect Active Indicative ────────────────────────────────
      (is (s= (find-form forms {:person 1 :number "singular" :tense "imperfect" :voice "active" :mood "indicative"})
              :wordform #{"audiēbam"}))
      (is (s= (find-form forms {:person 1 :number "singular" :tense "imperfect" :voice "active" :mood "indicative"})
              :gloss #{"I was hearing"}))

      ;; ── Future Active Indicative ───────────────────────────────────
      (is (s= (find-form forms {:person 1 :number "singular" :tense "future" :voice "active" :mood "indicative"})
              :wordform #{"audiam"}))
      (is (s= (find-form forms {:person 1 :number "singular" :tense "future" :voice "active" :mood "indicative"})
              :gloss #{"I will hear"}))

      (is (s= (find-form forms {:person 2 :number "singular" :tense "future" :voice "active" :mood "indicative"})
              :wordform #{"audiēs"}))
      (is (s= (find-form forms {:person 2 :number "singular" :tense "future" :voice "active" :mood "indicative"})
              :gloss #{"you will hear"}))

      ;; ── Perfect Active Indicative ──────────────────────────────────
      (is (s= (find-form forms {:person 1 :number "singular" :tense "perfect" :voice "active" :mood "indicative"})
              :wordform #{"audīvī"}))
      (is (s= (find-form forms {:person 1 :number "singular" :tense "perfect" :voice "active" :mood "indicative"})
              :gloss #{"I heard"}))

      (is (s= (find-form forms {:person 2 :number "plural" :tense "perfect" :voice "active" :mood "indicative"})
              :wordform #{"audīvistis"}))
      (is (s= (find-form forms {:person 2 :number "plural" :tense "perfect" :voice "active" :mood "indicative"})
              :gloss #{"you heard"}))

      ;; ── Present Active Subjunctive ─────────────────────────────────
      (is (s= (find-form forms {:person 1 :number "singular" :tense "present" :voice "active" :mood "subjunctive"})
              :wordform #{"audiam"}))
      (is (s= (find-form forms {:person 1 :number "singular" :tense "present" :voice "active" :mood "subjunctive"})
              :gloss #{"I hear"}))

      (is (s= (find-form forms {:person 3 :number "plural" :tense "present" :voice "active" :mood "subjunctive"})
              :wordform #{"audiant"}))
      (is (s= (find-form forms {:person 3 :number "plural" :tense "present" :voice "active" :mood "subjunctive"})
              :gloss #{"they hear"}))

      ;; ── Present Passive Subjunctive ────────────────────────────────
      (is (s= (find-form forms {:person 3 :number "singular" :tense "present" :voice "passive" :mood "subjunctive"})
              :wordform #{"audiātur"}))
      (is (s= (find-form forms {:person 3 :number "singular" :tense "present" :voice "passive" :mood "subjunctive"})
              :gloss #{"he/she/it is heard"}))

      ;; ── Imperfect Active Subjunctive ───────────────────────────────
      (is (s= (find-form forms {:person 1 :number "plural" :tense "imperfect" :voice "active" :mood "subjunctive"})
              :wordform #{"audīrēmus"}))
      (is (s= (find-form forms {:person 1 :number "plural" :tense "imperfect" :voice "active" :mood "subjunctive"})
              :gloss #{"we heard"}))

      ;; ── Perfect Active Subjunctive ─────────────────────────────────
      (is (s= (find-form forms {:person 3 :number "singular" :tense "perfect" :voice "active" :mood "subjunctive"})
              :wordform #{"audīverit" "audīerit"}))
      (is (s= (find-form forms {:person 3 :number "singular" :tense "perfect" :voice "active" :mood "subjunctive"})
              :gloss #{"he/she/it heard"}))

      ;; ── Infinitives ─────────────────────────────────────────────────
      (is (s= (find-form forms {:tense "present" :voice "active" :mood "infinitive"})
              :wordform #{"audīre"}))
      (is (s= (find-form forms {:tense "present" :voice "active" :mood "infinitive"})
              :gloss #{"to hear"}))

      (is (s= (find-form forms {:tense "present" :voice "passive" :mood "infinitive"})
              :wordform #{"audīrī"}))
      (is (s= (find-form forms {:tense "present" :voice "passive" :mood "infinitive"})
              :gloss #{"to be heard"}))

      (is (s= (find-form forms {:tense "perfect" :voice "active" :mood "infinitive"})
              :wordform #{"audīvisse" "audīsse"}))
      (is (s= (find-form forms {:tense "perfect" :voice "active" :mood "infinitive"})
              :gloss #{"to have heard"})))))

(deftest īrī-deponent
  (testing "potior"
    (let [forms
          (get-verb-forms*
           {:dictionary-form "potior, potīrī, potītum"
            :first-person-present-sg-gloss "obtain"
            :third-person-present-sg-gloss "obtains"
            :first-person-perfect-sg-gloss "obtained"
            :present-participle "obtaining"
            :perfect-participle "obtained"})]
      (is (s= (find-form forms {:person 1 :number "singular" :tense "present" :voice "active" :mood "indicative"}) :wordform #{"potior"}))
      (is (s= (find-form forms {:person 1 :number "singular" :tense "present" :voice "active" :mood "indicative"}) :gloss #{"I obtain"}))
      (is (s= (find-form forms {:person 2 :number "singular" :tense "present" :voice "active" :mood "indicative"}) :wordform #{"potīris"}))
      (is (s= (find-form forms {:person 3 :number "singular" :tense "imperfect" :voice "active" :mood "indicative"}) :wordform #{"potiēbātur"}))
      (is (s= (find-form forms {:person 2 :number "plural" :tense "future" :voice "active" :mood "indicative"}) :wordform #{"potiēminī"}))
      (is (s= (find-form forms {:person 2 :number "plural" :tense "future" :voice "active" :mood "indicative"}) :gloss #{"you will obtain"}))
      (is (s= (find-form forms {:person 1 :number "singular" :tense "present" :voice "active" :mood "subjunctive"}) :wordform #{"potiar"}))
      (is (s= (find-form forms {:person 1 :number "singular" :tense "present" :voice "active" :mood "subjunctive"}) :gloss #{"I obtain"}))
      (is (s= (find-form forms {:person 3 :number "singular" :tense "imperfect" :voice "active" :mood "subjunctive"}) :wordform #{"potīrētur"}))
      (is (s= (find-form forms {:person 3 :number "singular" :tense "imperfect" :voice "active" :mood "subjunctive"}) :gloss #{"he/she/it obtained"}))
      ;; participles
      (is (s= (find-form forms {:number "singular" :tense "present" :case_ "nominative" :gender "masculine"}) :wordform #{"potiēns"}))
      (is (s= (find-form forms {:number "singular" :tense "present" :case_ "nominative" :gender "masculine"}) :gloss #{"obtaining"}))
      (is (s= (find-form forms {:number "singular" :tense "present" :case_ "nominative" :gender "feminine"}) :wordform #{"potiēns"}))
      (is (s= (find-form forms {:number "singular" :tense "present" :case_ "nominative" :gender "feminine"}) :gloss #{"obtaining"}))
      (is (s= (find-form forms {:number "plural" :tense "present" :case_ "nominative" :gender "neuter"}) :wordform #{"potientia"}))
      (is (s= (find-form forms {:number "plural" :tense "present" :case_ "nominative" :gender "neuter"}) :gloss #{"obtaining"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "passive" :case_ "nominative" :gender "masculine"}) :wordform #{"potiendī"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "passive" :case_ "nominative" :gender "masculine"}) :gloss #{"obtaining"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "passive" :case_ "genitive" :gender "feminine"}) :wordform #{"potiendārum"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "passive" :case_ "genitive" :gender "feminine"}) :gloss #{"obtaining"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "passive" :case_ "genitive" :gender "neuter"}) :wordform #{"potiendōrum"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "passive" :case_ "genitive" :gender "neuter"}) :gloss #{"obtaining"}))
      (is (s= (find-form forms {:number "plural" :tense "perfect" :voice "passive" :case_ "dative" :gender "masculine"}) :wordform #{"potītīs"}))
      (is (s= (find-form forms {:number "plural" :tense "perfect" :voice "passive" :case_ "dative" :gender "masculine"}) :gloss #{"was obtaining"}))
      (is (s= (find-form forms {:number "plural" :tense "perfect" :voice "passive" :case_ "dative" :gender "feminine"}) :wordform #{"potītīs"}))
      (is (s= (find-form forms {:number "plural" :tense "perfect" :voice "passive" :case_ "dative" :gender "feminine"}) :gloss #{"was obtaining"}))
      (is (s= (find-form forms {:number "plural" :tense "perfect" :voice "passive" :case_ "dative" :gender "neuter"}) :wordform #{"potītīs"}))
      (is (s= (find-form forms {:number "plural" :tense "perfect" :voice "passive" :case_ "dative" :gender "neuter"}) :gloss #{"was obtaining"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "active" :case_ "accusative" :gender "masculine"}) :wordform #{"potītūrōs"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "active" :case_ "accusative" :gender "masculine"}) :gloss #{"obtaining"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "active" :case_ "accusative" :gender "feminine"}) :wordform #{"potītūrās"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "active" :case_ "accusative" :gender "feminine"}) :gloss #{"obtaining"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "active" :case_ "accusative" :gender "neuter"}) :wordform #{"potītūra"}))
      (is (s= (find-form forms {:number "plural" :tense "future" :voice "active" :case_ "accusative" :gender "neuter"}) :gloss #{"obtaining"}))
      (is (s= (find-form forms {:tense "present" :mood "infinitive"}) :wordform #{"potīrī"}))
      (is (s= (find-form forms {:tense "present" :mood "infinitive"}) :gloss #{"to obtain"}))
      (is (s= (find-form forms {:number "singular" :mood "imperative"}) :wordform #{"potīre"}))
      (is (s= (find-form forms {:number "singular" :mood "imperative"}) :gloss #{"obtain!"}))
      (is (s= (find-form forms {:number "plural" :mood "imperative"}) :wordform #{"potīminī"}))
      (is (s= (find-form forms {:number "plural" :mood "imperative"}) :gloss #{"obtain!"}))
      )))
