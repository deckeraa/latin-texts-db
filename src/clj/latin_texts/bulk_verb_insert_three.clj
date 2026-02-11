(ns latin-texts.bulk-verb-insert-three
  (:require
   [latin-texts.db :refer [ll]]))

(defn present-active-indicative [{:keys [first-person-present first-person-present-sg-gloss present-stem df third-person-present-sg-gloss]}]
  [{:wordform first-person-present :gloss (str "I " first-person-present-sg-gloss) :part_of_speech "verb" :person 1 :number "singular" :tense "present" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "is") :gloss (str "you " first-person-present-sg-gloss) :part_of_speech "verb" :person 2 :number "singular" :tense "present" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "it") :gloss (str "he/she/it " third-person-present-sg-gloss) :part_of_speech "verb" :person 3 :number "singular" :tense "present" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "imus") :gloss (str "we " first-person-present-sg-gloss) :part_of_speech "verb" :person 1 :number "plural" :tense "present" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "itis") :gloss (str "you " first-person-present-sg-gloss) :part_of_speech "verb" :person 2 :number "plural" :tense "present" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "unt") :gloss (str "they " first-person-present-sg-gloss) :part_of_speech "verb" :person 3 :number "plural" :tense "present" :voice "active" :mood "indicative" :lexeme_id (ll df)}])

(defn perfect-active-indicative [{:keys [first-person-perfect first-person-perfect-sg-gloss perfect-stem df]}]
  [{:wordform first-person-perfect :gloss (str "I " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 1 :number "singular" :tense "perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str perfect-stem "istī") :gloss (str "you " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 2 :number "singular" :tense "perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str perfect-stem "it") :gloss (str "he/she/it " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 3 :number "singular" :tense "perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str perfect-stem "imus") :gloss (str "we " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 1 :number "plural" :tense "perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str perfect-stem "istis") :gloss (str "you " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 2 :number "plural" :tense "perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str perfect-stem "ērunt") :gloss (str "they " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 3 :number "plural" :tense "perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str perfect-stem "ēre") :gloss (str "they " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 3 :number "plural" :tense "perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}])

(defn imperfect-active-indicative [{:keys [present-stem present-participle df]}]
  [{:wordform (str present-stem "ēbam") :gloss (str "I was " present-participle) :part_of_speech "verb" :person 1 :number "singular" :tense "imperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ēbās") :gloss (str "you were " present-participle) :part_of_speech "verb" :person 2 :number "singular" :tense "imperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ēbat") :gloss (str "he/she/it was " present-participle) :part_of_speech "verb" :person 3 :number "singular" :tense "imperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ēbāmus") :gloss (str "we were " present-participle) :part_of_speech "verb" :person 1 :number "plural" :tense "imperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ēbātis") :gloss (str "you were " present-participle) :part_of_speech "verb" :person 2 :number "plural" :tense "imperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ēbant") :gloss (str "they were " present-participle) :part_of_speech "verb" :person 3 :number "plural" :tense "imperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}])

(defn future-active-indicative [{:keys [present-stem first-person-present-sg-gloss df]}]
  [{:wordform (str present-stem "am") :gloss (str "I will " first-person-present-sg-gloss) :part_of_speech "verb" :person 1 :number "singular" :tense "future" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ēs") :gloss (str "you will " first-person-present-sg-gloss) :part_of_speech "verb" :person 2 :number "singular" :tense "future" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "et") :gloss (str "he/she/it will " first-person-present-sg-gloss) :part_of_speech "verb" :person 3 :number "singular" :tense "future" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ēmus") :gloss (str "we will " first-person-present-sg-gloss) :part_of_speech "verb" :person 1 :number "plural" :tense "future" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ētis") :gloss (str "you will " first-person-present-sg-gloss) :part_of_speech "verb" :person 2 :number "plural" :tense "future" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ent") :gloss (str "they will " first-person-present-sg-gloss) :part_of_speech "verb" :person 3 :number "plural" :tense "future" :voice "active" :mood "indicative" :lexeme_id (ll df)}])

(defn pluperfect-active-indicative [{:keys [perfect-stem perfect-participle df]}]
  [{:wordform (str perfect-stem "eram") :gloss (str "I had " perfect-participle) :part_of_speech "verb" :person 1 :number "singular" :tense "pluperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str perfect-stem "erās") :gloss (str "you had " perfect-participle) :part_of_speech "verb" :person 2 :number "singular" :tense "pluperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str perfect-stem "erat") :gloss (str "he/she/it had " perfect-participle) :part_of_speech "verb" :person 3 :number "singular" :tense "pluperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str perfect-stem "erāmus") :gloss (str "we had " perfect-participle) :part_of_speech "verb" :person 1 :number "plural" :tense "pluperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str perfect-stem "erātis") :gloss (str "you had " perfect-participle) :part_of_speech "verb" :person 2 :number "plural" :tense "pluperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str perfect-stem "erant") :gloss (str "they had " perfect-participle) :part_of_speech "verb" :person 3 :number "plural" :tense "pluperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}])

