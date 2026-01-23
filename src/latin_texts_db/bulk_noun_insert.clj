(ns latin-texts-db.bulk-noun-insert
  (:require
   [next.jdbc :as jdbc]
   [migratus.core :as migratus]
   [honey.sql :as sql]
   [honey.sql.helpers :as h]
   [latin-texts-db.migrations.basic-tables]
   [latin-texts-db.db :refer [ds do!]]))

(defn genitive->declension [gen]
  (cond
    (clojure.string/ends-with? gen "ae") 1
    (clojure.string/ends-with? gen "ī") 2
    (clojure.string/ends-with? gen "is") 3
    (clojure.string/ends-with? gen "ūs") 4
    (clojure.string/ends-with? gen "ēs") 5))

(defn get-noun-forms-first-declension [nom gen gender gloss]
  (let [stem (subs nom 0 (dec (count nom)))]
    [{:wordform (str stem "a") :gloss gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "nominative"}
     {:wordform (str stem "ae") :gloss gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "genitive"}
     {:wordform (str stem "ae") :gloss gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "dative"}
     {:wordform (str stem "am") :gloss gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "accusative"}
     {:wordform (str stem "ā") :gloss gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "ablative"}
     {:wordform (str stem "ae") :gloss gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "nominative"}
     {:wordform (str stem "ārum") :gloss gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "genitive"}
     {:wordform (str stem "īs") :gloss gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "dative"}
     {:wordform (str stem "ās") :gloss gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "accusative"}
     {:wordform (str stem "īs") :gloss gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "ablative"}
     ])
  )

(defn insert-noun-meaning! [meaning-values]
  (let [existing-match (do! {:select [:meaning_id]
                             :from :meanings
                             :where [:and
                                     [:= :wordform (:wordform meaning-values)]
                                     [:= :case_ (name (:case_ meaning-values))]
                                     [:= :number (name (:number meaning-values))]
                                     [:= :gender (name (:gender meaning-values))]]})
        match-id (:meanings/meaning_id (first existing-match))]
    (if match-id
      (println "Meaning is already present in the database: " match-id)
      (do! {:insert-into [:meanings]
            :values [meaning-values]}))))

(defn insert-noun-meanings! [dict-entry gender gloss]
  (let [[nom gen] (clojure.string/split dict-entry #", ")
        declension (genitive->declension gen)
        meanings (case declension
                   1 (get-noun-forms-first-declension nom gen gender gloss))]
    (doseq [meaning meanings]
      (insert-noun-meaning! meaning)))
  )
