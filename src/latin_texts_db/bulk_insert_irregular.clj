(ns latin-texts-db.bulk-insert-irregular
  (:require
   [next.jdbc :as jdbc]
   [migratus.core :as migratus]
   [honey.sql :as sql]
   [honey.sql.helpers :as h]
   [latin-texts-db.migrations.basic-tables]
   [latin-texts-db.db :refer [ds do! ll]]))

(def meanings-to-insert
  [;; sum, es, ...
   {:wordform "sum" :gloss "I am" :part_of_speech "verb" :person 1 :number "singular" :tense "present" :mood "indicative" :voice "active" :lexeme_id (ll "sum, esse, fuī, futūrum")}
   {:wordform "es" :gloss "you are" :part_of_speech "verb" :person 2 :number "singular" :tense "present" :mood "indicative" :voice "active" :lexeme_id (ll "sum, esse, fuī, futūrum")}
   {:wordform "est" :gloss "he/she/it is" :part_of_speech "verb" :person 3 :number "singular" :tense "present" :mood "indicative" :voice "active" :lexeme_id (ll "sum, esse, fuī, futūrum")}
   {:wordform "sumus" :gloss "we are" :part_of_speech "verb" :person 1 :number "plural" :tense "present" :mood "indicative" :voice "active" :lexeme_id (ll "sum, esse, fuī, futūrum")}
   {:wordform "estis" :gloss "you are" :part_of_speech "verb" :person 2 :number "plural" :tense "present" :mood "indicative" :voice "active" :lexeme_id (ll "sum, esse, fuī, futūrum")}
   {:wordform "sunt" :gloss "they are" :part_of_speech "verb" :person 3 :number "plural" :tense "present" :mood "indicative" :voice "active" :lexeme_id (ll "sum, esse, fuī, futūrum")}
   ;; hic, haec, hoc
   {:wordform "hic" :gloss "this" :part_of_speech "noun" :number "singular" :gender "masculine" :case_ "nominative" :lexeme_id (ll "hic, haec, hoc")}
   {:wordform "huius" :gloss "of this" :part_of_speech "noun" :number "singular" :gender "masculine" :case_ "genitive" :lexeme_id (ll "hic, haec, hoc")}
   {:wordform "huic" :gloss "this" :part_of_speech "noun" :number "singular" :gender "masculine" :case_ "dative" :lexeme_id (ll "hic, haec, hoc")}
   {:wordform "hunc" :gloss "this" :part_of_speech "noun" :number "singular" :gender "masculine" :case_ "accusative" :lexeme_id (ll "hic, haec, hoc")}
   {:wordform "hōc" :gloss "this" :part_of_speech "noun" :number "singular" :gender "masculine" :case_ "ablative" :lexeme_id (ll "hic, haec, hoc")}

   {:wordform "haec" :gloss "this" :part_of_speech "noun" :number "singular" :gender "feminine" :case_ "nominative" :lexeme_id (ll "hic, haec, hoc")}
   {:wordform "huius" :gloss "of this" :part_of_speech "noun" :number "singular" :gender "feminine" :case_ "genitive" :lexeme_id (ll "hic, haec, hoc")}
   {:wordform "huic" :gloss "this" :part_of_speech "noun" :number "singular" :gender "feminine" :case_ "dative" :lexeme_id (ll "hic, haec, hoc")}
   {:wordform "hanc" :gloss "this" :part_of_speech "noun" :number "singular" :gender "feminine" :case_ "accusative" :lexeme_id (ll "hic, haec, hoc")}
   {:wordform "hāc" :gloss "this" :part_of_speech "noun" :number "singular" :gender "feminine" :case_ "ablative" :lexeme_id (ll "hic, haec, hoc")}

   {:wordform "hoc" :gloss "this" :part_of_speech "noun" :number "singular" :gender "neuter" :case_ "nominative" :lexeme_id (ll "hic, haec, hoc")}
   {:wordform "huius" :gloss "of this" :part_of_speech "noun" :number "singular" :gender "neuter" :case_ "genitive" :lexeme_id (ll "hic, haec, hoc")}
   {:wordform "huic" :gloss "this" :part_of_speech "noun" :number "singular" :gender "neuter" :case_ "dative" :lexeme_id (ll "hic, haec, hoc")}
   {:wordform "hoc" :gloss "this" :part_of_speech "noun" :number "singular" :gender "neuter" :case_ "accusative" :lexeme_id (ll "hic, haec, hoc")}
   {:wordform "hōc" :gloss "this" :part_of_speech "noun" :number "singular" :gender "neuter" :case_ "ablative" :lexeme_id (ll "hic, haec, hoc")}

   {:wordform "hī" :gloss "these" :part_of_speech "noun" :number "singular" :gender "masculine" :case_ "nominative" :lexeme_id (ll "hic, haec, hoc")}
   {:wordform "hōrum" :gloss "of these" :part_of_speech "noun" :number "singular" :gender "masculine" :case_ "genitive" :lexeme_id (ll "hic, haec, hoc")}
   {:wordform "hīs" :gloss "these" :part_of_speech "noun" :number "singular" :gender "masculine" :case_ "dative" :lexeme_id (ll "hic, haec, hoc")}
   {:wordform "hōs" :gloss "these" :part_of_speech "noun" :number "singular" :gender "masculine" :case_ "accusative" :lexeme_id (ll "hic, haec, hoc")}
   {:wordform "hīs" :gloss "these" :part_of_speech "noun" :number "singular" :gender "masculine" :case_ "ablative" :lexeme_id (ll "hic, haec, hoc")}

   {:wordform "hae" :gloss "these" :part_of_speech "noun" :number "singular" :gender "feminine" :case_ "nominative" :lexeme_id (ll "hic, haec, hoc")}
   {:wordform "hārum" :gloss "of these" :part_of_speech "noun" :number "singular" :gender "feminine" :case_ "genitive" :lexeme_id (ll "hic, haec, hoc")}
   {:wordform "hīs" :gloss "these" :part_of_speech "noun" :number "singular" :gender "feminine" :case_ "dative" :lexeme_id (ll "hic, haec, hoc")}
   {:wordform "hās" :gloss "these" :part_of_speech "noun" :number "singular" :gender "feminine" :case_ "accusative" :lexeme_id (ll "hic, haec, hoc")}
   {:wordform "hīs" :gloss "these" :part_of_speech "noun" :number "singular" :gender "feminine" :case_ "ablative" :lexeme_id (ll "hic, haec, hoc")}

   {:wordform "haec" :gloss "these" :part_of_speech "noun" :number "singular" :gender "neuter" :case_ "nominative" :lexeme_id (ll "hic, haec, hoc")}
   {:wordform "hōrum" :gloss "of these" :part_of_speech "noun" :number "singular" :gender "neuter" :case_ "genitive" :lexeme_id (ll "hic, haec, hoc")}
   {:wordform "hīs" :gloss "these" :part_of_speech "noun" :number "singular" :gender "neuter" :case_ "dative" :lexeme_id (ll "hic, haec, hoc")}
   {:wordform "haec" :gloss "these" :part_of_speech "noun" :number "singular" :gender "neuter" :case_ "accusative" :lexeme_id (ll "hic, haec, hoc")}
   {:wordform "hīs" :gloss "these" :part_of_speech "noun" :number "singular" :gender "neuter" :case_ "ablative" :lexeme_id (ll "hic, haec, hoc")}
   ])

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
