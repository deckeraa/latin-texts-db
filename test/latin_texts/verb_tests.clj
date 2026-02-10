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
      ;; (is (s= (find-form forms {:person 1 :number "singular" :tense "present" :voice "active" :mood "subjunctive"}) :wordform #{"verear"}))
      ;; (is (s= (find-form forms {:person 1 :number "singular" :tense "present" :voice "active" :mood "subjunctive"}) :gloss #{"I fear"}))
      ;; (is (s= (find-form forms {:person 3 :number "singular" :tense "imperfect" :voice "active" :mood "subjunctive"}) :wordform #{"verērētur"}))
      ;; (is (s= (find-form forms {:person 3 :number "singular" :tense "imperfect" :voice "active" :mood "subjunctive"}) :gloss #{"he/she/it feared"}))
      ;; ;; participles
      ;; (is (s= (find-form forms {:number "singular" :tense "present" :case_ "nominative" :gender "masculine"}) :wordform #{"verēns"}))
      ;; (is (s= (find-form forms {:number "singular" :tense "present" :case_ "nominative" :gender "masculine"}) :gloss #{"fearing"}))
      ;; (is (s= (find-form forms {:number "singular" :tense "present" :case_ "nominative" :gender "feminine"}) :wordform #{"verēns"}))
      ;; (is (s= (find-form forms {:number "singular" :tense "present" :case_ "nominative" :gender "feminine"}) :gloss #{"fearing"}))
      ;; (is (s= (find-form forms {:number "plural" :tense "present" :case_ "nominative" :gender "neuter"}) :wordform #{"verentia"}))
      ;; (is (s= (find-form forms {:number "plural" :tense "present" :case_ "nominative" :gender "neuter"}) :gloss #{"fearing"}))
      ;; (is (s= (find-form forms {:number "plural" :tense "future" :voice "passive" :case_ "nominative" :gender "masculine"}) :wordform #{"verendī"}))
      ;; (is (s= (find-form forms {:number "plural" :tense "future" :voice "passive" :case_ "nominative" :gender "masculine"}) :gloss #{"fearing"}))
      ;; (is (s= (find-form forms {:number "plural" :tense "future" :voice "passive" :case_ "genitive" :gender "feminine"}) :wordform #{"verendārum"}))
      ;; (is (s= (find-form forms {:number "plural" :tense "future" :voice "passive" :case_ "genitive" :gender "feminine"}) :gloss #{"fearing"}))
      ;; (is (s= (find-form forms {:number "plural" :tense "future" :voice "passive" :case_ "genitive" :gender "neuter"}) :wordform #{"verendōrum"}))
      ;; (is (s= (find-form forms {:number "plural" :tense "future" :voice "passive" :case_ "genitive" :gender "neuter"}) :gloss #{"fearing"}))
      ;; (is (s= (find-form forms {:number "plural" :tense "perfect" :voice "passive" :case_ "dative" :gender "masculine"}) :wordform #{"veritīs"}))
      ;; (is (s= (find-form forms {:number "plural" :tense "perfect" :voice "passive" :case_ "dative" :gender "masculine"}) :gloss #{"was fearing"}))
      ;; (is (s= (find-form forms {:number "plural" :tense "perfect" :voice "passive" :case_ "dative" :gender "feminine"}) :wordform #{"veritīs"}))
      ;; (is (s= (find-form forms {:number "plural" :tense "perfect" :voice "passive" :case_ "dative" :gender "feminine"}) :gloss #{"was fearing"}))
      ;; (is (s= (find-form forms {:number "plural" :tense "perfect" :voice "passive" :case_ "dative" :gender "neuter"}) :wordform #{"veritīs"}))
      ;; (is (s= (find-form forms {:number "plural" :tense "perfect" :voice "passive" :case_ "dative" :gender "neuter"}) :gloss #{"was fearing"}))
      ;; (is (s= (find-form forms {:number "plural" :tense "future" :voice "active" :case_ "accusative" :gender "masculine"}) :wordform #{"veritūrōs"}))
      ;; (is (s= (find-form forms {:number "plural" :tense "future" :voice "active" :case_ "accusative" :gender "masculine"}) :gloss #{"fearing"}))
      ;; (is (s= (find-form forms {:number "plural" :tense "future" :voice "active" :case_ "accusative" :gender "feminine"}) :wordform #{"veritūrās"}))
      ;; (is (s= (find-form forms {:number "plural" :tense "future" :voice "active" :case_ "accusative" :gender "feminine"}) :gloss #{"fearing"}))
      ;; (is (s= (find-form forms {:number "plural" :tense "future" :voice "active" :case_ "accusative" :gender "neuter"}) :wordform #{"veritūra"}))
      ;; (is (s= (find-form forms {:number "plural" :tense "future" :voice "active" :case_ "accusative" :gender "neuter"}) :gloss #{"fearing"}))
      ;; (is (s= (find-form forms {:tense "present" :mood "infinitive"}) :wordform #{"verērī"}))
      ;; (is (s= (find-form forms {:tense "present" :mood "infinitive"}) :gloss #{"to fear"}))
      ;; (is (s= (find-form forms {:number "singular" :mood "imperative"}) :wordform #{"verēre"}))
      ;; (is (s= (find-form forms {:number "singular" :mood "imperative"}) :gloss #{"fear!"}))
      ;; (is (s= (find-form forms {:number "plural" :mood "imperative"}) :wordform #{"verēminī"}))
      ;; (is (s= (find-form forms {:number "plural" :mood "imperative"}) :gloss #{"fear!"}))
      )))