(defn future-perfect-active-indicative [{:keys [perfect-stem perfect-participle df]}]
  [{:wordform (str perfect-stem "erō") :gloss (str "I will have " perfect-participle) :part_of_speech "verb" :person 1 :number "singular" :tense "future-perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str perfect-stem "eris") :gloss (str "you will have " perfect-participle) :part_of_speech "verb" :person 2 :number "singular" :tense "future-perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str perfect-stem "erit") :gloss (str "he/she/it will have " perfect-participle) :part_of_speech "verb" :person 3 :number "singular" :tense "future-perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str perfect-stem "erimus") :gloss (str "we will have " perfect-participle) :part_of_speech "verb" :person 1 :number "plural" :tense "future-perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str perfect-stem "eritis") :gloss (str "you will have " perfect-participle) :part_of_speech "verb" :person 2 :number "plural" :tense "future-perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str perfect-stem "erint") :gloss (str "they will have " perfect-participle) :part_of_speech "verb" :person 3 :number "plural" :tense "future-perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}])

(defn present-active-subjunctive [{:keys [present-stem first-person-present-sg-gloss third-person-present-sg-gloss df]}]
  [{:wordform (str present-stem "am") :gloss (str "I " first-person-present-sg-gloss) :part_of_speech "verb" :person 1 :number "singular" :tense "present" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str present-stem "ās") :gloss (str "you " first-person-present-sg-gloss) :part_of_speech "verb" :person 2 :number "singular" :tense "present" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str present-stem "at") :gloss (str "he/she/it " third-person-present-sg-gloss) :part_of_speech "verb" :person 3 :number "singular" :tense "present" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str present-stem "āmus") :gloss (str "we " first-person-present-sg-gloss) :part_of_speech "verb" :person 1 :number "plural" :tense "present" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str present-stem "ātis") :gloss (str "you " first-person-present-sg-gloss) :part_of_speech "verb" :person 2 :number "plural" :tense "present" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str present-stem "ant") :gloss (str "they " first-person-present-sg-gloss) :part_of_speech "verb" :person 3 :number "plural" :tense "present" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}])

(defn imperfect-active-subjunctive [{:keys [infinitive-stem first-person-present-sg-gloss third-person-present-sg-gloss df]}]
  [{:wordform (str infinitive-stem "em") :gloss (str "I " first-person-present-sg-gloss) :part_of_speech "verb" :person 1 :number "singular" :tense "imperfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str infinitive-stem "ēs") :gloss (str "you " first-person-present-sg-gloss) :part_of_speech "verb" :person 2 :number "singular" :tense "imperfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str infinitive-stem "et") :gloss (str "he/she/it " third-person-present-sg-gloss) :part_of_speech "verb" :person 3 :number "singular" :tense "imperfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str infinitive-stem "ēmus") :gloss (str "we " first-person-present-sg-gloss) :part_of_speech "verb" :person 1 :number "plural" :tense "imperfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str infinitive-stem "ētis") :gloss (str "you " first-person-present-sg-gloss) :part_of_speech "verb" :person 2 :number "plural" :tense "imperfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str infinitive-stem "ent") :gloss (str "they " first-person-present-sg-gloss) :part_of_speech "verb" :person 3 :number "plural" :tense "imperfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}])

(defn perfect-active-subjunctive [{:keys [perfect-stem first-person-perfect-sg-gloss df]}]
  [{:wordform (str perfect-stem "erim") :gloss (str "I " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 1 :number "singular" :tense "perfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str perfect-stem "eris") :gloss (str "you " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 2 :number "singular" :tense "perfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str perfect-stem "erit") :gloss (str "he/she/it " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 3 :number "singular" :tense "perfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str perfect-stem "erimus") :gloss (str "we " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 1 :number "plural" :tense "perfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str perfect-stem "eritis") :gloss (str "you " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 2 :number "plural" :tense "perfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str perfect-stem "erint") :gloss (str "they " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 3 :number "plural" :tense "perfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}])

(defn pluperfect-active-subjunctive [{:keys [perfect-stem perfect-participle df]}]
  [{:wordform (str perfect-stem "issem") :gloss (str "I had " perfect-participle) :part_of_speech "verb" :person 1 :number "singular" :tense "pluperfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str perfect-stem "issēs") :gloss (str "you had " perfect-participle) :part_of_speech "verb" :person 2 :number "singular" :tense "pluperfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str perfect-stem "isset") :gloss (str "he/she/it had " perfect-participle) :part_of_speech "verb" :person 3 :number "singular" :tense "pluperfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str perfect-stem "issēmus") :gloss (str "we had " perfect-participle) :part_of_speech "verb" :person 1 :number "plural" :tense "pluperfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str perfect-stem "issētis") :gloss (str "you had " perfect-participle) :part_of_speech "verb" :person 2 :number "plural" :tense "pluperfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str perfect-stem "issent") :gloss (str "they had " perfect-participle) :part_of_speech "verb" :person 3 :number "plural" :tense "pluperfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}])

