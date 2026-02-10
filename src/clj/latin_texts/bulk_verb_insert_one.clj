(ns latin-texts.bulk-verb-insert-one
  (:require
   [latin-texts.db :refer [ll]]))

(defn femize [forms]
  (map (fn [form] (assoc form :gender "feminine"))
       forms))

(defn present-active-indicative [{:keys [first-person-present first-person-present-sg-gloss df present-stem third-person-present-sg-gloss]}]
  [{:wordform first-person-present :gloss (str "I " first-person-present-sg-gloss) :part_of_speech "verb" :person 1 :number "singular" :tense "present" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ās") :gloss (str "you " first-person-present-sg-gloss) :part_of_speech "verb" :person 2 :number "singular" :tense "present" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "at") :gloss (str "he/she/it " third-person-present-sg-gloss) :part_of_speech "verb" :person 3 :number "singular" :tense "present" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "āmus") :gloss (str "we " first-person-present-sg-gloss) :part_of_speech "verb" :person 1 :number "plural" :tense "present" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ātis") :gloss (str "you " first-person-present-sg-gloss) :part_of_speech "verb" :person 2 :number "plural" :tense "present" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ant") :gloss (str "they " first-person-present-sg-gloss) :part_of_speech "verb" :person 3 :number "plural" :tense "present" :voice "active" :mood "indicative" :lexeme_id (ll df)}])

(defn perfect-active-indicative [{:keys [first-person-perfect first-person-perfect-sg-gloss df perfect-stem]}]
  [{:wordform first-person-perfect :gloss (str "I " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 1 :number "singular" :tense "perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str perfect-stem "istī") :gloss (str "you " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 2 :number "singular" :tense "perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str perfect-stem "it") :gloss (str "he/she/it " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 3 :number "singular" :tense "perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str perfect-stem "imus") :gloss (str "we " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 1 :number "plural" :tense "perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str perfect-stem "istis") :gloss (str "you " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 2 :number "plural" :tense "perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str perfect-stem "ērunt") :gloss (str "they " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 3 :number "plural" :tense "perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str perfect-stem "ēre") :gloss (str "they " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 3 :number "plural" :tense "perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}])

(defn imperfect-active-indicative [{:keys [present-stem present-participle df]}]
  [{:wordform (str present-stem "ābam") :gloss (str "I was " present-participle) :part_of_speech "verb" :person 1 :number "singular" :tense "imperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ābās") :gloss (str "you were " present-participle) :part_of_speech "verb" :person 2 :number "singular" :tense "imperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ābat") :gloss (str "he/she/it was " present-participle) :part_of_speech "verb" :person 3 :number "singular" :tense "imperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ābāmus") :gloss (str "we were " present-participle) :part_of_speech "verb" :person 1 :number "plural" :tense "imperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ābātis") :gloss (str "you were " present-participle) :part_of_speech "verb" :person 2 :number "plural" :tense "imperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ābant") :gloss (str "they were " present-participle) :part_of_speech "verb" :person 3 :number "plural" :tense "imperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}])

(defn future-active-indicative [{:keys [present-stem first-person-present-sg-gloss df]}]
  [{:wordform (str present-stem "ābō") :gloss (str "I will " first-person-present-sg-gloss) :part_of_speech "verb" :person 1 :number "singular" :tense "future" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ābis") :gloss (str "you will " first-person-present-sg-gloss) :part_of_speech "verb" :person 2 :number "singular" :tense "future" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ābit") :gloss (str "he/she/it will " first-person-present-sg-gloss) :part_of_speech "verb" :person 3 :number "singular" :tense "future" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ābimus") :gloss (str "we will " first-person-present-sg-gloss) :part_of_speech "verb" :person 1 :number "plural" :tense "future" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ābitis") :gloss (str "you will " first-person-present-sg-gloss) :part_of_speech "verb" :person 2 :number "plural" :tense "future" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ābunt") :gloss (str "they will " first-person-present-sg-gloss) :part_of_speech "verb" :person 3 :number "plural" :tense "future" :voice "active" :mood "indicative" :lexeme_id (ll df)}])

(defn pluperfect-active-indicative [{:keys [perfect-stem perfect-stem-minus-v perfect-participle df]}]
  [{:wordform (str perfect-stem "eram") :gloss (str "I had " perfect-participle) :part_of_speech "verb" :person 1 :number "singular" :tense "pluperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str perfect-stem-minus-v "ram") :gloss (str "I had " perfect-participle) :part_of_speech "verb" :person 1 :number "singular" :tense "pluperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str perfect-stem "erās") :gloss (str "you had " perfect-participle) :part_of_speech "verb" :person 2 :number "singular" :tense "pluperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str perfect-stem-minus-v "rās") :gloss (str "you had " perfect-participle) :part_of_speech "verb" :person 2 :number "singular" :tense "pluperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str perfect-stem "erat") :gloss (str "he/she/it had " perfect-participle) :part_of_speech "verb" :person 3 :number "singular" :tense "pluperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str perfect-stem-minus-v "rat") :gloss (str "he/she/it had " perfect-participle) :part_of_speech "verb" :person 3 :number "singular" :tense "pluperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str perfect-stem "erāmus") :gloss (str "we had " perfect-participle) :part_of_speech "verb" :person 1 :number "plural" :tense "pluperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str perfect-stem-minus-v "rāmus") :gloss (str "we had " perfect-participle) :part_of_speech "verb" :person 1 :number "plural" :tense "pluperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str perfect-stem "erātis") :gloss (str "you had " perfect-participle) :part_of_speech "verb" :person 2 :number "plural" :tense "pluperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str perfect-stem-minus-v "rātis") :gloss (str "you had " perfect-participle) :part_of_speech "verb" :person 2 :number "plural" :tense "pluperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str perfect-stem "erant") :gloss (str "they had " perfect-participle) :part_of_speech "verb" :person 3 :number "plural" :tense "pluperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str perfect-stem-minus-v "rant") :gloss (str "they had " perfect-participle) :part_of_speech "verb" :person 3 :number "plural" :tense "pluperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}])
