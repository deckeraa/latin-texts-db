(ns latin-texts-db.bulk-verb-insert
  (:require
   [next.jdbc :as jdbc]
   [migratus.core :as migratus]
   [honey.sql :as sql]
   [honey.sql.helpers :as h]
   [latin-texts-db.migrations.basic-tables]
   [latin-texts-db.db :refer [ds do! ll]]
   [latin-texts-db.bulk-verb-insert-one :refer [get-verb-forms-āre]]
   [latin-texts-db.bulk-verb-insert-two :refer [get-verb-forms-ēre]]
   [latin-texts-db.bulk-verb-insert-three :refer [get-verb-forms-ere]]
   [latin-texts-db.bulk-verb-insert-three-i :refer [get-verb-forms-ere-i]]))

(defn quickprint [wordform]
  (clojure.string/join " " [(:wordform wordform) (:gloss wordform) (:gender wordform)]))

(defn get-conjugation [first-person-present infinitive]
  (cond
    (clojure.string/ends-with? infinitive "āre") "1"
    (clojure.string/ends-with? infinitive "ēre") "2"
    (clojure.string/ends-with? infinitive "īre") "4"
    (clojure.string/ends-with? infinitive "ere")
    (if (clojure.string/ends-with? first-person-present "iō")
      "3i"
      "3")))

(defn get-verb-forms [dictionary-form first-person-present-sg-gloss third-person-present-sg-gloss first-person-perfect-sg-gloss present-participle]
  (let [[first-person-present infinitive first-person-perfect supine] (clojure.string/split dictionary-form #", ")
        conjugation (get-conjugation first-person-present infinitive)]
    (case conjugation
      "1" (get-verb-forms-āre first-person-present infinitive first-person-perfect supine first-person-present-sg-gloss third-person-present-sg-gloss first-person-perfect-sg-gloss present-participle)
      "2" (get-verb-forms-ēre first-person-present infinitive first-person-perfect supine first-person-present-sg-gloss third-person-present-sg-gloss first-person-perfect-sg-gloss present-participle)
      "3" (get-verb-forms-ere first-person-present infinitive first-person-perfect supine first-person-present-sg-gloss third-person-present-sg-gloss first-person-perfect-sg-gloss present-participle)
      "3i" (get-verb-forms-ere-i first-person-present infinitive first-person-perfect supine first-person-present-sg-gloss third-person-present-sg-gloss first-person-perfect-sg-gloss present-participle))))

(defn insert-verb-meaning! [meaning-values]
  (let [existing-match (do! {:select [:meaning_id]
                             :from :meanings
                             :where [:and
                                     [:= :gloss (:gloss meaning-values)]
                                     [:= :wordform (:wordform meaning-values)]
                                     [:= :case_ (:case_ meaning-values)]
                                     [:= :number (:number meaning-values)]
                                     [:= :gender (:gender meaning-values)]
                                     [:= :tense (:tense meaning-values)]
                                     [:= :voice (:voice meaning-values)]
                                     [:= :mood (:voice meaning-values)]]})
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

(def wordlist
  [["intrō, intrāre, intrāvī, intrātum" "enter" "enters" "entered" "entering"]
   ["ambulō, ambulāre, ambulāvī, ambulātum" "walk" "walks" "walked" "walking"]
   ["pulsō, pulsāre, pulsāvī, pulsātum" "hit" "hits" "hit" "hitting"]
   ["moneō, monēre, monuī, monitum" "warn" "warns" "warned" "warning"]
   ["agō, agere, ēgī, āctum" "carry out" "carried out" "carried out" "carrying out"]
   ["mergō, mergere, mersī, mersum" "plunge" "plunges" "plunged" "plunging"]
   ["capiō, capere, cēpī, captum" "seize" "seizes" "seized" "seizing"]
   ])

(defn insert-all! []
  (doseq [args wordlist]
    (let [meanings (apply get-verb-forms args)]
      (doseq [meaning meanings]
        (insert-verb-meaning! meaning))))
  ;; I __hit__, he _hit___, I __hit__, I am ___hit__-ing
  ;; (map quickprint (get-verb-forms ...)) to check you glosses before insertion
  
  ;; (get-verb-forms-āre "intrō" "intrāre" "intrāvī" "intrātum" "enter" "enters" "entered" "entering")
  ;; (get-verb-forms-āre "ambulō" "ambulāre" "ambulāvī" "ambulātum" "walk" "walks" "walked" "walking")
  ;; (get-verb-forms-āre "pulsō" "pulsāre" "pulsāvī" "pulsātum" "hit" "hits" "hit" "hitting")
  
  ;; (get-verb-forms "intrō, intrāre, intrāvī, intrātum" "enter" "enters" "entered" "entering")
  ;; (get-verb-forms "ambulō, ambulāre, ambulāvī, ambulātum" "walk" "walks" "walked" "walking")
  ;; (get-verb-forms "pulsō, pulsāre, pulsāvī, pulsātum" "hit" "hits" "hit" "hitting")
  )
