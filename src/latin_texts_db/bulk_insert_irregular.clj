(ns latin-texts-db.bulk-insert-irregular
  (:require
   [next.jdbc :as jdbc]
   [migratus.core :as migratus]
   [honey.sql :as sql]
   [honey.sql.helpers :as h]
   [latin-texts-db.migrations.basic-tables]
   [latin-texts-db.db :refer [ds do!]]))

(def meanings-to-insert
  [{:wordform "est" :gloss "he/she/it is" :part_of_speech "verb" :person 3 :number "singular" :tense "present" :mood "indicative" :voice "active"}
   {:wordform "sunt" :gloss "they are" :part_of_speech "verb" :person 3 :number "plural" :tense "present" :mood "indicative" :voice "active"}])

(defn insert-meaning! [meaning-values]
  (let [existing-match (do! {:select [:meaning_id]
                             :from :meanings
                             :where [:= :wordform (:wordform meaning-values)]})
        match-id (:meanings/meaning_id (first existing-match))]
    (if match-id
      (println "Meaning's wordform is already present in the database: " match-id)
      (do! {:insert-into [:meanings]
            :values [meaning-values]}))))

(defn insert-all! []
  (doseq [meaning meanings-to-insert]
    (insert-meaning! meaning)))