;; START COPY
(defn future-perfect-active-indicative [{:keys [perfect-stem perfect-stem-minus-v perfect-participle df]}]
  [{:wordform (str perfect-stem "erō") :gloss (str "I will have " perfect-participle) :part_of_speech "verb" :person 1 :number "singular" :tense "future-perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str perfect-stem-minus-v "rō") :gloss (str "I will have " perfect-participle) :part_of_speech "verb" :person 1 :number "singular" :tense "future-perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str perfect-stem "eris") :gloss (str "you will have " perfect-participle) :part_of_speech "verb" :person 2 :number "singular" :tense "future-perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str perfect-stem-minus-v "ris") :gloss (str "you will have " perfect-participle) :part_of_speech "verb" :person 2 :number "singular" :tense "future-perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str perfect-stem "erit") :gloss (str "he/she/it will have " perfect-participle) :part_of_speech "verb" :person 3 :number "singular" :tense "future-perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str perfect-stem-minus-v "rit") :gloss (str "he/she/it will have " perfect-participle) :part_of_speech "verb" :person 3 :number "singular" :tense "future-perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str perfect-stem "erimus") :gloss (str "we will have " perfect-participle) :part_of_speech "verb" :person 1 :number "plural" :tense "future-perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str perfect-stem-minus-v "rimus") :gloss (str "we will have " perfect-participle) :part_of_speech "verb" :person 1 :number "plural" :tense "future-perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str perfect-stem "eritis") :gloss (str "you will have " perfect-participle) :part_of_speech "verb" :person 2 :number "plural" :tense "future-perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str perfect-stem-minus-v "ritis") :gloss (str "you will have " perfect-participle) :part_of_speech "verb" :person 2 :number "plural" :tense "future-perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str perfect-stem "erint") :gloss (str "they will have " perfect-participle) :part_of_speech "verb" :person 3 :number "plural" :tense "future-perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str perfect-stem-minus-v "rint") :gloss (str "they will have " perfect-participle) :part_of_speech "verb" :person 3 :number "plural" :tense "future-perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}])

(defn present-active-subjunctive [{:keys [present-stem first-person-present-sg-gloss third-person-present-sg-gloss df]}]
  [{:wordform (str present-stem "em") :gloss (str "I " first-person-present-sg-gloss) :part_of_speech "verb" :person 1 :number "singular" :tense "present" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str present-stem "ēs") :gloss (str "you " first-person-present-sg-gloss) :part_of_speech "verb" :person 2 :number "singular" :tense "present" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str present-stem "et") :gloss (str "he/she/it " third-person-present-sg-gloss) :part_of_speech "verb" :person 3 :number "singular" :tense "present" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str present-stem "ēmus") :gloss (str "we " first-person-present-sg-gloss) :part_of_speech "verb" :person 1 :number "plural" :tense "present" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str present-stem "ētis") :gloss (str "you " first-person-present-sg-gloss) :part_of_speech "verb" :person 2 :number "plural" :tense "present" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str present-stem "ent") :gloss (str "they " first-person-present-sg-gloss) :part_of_speech "verb" :person 3 :number "plural" :tense "present" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}])

(defn imperfect-active-subjunctive [{:keys [infinitive-stem first-person-present-sg-gloss third-person-present-sg-gloss df]}]
  [{:wordform (str infinitive-stem "em") :gloss (str "I " first-person-present-sg-gloss) :part_of_speech "verb" :person 1 :number "singular" :tense "imperfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str infinitive-stem "ēs") :gloss (str "you " first-person-present-sg-gloss) :part_of_speech "verb" :person 2 :number "singular" :tense "imperfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str infinitive-stem "et") :gloss (str "he/she/it " third-person-present-sg-gloss) :part_of_speech "verb" :person 3 :number "singular" :tense "imperfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str infinitive-stem "ēmus") :gloss (str "we " first-person-present-sg-gloss) :part_of_speech "verb" :person 1 :number "plural" :tense "imperfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str infinitive-stem "ētis") :gloss (str "you " first-person-present-sg-gloss) :part_of_speech "verb" :person 2 :number "plural" :tense "imperfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str infinitive-stem "ent") :gloss (str "they " first-person-present-sg-gloss) :part_of_speech "verb" :person 3 :number "plural" :tense "imperfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}])