(defn present-passive-indicative [{:keys [present-stem perfect-participle df]}]
  [{:wordform (str present-stem "or") :gloss (str "I am " perfect-participle) :part_of_speech "verb" :person 1 :number "singular" :tense "present" :voice "passive" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ēris") :gloss (str "you are " perfect-participle) :part_of_speech "verb" :person 2 :number "singular" :tense "present" :voice "passive" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "itur") :gloss (str "he/she/it is " perfect-participle) :part_of_speech "verb" :person 3 :number "singular" :tense "present" :voice "passive" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "imur") :gloss (str "we are " perfect-participle) :part_of_speech "verb" :person 1 :number "plural" :tense "present" :voice "passive" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "iminī") :gloss (str "you are " perfect-participle) :part_of_speech "verb" :person 2 :number "plural" :tense "present" :voice "passive" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "untur") :gloss (str "they are " perfect-participle) :part_of_speech "verb" :person 3 :number "plural" :tense "present" :voice "passive" :mood "indicative" :lexeme_id (ll df)}])

(defn future-passive-indicative [{:keys [present-stem perfect-participle df]}]
  [{:wordform (str present-stem "ar") :gloss (str "I will be " perfect-participle) :part_of_speech "verb" :person 1 :number "singular" :tense "future" :voice "passive" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ēris") :gloss (str "you will be " perfect-participle) :part_of_speech "verb" :person 2 :number "singular" :tense "future" :voice "passive" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ēre") :gloss (str "you will be " perfect-participle) :part_of_speech "verb" :person 2 :number "singular" :tense "future" :voice "passive" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ētur") :gloss (str "he/she/it will be " perfect-participle) :part_of_speech "verb" :person 3 :number "singular" :tense "future" :voice "passive" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ēmur") :gloss (str "we will be " perfect-participle) :part_of_speech "verb" :person 1 :number "plural" :tense "future" :voice "passive" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ēminī") :gloss (str "you will be " perfect-participle) :part_of_speech "verb" :person 2 :number "plural" :tense "future" :voice "passive" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "entur") :gloss (str "they will be " perfect-participle) :part_of_speech "verb" :person 3 :number "plural" :tense "future" :voice "passive" :mood "indicative" :lexeme_id (ll df)}])

(defn imperfect-passive-indicative [{:keys [present-stem perfect-participle df]}]
  [{:wordform (str present-stem "ēbar") :gloss (str "I was being " perfect-participle) :part_of_speech "verb" :person 1 :number "singular" :tense "imperfect" :voice "passive" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ēbāris") :gloss (str "you were being " perfect-participle) :part_of_speech "verb" :person 2 :number "singular" :tense "imperfect" :voice "passive" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ēbāre") :gloss (str "you were being " perfect-participle) :part_of_speech "verb" :person 2 :number "singular" :tense "imperfect" :voice "passive" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ēbātur") :gloss (str "he/she/it was being " perfect-participle) :part_of_speech "verb" :person 3 :number "singular" :tense "imperfect" :voice "passive" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ēbāmur") :gloss (str "we were being " perfect-participle) :part_of_speech "verb" :person 1 :number "plural" :tense "imperfect" :voice "passive" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ēbāminī") :gloss (str "you were being " perfect-participle) :part_of_speech "verb" :person 2 :number "plural" :tense "imperfect" :voice "passive" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ēbantur") :gloss (str "they were being " perfect-participle) :part_of_speech "verb" :person 3 :number "plural" :tense "imperfect" :voice "passive" :mood "indicative" :lexeme_id (ll df)}])

(defn present-passive-subjunctive [{:keys [present-stem perfect-participle df]}]
  [{:wordform (str present-stem "ar") :gloss (str "I am " perfect-participle) :part_of_speech "verb" :person 1 :number "singular" :tense "present" :voice "passive" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str present-stem "āris") :gloss (str "you are " perfect-participle) :part_of_speech "verb" :person 2 :number "singular" :tense "present" :voice "passive" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str present-stem "āre") :gloss (str "you are " perfect-participle) :part_of_speech "verb" :person 2 :number "singular" :tense "present" :voice "passive" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str present-stem "ātur") :gloss (str "he/she/it is " perfect-participle) :part_of_speech "verb" :person 3 :number "singular" :tense "present" :voice "passive" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str present-stem "āmur") :gloss (str "we are " perfect-participle) :part_of_speech "verb" :person 1 :number "plural" :tense "present" :voice "passive" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str present-stem "āminī") :gloss (str "you are " perfect-participle) :part_of_speech "verb" :person 2 :number "plural" :tense "present" :voice "passive" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str present-stem "antur") :gloss (str "they are " perfect-participle) :part_of_speech "verb" :person 3 :number "plural" :tense "present" :voice "passive" :mood "subjunctive" :lexeme_id (ll df)}])

(defn imperfect-passive-subjunctive [{:keys [infinitive-stem perfect-participle df third-person-present-sg-gloss]}]
  [{:wordform (str infinitive-stem "er") :gloss (str "I was being " perfect-participle) :part_of_speech "verb" :person 1 :number "singular" :tense "imperfect" :voice "passive" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str infinitive-stem "ēris") :gloss (str "you were being " perfect-participle) :part_of_speech "verb" :person 2 :number "singular" :tense "imperfect" :voice "passive" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str infinitive-stem "ēre") :gloss (str "you were being " perfect-participle) :part_of_speech "verb" :person 2 :number "singular" :tense "imperfect" :voice "passive" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str infinitive-stem "ētur") :gloss (str "he/she/it was being " third-person-present-sg-gloss) :part_of_speech "verb" :person 3 :number "singular" :tense "imperfect" :voice "passive" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str infinitive-stem "ēmur") :gloss (str "we were being " perfect-participle) :part_of_speech "verb" :person 1 :number "plural" :tense "imperfect" :voice "passive" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str infinitive-stem "ēminī") :gloss (str "you were being " perfect-participle) :part_of_speech "verb" :person 2 :number "plural" :tense "imperfect" :voice "passive" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str infinitive-stem "entur") :gloss (str "they were being " perfect-participle) :part_of_speech "verb" :person 3 :number "plural" :tense "imperfect" :voice "passive" :mood "subjunctive" :lexeme_id (ll df)}])

