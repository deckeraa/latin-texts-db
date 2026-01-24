(ns latin-texts-db.bulk-verb-insert
  (:require
   [next.jdbc :as jdbc]
   [migratus.core :as migratus]
   [honey.sql :as sql]
   [honey.sql.helpers :as h]
   [latin-texts-db.migrations.basic-tables]
   [latin-texts-db.db :refer [ds do! ll]]))

(defn get-verb-forms-āre [first-person-present infinitive first-person-perfect supine first-person-present-sg-gloss third-person-present-sg-gloss first-person-perfect-sg-gloss present-participle]
  (let [present-stem (subs first-person-present 0 (dec (count first-person-present)))
        perfect-stem (subs first-person-perfect 0 (dec (count first-person-perfect)))
        perfect-stem-minus-v (subs perfect-stem 0 (dec (count perfect-stem))) ;; for -ve syncopation
        df (clojure.string/join ", " [first-person-present infinitive first-person-perfect supine])]
    ;; present active indicative
    [{:wordform first-person-present :gloss (str "I " first-person-present-sg-gloss) :part_of_speech "verb" :person 1 :number "singular" :tense "present" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str present-stem "ās") :gloss (str "you " first-person-present-sg-gloss) :part_of_speech "verb" :person 2 :number "singular" :tense "present" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str present-stem "at") :gloss (str "he/she/it " third-person-present-sg-gloss) :part_of_speech "verb" :person 3 :number "singular" :tense "present" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str present-stem "āmus") :gloss (str "we " first-person-present-sg-gloss) :part_of_speech "verb" :person 1 :number "plural" :tense "present" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str present-stem "ātis") :gloss (str "you " first-person-present-sg-gloss) :part_of_speech "verb" :person 2 :number "plural" :tense "present" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str present-stem "ant") :gloss (str "they " first-person-present-sg-gloss) :part_of_speech "verb" :person 3 :number "plural" :tense "present" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     ;; perfect active indicative
     {:wordform first-person-perfect :gloss (str "I " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 1 :number "singular" :tense "perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str perfect-stem "isti") :gloss (str "you " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 2 :number "singular" :tense "perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str perfect-stem "it") :gloss (str "he/she/it " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 3 :number "singular" :tense "perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str perfect-stem "imus") :gloss (str "we " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 1 :number "plural" :tense "perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str perfect-stem "istis") :gloss (str "you " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 2 :number "plural" :tense "perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str perfect-stem "ērunt") :gloss (str "they " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 3 :number "plural" :tense "perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str perfect-stem "ēre") :gloss (str "they " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 3 :number "plural" :tense "perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     ;; imperfect active indicative
     {:wordform (str present-stem "ābam") :gloss (str "I was " present-participle) :part_of_speech "verb" :person 1 :number "singular" :tense "imperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str present-stem "ābās") :gloss (str "you were " present-participle) :part_of_speech "verb" :person 2 :number "singular" :tense "imperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str present-stem "ābat") :gloss (str "he/she/it was " present-participle) :part_of_speech "verb" :person 3 :number "singular" :tense "imperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str present-stem "ābāmus") :gloss (str "we were " present-participle) :part_of_speech "verb" :person 1 :number "plural" :tense "imperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str present-stem "ābātis") :gloss (str "you were " present-participle) :part_of_speech "verb" :person 2 :number "plural" :tense "imperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str present-stem "ābant") :gloss (str "they were " present-participle) :part_of_speech "verb" :person 3 :number "plural" :tense "imperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     ;; future active indicative
     {:wordform (str present-stem "ābō") :gloss (str "I will " first-person-present-sg-gloss) :part_of_speech "verb" :person 1 :number "singular" :tense "future" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str present-stem "ābis") :gloss (str "you will " first-person-present-sg-gloss) :part_of_speech "verb" :person 2 :number "singular" :tense "future" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str present-stem "ābit") :gloss (str "he/she/it will " first-person-present-sg-gloss) :part_of_speech "verb" :person 3 :number "singular" :tense "future" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str present-stem "ābimus") :gloss (str "we will " first-person-present-sg-gloss) :part_of_speech "verb" :person 1 :number "plural" :tense "future" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str present-stem "ābitis") :gloss (str "you will " first-person-present-sg-gloss) :part_of_speech "verb" :person 2 :number "plural" :tense "future" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str present-stem "ābunt") :gloss (str "they will " first-person-present-sg-gloss) :part_of_speech "verb" :person 3 :number "plural" :tense "future" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     ;; pluperfect active indicative
     {:wordform (str perfect-stem "eram") :gloss (str "I had " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 1 :number "singular" :tense "pluperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str perfect-stem-minus-v "ram") :gloss (str "I had " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 1 :number "singular" :tense "pluperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str perfect-stem "erās") :gloss (str "you had " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 2 :number "singular" :tense "pluperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str perfect-stem-minus-v "rās") :gloss (str "you had " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 2 :number "singular" :tense "pluperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str perfect-stem "erat") :gloss (str "he/she/it had " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 3 :number "singular" :tense "pluperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str perfect-stem-minus-v "rat") :gloss (str "he/she/it had " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 3 :number "singular" :tense "pluperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str perfect-stem "erāmus") :gloss (str "we had " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 1 :number "plural" :tense "pluperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str perfect-stem-minus-v "rāmus") :gloss (str "we had " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 1 :number "plural" :tense "pluperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str perfect-stem "erātis") :gloss (str "you had " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 2 :number "plural" :tense "pluperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str perfect-stem-minus-v "rātis") :gloss (str "you had " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 2 :number "plural" :tense "pluperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str perfect-stem "erant") :gloss (str "they had " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 3 :number "plural" :tense "pluperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str perfect-stem-minus-v "rant") :gloss (str "they had " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 3 :number "plural" :tense "pluperfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     ;; future perfect indicative
     {:wordform (str perfect-stem "erō") :gloss (str "I will have " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 1 :number "singular" :tense "future-perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str perfect-stem-minus-v "rō") :gloss (str "I will have " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 1 :number "singular" :tense "future-perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str perfect-stem "eris") :gloss (str "you will have " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 2 :number "singular" :tense "future-perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str perfect-stem-minus-v "ris") :gloss (str "you will have " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 2 :number "singular" :tense "future-perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str perfect-stem "erit") :gloss (str "he/she/it will have " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 3 :number "singular" :tense "future-perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str perfect-stem-minus-v "rit") :gloss (str "he/she/it will have " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 3 :number "singular" :tense "future-perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str perfect-stem "erimus") :gloss (str "we will have " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 1 :number "plural" :tense "future-perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str perfect-stem-minus-v "rimus") :gloss (str "we will have " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 1 :number "plural" :tense "future-perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str perfect-stem "eritis") :gloss (str "you will have " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 2 :number "plural" :tense "future-perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str perfect-stem-minus-v "ritis") :gloss (str "you will have " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 2 :number "plural" :tense "future-perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str perfect-stem "erint") :gloss (str "they will have " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 3 :number "plural" :tense "future-perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     {:wordform (str perfect-stem-minus-v "rint") :gloss (str "they will have " first-person-perfect-sg-gloss) :part_of_speech "verb" :person 3 :number "plural" :tense "future-perfect" :voice "active" :mood "indicative" :lexeme_id (ll df)}
     ]))


(defn insert-verb-meaning! [meaning-values]
  (let [existing-match (do! {:select [:meaning_id]
                             :from :meanings
                             :where [:and
                                     [:= :gloss (:gloss meaning-values)]
                                     [:= :wordform (:wordform meaning-values)]
                                     [:= :case_ (name (:case_ meaning-values))]
                                     [:= :number (name (:number meaning-values))]
                                     [:= :gender (name (:gender meaning-values))]]})
        match-id (:meanings/meaning_id (first existing-match))]
    (if match-id
      (println "Meaning is already present in the database: " match-id)
      (do! {:insert-into [:meanings]
            :values [meaning-values]}))))

;; (defn insert-verb-meanings! [dict-entry gender sn-gloss sg-gloss pn-gloss pg-gloss]
;;   (let [[nom gen] (clojure.string/split dict-entry #", ")
;;         declension (genitive->declension gen)
;;         meanings (case declension
;;                    1 (get-noun-forms-first-declension nom gen gender sn-gloss sg-gloss pn-gloss pg-gloss)
;;                    2 (case gender 
;;                        "masculine" (get-noun-forms-second-declension-mf nom gen "masculine" sn-gloss sg-gloss pn-gloss pg-gloss)
;;                        "neuter"    (get-noun-forms-second-declension-neuter    nom gen      sn-gloss sg-gloss pn-gloss pg-gloss)
;;                        "feminine"  (get-noun-forms-second-declension-mf nom gen "feminine"  sn-gloss sg-gloss pn-gloss pg-gloss)
;;                        ))]
;;     (doseq [meaning meanings]
;;       (insert-verb-meaning! meaning)))
;;   )

(defn insert-all! []
  (get-verb-forms-āre "intrō" "intrāre" "intrāvī" "intrātum" "enter" "enters" "entered" "entering")
  )