(defn perfect-active-subjunctive [{:keys [perfect-stem perfect-stem-minus-v first-person-perfect-sg-gloss df]}]
  [{:wordform (str perfect-stem "erim") :gloss (str "I " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 1 :number "singular" :tense "perfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str perfect-stem-minus-v "rim") :gloss (str "I " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 1 :number "singular" :tense "perfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str perfect-stem "eris") :gloss (str "you " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 2 :number "singular" :tense "perfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str perfect-stem-minus-v "ris") :gloss (str "you " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 2 :number "singular" :tense "perfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str perfect-stem "erit") :gloss (str "he/she/it " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 3 :number "singular" :tense "perfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str perfect-stem-minus-v "rit") :gloss (str "he/she/it " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 3 :number "singular" :tense "perfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str perfect-stem "erimus") :gloss (str "we " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 1 :number "plural" :tense "perfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str perfect-stem-minus-v "rimus") :gloss (str "we " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 1 :number "plural" :tense "perfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str perfect-stem "eritis") :gloss (str "you " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 2 :number "plural" :tense "perfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str perfect-stem-minus-v "ritis") :gloss (str "you " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 2 :number "plural" :tense "perfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str perfect-stem "erint") :gloss (str "they " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 3 :number "plural" :tense "perfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str perfect-stem-minus-v "rint") :gloss (str "they " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 3 :number "plural" :tense "perfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}])

(defn pluperfect-active-subjunctive [{:keys [perfect-stem perfect-stem-minus-v first-person-perfect-sg-gloss df perfect-participle]}]
  [{:wordform (str perfect-stem "issem") :gloss (str "I had " perfect-participle) :part_of_speech "verb" :person 1 :number "singular" :tense "pluperfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str perfect-stem-minus-v "ssem") :gloss (str "I had " perfect-participle) :part_of_speech "verb" :person 1 :number "singular" :tense "pluperfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str perfect-stem "issēs") :gloss (str "you had " perfect-participle) :part_of_speech "verb" :person 2 :number "singular" :tense "pluperfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str perfect-stem-minus-v "ssēs") :gloss (str "you had " perfect-participle) :part_of_speech "verb" :person 2 :number "singular" :tense "pluperfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str perfect-stem "isset") :gloss (str "he/she/it had " perfect-participle) :part_of_speech "verb" :person 3 :number "singular" :tense "pluperfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str perfect-stem-minus-v "sset") :gloss (str "he/she/it had " perfect-participle) :part_of_speech "verb" :person 3 :number "singular" :tense "pluperfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str perfect-stem "issēmus") :gloss (str "we had " perfect-participle) :part_of_speech "verb" :person 1 :number "plural" :tense "pluperfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str perfect-stem-minus-v "ssēmus") :gloss (str "we had " perfect-participle) :part_of_speech "verb" :person 1 :number "plural" :tense "pluperfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str perfect-stem "issētis") :gloss (str "you had " perfect-participle) :part_of_speech "verb" :person 2 :number "plural" :tense "pluperfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str perfect-stem-minus-v "ssētis") :gloss (str "you had " perfect-participle) :part_of_speech "verb" :person 2 :number "plural" :tense "pluperfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str perfect-stem "issent") :gloss (str "they had " perfect-participle) :part_of_speech "verb" :person 3 :number "plural" :tense "pluperfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str perfect-stem-minus-v "ssent") :gloss (str "they had " perfect-participle) :part_of_speech "verb" :person 3 :number "plural" :tense "pluperfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}])

