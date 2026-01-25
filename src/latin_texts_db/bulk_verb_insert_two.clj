(ns latin-texts-db.bulk-verb-insert-two
  (:require
   [latin-texts-db.db :refer [ll]]))

(defn get-verb-forms-ēre [first-person-present infinitive first-person-perfect supine first-person-present-sg-gloss third-person-present-sg-gloss first-person-perfect-sg-gloss perfect-participle present-participle]
  (let [present-stem (subs first-person-present 0 (- (count first-person-present) 2))
        perfect-stem (subs first-person-perfect 0 (dec (count first-person-perfect)))
        perfect-stem-minus-v (subs perfect-stem 0 (dec (count perfect-stem))) ;; for -ve syncopation
        infinitive-stem (subs infinitive 0 (dec (count infinitive)))
        df (clojure.string/join ", " [first-person-present infinitive first-person-perfect supine])
        participial-stem (subs supine 0 (- (count supine) 2))]
    [
     ;; present active indicative
     {:wordform first-person-present :gloss (str "I " first-person-present-sg-gloss) :part_of_speech "verb" :person 1 :number "singular" :tense "present" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str present-stem "ēs") :gloss (str "you " first-person-present-sg-gloss) :part_of_speech "verb" :person 2 :number "singular" :tense "present" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str present-stem "et") :gloss (str "he/she/it " third-person-present-sg-gloss) :part_of_speech "verb" :person 3 :number "singular" :tense "present" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str present-stem "ēmus") :gloss (str "we " first-person-present-sg-gloss) :part_of_speech "verb" :person 1 :number "plural" :tense "present" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str present-stem "ētis") :gloss (str "you " first-person-present-sg-gloss) :part_of_speech "verb" :person 2 :number "plural" :tense "present" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str present-stem "ent") :gloss (str "they " first-person-present-sg-gloss) :part_of_speech "verb" :person 3 :number "plural" :tense "present" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     ;; perfect active indicative
     {:wordform first-person-perfect :gloss (str "I " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 1 :number "singular" :tense "perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str perfect-stem "isti") :gloss (str "you " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 2 :number "singular" :tense "perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str perfect-stem "it") :gloss (str "he/she/it " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 3 :number "singular" :tense "perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str perfect-stem "imus") :gloss (str "we " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 1 :number "plural" :tense "perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str perfect-stem "istis") :gloss (str "you " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 2 :number "plural" :tense "perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str perfect-stem "ērunt") :gloss (str "they " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 3 :number "plural" :tense "perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str perfect-stem "ēre") :gloss (str "they " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 3 :number "plural" :tense "perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     ;; imperfect active indicative
     {:wordform (str present-stem "ēbam") :gloss (str "I was " present-participle) :part_of_speech "verb" :person 1 :number "singular" :tense "imperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str present-stem "ēbās") :gloss (str "you were " present-participle) :part_of_speech "verb" :person 2 :number "singular" :tense "imperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str present-stem "ēbat") :gloss (str "he/she/it was " present-participle) :part_of_speech "verb" :person 3 :number "singular" :tense "imperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str present-stem "ēbāmus") :gloss (str "we were " present-participle) :part_of_speech "verb" :person 1 :number "plural" :tense "imperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str present-stem "ēbātis") :gloss (str "you were " present-participle) :part_of_speech "verb" :person 2 :number "plural" :tense "imperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str present-stem "ēbant") :gloss (str "they were " present-participle) :part_of_speech "verb" :person 3 :number "plural" :tense "imperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     ;; future active indicative
     {:wordform (str present-stem "ēbō") :gloss (str "I will " first-person-present-sg-gloss) :part_of_speech "verb" :person 1 :number "singular" :tense "future" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str present-stem "ēbis") :gloss (str "you will " first-person-present-sg-gloss) :part_of_speech "verb" :person 2 :number "singular" :tense "future" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str present-stem "ēbit") :gloss (str "he/she/it will " first-person-present-sg-gloss) :part_of_speech "verb" :person 3 :number "singular" :tense "future" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str present-stem "ēbimus") :gloss (str "we will " first-person-present-sg-gloss) :part_of_speech "verb" :person 1 :number "plural" :tense "future" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str present-stem "ēbitis") :gloss (str "you will " first-person-present-sg-gloss) :part_of_speech "verb" :person 2 :number "plural" :tense "future" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str present-stem "ēbunt") :gloss (str "they will " first-person-present-sg-gloss) :part_of_speech "verb" :person 3 :number "plural" :tense "future" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     ;; pluperfect active indicative
     {:wordform (str perfect-stem "eram") :gloss (str "I had " perfect-participle) :part_of_speech "verb" :person 1 :number "singular" :tense "pluperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str perfect-stem "erās") :gloss (str "you had " perfect-participle) :part_of_speech "verb" :person 2 :number "singular" :tense "pluperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str perfect-stem "erat") :gloss (str "he/she/it had " perfect-participle) :part_of_speech "verb" :person 3 :number "singular" :tense "pluperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str perfect-stem "erāmus") :gloss (str "we had " perfect-participle) :part_of_speech "verb" :person 1 :number "plural" :tense "pluperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str perfect-stem "erātis") :gloss (str "you had " perfect-participle) :part_of_speech "verb" :person 2 :number "plural" :tense "pluperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str perfect-stem "erant") :gloss (str "they had " perfect-participle) :part_of_speech "verb" :person 3 :number "plural" :tense "pluperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     ;; future-perfect active indicative
     {:wordform (str perfect-stem "erō") :gloss (str "I will have " perfect-participle) :part_of_speech "verb" :person 1 :number "singular" :tense "future-perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str perfect-stem "eris") :gloss (str "you will have " perfect-participle) :part_of_speech "verb" :person 2 :number "singular" :tense "future-perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str perfect-stem "erit") :gloss (str "he/she/it will have " perfect-participle) :part_of_speech "verb" :person 3 :number "singular" :tense "future-perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str perfect-stem "erimus") :gloss (str "we will have " perfect-participle) :part_of_speech "verb" :person 1 :number "plural" :tense "future-perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str perfect-stem "eritis") :gloss (str "you will have " perfect-participle) :part_of_speech "verb" :person 2 :number "plural" :tense "future-perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str perfect-stem "erint") :gloss (str "they will have " perfect-participle) :part_of_speech "verb" :person 3 :number "plural" :tense "future-perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     ;; present active subjunctive
     {:wordform (str present-stem "eam") :gloss (str "I " first-person-present-sg-gloss) :part_of_speech "verb" :person 1 :number "singular" :tense "present" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
     {:wordform (str present-stem "eās") :gloss (str "you " first-person-present-sg-gloss) :part_of_speech "verb" :person 2 :number "singular" :tense "present" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
     {:wordform (str present-stem "eat") :gloss (str "he/she/it " third-person-present-sg-gloss) :part_of_speech "verb" :person 3 :number "singular" :tense "present" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
     {:wordform (str present-stem "eāmus") :gloss (str "we " first-person-present-sg-gloss) :part_of_speech "verb" :person 1 :number "plural" :tense "present" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
     {:wordform (str present-stem "eātis") :gloss (str "you " first-person-present-sg-gloss) :part_of_speech "verb" :person 2 :number "plural" :tense "present" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
     {:wordform (str present-stem "eant") :gloss (str "they " first-person-present-sg-gloss) :part_of_speech "verb" :person 3 :number "plural" :tense "present" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
     ;; imperfect active subjunctive 
     {:wordform (str infinitive-stem "em") :gloss (str "I " first-person-present-sg-gloss) :part_of_speech "verb" :person 1 :number "singular" :tense "imperfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
     {:wordform (str infinitive-stem "ēs") :gloss (str "you " first-person-present-sg-gloss) :part_of_speech "verb" :person 2 :number "singular" :tense "imperfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
     {:wordform (str infinitive-stem "et") :gloss (str "he/she/it " third-person-present-sg-gloss) :part_of_speech "verb" :person 3 :number "singular" :tense "imperfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
     {:wordform (str infinitive-stem "ēmus") :gloss (str "we " first-person-present-sg-gloss) :part_of_speech "verb" :person 1 :number "plural" :tense "imperfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
     {:wordform (str infinitive-stem "ētis") :gloss (str "you " first-person-present-sg-gloss) :part_of_speech "verb" :person 2 :number "plural" :tense "imperfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
     {:wordform (str infinitive-stem "ent") :gloss (str "they " first-person-present-sg-gloss) :part_of_speech "verb" :person 3 :number "plural" :tense "imperfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
     ;; perfect active subjunctive
     {:wordform (str perfect-stem "erim") :gloss (str "I " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 1 :number "singular" :tense "perfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
     {:wordform (str perfect-stem "eris") :gloss (str "you " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 2 :number "singular" :tense "perfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
     {:wordform (str perfect-stem "erit") :gloss (str "he/she/it " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 3 :number "singular" :tense "perfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
     {:wordform (str perfect-stem "erimus") :gloss (str "we " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 1 :number "plural" :tense "perfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
     {:wordform (str perfect-stem "eritis") :gloss (str "you " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 2 :number "plural" :tense "perfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
     {:wordform (str perfect-stem "erint") :gloss (str "they " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 3 :number "plural" :tense "perfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
     ;; pluperfect active subjunctive
     {:wordform (str perfect-stem "issem") :gloss (str "I had " perfect-participle) :part_of_speech "verb" :person 1 :number "singular" :tense "pluperfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
     {:wordform (str perfect-stem "issēs") :gloss (str "you had " perfect-participle) :part_of_speech "verb" :person 2 :number "singular" :tense "pluperfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
     {:wordform (str perfect-stem "isset") :gloss (str "he/she/it had " perfect-participle) :part_of_speech "verb" :person 3 :number "singular" :tense "pluperfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
     {:wordform (str perfect-stem "issēmus") :gloss (str "we had " perfect-participle) :part_of_speech "verb" :person 1 :number "plural" :tense "pluperfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
     {:wordform (str perfect-stem "issētis") :gloss (str "you had " perfect-participle) :part_of_speech "verb" :person 2 :number "plural" :tense "pluperfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
     {:wordform (str perfect-stem "issent") :gloss (str "they had " perfect-participle) :part_of_speech "verb" :person 3 :number "plural" :tense "pluperfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}

     ;; present passive indicative
     {:wordform (str present-stem "eor") :gloss (str "I am " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 1 :number "singular" :tense "present" :voice "passive" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str present-stem "ēris") :gloss (str "you are " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 2 :number "singular" :tense "present" :voice "passive" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str present-stem "ētur") :gloss (str "he/she/it is " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 3 :number "singular" :tense "present" :voice "passive" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str present-stem "ēmur") :gloss (str "we are " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 1 :number "plural" :tense "present" :voice "passive" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str present-stem "ēminī") :gloss (str "you are " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 2 :number "plural" :tense "present" :voice "passive" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str present-stem "entur") :gloss (str "they are " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 3 :number "plural" :tense "present" :voice "passive" :mood "indicative" :lexeme_id (ll df)}
     ;; skipping perfect passive indicative since those require two words like 'pulsātus sum'
     ;; future passive indicative
     {:wordform (str present-stem "ēbor") :gloss (str "I will be " perfect-participle) :part_of_speech "verb" :person 1 :number "singular" :tense "future" :voice "passive" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str present-stem "ēberis") :gloss (str "you will be " perfect-participle) :part_of_speech "verb" :person 2 :number "singular" :tense "future" :voice "passive" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str present-stem "ēbere") :gloss (str "you will be " perfect-participle) :part_of_speech "verb" :person 2 :number "singular" :tense "future" :voice "passive" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str present-stem "ēbitur") :gloss (str "he/she/it will be " perfect-participle) :part_of_speech "verb" :person 3 :number "singular" :tense "future" :voice "passive" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str present-stem "ēbimur") :gloss (str "we will be " perfect-participle) :part_of_speech "verb" :person 1 :number "plural" :tense "future" :voice "passive" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str present-stem "ēbiminī") :gloss (str "you will be " perfect-participle) :part_of_speech "verb" :person 2 :number "plural" :tense "future" :voice "passive" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str present-stem "ēbuntur") :gloss (str "they will be " perfect-participle) :part_of_speech "verb" :person 3 :number "plural" :tense "future" :voice "passive" :mood "indicative" :lexeme_id (ll df)}
     ;; imperfect passive indicative
     {:wordform (str present-stem "ēbar") :gloss (str "I was being " perfect-participle) :part_of_speech "verb" :person 1 :number "singular" :tense "imperfect" :voice "passive" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str present-stem "ēbāris") :gloss (str "you were being " perfect-participle) :part_of_speech "verb" :person 2 :number "singular" :tense "imperfect" :voice "passive" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str present-stem "ēbāre") :gloss (str "you were being " perfect-participle) :part_of_speech "verb" :person 2 :number "singular" :tense "imperfect" :voice "passive" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str present-stem "ēbātur") :gloss (str "he/she/it was being " perfect-participle) :part_of_speech "verb" :person 3 :number "singular" :tense "imperfect" :voice "passive" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str present-stem "ēbāmur") :gloss (str "we were being " perfect-participle) :part_of_speech "verb" :person 1 :number "plural" :tense "imperfect" :voice "passive" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str present-stem "ēbāminī") :gloss (str "you were being " perfect-participle) :part_of_speech "verb" :person 2 :number "plural" :tense "imperfect" :voice "passive" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str present-stem "ēbantur") :gloss (str "they were being " perfect-participle) :part_of_speech "verb" :person 3 :number "plural" :tense "imperfect" :voice "passive" :mood "indicative" :lexeme_id (ll df)}
     ;; skipping pluperfect passive indicative and future-perfect passive indicative because they require helper words
     ;; present passive subjunctive
     {:wordform (str present-stem "ear") :gloss (str "I am " perfect-participle) :part_of_speech "verb" :person 1 :number "singular" :tense "present" :voice "passive" :mood "subjunctive" :lexeme_id (ll df)}
     {:wordform (str present-stem "eāris") :gloss (str "you are " perfect-participle) :part_of_speech "verb" :person 2 :number "singular" :tense "present" :voice "passive" :mood "subjunctive" :lexeme_id (ll df)}
     {:wordform (str present-stem "eāre") :gloss (str "you are " perfect-participle) :part_of_speech "verb" :person 2 :number "singular" :tense "present" :voice "passive" :mood "subjunctive" :lexeme_id (ll df)}
     {:wordform (str present-stem "eātur") :gloss (str "he/she/it is " perfect-participle) :part_of_speech "verb" :person 3 :number "singular" :tense "present" :voice "passive" :mood "subjunctive" :lexeme_id (ll df)}
     {:wordform (str present-stem "eāmur") :gloss (str "we are " perfect-participle) :part_of_speech "verb" :person 1 :number "plural" :tense "present" :voice "passive" :mood "subjunctive" :lexeme_id (ll df)}
     {:wordform (str present-stem "eāminī") :gloss (str "you are " perfect-participle) :part_of_speech "verb" :person 2 :number "plural" :tense "present" :voice "passive" :mood "subjunctive" :lexeme_id (ll df)}
     {:wordform (str present-stem "eantur") :gloss (str "they are " perfect-participle) :part_of_speech "verb" :person 3 :number "plural" :tense "present" :voice "passive" :mood "subjunctive" :lexeme_id (ll df)}
     ;; imperfect passive subjunctive 
     {:wordform (str infinitive-stem "er") :gloss (str "I was being " perfect-participle) :part_of_speech "verb" :person 1 :number "singular" :tense "imperfect" :voice "passive" :mood "subjunctive" :lexeme_id (ll df)}
     {:wordform (str infinitive-stem "ēris") :gloss (str "you were being " perfect-participle) :part_of_speech "verb" :person 2 :number "singular" :tense "imperfect" :voice "passive" :mood "subjunctive" :lexeme_id (ll df)}
     {:wordform (str infinitive-stem "ēre") :gloss (str "you were being " perfect-participle) :part_of_speech "verb" :person 2 :number "singular" :tense "imperfect" :voice "passive" :mood "subjunctive" :lexeme_id (ll df)}
     {:wordform (str infinitive-stem "ētur") :gloss (str "he/she/it was being " perfect-participle) :part_of_speech "verb" :person 3 :number "singular" :tense "imperfect" :voice "passive" :mood "subjunctive" :lexeme_id (ll df)}
     {:wordform (str infinitive-stem "ēmur") :gloss (str "we were being " perfect-participle) :part_of_speech "verb" :person 1 :number "plural" :tense "imperfect" :voice "passive" :mood "subjunctive" :lexeme_id (ll df)}
     {:wordform (str infinitive-stem "ēminī") :gloss (str "you were being " perfect-participle) :part_of_speech "verb" :person 2 :number "plural" :tense "imperfect" :voice "passive" :mood "subjunctive" :lexeme_id (ll df)}
     {:wordform (str infinitive-stem "entur") :gloss (str "they were being " perfect-participle) :part_of_speech "verb" :person 3 :number "plural" :tense "imperfect" :voice "passive" :mood "subjunctive" :lexeme_id (ll df)}
     ;; infinitives
     {:wordform  infinitive :gloss (str "to " first-person-present-sg-gloss) :part_of_speech "verb" :tense "present" :voice "active" :mood "infinitive" :lexeme_id (ll df)}
     {:wordform  (str perfect-stem "isse") :gloss (str "to have " perfect-participle) :part_of_speech "verb" :tense "perfect" :voice "active" :mood "infinitive" :lexeme_id (ll df)}
     ;; skipping perfect passive infinitive
     ;; skipping future active infinitive since it requires a helper word
     {:wordform  (str infinitive-stem "ī") :gloss (str "to be " perfect-participle) :part_of_speech "verb" :tense "present" :voice "passive" :mood "infinitive" :lexeme_id (ll df)}
     ;; imperatives (skipping future imperative for now since it's rare)
     {:wordform  (str present-stem "ē") :gloss (str first-person-present-sg-gloss "!") :part_of_speech "verb" :tense "present" :voice "active" :number "singular" :mood "imperative" :lexeme_id (ll df)}
     {:wordform  (str present-stem "ēte") :gloss (str first-person-present-sg-gloss "!") :part_of_speech "verb" :tense "present" :voice "active" :number "plural" :mood "imperative" :lexeme_id (ll df)}
     {:wordform  (str present-stem "ēre") :gloss (str "be "perfect-participle "!") :part_of_speech "verb" :tense "present" :voice "passive" :number "singular" :mood "imperative" :lexeme_id (ll df)}
     {:wordform  (str present-stem "ēminī") :gloss (str "be "perfect-participle "!") :part_of_speech "verb" :tense "present" :voice "passive" :number "plural" :mood "imperative" :lexeme_id (ll df)}
     ;; present active participles MASCULINE
     {:wordform (str present-stem "ēns") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "masculine" :number "singular" :case_ "nominative" :lexeme_id (ll df)}
     {:wordform (str present-stem "entis") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "masculine" :number "singular" :case_ "genitive" :lexeme_id (ll df)}
     {:wordform (str present-stem "entī") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "masculine" :number "singular" :case_ "dative" :lexeme_id (ll df)}
     {:wordform (str present-stem "entem") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "masculine" :number "singular" :case_ "accusative" :lexeme_id (ll df)}
     {:wordform (str present-stem "ente") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "masculine" :number "singular" :case_ "ablative" :lexeme_id (ll df)}
     {:wordform (str present-stem "entēs") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "masculine" :number "plural" :case_ "nominative" :lexeme_id (ll df)}
     {:wordform (str present-stem "entium") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "masculine" :number "plural" :case_ "genitive" :lexeme_id (ll df)}
     {:wordform (str present-stem "entibus") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "masculine" :number "plural" :case_ "dative" :lexeme_id (ll df)}
     {:wordform (str present-stem "entēs") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "masculine" :number "plural" :case_ "accusative" :lexeme_id (ll df)}
     {:wordform (str present-stem "entibus") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "masculine" :number "plural" :case_ "ablative" :lexeme_id (ll df)}
     ;; future passive participles / gerundives MASCULINE
     {:wordform (str present-stem "endus") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "masculine" :number "singular" :case_ "nominative" :lexeme_id (ll df)}
     {:wordform (str present-stem "endī") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "masculine" :number "singular" :case_ "genitive" :lexeme_id (ll df)}
     {:wordform (str present-stem "endō") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "masculine" :number "singular" :case_ "dative" :lexeme_id (ll df)}
     {:wordform (str present-stem "endum") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "masculine" :number "singular" :case_ "accusative" :lexeme_id (ll df)}
     {:wordform (str present-stem "endō") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "masculine" :number "singular" :case_ "ablative" :lexeme_id (ll df)}
     {:wordform (str present-stem "endī") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "masculine" :number "plural" :case_ "nominative" :lexeme_id (ll df)}
     {:wordform (str present-stem "endōrum") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "masculine" :number "plural" :case_ "genitive" :lexeme_id (ll df)}
     {:wordform (str present-stem "endīs") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "masculine" :number "plural" :case_ "dative" :lexeme_id (ll df)}
     {:wordform (str present-stem "endōs") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "masculine" :number "plural" :case_ "accusative" :lexeme_id (ll df)}
     {:wordform (str present-stem "endīs") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "masculine" :number "plural" :case_ "ablative" :lexeme_id (ll df)}
     ;; perfect passive participles MASCULINE
     {:wordform (str participial-stem "us") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "masculine" :number "singular" :case_ "nominative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ī") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "masculine" :number "singular" :case_ "genitive" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ō") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "masculine" :number "singular" :case_ "dative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "um") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "masculine" :number "singular" :case_ "accusative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ō") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "masculine" :number "singular" :case_ "ablative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ī") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "masculine" :number "plural" :case_ "nominative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ōrum") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "masculine" :number "plural" :case_ "genitive" :lexeme_id (ll df)}
     {:wordform (str participial-stem "īs") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "masculine" :number "plural" :case_ "dative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ōs") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "masculine" :number "plural" :case_ "accusative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "īs") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "masculine" :number "plural" :case_ "ablative" :lexeme_id (ll df)}
     ;; future active participles MASCULINE
     {:wordform (str participial-stem "ūrus") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "masculine" :number "singular" :case_ "nominative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ūrī") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "masculine" :number "singular" :case_ "genitive" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ūrō") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "masculine" :number "singular" :case_ "dative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ūrum") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "masculine" :number "singular" :case_ "accusative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ūrō") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "masculine" :number "singular" :case_ "ablative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ūrī") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "masculine" :number "plural" :case_ "nominative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ūrōrum") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "masculine" :number "plural" :case_ "genitive" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ūrīs") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "masculine" :number "plural" :case_ "dative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ūrōs") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "masculine" :number "plural" :case_ "accusative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ūrīs") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "masculine" :number "plural" :case_ "ablative" :lexeme_id (ll df)}
     ;; present active participles FEMININE
     {:wordform (str present-stem "ēns") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "feminine" :number "singular" :case_ "nominative" :lexeme_id (ll df)}
     {:wordform (str present-stem "entis") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "feminine" :number "singular" :case_ "genitive" :lexeme_id (ll df)}
     {:wordform (str present-stem "entī") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "feminine" :number "singular" :case_ "dative" :lexeme_id (ll df)}
     {:wordform (str present-stem "entem") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "feminine" :number "singular" :case_ "accusative" :lexeme_id (ll df)}
     {:wordform (str present-stem "ente") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "feminine" :number "singular" :case_ "ablative" :lexeme_id (ll df)}
     {:wordform (str present-stem "entēs") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "feminine" :number "plural" :case_ "nominative" :lexeme_id (ll df)}
     {:wordform (str present-stem "entium") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "feminine" :number "plural" :case_ "genitive" :lexeme_id (ll df)}
     {:wordform (str present-stem "entibus") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "feminine" :number "plural" :case_ "dative" :lexeme_id (ll df)}
     {:wordform (str present-stem "entēs") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "feminine" :number "plural" :case_ "accusative" :lexeme_id (ll df)}
     {:wordform (str present-stem "entibus") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "feminine" :number "plural" :case_ "ablative" :lexeme_id (ll df)}
     ;; future passive participles / gerundives FEMININE
     {:wordform (str present-stem "enda") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "feminine" :number "singular" :case_ "nominative" :lexeme_id (ll df)}
     {:wordform (str present-stem "endae") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "feminine" :number "singular" :case_ "genitive" :lexeme_id (ll df)}
     {:wordform (str present-stem "endae") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "feminine" :number "singular" :case_ "dative" :lexeme_id (ll df)}
     {:wordform (str present-stem "endam") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "feminine" :number "singular" :case_ "accusative" :lexeme_id (ll df)}
     {:wordform (str present-stem "endā") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "feminine" :number "singular" :case_ "ablative" :lexeme_id (ll df)}
     {:wordform (str present-stem "endae") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "feminine" :number "plural" :case_ "nominative" :lexeme_id (ll df)}
     {:wordform (str present-stem "endārum") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "feminine" :number "plural" :case_ "genitive" :lexeme_id (ll df)}
     {:wordform (str present-stem "endīs") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "feminine" :number "plural" :case_ "dative" :lexeme_id (ll df)}
     {:wordform (str present-stem "endās") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "feminine" :number "plural" :case_ "accusative" :lexeme_id (ll df)}
     {:wordform (str present-stem "endīs") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "feminine" :number "plural" :case_ "ablative" :lexeme_id (ll df)}
     ;; perfect passive participles FEMININE
     {:wordform (str participial-stem "a") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "feminine" :number "singular" :case_ "nominative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ae") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "feminine" :number "singular" :case_ "genitive" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ae") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "feminine" :number "singular" :case_ "dative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "am") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "feminine" :number "singular" :case_ "accusative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ā") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "feminine" :number "singular" :case_ "ablative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ae") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "feminine" :number "plural" :case_ "nominative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ārum") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "feminine" :number "plural" :case_ "genitive" :lexeme_id (ll df)}
     {:wordform (str participial-stem "īs") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "feminine" :number "plural" :case_ "dative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ās") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "feminine" :number "plural" :case_ "accusative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "īs") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "feminine" :number "plural" :case_ "ablative" :lexeme_id (ll df)}
     ;; future active participles FEMININE
     {:wordform (str participial-stem "ūra") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "feminine" :number "singular" :case_ "nominative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ūrae") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "feminine" :number "singular" :case_ "genitive" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ūrae") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "feminine" :number "singular" :case_ "dative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ūram") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "feminine" :number "singular" :case_ "accusative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ūrā") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "feminine" :number "singular" :case_ "ablative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ūrae") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "feminine" :number "plural" :case_ "nominative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ūrārum") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "feminine" :number "plural" :case_ "genitive" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ūrīs") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "feminine" :number "plural" :case_ "dative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ūrās") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "feminine" :number "plural" :case_ "accusative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ūrīs") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "feminine" :number "plural" :case_ "ablative" :lexeme_id (ll df)}

     ;; present active participles NEUTER
     {:wordform (str present-stem "ēns") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "neuter" :number "singular" :case_ "nominative" :lexeme_id (ll df)}
     {:wordform (str present-stem "entis") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "neuter" :number "singular" :case_ "genitive" :lexeme_id (ll df)}
     {:wordform (str present-stem "entī") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "neuter" :number "singular" :case_ "dative" :lexeme_id (ll df)}
     {:wordform (str present-stem "ēns") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "neuter" :number "singular" :case_ "accusative" :lexeme_id (ll df)}
     {:wordform (str present-stem "ente") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "neuter" :number "singular" :case_ "ablative" :lexeme_id (ll df)}
     {:wordform (str present-stem "entia") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "neuter" :number "plural" :case_ "nominative" :lexeme_id (ll df)}
     {:wordform (str present-stem "entium") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "neuter" :number "plural" :case_ "genitive" :lexeme_id (ll df)}
     {:wordform (str present-stem "entibus") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "neuter" :number "plural" :case_ "dative" :lexeme_id (ll df)}
     {:wordform (str present-stem "entia") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "neuter" :number "plural" :case_ "accusative" :lexeme_id (ll df)}
     {:wordform (str present-stem "entibus") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "neuter" :number "plural" :case_ "ablative" :lexeme_id (ll df)}
     ;; future passive participles / gerundives NEUTER
     {:wordform (str present-stem "endum") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "neuter" :number "singular" :case_ "nominative" :lexeme_id (ll df)}
     {:wordform (str present-stem "endī") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "neuter" :number "singular" :case_ "genitive" :lexeme_id (ll df)}
     {:wordform (str present-stem "endō") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "neuter" :number "singular" :case_ "dative" :lexeme_id (ll df)}
     {:wordform (str present-stem "endum") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "neuter" :number "singular" :case_ "accusative" :lexeme_id (ll df)}
     {:wordform (str present-stem "endō") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "neuter" :number "singular" :case_ "ablative" :lexeme_id (ll df)}
     {:wordform (str present-stem "enda") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "neuter" :number "plural" :case_ "nominative" :lexeme_id (ll df)}
     {:wordform (str present-stem "endōrum") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "neuter" :number "plural" :case_ "genitive" :lexeme_id (ll df)}
     {:wordform (str present-stem "endīs") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "neuter" :number "plural" :case_ "dative" :lexeme_id (ll df)}
     {:wordform (str present-stem "enda") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "neuter" :number "plural" :case_ "accusative" :lexeme_id (ll df)}
     {:wordform (str present-stem "endīs") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "neuter" :number "plural" :case_ "ablative" :lexeme_id (ll df)}
     ;; perfect passive participles NEUTER
     {:wordform (str participial-stem "um") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "neuter" :number "singular" :case_ "nominative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ī") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "neuter" :number "singular" :case_ "genitive" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ō") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "neuter" :number "singular" :case_ "dative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "um") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "neuter" :number "singular" :case_ "accusative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ō") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "neuter" :number "singular" :case_ "ablative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "a") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "neuter" :number "plural" :case_ "nominative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ōrum") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "neuter" :number "plural" :case_ "genitive" :lexeme_id (ll df)}
     {:wordform (str participial-stem "īs") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "neuter" :number "plural" :case_ "dative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "a") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "neuter" :number "plural" :case_ "accusative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "īs") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "neuter" :number "plural" :case_ "ablative" :lexeme_id (ll df)}
     ;; future active participles NEUTER
     {:wordform (str participial-stem "ūrum") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "neuter" :number "singular" :case_ "nominative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ūrī") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "neuter" :number "singular" :case_ "genitive" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ūrō") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "neuter" :number "singular" :case_ "dative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ūrum") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "neuter" :number "singular" :case_ "accusative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ūrō") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "neuter" :number "singular" :case_ "ablative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ūra") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "neuter" :number "plural" :case_ "nominative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ūrōrum") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "neuter" :number "plural" :case_ "genitive" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ūrīs") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "neuter" :number "plural" :case_ "dative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ūra") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "neuter" :number "plural" :case_ "accusative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ūrīs") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "neuter" :number "plural" :case_ "ablative" :lexeme_id (ll df)}
     ]))