(defn infinitives [{:keys [infinitive first-person-present-sg-gloss perfect-stem perfect-participle df present-stem]}]
  [{:wordform  infinitive :gloss (str "to " first-person-present-sg-gloss) :part_of_speech "verb" :tense "present" :voice "active" :mood "infinitive" :lexeme_id (ll df)}
   {:wordform  (str perfect-stem "isse") :gloss (str "to have " perfect-participle) :part_of_speech "verb" :tense "perfect" :voice "active" :mood "infinitive" :lexeme_id (ll df)}
   ;; skipping future active infinitive since it requires a helper word
   {:wordform  (str present-stem "ī") :gloss (str "to be " perfect-participle) :part_of_speech "verb" :tense "present" :voice "passive" :mood "infinitive" :lexeme_id (ll df)}])

(defn imperatives [{:keys [present-stem first-person-present-sg-gloss df perfect-participle]}]
  [{:wordform  (str present-stem "e") :gloss (str first-person-present-sg-gloss "!") :part_of_speech "verb" :tense "present" :voice "active" :number "singular" :mood "imperative" :lexeme_id (ll df)}
   {:wordform  (str present-stem "ite") :gloss (str first-person-present-sg-gloss "!") :part_of_speech "verb" :tense "present" :voice "active" :number "plural" :mood "imperative" :lexeme_id (ll df)}
   {:wordform  (str present-stem "ere") :gloss (str "be "perfect-participle "!") :part_of_speech "verb" :tense "present" :voice "passive" :number "singular" :mood "imperative" :lexeme_id (ll df)}
   {:wordform  (str present-stem "iminī") :gloss (str "be "perfect-participle "!") :part_of_speech "verb" :tense "present" :voice "passive" :number "plural" :mood "imperative" :lexeme_id (ll df)}])

(defn present-active-participles-m [{:keys [present-stem present-participle df]}]
  [{:wordform (str present-stem "ēns") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "masculine" :number "singular" :case_ "nominative" :lexeme_id (ll df)}
   {:wordform (str present-stem "entis") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "masculine" :number "singular" :case_ "genitive" :lexeme_id (ll df)}
   {:wordform (str present-stem "entī") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "masculine" :number "singular" :case_ "dative" :lexeme_id (ll df)}
   {:wordform (str present-stem "entem") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "masculine" :number "singular" :case_ "accusative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ente") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "masculine" :number "singular" :case_ "ablative" :lexeme_id (ll df)}
   {:wordform (str present-stem "entēs") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "masculine" :number "plural" :case_ "nominative" :lexeme_id (ll df)}
   {:wordform (str present-stem "entium") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "masculine" :number "plural" :case_ "genitive" :lexeme_id (ll df)}
   {:wordform (str present-stem "entibus") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "masculine" :number "plural" :case_ "dative" :lexeme_id (ll df)}
   {:wordform (str present-stem "entēs") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "masculine" :number "plural" :case_ "accusative" :lexeme_id (ll df)}
   {:wordform (str present-stem "entibus") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "masculine" :number "plural" :case_ "ablative" :lexeme_id (ll df)}])

(defn present-active-participles-f [{:keys [present-stem present-participle df]}]
  [{:wordform (str present-stem "ēns") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "feminine" :number "singular" :case_ "nominative" :lexeme_id (ll df)}
   {:wordform (str present-stem "entis") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "feminine" :number "singular" :case_ "genitive" :lexeme_id (ll df)}
   {:wordform (str present-stem "entī") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "feminine" :number "singular" :case_ "dative" :lexeme_id (ll df)}
   {:wordform (str present-stem "entem") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "feminine" :number "singular" :case_ "accusative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ente") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "feminine" :number "singular" :case_ "ablative" :lexeme_id (ll df)}
   {:wordform (str present-stem "entēs") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "feminine" :number "plural" :case_ "nominative" :lexeme_id (ll df)}
   {:wordform (str present-stem "entium") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "feminine" :number "plural" :case_ "genitive" :lexeme_id (ll df)}
   {:wordform (str present-stem "entibus") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "feminine" :number "plural" :case_ "dative" :lexeme_id (ll df)}
   {:wordform (str present-stem "entēs") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "feminine" :number "plural" :case_ "accusative" :lexeme_id (ll df)}
   {:wordform (str present-stem "entibus") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "feminine" :number "plural" :case_ "ablative" :lexeme_id (ll df)}])

(defn present-active-participles-n [{:keys [present-stem present-participle df]}]
  [{:wordform (str present-stem "ēns") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "neuter" :number "singular" :case_ "nominative" :lexeme_id (ll df)}
   {:wordform (str present-stem "entis") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "neuter" :number "singular" :case_ "genitive" :lexeme_id (ll df)}
   {:wordform (str present-stem "entī") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "neuter" :number "singular" :case_ "dative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ēns") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "neuter" :number "singular" :case_ "accusative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ente") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "neuter" :number "singular" :case_ "ablative" :lexeme_id (ll df)}
   {:wordform (str present-stem "entia") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "neuter" :number "plural" :case_ "nominative" :lexeme_id (ll df)}
   {:wordform (str present-stem "entium") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "neuter" :number "plural" :case_ "genitive" :lexeme_id (ll df)}
   {:wordform (str present-stem "entibus") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "neuter" :number "plural" :case_ "dative" :lexeme_id (ll df)}
   {:wordform (str present-stem "entia") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "neuter" :number "plural" :case_ "accusative" :lexeme_id (ll df)}
   {:wordform (str present-stem "entibus") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "neuter" :number "plural" :case_ "ablative" :lexeme_id (ll df)}])