(defn present-passive-indicative [{:keys [present-stem perfect-participle df]}]
  [{:wordform (str present-stem "or") :gloss (str "I am " perfect-participle) :part_of_speech "verb" :person 1 :number "singular" :tense "present" :voice "passive" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "āris") :gloss (str "you are " perfect-participle) :part_of_speech "verb" :person 2 :number "singular" :tense "present" :voice "passive" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ātur") :gloss (str "he/she/it is " perfect-participle) :part_of_speech "verb" :person 3 :number "singular" :tense "present" :voice "passive" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "āmur") :gloss (str "we are " perfect-participle) :part_of_speech "verb" :person 1 :number "plural" :tense "present" :voice "passive" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "āminī") :gloss (str "you are " perfect-participle) :part_of_speech "verb" :person 2 :number "plural" :tense "present" :voice "passive" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "antur") :gloss (str "they are " perfect-participle) :part_of_speech "verb" :person 3 :number "plural" :tense "present" :voice "passive" :mood "indicative" :lexeme_id (ll df)}])

(defn future-passive-indicative [{:keys [present-stem perfect-participle df]}]
  [{:wordform (str present-stem "ābor") :gloss (str "I will be " perfect-participle) :part_of_speech "verb" :person 1 :number "singular" :tense "future" :voice "passive" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "āberis") :gloss (str "you will be " perfect-participle) :part_of_speech "verb" :person 2 :number "singular" :tense "future" :voice "passive" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ābere") :gloss (str "you will be " perfect-participle) :part_of_speech "verb" :person 2 :number "singular" :tense "future" :voice "passive" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ābitur") :gloss (str "he/she/it will be " perfect-participle) :part_of_speech "verb" :person 3 :number "singular" :tense "future" :voice "passive" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ābimur") :gloss (str "we will be " perfect-participle) :part_of_speech "verb" :person 1 :number "plural" :tense "future" :voice "passive" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ābiminī") :gloss (str "you will be " perfect-participle) :part_of_speech "verb" :person 2 :number "plural" :tense "future" :voice "passive" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ābuntur") :gloss (str "they will be " perfect-participle) :part_of_speech "verb" :person 3 :number "plural" :tense "future" :voice "passive" :mood "indicative" :lexeme_id (ll df)}])

(defn imperfect-passive-indicative [{:keys [present-stem perfect-participle df]}]
  [{:wordform (str present-stem "ābar") :gloss (str "I was being " perfect-participle) :part_of_speech "verb" :person 1 :number "singular" :tense "imperfect" :voice "passive" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ābāris") :gloss (str "you were being " perfect-participle) :part_of_speech "verb" :person 2 :number "singular" :tense "imperfect" :voice "passive" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ābāre") :gloss (str "you were being " perfect-participle) :part_of_speech "verb" :person 2 :number "singular" :tense "imperfect" :voice "passive" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ābātur") :gloss (str "he/she/it was being " perfect-participle) :part_of_speech "verb" :person 3 :number "singular" :tense "imperfect" :voice "passive" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ābāmur") :gloss (str "we were being " perfect-participle) :part_of_speech "verb" :person 1 :number "plural" :tense "imperfect" :voice "passive" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ābāminī") :gloss (str "you were being " perfect-participle) :part_of_speech "verb" :person 2 :number "plural" :tense "imperfect" :voice "passive" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ābantur") :gloss (str "they were being " perfect-participle) :part_of_speech "verb" :person 3 :number "plural" :tense "imperfect" :voice "passive" :mood "indicative" :lexeme_id (ll df)}])

(defn present-passive-subjunctive [{:keys [present-stem perfect-participle df]}]
  [{:wordform (str present-stem "er") :gloss (str "I am " perfect-participle) :part_of_speech "verb" :person 1 :number "singular" :tense "present" :voice "passive" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str present-stem "ēris") :gloss (str "you are " perfect-participle) :part_of_speech "verb" :person 2 :number "singular" :tense "present" :voice "passive" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str present-stem "ēre") :gloss (str "you are " perfect-participle) :part_of_speech "verb" :person 2 :number "singular" :tense "present" :voice "passive" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str present-stem "ētur") :gloss (str "he/she/it is " perfect-participle) :part_of_speech "verb" :person 3 :number "singular" :tense "present" :voice "passive" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str present-stem "ēmur") :gloss (str "we are " perfect-participle) :part_of_speech "verb" :person 1 :number "plural" :tense "present" :voice "passive" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str present-stem "ēminī") :gloss (str "you are " perfect-participle) :part_of_speech "verb" :person 2 :number "plural" :tense "present" :voice "passive" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str present-stem "entur") :gloss (str "they are " perfect-participle) :part_of_speech "verb" :person 3 :number "plural" :tense "present" :voice "passive" :mood "subjunctive" :lexeme_id (ll df)}])

(defn imperfect-passive-subjunctive [{:keys [infinitive-stem perfect-participle df third-person-present-sg-gloss]}]
  [{:wordform (str infinitive-stem "er") :gloss (str "I was being " perfect-participle) :part_of_speech "verb" :person 1 :number "singular" :tense "imperfect" :voice "passive" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str infinitive-stem "ēris") :gloss (str "you were being " perfect-participle) :part_of_speech "verb" :person 2 :number "singular" :tense "imperfect" :voice "passive" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str infinitive-stem "ēre") :gloss (str "you were being " perfect-participle) :part_of_speech "verb" :person 2 :number "singular" :tense "imperfect" :voice "passive" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str infinitive-stem "ētur") :gloss (str "he/she/it was being " third-person-present-sg-gloss) :part_of_speech "verb" :person 3 :number "singular" :tense "imperfect" :voice "passive" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str infinitive-stem "ēmur") :gloss (str "we were being " perfect-participle) :part_of_speech "verb" :person 1 :number "plural" :tense "imperfect" :voice "passive" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str infinitive-stem "ēminī") :gloss (str "you were being " perfect-participle) :part_of_speech "verb" :person 2 :number "plural" :tense "imperfect" :voice "passive" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str infinitive-stem "entur") :gloss (str "they were being " perfect-participle) :part_of_speech "verb" :person 3 :number "plural" :tense "imperfect" :voice "passive" :mood "subjunctive" :lexeme_id (ll df)}])

(defn infinitives [{:keys [infinitive infinitive-stem first-person-present-sg-gloss df perfect-stem perfect-stem-minus-v perfect-participle]}]
  [{:wordform  infinitive :gloss (str "to " first-person-present-sg-gloss) :part_of_speech "verb" :tense "present" :voice "active" :mood "infinitive" :lexeme_id (ll df)}
   {:wordform  (str perfect-stem "isse") :gloss (str "to have " perfect-participle) :part_of_speech "verb" :tense "perfect" :voice "active" :mood "infinitive" :lexeme_id (ll df)}
   {:wordform  (str perfect-stem-minus-v "sse") :gloss (str "to have " perfect-participle) :part_of_speech "verb" :tense "perfect" :voice "active" :mood "infinitive" :lexeme_id (ll df)}
   ;; skipping future active infinitive since it requires a helper word
   {:wordform  (str infinitive-stem "ī") :gloss (str "to be " perfect-participle) :part_of_speech "verb" :tense "present" :voice "passive" :mood "infinitive" :lexeme_id (ll df)}])

