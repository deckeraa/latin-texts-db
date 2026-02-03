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

   {:wordform "hī" :gloss "these" :part_of_speech "noun" :number "plural" :gender "masculine" :case_ "nominative" :lexeme_id (ll "hic, haec, hoc")}
   {:wordform "hōrum" :gloss "of these" :part_of_speech "noun" :number "plural" :gender "masculine" :case_ "genitive" :lexeme_id (ll "hic, haec, hoc")}
   {:wordform "hīs" :gloss "these" :part_of_speech "noun" :number "plural" :gender "masculine" :case_ "dative" :lexeme_id (ll "hic, haec, hoc")}
   {:wordform "hōs" :gloss "these" :part_of_speech "noun" :number "plural" :gender "masculine" :case_ "accusative" :lexeme_id (ll "hic, haec, hoc")}
   {:wordform "hīs" :gloss "these" :part_of_speech "noun" :number "plural" :gender "masculine" :case_ "ablative" :lexeme_id (ll "hic, haec, hoc")}

   {:wordform "hae" :gloss "these" :part_of_speech "noun" :number "plural" :gender "feminine" :case_ "nominative" :lexeme_id (ll "hic, haec, hoc")}
   {:wordform "hārum" :gloss "of these" :part_of_speech "noun" :number "plural" :gender "feminine" :case_ "genitive" :lexeme_id (ll "hic, haec, hoc")}
   {:wordform "hīs" :gloss "these" :part_of_speech "noun" :number "plural" :gender "feminine" :case_ "dative" :lexeme_id (ll "hic, haec, hoc")}
   {:wordform "hās" :gloss "these" :part_of_speech "noun" :number "plural" :gender "feminine" :case_ "accusative" :lexeme_id (ll "hic, haec, hoc")}
   {:wordform "hīs" :gloss "these" :part_of_speech "noun" :number "plural" :gender "feminine" :case_ "ablative" :lexeme_id (ll "hic, haec, hoc")}

   {:wordform "haec" :gloss "these" :part_of_speech "noun" :number "plural" :gender "neuter" :case_ "nominative" :lexeme_id (ll "hic, haec, hoc")}
   {:wordform "hōrum" :gloss "of these" :part_of_speech "noun" :number "plural" :gender "neuter" :case_ "genitive" :lexeme_id (ll "hic, haec, hoc")}
   {:wordform "hīs" :gloss "these" :part_of_speech "noun" :number "plural" :gender "neuter" :case_ "dative" :lexeme_id (ll "hic, haec, hoc")}
   {:wordform "haec" :gloss "these" :part_of_speech "noun" :number "plural" :gender "neuter" :case_ "accusative" :lexeme_id (ll "hic, haec, hoc")}
   {:wordform "hīs" :gloss "these" :part_of_speech "noun" :number "plural" :gender "neuter" :case_ "ablative" :lexeme_id (ll "hic, haec, hoc")}
   ;;
   {:wordform "et" :gloss "and" :part_of_speech "conjunction" :lexeme_id (ll "et")}
   {:wordform "vel" :gloss "or" :part_of_speech "conjunction" :lexeme_id (ll "vel")}
   {:wordform "seu" :gloss "or" :part_of_speech "conjunction" :lexeme_id (ll "seu")}
   ;;
   {:wordform "hīc" :gloss "here" :part_of_speech "adverb" :lexeme_id (ll "hīc")}
   {:wordform "illīc" :gloss "there" :part_of_speech "adverb" :lexeme_id (ll "illīc")}
   {:wordform "num" :gloss "makes something a question" :part_of_speech "particle" :lexeme_id (ll "num")}
   {:wordform "num" :gloss "for, because" :part_of_speech "conjunction" :lexeme_id (ll "nam")}
   {:wordform "in" :gloss "in, on" :part_of_speech "preposition" :lexeme_id (ll "in")}
   {:wordform "enim" :gloss "for, because" :part_of_speech "conjunction" :lexeme_id (ll "enim")}
   {:wordform "dē" :gloss "from" :part_of_speech "preposition" :lexeme_id (ll "dē")}
   {:wordform "dē" :gloss "of" :part_of_speech "preposition" :lexeme_id (ll "dē")}
   {:wordform "dē" :gloss "away from" :part_of_speech "preposition" :lexeme_id (ll "dē")}
   {:wordform "ex" :gloss "out of" :part_of_speech "preposition" :lexeme_id (ll "ex")}
   {:wordform "ex" :gloss "from" :part_of_speech "preposition" :lexeme_id (ll "ex")}
   {:wordform "ē" :gloss "out of" :part_of_speech "preposition" :lexeme_id (ll "ē")}
   {:wordform "ē" :gloss "from" :part_of_speech "preposition" :lexeme_id (ll "ē")}
   {:wordform "ad" :gloss "to, towards" :part_of_speech "preposition" :lexeme_id (ll "ex")}
   {:wordform "dum" :gloss "while" :part_of_speech "conjunction" :lexeme_id (ll "dum")}
   {:wordform "ergō" :gloss "therefore" :part_of_speech "conjunction" :lexeme_id (ll "ergō")}
   {:wordform "ergā" :gloss "towards, against" :part_of_speech "preposition" :lexeme_id (ll "ergā")}
   {:wordform "ergā" :gloss "towards, against" :part_of_speech "preposition" :lexeme_id (ll "ergā")}
   {:wordform "nōn" :gloss "not" :part_of_speech "conjunction" :lexeme_id (ll "nōn")}
   {:wordform "nec" :gloss "not" :part_of_speech "conjunction" :lexeme_id (ll "nec")}
   ])

(defn append-namespace [m namespace-to-append]
  (into {}
        (map (fn [[k v]]
               (if (namespace k)
                 [k v]
                 [(keyword namespace-to-append (name k)) v])
               )
             m)))

(defn grammatically-equivalent [m1 m2]
  (let [m1* (append-namespace m1 "meanings")
        m2* (append-namespace m2 "meanings")]
    (and (= (:meanings/wordform m1*) (:meanings/wordform m2*))
         (= (:meanings/part_of_speech m1*) (:meanings/part_of_speech m2*))
         (= (:meanings/person m1*) (:meanings/person m2*))
         (= (:meanings/number m1*) (:meanings/number m2*))
         (= (:meanings/gender m1*) (:meanings/gender m2*))
         (= (:meanings/case_ m1*) (:meanings/case_ m2*))
         (= (:meanings/tense m1*) (:meanings/tense m2*))
         (= (:meanings/mood m1*) (:meanings/mood m2*))
         (= (:meanings/voice m1*) (:meanings/voice m2*)))))

(defn insert-meaning!
  ([meaning-values]
   (insert-meaning! meaning-values false))
  ([meaning-values force?]
   (let [existing-matches (-> (do! {:select [:*]
                                    :from :meanings
                                    :where [:= :wordform (:wordform meaning-values)]}))
         ;; match-id (:meanings/meaning_id existing-match)
         ]
     (if (and (not (empty?
                   (filter #(grammatically-equivalent % meaning-values) existing-matches)))
              (not force?))
       (println "Not adding due to grammatical equivalence: " meaning-values existing-matches)
       (do! {:insert-into [:meanings]
             :values [meaning-values]})))))

(defn insert-all! []
  (doseq [meaning meanings-to-insert]
    (insert-meaning! meaning)))