(defn future-passive-participles-m [{:keys [present-stem present-participle df]}]
  [{:wordform (str present-stem "endus") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "masculine" :number "singular" :case_ "nominative" :lexeme_id (ll df)}
   {:wordform (str present-stem "endī") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "masculine" :number "singular" :case_ "genitive" :lexeme_id (ll df)}
   {:wordform (str present-stem "endō") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "masculine" :number "singular" :case_ "dative" :lexeme_id (ll df)}
   {:wordform (str present-stem "endum") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "masculine" :number "singular" :case_ "accusative" :lexeme_id (ll df)}
   {:wordform (str present-stem "endō") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "masculine" :number "singular" :case_ "ablative" :lexeme_id (ll df)}
   {:wordform (str present-stem "endī") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "masculine" :number "plural" :case_ "nominative" :lexeme_id (ll df)}
   {:wordform (str present-stem "endōrum") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "masculine" :number "plural" :case_ "genitive" :lexeme_id (ll df)}
   {:wordform (str present-stem "endīs") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "masculine" :number "plural" :case_ "dative" :lexeme_id (ll df)}
   {:wordform (str present-stem "endōs") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "masculine" :number "plural" :case_ "accusative" :lexeme_id (ll df)}
   {:wordform (str present-stem "endīs") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "masculine" :number "plural" :case_ "ablative" :lexeme_id (ll df)}])

(defn future-passive-participles-f [{:keys [present-stem present-participle df]}]
  [{:wordform (str present-stem "enda") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "feminine" :number "singular" :case_ "nominative" :lexeme_id (ll df)}
   {:wordform (str present-stem "endae") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "feminine" :number "singular" :case_ "genitive" :lexeme_id (ll df)}
   {:wordform (str present-stem "endae") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "feminine" :number "singular" :case_ "dative" :lexeme_id (ll df)}
   {:wordform (str present-stem "endam") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "feminine" :number "singular" :case_ "accusative" :lexeme_id (ll df)}
   {:wordform (str present-stem "endā") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "feminine" :number "singular" :case_ "ablative" :lexeme_id (ll df)}
   {:wordform (str present-stem "endae") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "feminine" :number "plural" :case_ "nominative" :lexeme_id (ll df)}
   {:wordform (str present-stem "endārum") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "feminine" :number "plural" :case_ "genitive" :lexeme_id (ll df)}
   {:wordform (str present-stem "endīs") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "feminine" :number "plural" :case_ "dative" :lexeme_id (ll df)}
   {:wordform (str present-stem "endās") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "feminine" :number "plural" :case_ "accusative" :lexeme_id (ll df)}
   {:wordform (str present-stem "endīs") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "feminine" :number "plural" :case_ "ablative" :lexeme_id (ll df)}])

(defn future-passive-participles-n [{:keys [present-stem present-participle df]}]
  [{:wordform (str present-stem "endum") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "neuter" :number "singular" :case_ "nominative" :lexeme_id (ll df)}
   {:wordform (str present-stem "endī") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "neuter" :number "singular" :case_ "genitive" :lexeme_id (ll df)}
   {:wordform (str present-stem "endō") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "neuter" :number "singular" :case_ "dative" :lexeme_id (ll df)}
   {:wordform (str present-stem "endum") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "neuter" :number "singular" :case_ "accusative" :lexeme_id (ll df)}
   {:wordform (str present-stem "endō") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "neuter" :number "singular" :case_ "ablative" :lexeme_id (ll df)}
   {:wordform (str present-stem "enda") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "neuter" :number "plural" :case_ "nominative" :lexeme_id (ll df)}
   {:wordform (str present-stem "endōrum") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "neuter" :number "plural" :case_ "genitive" :lexeme_id (ll df)}
   {:wordform (str present-stem "endīs") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "neuter" :number "plural" :case_ "dative" :lexeme_id (ll df)}
   {:wordform (str present-stem "enda") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "neuter" :number "plural" :case_ "accusative" :lexeme_id (ll df)}
   {:wordform (str present-stem "endīs") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "neuter" :number "plural" :case_ "ablative" :lexeme_id (ll df)}])

(defn perfect-passive-participles-m [{:keys [participial-stem perfect-participle present-participle df dep?]}]
  (let [gloss (if dep?
                (str "was " present-participle)
                (str "having been " perfect-participle))]
    [{:wordform (str participial-stem "us") :gloss gloss :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "masculine" :number "singular" :case_ "nominative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ī") :gloss gloss :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "masculine" :number "singular" :case_ "genitive" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ō") :gloss gloss :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "masculine" :number "singular" :case_ "dative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "um") :gloss gloss :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "masculine" :number "singular" :case_ "accusative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ō") :gloss gloss :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "masculine" :number "singular" :case_ "ablative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ī") :gloss gloss :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "masculine" :number "plural" :case_ "nominative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ōrum") :gloss gloss :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "masculine" :number "plural" :case_ "genitive" :lexeme_id (ll df)}
     {:wordform (str participial-stem "īs") :gloss gloss :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "masculine" :number "plural" :case_ "dative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ōs") :gloss gloss :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "masculine" :number "plural" :case_ "accusative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "īs") :gloss gloss :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "masculine" :number "plural" :case_ "ablative" :lexeme_id (ll df)}]))