(defn imperatives [{:keys [present-stem first-person-present-sg-gloss df perfect-participle]}]
  ;; (skipping future imperative for now since it's rare)
  [{:wordform  (str present-stem "ā") :gloss (str first-person-present-sg-gloss "!") :part_of_speech "verb" :tense "present" :voice "active" :number "singular" :mood "imperative" :lexeme_id (ll df)}
   {:wordform  (str present-stem "āte") :gloss (str first-person-present-sg-gloss "!") :part_of_speech "verb" :tense "present" :voice "active" :number "plural" :mood "imperative" :lexeme_id (ll df)}
   {:wordform  (str present-stem "āre") :gloss (str "be "perfect-participle "!") :part_of_speech "verb" :tense "present" :voice "passive" :number "singular" :mood "imperative" :lexeme_id (ll df)}
   {:wordform  (str present-stem "āminī") :gloss (str "be "perfect-participle "!") :part_of_speech "verb" :tense "present" :voice "passive" :number "plural" :mood "imperative" :lexeme_id (ll df)}])

(defn present-active-participles-m [{:keys [present-stem present-participle df]}]
  [{:wordform (str present-stem "āns") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "masculine" :number "singular" :case_ "nominative" :lexeme_id (ll df)}
   {:wordform (str present-stem "antis") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "masculine" :number "singular" :case_ "genitive" :lexeme_id (ll df)}
   {:wordform (str present-stem "antī") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "masculine" :number "singular" :case_ "dative" :lexeme_id (ll df)}
   {:wordform (str present-stem "antem") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "masculine" :number "singular" :case_ "accusative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ante") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "masculine" :number "singular" :case_ "ablative" :lexeme_id (ll df)}
   {:wordform (str present-stem "antēs") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "masculine" :number "plural" :case_ "nominative" :lexeme_id (ll df)}
   {:wordform (str present-stem "antium") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "masculine" :number "plural" :case_ "genitive" :lexeme_id (ll df)}
   {:wordform (str present-stem "antibus") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "masculine" :number "plural" :case_ "dative" :lexeme_id (ll df)}
   {:wordform (str present-stem "antēs") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "masculine" :number "plural" :case_ "accusative" :lexeme_id (ll df)}
   {:wordform (str present-stem "antibus") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "masculine" :number "plural" :case_ "ablative" :lexeme_id (ll df)}])

(defn present-active-participles-f [{:keys [present-stem present-participle df]}]
  [{:wordform (str present-stem "āns") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "feminine" :number "singular" :case_ "nominative" :lexeme_id (ll df)}
   {:wordform (str present-stem "antis") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "feminine" :number "singular" :case_ "genitive" :lexeme_id (ll df)}
   {:wordform (str present-stem "antī") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "feminine" :number "singular" :case_ "dative" :lexeme_id (ll df)}
   {:wordform (str present-stem "antem") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "feminine" :number "singular" :case_ "accusative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ante") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "feminine" :number "singular" :case_ "ablative" :lexeme_id (ll df)}
   {:wordform (str present-stem "antēs") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "feminine" :number "plural" :case_ "nominative" :lexeme_id (ll df)}
   {:wordform (str present-stem "antium") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "feminine" :number "plural" :case_ "genitive" :lexeme_id (ll df)}
   {:wordform (str present-stem "antibus") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "feminine" :number "plural" :case_ "dative" :lexeme_id (ll df)}
   {:wordform (str present-stem "antēs") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "feminine" :number "plural" :case_ "accusative" :lexeme_id (ll df)}
   {:wordform (str present-stem "antibus") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "feminine" :number "plural" :case_ "ablative" :lexeme_id (ll df)}])

(defn present-active-participles-n [{:keys [present-stem present-participle df]}]
  [{:wordform (str present-stem "āns") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "neuter" :number "singular" :case_ "nominative" :lexeme_id (ll df)}
   {:wordform (str present-stem "antis") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "neuter" :number "singular" :case_ "genitive" :lexeme_id (ll df)}
   {:wordform (str present-stem "antī") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "neuter" :number "singular" :case_ "dative" :lexeme_id (ll df)}
   {:wordform (str present-stem "āns") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "neuter" :number "singular" :case_ "accusative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ante") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "neuter" :number "singular" :case_ "ablative" :lexeme_id (ll df)}
   {:wordform (str present-stem "antia") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "neuter" :number "plural" :case_ "nominative" :lexeme_id (ll df)}
   {:wordform (str present-stem "antium") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "neuter" :number "plural" :case_ "genitive" :lexeme_id (ll df)}
   {:wordform (str present-stem "antibus") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "neuter" :number "plural" :case_ "dative" :lexeme_id (ll df)}
   {:wordform (str present-stem "antia") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "neuter" :number "plural" :case_ "accusative" :lexeme_id (ll df)}
   {:wordform (str present-stem "antibus") :gloss present-participle :part_of_speech "participle" :tense "present" :voice "active" :mood "indicative" :gender "neuter" :number "plural" :case_ "ablative" :lexeme_id (ll df)}])

