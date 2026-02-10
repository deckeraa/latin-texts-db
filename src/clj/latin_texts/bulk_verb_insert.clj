(ns latin-texts.bulk-verb-insert
  (:require
   [next.jdbc :as jdbc]
   [migratus.core :as migratus]
   [honey.sql :as sql]
   [honey.sql.helpers :as h]
   [latin-texts.migrations.basic-tables]
   [latin-texts.db :refer [ds do! ll]]
   [latin-texts.bulk-verb-insert-one :refer [get-verb-forms-āre get-verb-forms-āre*]]
   [latin-texts.bulk-verb-insert-two :refer [get-verb-forms-ēre get-verb-forms-ēre* get-verb-forms-ēre*-dep]]
   [latin-texts.bulk-verb-insert-three :refer [get-verb-forms-ere]]
   [latin-texts.bulk-verb-insert-three-i :refer [get-verb-forms-ere-i]]
   [latin-texts.bulk-verb-insert-four :refer [get-verb-forms-īre]]))

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

(defn get-conjugation-dep [first-person-present infinitive]
  (cond
    (clojure.string/ends-with? infinitive "ārī") "1"
    (clojure.string/ends-with? infinitive "ērī") "2"
    (clojure.string/ends-with? infinitive "īrī") "4"
    (clojure.string/ends-with? infinitive "ī")
    (if (clojure.string/ends-with? first-person-present "ior")
      "3i"
      "3")))