(defn perfect-passive-participles-f [{:keys [participial-stem perfect-participle present-participle df dep?]}]
  (let [gloss (if dep?
                (str "was " present-participle)
                (str "having been " perfect-participle))]
    [{:wordform (str participial-stem "a") :gloss gloss :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "feminine" :number "singular" :case_ "nominative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ae") :gloss gloss :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "feminine" :number "singular" :case_ "genitive" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ae") :gloss gloss :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "feminine" :number "singular" :case_ "dative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "am") :gloss gloss :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "feminine" :number "singular" :case_ "accusative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ā") :gloss gloss :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "feminine" :number "singular" :case_ "ablative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ae") :gloss gloss :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "feminine" :number "plural" :case_ "nominative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ārum") :gloss gloss :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "feminine" :number "plural" :case_ "genitive" :lexeme_id (ll df)}
     {:wordform (str participial-stem "īs") :gloss gloss :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "feminine" :number "plural" :case_ "dative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ās") :gloss gloss :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "feminine" :number "plural" :case_ "accusative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "īs") :gloss gloss :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "feminine" :number "plural" :case_ "ablative" :lexeme_id (ll df)}]))

(defn perfect-passive-participles-n [{:keys [participial-stem perfect-participle present-participle df dep?]}]
  (let [gloss (if dep?
                (str "was " present-participle)
                (str "having been " perfect-participle))]
    [{:wordform (str participial-stem "um") :gloss gloss :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "neuter" :number "singular" :case_ "nominative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ī") :gloss gloss :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "neuter" :number "singular" :case_ "genitive" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ō") :gloss gloss :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "neuter" :number "singular" :case_ "dative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "um") :gloss gloss :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "neuter" :number "singular" :case_ "accusative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ō") :gloss gloss :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "neuter" :number "singular" :case_ "ablative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "a") :gloss gloss :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "neuter" :number "plural" :case_ "nominative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "ōrum") :gloss gloss :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "neuter" :number "plural" :case_ "genitive" :lexeme_id (ll df)}
     {:wordform (str participial-stem "īs") :gloss gloss :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "neuter" :number "plural" :case_ "dative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "a") :gloss gloss :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "neuter" :number "plural" :case_ "accusative" :lexeme_id (ll df)}
     {:wordform (str participial-stem "īs") :gloss gloss :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "neuter" :number "plural" :case_ "ablative" :lexeme_id (ll df)}]))

(defn future-active-participles-m [{:keys [participial-stem present-participle df]}]
  [{:wordform (str participial-stem "ūrus") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "masculine" :number "singular" :case_ "nominative" :lexeme_id (ll df)}
   {:wordform (str participial-stem "ūrī") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "masculine" :number "singular" :case_ "genitive" :lexeme_id (ll df)}
   {:wordform (str participial-stem "ūrō") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "masculine" :number "singular" :case_ "dative" :lexeme_id (ll df)}
   {:wordform (str participial-stem "ūrum") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "masculine" :number "singular" :case_ "accusative" :lexeme_id (ll df)}
   {:wordform (str participial-stem "ūrō") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "masculine" :number "singular" :case_ "ablative" :lexeme_id (ll df)}
   {:wordform (str participial-stem "ūrī") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "masculine" :number "plural" :case_ "nominative" :lexeme_id (ll df)}
   {:wordform (str participial-stem "ūrōrum") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "masculine" :number "plural" :case_ "genitive" :lexeme_id (ll df)}
   {:wordform (str participial-stem "ūrīs") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "masculine" :number "plural" :case_ "dative" :lexeme_id (ll df)}
   {:wordform (str participial-stem "ūrōs") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "masculine" :number "plural" :case_ "accusative" :lexeme_id (ll df)}
   {:wordform (str participial-stem "ūrīs") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "masculine" :number "plural" :case_ "ablative" :lexeme_id (ll df)}])

(defn future-active-participles-f [{:keys [participial-stem present-participle df]}]
  [{:wordform (str participial-stem "ūra") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "feminine" :number "singular" :case_ "nominative" :lexeme_id (ll df)}
   {:wordform (str participial-stem "ūrae") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "feminine" :number "singular" :case_ "genitive" :lexeme_id (ll df)}
   {:wordform (str participial-stem "ūrae") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "feminine" :number "singular" :case_ "dative" :lexeme_id (ll df)}
   {:wordform (str participial-stem "ūram") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "feminine" :number "singular" :case_ "accusative" :lexeme_id (ll df)}
   {:wordform (str participial-stem "ūrā") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "feminine" :number "singular" :case_ "ablative" :lexeme_id (ll df)}
   {:wordform (str participial-stem "ūrae") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "feminine" :number "plural" :case_ "nominative" :lexeme_id (ll df)}
   {:wordform (str participial-stem "ūrārum") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "feminine" :number "plural" :case_ "genitive" :lexeme_id (ll df)}
   {:wordform (str participial-stem "ūrīs") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "feminine" :number "plural" :case_ "dative" :lexeme_id (ll df)}
   {:wordform (str participial-stem "ūrās") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "feminine" :number "plural" :case_ "accusative" :lexeme_id (ll df)}
   {:wordform (str participial-stem "ūrīs") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "feminine" :number "plural" :case_ "ablative" :lexeme_id (ll df)}])