(defn future-passive-participles-m [{:keys [present-stem present-participle df]}]
  [{:wordform (str present-stem "andus") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "masculine" :number "singular" :case_ "nominative" :lexeme_id (ll df)}
   {:wordform (str present-stem "andī") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "masculine" :number "singular" :case_ "genitive" :lexeme_id (ll df)}
   {:wordform (str present-stem "andō") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "masculine" :number "singular" :case_ "dative" :lexeme_id (ll df)}
   {:wordform (str present-stem "andum") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "masculine" :number "singular" :case_ "accusative" :lexeme_id (ll df)}
   {:wordform (str present-stem "andō") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "masculine" :number "singular" :case_ "ablative" :lexeme_id (ll df)}
   {:wordform (str present-stem "andī") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "masculine" :number "plural" :case_ "nominative" :lexeme_id (ll df)}
   {:wordform (str present-stem "andōrum") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "masculine" :number "plural" :case_ "genitive" :lexeme_id (ll df)}
   {:wordform (str present-stem "andīs") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "masculine" :number "plural" :case_ "dative" :lexeme_id (ll df)}
   {:wordform (str present-stem "andōs") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "masculine" :number "plural" :case_ "accusative" :lexeme_id (ll df)}
   {:wordform (str present-stem "andīs") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "masculine" :number "plural" :case_ "ablative" :lexeme_id (ll df)}])

(defn future-passive-participles-f [{:keys [present-stem present-participle df]}]
  [{:wordform (str present-stem "anda") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "feminine" :number "singular" :case_ "nominative" :lexeme_id (ll df)}
   {:wordform (str present-stem "andae") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "feminine" :number "singular" :case_ "genitive" :lexeme_id (ll df)}
   {:wordform (str present-stem "andae") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "feminine" :number "singular" :case_ "dative" :lexeme_id (ll df)}
   {:wordform (str present-stem "andam") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "feminine" :number "singular" :case_ "accusative" :lexeme_id (ll df)}
   {:wordform (str present-stem "andā") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "feminine" :number "singular" :case_ "ablative" :lexeme_id (ll df)}
   {:wordform (str present-stem "andae") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "feminine" :number "plural" :case_ "nominative" :lexeme_id (ll df)}
   {:wordform (str present-stem "andārum") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "feminine" :number "plural" :case_ "genitive" :lexeme_id (ll df)}
   {:wordform (str present-stem "andīs") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "feminine" :number "plural" :case_ "dative" :lexeme_id (ll df)}
   {:wordform (str present-stem "andās") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "feminine" :number "plural" :case_ "accusative" :lexeme_id (ll df)}
   {:wordform (str present-stem "andīs") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "feminine" :number "plural" :case_ "ablative" :lexeme_id (ll df)}])

(defn future-passive-participles-n [{:keys [present-stem present-participle df]}]
  [{:wordform (str present-stem "andum") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "neuter" :number "singular" :case_ "nominative" :lexeme_id (ll df)}
   {:wordform (str present-stem "andī") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "neuter" :number "singular" :case_ "genitive" :lexeme_id (ll df)}
   {:wordform (str present-stem "andō") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "neuter" :number "singular" :case_ "dative" :lexeme_id (ll df)}
   {:wordform (str present-stem "andum") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "neuter" :number "singular" :case_ "accusative" :lexeme_id (ll df)}
   {:wordform (str present-stem "andō") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "neuter" :number "singular" :case_ "ablative" :lexeme_id (ll df)}
   {:wordform (str present-stem "anda") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "neuter" :number "plural" :case_ "nominative" :lexeme_id (ll df)}
   {:wordform (str present-stem "andōrum") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "neuter" :number "plural" :case_ "genitive" :lexeme_id (ll df)}
   {:wordform (str present-stem "andīs") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "neuter" :number "plural" :case_ "dative" :lexeme_id (ll df)}
   {:wordform (str present-stem "anda") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "neuter" :number "plural" :case_ "accusative" :lexeme_id (ll df)}
   {:wordform (str present-stem "andīs") :gloss present-participle :part_of_speech "participle" :tense "future" :voice "passive" :mood "indicative" :gender "neuter" :number "plural" :case_ "ablative" :lexeme_id (ll df)}])