(defn get-verb-forms [dictionary-form
                      first-person-present-sg-gloss
                      third-person-present-sg-gloss
                      first-person-perfect-sg-gloss
                      perfect-participle
                      present-participle]
  (let [[first-person-present infinitive first-person-perfect supine] (clojure.string/split dictionary-form #", ")
        conjugation (get-conjugation first-person-present infinitive)]
    (case conjugation
      "1" (get-verb-forms-āre first-person-present infinitive first-person-perfect supine first-person-present-sg-gloss third-person-present-sg-gloss first-person-perfect-sg-gloss perfect-participle present-participle)
      "2" (get-verb-forms-ēre first-person-present infinitive first-person-perfect supine first-person-present-sg-gloss third-person-present-sg-gloss first-person-perfect-sg-gloss perfect-participle present-participle)
      "3" (get-verb-forms-ere first-person-present infinitive first-person-perfect supine first-person-present-sg-gloss third-person-present-sg-gloss first-person-perfect-sg-gloss perfect-participle present-participle)
      "3i" (get-verb-forms-ere-i first-person-present infinitive first-person-perfect supine first-person-present-sg-gloss third-person-present-sg-gloss first-person-perfect-sg-gloss perfect-participle present-participle)
      "4" (get-verb-forms-īre first-person-present infinitive first-person-perfect supine first-person-present-sg-gloss third-person-present-sg-gloss first-person-perfect-sg-gloss perfect-participle present-participle))))

(defn get-verb-forms* [{:keys [dictionary-form
                               first-person-present-sg-gloss
                               third-person-present-sg-gloss
                               first-person-perfect-sg-gloss
                               present-participle
                               perfect-participle
                               ] :as args}]
  (let [[first-person-present infinitive first-person-perfect supine] (clojure.string/split dictionary-form #", ")
        args (assoc args
                    :first-person-present first-person-present
                    :infinitive infinitive
                    :first-person-perfect first-person-perfect
                    :supine supine)
        conjugation (get-conjugation first-person-present infinitive)]
    (case conjugation
      "1" (get-verb-forms-āre* args)
      "2" (get-verb-forms-ēre* args)
      "3" (get-verb-forms-ere first-person-present infinitive first-person-perfect supine first-person-present-sg-gloss third-person-present-sg-gloss first-person-perfect-sg-gloss perfect-participle present-participle)
      "3i" (get-verb-forms-ere-i first-person-present infinitive first-person-perfect supine first-person-present-sg-gloss third-person-present-sg-gloss first-person-perfect-sg-gloss perfect-participle present-participle)
      "4" (get-verb-forms-īre first-person-present infinitive first-person-perfect supine first-person-present-sg-gloss third-person-present-sg-gloss first-person-perfect-sg-gloss perfect-participle present-participle))))

(defn get-verb-forms*-dep [{:keys [dictionary-form
                               first-person-present-sg-gloss
                               third-person-present-sg-gloss
                               first-person-perfect-sg-gloss
                               present-participle
                               ] :as args}]
  (let [[first-person-present infinitive supine] (clojure.string/split dictionary-form #", ")
        args (assoc args
                    :first-person-present first-person-present
                    :infinitive infinitive
                    :supine supine)
        conjugation (get-conjugation-dep first-person-present infinitive)]
    (case conjugation
;;      "1" (get-verb-forms-āre* args)
      "2" (get-verb-forms-ēre*-dep args)
      ;; handler more cases
)))

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
  [
   ["adveniō, advenīre, advēnī, adventum" "arrive" "arrives" "arrived" "arrived" "arriving"]
   ["agō, agere, ēgī, āctum" "carry out" "carries out" "carried out"  "carried out" "carrying out"]
   ["ambulō, ambulāre, ambulāvī, ambulātum" "walk" "walks" "walked" "walked" "walking"]
   ["aperiō, aperīre, aperuī, apertum" "open" "opens" "opened" "opened" "opening"]
   ["audiō, audīre, audīvī, audītum" "hear" "hears" "heard" "heard" "hearing"]
   ["capiō, capere, cēpī, captum" "seize" "seizes" "seized" "seized" "seizing"]
   ["clāmō, clāmāre, clāmāvī, clāmātum" "shout" "shouts" "shouted" "shouted" "shouting"]
   ["cōnsīdō, cōnsīdere, cōnsēdī, cōnsessum" "sit down" "sits down" "sat down" "sat down" "sitting down"]
   ["dormiō, dormīre, dormīvī, dormītum" "sleep" "sleeps" "slept" "sleeped" "sleeping"]
   ["exclāmō, exclāmāre, exclāmāvī, exclāmātum" "cry out" "cries out" "cried out" "cried out" "crying out"]
   ["excitō, excitāre, excitāvī, excitātum" "wake/stir up" "wakes/stirs up" "woke/stirred up" "woken/stirred up" "waking/stirring up"]
   ["habeō, habēre, habuī, habitum" "have" "has" "had" "had" "having"]
   ["incipiō, incipere, incēpī, inceptum" "begin" "begins" "begans" "begun" "beginning"]
   ["interrogō, interrogāre, interrogāvī, interrogātum" "ask" "asks" "asked" "asked" "asking"]
   ["intrō, intrāre, intrāvī, intrātum" "enter" "enters" "entered" "entered" "entering"]
   ["lacrimō, lacrimāre, lacrimāvī, lacrimātum" "cry" "cries" "cried" "cried" "crying"]
   ["mergō, mergere, mersī, mersum" "plunge" "plunges" "plunged" "plunged" "plunging"]
   ["metuō, metuere, metuī, metūtum" "fear" "fears" "feared" "feared" "fearing"]
   ["moneō, monēre, monuī, monitum" "warn" "warns" "warned" "warned" "warning"]
   ["nōminō, nōmināre, nōmināvī, nōminātum" "name/call" "names/calls" "named/called" "named/called" "naming/calling"]
   ["pulsō, pulsāre, pulsāvī, pulsātum" "hit" "hits" "hit" "hit" "hitting"]
   ["pūniō, pūnīre, pūnīvī, pūnītum" "punish" "punishes" "punished" "punished" "punishing"]
   ["recitō, recitāre, recitāvī, recitātum" "recite" "recites" "recited" "recited" "reciting"]
   ["redeō, redīre, redivī, reditum" "return" "returns" "returned" "returned" "returning"]
   ["respondeō, respondēre, respondī, respōnsum" "respond" "responds" "responded" "responded" "responding"]
   ["salūtō, salūtāre, salūtāvī, salūtātum" "greet" "greets" "greeted" "greeted" "greeting"]
   ["sedeō, sedēre, sēdī, sessum" "sit" "sits" "sat" "sat" "sitting"]
   ["stō, stāre, stetī, statum" "stand" "stands" "stood" "stood" "standing"]
   ;; ["taceō, tacēre, tacuī, tacitum" "am silent" "is silent" ""]
   ["veniō, venīre, vēnī, ventum" "come" "comes" "came" "come" "coming"]
   ["verberō, verberāre, verberāvī, verberātum" "beat" "beats" "beat" "beaten" "beating"]
   ["videō, vidēre, vīdī, vīsum" "see" "sees" "saw" "seen" "seeing"]
   ["vigilō, vigilāre, vigilāvī, vigilātum" "wake/watch" "wakes/watches" "woke/watched" "awaken/watched" "awaking/watching"]
   ])

(defn insert-single-verb-from-args! [args]
  (let [meanings (apply get-verb-forms args)]
      (doseq [meaning meanings]
        (insert-verb-meaning! meaning))))

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