(defn future-active-participles-n [{:keys [participial-stem present-participle df]}]
  [{:wordform (str participial-stem "ūrum") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "neuter" :number "singular" :case_ "nominative" :lexeme_id (ll df)}
   {:wordform (str participial-stem "ūrī") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "neuter" :number "singular" :case_ "genitive" :lexeme_id (ll df)}
   {:wordform (str participial-stem "ūrō") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "neuter" :number "singular" :case_ "dative" :lexeme_id (ll df)}
   {:wordform (str participial-stem "ūrum") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "neuter" :number "singular" :case_ "accusative" :lexeme_id (ll df)}
   {:wordform (str participial-stem "ūrō") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "neuter" :number "singular" :case_ "ablative" :lexeme_id (ll df)}
   {:wordform (str participial-stem "ūra") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "neuter" :number "plural" :case_ "nominative" :lexeme_id (ll df)}
   {:wordform (str participial-stem "ūrōrum") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "neuter" :number "plural" :case_ "genitive" :lexeme_id (ll df)}
   {:wordform (str participial-stem "ūrīs") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "neuter" :number "plural" :case_ "dative" :lexeme_id (ll df)}
   {:wordform (str participial-stem "ūra") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "neuter" :number "plural" :case_ "accusative" :lexeme_id (ll df)}
   {:wordform (str participial-stem "ūrīs") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "active" :mood "indicative" :gender "neuter" :number "plural" :case_ "ablative" :lexeme_id (ll df)}])

(defn get-verb-forms-ere* [{:keys [first-person-present infinitive first-person-perfect supine first-person-present-sg-gloss third-person-present-sg-gloss first-person-perfect-sg-gloss perfect-participle present-participle] :as args}]
  (let [present-stem (subs first-person-present 0 (dec (count first-person-present)))
        perfect-stem (subs first-person-perfect 0 (dec (count first-person-perfect)))
        perfect-stem-minus-v (subs perfect-stem 0 (dec (count perfect-stem))) ;; for -ve syncopation
        infinitive-stem (subs infinitive 0 (dec (count infinitive)))
        df (clojure.string/join ", " [first-person-present infinitive first-person-perfect supine])
        participial-stem (subs supine 0 (- (count supine) 2))
        args (assoc
              args
              :present-stem present-stem
              :perfect-stem perfect-stem
              :perfect-stem-minus-v perfect-stem-minus-v
              :infinitive-stem infinitive-stem
              :df df
              :participial-stem participial-stem)]
    (concat
     (present-active-indicative args)
     (perfect-active-indicative args)
     (imperfect-active-indicative args)
     (future-active-indicative args)
     (pluperfect-active-indicative args)
     (future-perfect-active-indicative args)
     (present-active-subjunctive args)
     (imperfect-active-subjunctive args)
     (perfect-active-subjunctive args)
     (pluperfect-active-subjunctive args)
     (present-passive-indicative args)
     (future-passive-indicative args)
     (imperfect-passive-indicative args)
     (present-passive-subjunctive args)
     (imperfect-passive-subjunctive args)
     (infinitives args)
     (imperatives args)
     (present-active-participles-m args)
     (present-active-participles-f args)
     (present-active-participles-n args)
     (future-passive-participles-m args)
     (future-passive-participles-f args)
     (future-passive-participles-n args)
     (perfect-passive-participles-m args)
     (perfect-passive-participles-f args)
     (perfect-passive-participles-n args)
     (future-active-participles-m args)
     (future-active-participles-f args)
     (future-active-participles-n args))))

(defn present-active-indicative-dep [{:keys [first-person-present first-person-present-sg-gloss present-stem third-person-present-sg-gloss df]}]
  [{:wordform first-person-present :gloss (str "I " first-person-present-sg-gloss) :part_of_speech "verb" :person 1 :number "singular" :tense "present" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "eris") :gloss (str "you " first-person-present-sg-gloss) :part_of_speech "verb" :person 2 :number "singular" :tense "present" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "itur") :gloss (str "he/she/it " third-person-present-sg-gloss) :part_of_speech "verb" :person 3 :number "singular" :tense "present" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "imur") :gloss (str "we " first-person-present-sg-gloss) :part_of_speech "verb" :person 1 :number "plural" :tense "present" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "iminī") :gloss (str "you " first-person-present-sg-gloss) :part_of_speech "verb" :person 2 :number "plural" :tense "present" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "untur") :gloss (str "they " first-person-present-sg-gloss) :part_of_speech "verb" :person 3 :number "plural" :tense "present" :voice "active" :mood "indicative" :lexeme_id (ll df)}])

(defn imperfect-active-indicative-dep [{:keys [first-person-present first-person-present-sg-gloss present-stem third-person-present-sg-gloss df]}]
  [{:wordform (str present-stem "ēbar") :gloss (str "I " first-person-present-sg-gloss) :part_of_speech "verb" :person 1 :number "singular" :tense "imperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ēbāris") :gloss (str "you " first-person-present-sg-gloss) :part_of_speech "verb" :person 2 :number "singular" :tense "imperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ēbātur") :gloss (str "he/she/it " third-person-present-sg-gloss) :part_of_speech "verb" :person 3 :number "singular" :tense "imperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ēbāmur") :gloss (str "we " first-person-present-sg-gloss) :part_of_speech "verb" :person 1 :number "plural" :tense "imperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ēbāminī") :gloss (str "you " first-person-present-sg-gloss) :part_of_speech "verb" :person 2 :number "plural" :tense "imperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ēbantur") :gloss (str "they " first-person-present-sg-gloss) :part_of_speech "verb" :person 3 :number "plural" :tense "imperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}])