(defn perfect-passive-participles-m [{:keys [participial-stem df perfect-participle]}]
  [{:wordform (str participial-stem "us") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "masculine" :number "singular" :case_ "nominative" :lexeme_id (ll df)}
   {:wordform (str participial-stem "ī") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "masculine" :number "singular" :case_ "genitive" :lexeme_id (ll df)}
   {:wordform (str participial-stem "ō") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "masculine" :number "singular" :case_ "dative" :lexeme_id (ll df)}
   {:wordform (str participial-stem "um") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "masculine" :number "singular" :case_ "accusative" :lexeme_id (ll df)}
   {:wordform (str participial-stem "ō") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "masculine" :number "singular" :case_ "ablative" :lexeme_id (ll df)}
   {:wordform (str participial-stem "ī") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "masculine" :number "plural" :case_ "nominative" :lexeme_id (ll df)}
   {:wordform (str participial-stem "ōrum") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "masculine" :number "plural" :case_ "genitive" :lexeme_id (ll df)}
   {:wordform (str participial-stem "īs") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "masculine" :number "plural" :case_ "dative" :lexeme_id (ll df)}
   {:wordform (str participial-stem "ōs") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "masculine" :number "plural" :case_ "accusative" :lexeme_id (ll df)}
   {:wordform (str participial-stem "īs") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "masculine" :number "plural" :case_ "ablative" :lexeme_id (ll df)}])

(defn perfect-passive-participles-f [{:keys [participial-stem df perfect-participle]}]
  [{:wordform (str participial-stem "a") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "feminine" :number "singular" :case_ "nominative" :lexeme_id (ll df)}
   {:wordform (str participial-stem "ae") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "feminine" :number "singular" :case_ "genitive" :lexeme_id (ll df)}
   {:wordform (str participial-stem "ae") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "feminine" :number "singular" :case_ "dative" :lexeme_id (ll df)}
   {:wordform (str participial-stem "am") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "feminine" :number "singular" :case_ "accusative" :lexeme_id (ll df)}
   {:wordform (str participial-stem "ā") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "feminine" :number "singular" :case_ "ablative" :lexeme_id (ll df)}
   {:wordform (str participial-stem "ae") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "feminine" :number "plural" :case_ "nominative" :lexeme_id (ll df)}
   {:wordform (str participial-stem "ārum") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "feminine" :number "plural" :case_ "genitive" :lexeme_id (ll df)}
   {:wordform (str participial-stem "īs") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "feminine" :number "plural" :case_ "dative" :lexeme_id (ll df)}
   {:wordform (str participial-stem "ās") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "feminine" :number "plural" :case_ "accusative" :lexeme_id (ll df)}
   {:wordform (str participial-stem "īs") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "feminine" :number "plural" :case_ "ablative" :lexeme_id (ll df)}])

(defn perfect-passive-participles-n [{:keys [participial-stem df perfect-participle]}]
  [{:wordform (str participial-stem "um") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "neuter" :number "singular" :case_ "nominative" :lexeme_id (ll df)}
   {:wordform (str participial-stem "ī") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "neuter" :number "singular" :case_ "genitive" :lexeme_id (ll df)}
   {:wordform (str participial-stem "ō") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "neuter" :number "singular" :case_ "dative" :lexeme_id (ll df)}
   {:wordform (str participial-stem "um") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "neuter" :number "singular" :case_ "accusative" :lexeme_id (ll df)}
   {:wordform (str participial-stem "ō") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "neuter" :number "singular" :case_ "ablative" :lexeme_id (ll df)}
   {:wordform (str participial-stem "a") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "neuter" :number "plural" :case_ "nominative" :lexeme_id (ll df)}
   {:wordform (str participial-stem "ōrum") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "neuter" :number "plural" :case_ "genitive" :lexeme_id (ll df)}
   {:wordform (str participial-stem "īs") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "neuter" :number "plural" :case_ "dative" :lexeme_id (ll df)}
   {:wordform (str participial-stem "a") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "neuter" :number "plural" :case_ "accusative" :lexeme_id (ll df)}
   {:wordform (str participial-stem "īs") :gloss (str "having been " perfect-participle) :part_of_speech "participle" :tense "perfect" :voice "passive" :mood "indicative" :gender "neuter" :number "plural" :case_ "ablative" :lexeme_id (ll df)}])

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

(defn get-verb-forms-āre* [{:keys [first-person-present infinitive first-person-perfect supine first-person-present-sg-gloss third-person-present-sg-gloss first-person-perfect-sg-gloss perfect-participle present-participle] :as args}]
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
     (future-active-participles-n args)
     )))

(defn present-active-indicative-dep [{:keys [first-person-present first-person-present-sg-gloss present-stem third-person-present-sg-gloss df]}]
  [{:wordform first-person-present :gloss (str "I " first-person-present-sg-gloss) :part_of_speech "verb" :person 1 :number "singular" :tense "present" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "āris") :gloss (str "you " first-person-present-sg-gloss) :part_of_speech "verb" :person 2 :number "singular" :tense "present" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ātur") :gloss (str "he/she/it " third-person-present-sg-gloss) :part_of_speech "verb" :person 3 :number "singular" :tense "present" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "āmur") :gloss (str "we " first-person-present-sg-gloss) :part_of_speech "verb" :person 1 :number "plural" :tense "present" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "āminī") :gloss (str "you " first-person-present-sg-gloss) :part_of_speech "verb" :person 2 :number "plural" :tense "present" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "antur") :gloss (str "they " first-person-present-sg-gloss) :part_of_speech "verb" :person 3 :number "plural" :tense "present" :voice "active" :mood "indicative" :lexeme_id (ll df)}])

(defn imperfect-active-indicative-dep [{:keys [first-person-present first-person-present-sg-gloss present-stem third-person-present-sg-gloss df]}]
  [{:wordform (str present-stem "ābar") :gloss (str "I " first-person-present-sg-gloss) :part_of_speech "verb" :person 1 :number "singular" :tense "imperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ābāris") :gloss (str "you " first-person-present-sg-gloss) :part_of_speech "verb" :person 2 :number "singular" :tense "imperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ābātur") :gloss (str "he/she/it " third-person-present-sg-gloss) :part_of_speech "verb" :person 3 :number "singular" :tense "imperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ābāmur") :gloss (str "we " first-person-present-sg-gloss) :part_of_speech "verb" :person 1 :number "plural" :tense "imperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ābāminī") :gloss (str "you " first-person-present-sg-gloss) :part_of_speech "verb" :person 2 :number "plural" :tense "imperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ābantur") :gloss (str "they " first-person-present-sg-gloss) :part_of_speech "verb" :person 3 :number "plural" :tense "imperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}])

