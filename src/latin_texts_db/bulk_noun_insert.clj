(ns latin-texts-db.bulk-noun-insert
  (:require
   [next.jdbc :as jdbc]
   [migratus.core :as migratus]
   [honey.sql :as sql]
   [honey.sql.helpers :as h]
   [latin-texts-db.migrations.basic-tables]
   [latin-texts-db.db :refer [ds do! ll]]))

(defn genitive->declension [gen]
  (cond
    (clojure.string/ends-with? gen "ae") 1
    (clojure.string/ends-with? gen "ī") 2
    (clojure.string/ends-with? gen "is") 3
    (clojure.string/ends-with? gen "ūs") 4
    (clojure.string/ends-with? gen "ēs") 5))

(defn get-noun-forms-first-declension [nom gen gender sn-gloss sg-gloss pn-gloss pg-gloss]
  (let [stem (subs nom 0 (dec (count nom)))
        dictionary-form (str nom ", " gen)]
    [{:wordform (str stem "a") :gloss sn-gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "nominative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "ae") :gloss sg-gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "genitive" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "ae") :gloss sn-gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "dative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "am") :gloss sn-gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "accusative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "ā") :gloss sn-gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "ablative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "ae") :gloss pn-gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "nominative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "ārum") :gloss pg-gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "genitive" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "īs") :gloss pn-gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "dative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "ās") :gloss pn-gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "accusative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "īs") :gloss pn-gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "ablative" :lexeme_id (ll dictionary-form)}
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

(defn insert-noun-meanings! [dict-entry gender sn-gloss sg-gloss pn-gloss pg-gloss]
  (let [[nom gen] (clojure.string/split dict-entry #", ")
        declension (genitive->declension gen)
        meanings (case declension
                   1 (get-noun-forms-first-declension nom gen gender sn-gloss sg-gloss pn-gloss pg-gloss))]
    (doseq [meaning meanings]
      (insert-noun-meaning! meaning)))
  )

(defn insert-all! []
  (insert-noun-meanings! "vacca, vaccae" "feminine" "cow" "cow's" "cows" "of the cows")
  (insert-noun-meanings! "nauta, nautae" "masculine" "sailor" "sailor's" "sailors" "of the sailors")
  )