(defn future-active-indicative-dep [{:keys [first-person-present first-person-present-sg-gloss present-stem third-person-present-sg-gloss df]}]
  [{:wordform (str present-stem "ar") :gloss (str "I will " first-person-present-sg-gloss) :part_of_speech "verb" :person 1 :number "singular" :tense "future" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ēris") :gloss (str "you will " first-person-present-sg-gloss) :part_of_speech "verb" :person 2 :number "singular" :tense "future" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ētur") :gloss (str "he/she/it will " third-person-present-sg-gloss) :part_of_speech "verb" :person 3 :number "singular" :tense "future" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ēmur") :gloss (str "we will " first-person-present-sg-gloss) :part_of_speech "verb" :person 1 :number "plural" :tense "future" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ēminī") :gloss (str "you will " first-person-present-sg-gloss) :part_of_speech "verb" :person 2 :number "plural" :tense "future" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "entur") :gloss (str "they will " first-person-present-sg-gloss) :part_of_speech "verb" :person 3 :number "plural" :tense "future" :voice "active" :mood "indicative" :lexeme_id (ll df)}])

(defn present-active-subjunctive-dep [{:keys [first-person-present first-person-present-sg-gloss present-stem third-person-present-sg-gloss df]}]
  [{:wordform (str present-stem "ar") :gloss (str "I " first-person-present-sg-gloss) :part_of_speech "verb" :person 1 :number "singular" :tense "present" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str present-stem "āris") :gloss (str "you " first-person-present-sg-gloss) :part_of_speech "verb" :person 2 :number "singular" :tense "present" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str present-stem "ātur") :gloss (str "he/she/it " third-person-present-sg-gloss) :part_of_speech "verb" :person 3 :number "singular" :tense "present" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str present-stem "āmur") :gloss (str "we " first-person-present-sg-gloss) :part_of_speech "verb" :person 1 :number "plural" :tense "present" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str present-stem "āminī") :gloss (str "you " first-person-present-sg-gloss) :part_of_speech "verb" :person 2 :number "plural" :tense "present" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str present-stem "antur") :gloss (str "they " first-person-present-sg-gloss) :part_of_speech "verb" :person 3 :number "plural" :tense "present" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}])

(defn imperfect-active-subjunctive-dep [{:keys [first-person-perfect-sg-gloss present-stem df]}]
  [{:wordform (str present-stem "erer") :gloss (str "I " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 1 :number "singular" :tense "imperfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str present-stem "erēris") :gloss (str "you " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 2 :number "singular" :tense "imperfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str present-stem "erētur") :gloss (str "he/she/it " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 3 :number "singular" :tense "imperfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str present-stem "erēmur") :gloss (str "we " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 1 :number "plural" :tense "imperfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str present-stem "erēminī") :gloss (str "you " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 2 :number "plural" :tense "imperfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str present-stem "erentur") :gloss (str "they " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 3 :number "plural" :tense "imperfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}])

(defn infinitives-dep [{:keys [infinitive infinitive-stem first-person-present-sg-gloss df perfect-stem perfect-participle]}]
  [{:wordform  infinitive :gloss (str "to " first-person-present-sg-gloss) :part_of_speech "verb" :tense "present" :voice "active" :mood "infinitive" :lexeme_id (ll df)}])

(defn imperatives-dep [{:keys [present-stem first-person-present-sg-gloss df perfect-participle]}]
  [{:wordform  (str present-stem "ere") :gloss (str first-person-present-sg-gloss "!") :part_of_speech "verb" :tense "present" :voice "active" :number "singular" :mood "imperative" :lexeme_id (ll df)}
   {:wordform  (str present-stem "iminī") :gloss (str first-person-present-sg-gloss "!") :part_of_speech "verb" :tense "present" :voice "active" :number "plural" :mood "imperative" :lexeme_id (ll df)}])

(defn get-verb-forms-ere*-dep [{:keys [first-person-present infinitive supine first-person-present-sg-gloss third-person-present-sg-gloss first-person-perfect-sg-gloss present-participle] :as args}]
  (let [present-stem (subs first-person-present 0 (- (count first-person-present) 2))
        participial-stem (subs supine 0 (- (count supine) 2))
        df (clojure.string/join ", " [first-person-present infinitive supine])
        args (assoc args :present-stem present-stem :df df :participial-stem participial-stem :dep? true)]
    (concat
     (present-active-indicative-dep args)
     (imperfect-active-indicative-dep args)
     (future-active-indicative-dep args)
     (present-active-subjunctive-dep args)
     (imperfect-active-subjunctive-dep args)
     (infinitives-dep args)
     (imperatives-dep args)
     (present-active-participles-m args)
     (present-active-participles-f args)
     (present-active-participles-n args)
     (future-passive-participles-m args)
     (future-passive-participles-f args)
     (future-passive-participles-n args)
     (perfect-passive-participles-m args)
     (perfect-passive-participles-f args)
     (perfect-passive-participles-n args)
     (future-active-participles-m args)
     (future-active-participles-f args)
     (future-active-participles-n args)
     )))