(defn future-active-indicative-dep [{:keys [first-person-present first-person-present-sg-gloss present-stem third-person-present-sg-gloss df]}]
  [{:wordform (str present-stem "ābor") :gloss (str "I will " first-person-present-sg-gloss) :part_of_speech "verb" :person 1 :number "singular" :tense "future" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "āberis") :gloss (str "you will " first-person-present-sg-gloss) :part_of_speech "verb" :person 2 :number "singular" :tense "future" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ābitur") :gloss (str "he/she/it will " third-person-present-sg-gloss) :part_of_speech "verb" :person 3 :number "singular" :tense "future" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ābimur") :gloss (str "we will " first-person-present-sg-gloss) :part_of_speech "verb" :person 1 :number "plural" :tense "future" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ābiminī") :gloss (str "you will " first-person-present-sg-gloss) :part_of_speech "verb" :person 2 :number "plural" :tense "future" :voice "active" :mood "indicative" :lexeme_id (ll df)}
   {:wordform (str present-stem "ābuntur") :gloss (str "they will " first-person-present-sg-gloss) :part_of_speech "verb" :person 3 :number "plural" :tense "future" :voice "active" :mood "indicative" :lexeme_id (ll df)}])

(defn present-active-subjunctive-dep [{:keys [first-person-present first-person-present-sg-gloss present-stem third-person-present-sg-gloss df]}]
  [{:wordform (str present-stem "er") :gloss (str "I " first-person-present-sg-gloss) :part_of_speech "verb" :person 1 :number "singular" :tense "present" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str present-stem "ēris") :gloss (str "you " first-person-present-sg-gloss) :part_of_speech "verb" :person 2 :number "singular" :tense "present" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str present-stem "ētur") :gloss (str "he/she/it " third-person-present-sg-gloss) :part_of_speech "verb" :person 3 :number "singular" :tense "present" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str present-stem "ēmur") :gloss (str "we " first-person-present-sg-gloss) :part_of_speech "verb" :person 1 :number "plural" :tense "present" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str present-stem "ēminī") :gloss (str "you " first-person-present-sg-gloss) :part_of_speech "verb" :person 2 :number "plural" :tense "present" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str present-stem "entur") :gloss (str "they " first-person-present-sg-gloss) :part_of_speech "verb" :person 3 :number "plural" :tense "present" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}])

(defn imperfect-active-subjunctive-dep [{:keys [first-person-perfect-sg-gloss present-stem df]}]
  [{:wordform (str present-stem "ārer") :gloss (str "I " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 1 :number "singular" :tense "imperfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str present-stem "ārēris") :gloss (str "you " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 2 :number "singular" :tense "imperfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str present-stem "ārētur") :gloss (str "he/she/it " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 3 :number "singular" :tense "imperfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str present-stem "ārēmur") :gloss (str "we " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 1 :number "plural" :tense "imperfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str present-stem "ārēminī") :gloss (str "you " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 2 :number "plural" :tense "imperfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}
   {:wordform (str present-stem "ārentur") :gloss (str "they " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 3 :number "plural" :tense "imperfect" :voice "active" :mood "subjunctive" :lexeme_id (ll df)}])

(defn infinitives-dep [{:keys [infinitive infinitive-stem first-person-present-sg-gloss df perfect-stem perfect-participle]}]
  [{:wordform  infinitive :gloss (str "to " first-person-present-sg-gloss) :part_of_speech "verb" :tense "present" :voice "active" :mood "infinitive" :lexeme_id (ll df)}])

(defn imperatives-dep [{:keys [present-stem first-person-present-sg-gloss df perfect-participle]}]
  [{:wordform  (str present-stem "āre") :gloss (str first-person-present-sg-gloss "!") :part_of_speech "verb" :tense "present" :voice "active" :number "singular" :mood "imperative" :lexeme_id (ll df)}
   {:wordform  (str present-stem "āminī") :gloss (str first-person-present-sg-gloss "!") :part_of_speech "verb" :tense "present" :voice "active" :number "plural" :mood "imperative" :lexeme_id (ll df)}])

(defn get-verb-forms-āre*-dep [{:keys [first-person-present infinitive first-person-perfect supine first-person-present-sg-gloss third-person-present-sg-gloss first-person-perfect-sg-gloss perfect-participle present-participle] :as args}]
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
     )))
