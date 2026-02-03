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
    (clojure.string/ends-with? gen "ēī") 5
    (clojure.string/ends-with? gen "eī") 5
    (clojure.string/ends-with? gen "ī") 2
    (clojure.string/ends-with? gen "is") 3
    (clojure.string/ends-with? gen "ūs") 4))

(defn get-noun-forms-first-declension [{:keys [nom gen gender sn-gloss sg-gloss pn-gloss pg-gloss]}]
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
     ]))

(defn get-noun-forms-second-declension-mf [{:keys [nom gen gender sn-gloss sg-gloss pn-gloss pg-gloss]}]
  (let [stem (subs gen 0 (dec (count gen)))
        dictionary-form (str nom ", " gen)]
    [{:wordform nom :gloss sn-gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "nominative" :lexeme_id (ll dictionary-form)}
     {:wordform gen :gloss sg-gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "genitive" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "ō") :gloss sn-gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "dative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "um") :gloss sn-gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "accusative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "ō") :gloss sn-gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "ablative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "ī") :gloss pn-gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "nominative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "ōrum") :gloss pg-gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "genitive" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "īs") :gloss pn-gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "dative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "ōs") :gloss pn-gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "accusative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "īs") :gloss pn-gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "ablative" :lexeme_id (ll dictionary-form)}
     ]))

(defn get-noun-forms-second-declension-neuter [{:keys [nom gen sn-gloss sg-gloss pn-gloss pg-gloss]}]
  (let [stem (subs gen 0 (dec (count gen)))
        dictionary-form (str nom ", " gen)
        gender "neuter"]
    [{:wordform nom :gloss sn-gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "nominative" :lexeme_id (ll dictionary-form)}
     {:wordform gen :gloss sg-gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "genitive" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "ō") :gloss sn-gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "dative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "um") :gloss sn-gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "accusative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "ō") :gloss sn-gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "ablative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "a") :gloss pn-gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "nominative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "ōrum") :gloss pg-gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "genitive" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "īs") :gloss pn-gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "dative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "a") :gloss pn-gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "accusative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "īs") :gloss pn-gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "ablative" :lexeme_id (ll dictionary-form)}
     ]))

(defn get-noun-forms-third-declension-mf [{:keys [nom gen gender sn-gloss sg-gloss pn-gloss pg-gloss pl-gen-ium?]}]
  (let [stem (subs gen 0 (- (count gen) 2))
        dictionary-form (str nom ", " gen)]
    [{:wordform nom :gloss sn-gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "nominative" :lexeme_id (ll dictionary-form)}
     {:wordform gen :gloss sg-gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "genitive" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "ī") :gloss sn-gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "dative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "em") :gloss sn-gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "accusative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "e") :gloss sn-gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "ablative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "ēs") :gloss pn-gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "nominative" :lexeme_id (ll dictionary-form)}
     {:wordform (if pl-gen-ium? (str stem "ium") (str stem "um")) :gloss pg-gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "genitive" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "ibus") :gloss pn-gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "dative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "ēs") :gloss pn-gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "accusative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "ibus") :gloss pn-gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "ablative" :lexeme_id (ll dictionary-form)}
     ]))

(defn get-noun-forms-third-declension-neuter [{:keys [nom gen sn-gloss sg-gloss pn-gloss pg-gloss pl-gen-ium?]}]
  (let [stem (subs gen 0 (- (count gen) 2))
        dictionary-form (str nom ", " gen)
        gender "neuter"]
    [{:wordform nom :gloss sn-gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "nominative" :lexeme_id (ll dictionary-form)}
     {:wordform gen :gloss sg-gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "genitive" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "ī") :gloss sn-gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "dative" :lexeme_id (ll dictionary-form)}
     {:wordform nom :gloss sn-gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "accusative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "e") :gloss sn-gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "ablative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "a") :gloss pn-gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "nominative" :lexeme_id (ll dictionary-form)}
     {:wordform (if pl-gen-ium? (str stem "ium") (str stem "um")) :gloss pg-gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "genitive" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "ibus") :gloss pn-gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "dative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "a") :gloss pn-gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "accusative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "ibus") :gloss pn-gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "ablative" :lexeme_id (ll dictionary-form)}
     ]))

(defn get-noun-forms-third-declension-neuter [{:keys [nom gen sn-gloss sg-gloss pn-gloss pg-gloss pl-gen-ium?]}]
  (let [stem (subs gen 0 (- (count gen) 2))
        dictionary-form (str nom ", " gen)
        gender "neuter"]
    [{:wordform nom :gloss sn-gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "nominative" :lexeme_id (ll dictionary-form)}
     {:wordform gen :gloss sg-gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "genitive" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "ī") :gloss sn-gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "dative" :lexeme_id (ll dictionary-form)}
     {:wordform nom :gloss sn-gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "accusative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "e") :gloss sn-gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "ablative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "a") :gloss pn-gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "nominative" :lexeme_id (ll dictionary-form)}
     {:wordform (if pl-gen-ium? (str stem "ium") (str stem "um")) :gloss pg-gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "genitive" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "ibus") :gloss pn-gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "dative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "a") :gloss pn-gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "accusative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "ibus") :gloss pn-gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "ablative" :lexeme_id (ll dictionary-form)}
     ]))

(defn get-noun-forms-third-declension-common [{:keys [nom gen sn-gloss sg-gloss pn-gloss pg-gloss pl-gen-ium?]}]
  (let [stem (subs gen 0 (- (count gen) 2))
        dictionary-form (str nom ", " gen)
        gender "common"]
    [{:wordform nom :gloss sn-gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "nominative" :lexeme_id (ll dictionary-form)}
     {:wordform gen :gloss sg-gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "genitive" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "ī") :gloss sn-gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "dative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "em") :gloss sn-gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "accusative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "e") :gloss sn-gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "ablative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "ēs") :gloss pn-gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "nominative" :lexeme_id (ll dictionary-form)}
     {:wordform (if pl-gen-ium? (str stem "ium") (str stem "um")) :gloss pg-gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "genitive" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "ibus") :gloss pn-gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "dative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "ēs") :gloss pn-gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "accusative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "ibus") :gloss pn-gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "ablative" :lexeme_id (ll dictionary-form)}
     ]))

(defn get-noun-forms-fourth-declension-mf [{:keys [nom gen gender sn-gloss sg-gloss pn-gloss pg-gloss]}]
  (let [stem (subs gen 0 (- (count gen) 2))
        dictionary-form (str nom ", " gen)]
    [{:wordform nom :gloss sn-gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "nominative" :lexeme_id (ll dictionary-form)}
     {:wordform gen :gloss sg-gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "genitive" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "uī") :gloss sn-gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "dative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "um") :gloss sn-gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "accusative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "ū") :gloss sn-gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "ablative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "ūs") :gloss pn-gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "nominative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "uum") :gloss pg-gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "genitive" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "ibus") :gloss pn-gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "dative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "ūs") :gloss pn-gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "accusative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "ibus") :gloss pn-gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "ablative" :lexeme_id (ll dictionary-form)}
     ]))

(defn get-noun-forms-fourth-declension-n [{:keys [nom gen sn-gloss sg-gloss pn-gloss pg-gloss]}]
  (let [stem (subs gen 0 (- (count gen) 2))
        dictionary-form (str nom ", " gen)
        gender "neuter"]
    [{:wordform nom :gloss sn-gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "nominative" :lexeme_id (ll dictionary-form)}
     {:wordform gen :gloss sg-gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "genitive" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "ū") :gloss sn-gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "dative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "ū") :gloss sn-gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "accusative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "ū") :gloss sn-gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "ablative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "ua") :gloss pn-gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "nominative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "uum") :gloss pg-gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "genitive" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "ibus") :gloss pn-gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "dative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "ua") :gloss pn-gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "accusative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "ibus") :gloss pn-gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "ablative" :lexeme_id (ll dictionary-form)}
     ]))

(defn get-noun-forms-fifth-declension-masculine [{:keys [nom gen sn-gloss sg-gloss pn-gloss pg-gloss]}]
  (let [stem (subs gen 0 (- (count gen) 2))
        dictionary-form (str nom ", " gen)
        gender "neuter"]
    [{:wordform nom :gloss sn-gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "nominative" :lexeme_id (ll dictionary-form)}
     {:wordform gen :gloss sg-gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "genitive" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "ēī") :gloss sn-gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "dative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "em") :gloss sn-gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "accusative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "ē") :gloss sn-gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "ablative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "ēs") :gloss pn-gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "nominative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "ērum") :gloss pg-gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "genitive" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "ēbus") :gloss pn-gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "dative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "ēs") :gloss pn-gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "accusative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "ēbus") :gloss pn-gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "ablative" :lexeme_id (ll dictionary-form)}
     ]))

(defn get-noun-forms-fifth-declension-feminine [{:keys [nom gen sn-gloss sg-gloss pn-gloss pg-gloss]}]
  (let [stem (subs gen 0 (- (count gen) 2))
        dictionary-form (str nom ", " gen)
        gender "neuter"]
    [{:wordform nom :gloss sn-gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "nominative" :lexeme_id (ll dictionary-form)}
     {:wordform gen :gloss sg-gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "genitive" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "eī") :gloss sn-gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "dative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "em") :gloss sn-gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "accusative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "ē") :gloss sn-gloss :part_of_speech "noun" :number "singular" :gender gender :case_ "ablative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "ēs") :gloss pn-gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "nominative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "ērum") :gloss pg-gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "genitive" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "ēbus") :gloss pn-gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "dative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "ēs") :gloss pn-gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "accusative" :lexeme_id (ll dictionary-form)}
     {:wordform (str stem "ēbus") :gloss pn-gloss :part_of_speech "noun" :number "plural" :gender gender :case_ "ablative" :lexeme_id (ll dictionary-form)}
     ]))

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

(defn insert-noun-meanings! [{:keys [dictionary-form gender sn-gloss sg-gloss pn-gloss pg-gloss pl-gen-ium?] :as args}]
  (let [[nom gen] (clojure.string/split dictionary-form #", ")
        args (assoc args :nom nom :gen gen)
        declension (genitive->declension gen)
        meanings (case declension
                   1 (get-noun-forms-first-declension args)
                   2 (case gender 
                       "masculine" (get-noun-forms-second-declension-mf args)
                       "neuter"    (get-noun-forms-second-declension-neuter args)
                       "feminine"  (get-noun-forms-second-declension-mf args))
                   3 (case gender
                       "masculine" (get-noun-forms-third-declension-mf args)
                       "feminine" (get-noun-forms-third-declension-mf args)
                       "neuter" (get-noun-forms-third-declension-neuter args)
                       "common" (get-noun-forms-third-declension-common args))
                   4 (case gender
                       "masculine" (get-noun-forms-fourth-declension-mf args)
                       "feminine" (get-noun-forms-fourth-declension-mf args)
                       "neuter" (get-noun-forms-fourth-declension-n args)
                       )
                   5 (case gender
                       "masculine" (get-noun-forms-fifth-declension-masculine args)
                       "feminine" (get-noun-forms-fifth-declension-feminine args)))]
    (doseq [meaning meanings]
      (insert-noun-meaning! meaning)))
  )

;; (defn insert-all! []
;;   ;; 1st decl
;;   (insert-noun-meanings! "vacca, vaccae" "feminine" "cow" "cow's" "cows" "of the cows")
;;   (insert-noun-meanings! "nauta, nautae" "masculine" "sailor" "sailor's" "sailors" "of the sailors")
;;   ;; 2nd decl
;;   (insert-noun-meanings! "vir, virī" "masculine" "man" "man's" "men" "of the men")
;;   (insert-noun-meanings! "saxum, saxī" "neuter" "rock" "rock's" "rocks" "of the rocks")
;;   (insert-noun-meanings! "atomus, atomī" "feminine" "atom" "atom's" "atoms" "of the atoms")
;;   (insert-noun-meanings! "Mārcus, Mārcī" "masculine" "Marcus" "Marcus's" "Marcuses" "of the Marcuses")
;;   (insert-noun-meanings! "Iūlius, Iūliī" "masculine" "Julius" "Julius's" "Juliuses" "of the Juliuses")
;;   (insert-noun-meanings! "Tlēpolemus, Tlēpolemī" "masculine" "Tlepolemus, the name of a tablet carrier" "Tlepolemus's" "Tlepolemuses" "of the Tlepolemuses")
;;   ;; 3rd decl
;;   (insert-noun-meanings! "rēx, rēgis" "masculine" "king" "king's" "kings" "of the kings")
;;   (insert-noun-meanings! "pāx, pācis" "feminine" "peace" "of peace" "peace" "of the peace")
;;   (insert-noun-meanings! "caput, capitis" "feminine" "peace" "of peace" "peace" "of the peace")

;;   ;; general usage
;;   (insert-noun-meanings! "magister, magistrī" "masculine" "teacher" "teacher's" "teachers" "of the teachers")
;;   (insert-noun-meanings! "discipulus, discipulī" "masculine" "student" "student's" "students" "of the students")
;;   (insert-noun-meanings! "liber, librī" "masculine" "book" "book's" "books" "of the books")
;;   (insert-noun-meanings! "tabula, tabulae" "feminine" "writing tablet" "writing tablet's" "writing tablets" "of the writing tablets")
;;   (insert-noun-meanings! "Quīntus, Quīntī" "masculine" "Quintus" "Quintus's" "Quintuses" "of the Quintuses")
;;   (insert-noun-meanings! "Sextus, Sextī" "masculine" "Sextus" "Sextus's" "Sextuses" "of the Sextuses")
;;   (insert-noun-meanings! "Diodōrus, Diodōrī" "masculine" "Diodorus" "Diodorus's" "Diodoruses" "of the Diodoruses")
;;   (insert-noun-meanings! "Titus, Titī" "masculine" "Titus" "Titus's" "Tituses" "of the Tituses")
;;   (insert-noun-meanings! "lūdus, lūdī" "masculine" "school" "school's" "schools" "of the schools")
;;   (insert-noun-meanings! "lūdus, lūdī" "masculine" "game" "game's" "games" "of the games")
;;   (insert-noun-meanings! "puer, puerī" "masculine" "boy" "boy's" "boys" "of the boys")
;;   (insert-noun-meanings! "virga, virgae" "feminine" "stick" "stick's" "sticks" "of the sticks")
;;   (insert-noun-meanings! "sella, sellae" "feminine" "chair" "chair's" "chairs" "of the chairs")
;;   (insert-noun-meanings! "nōmen, nōminis" "neuter" "name" "name's" "names" "of the names")
;;   (insert-noun-meanings! "Tusculum, Tusculī" "neuter" "Tusculum (a city in ancient/medieval Rome)" "Tusculum's (a city in ancient/medieval Rome)" "Tusculums (a city in ancient/medieval Rome)" "of the Tusculums (a city in ancient/medieval Rome)")
;;   (insert-noun-meanings! "modus, modī" "masculine" "way, manner" "way's, manner's" "ways, manners" "of the ways, manners")
;;   (insert-noun-meanings! "mālum, mālī" "neuter" "apple" "apple's" "apples" "of the apples")
;;   (insert-noun-meanings! "iānua, iānuae" "feminine" "entrance, door" "entrance's, door's" "entrances, doors" "of the entrances, doors")
;;   (insert-noun-meanings! "tergum, tergī" "neuter" "back, rear" "back's, rear's" "backs, rears" "of the backs, rears")
;;   (insert-noun-meanings! "pars, partis" "feminine" "part" "part's" "parts" "of the parts")
;;   (insert-noun-meanings! "corpus, corporis" "neuter" "body" "body's" "bodies" "of the bodies")
;;   (insert-noun-meanings! "verbum, verbī" "neuter" "word" "word's" "words" "of the words")
;;   (insert-noun-meanings! "lectulus, lectulī" "masculine" "small couch/bed" "small couch/bed's" "small couches/beds" "of the small couches/beds")
;;   (insert-noun-meanings! "hōra, hōrae" "feminine" "hour" "hour's" "hours" "of the hours")
;;   (insert-noun-meanings! "frūctus, frūctūs" "masculine" "fruit" "fruit's" "fruits" "of the fruits")
;;   (insert-noun-meanings! "cornū, cornūs" "neuter" "horn" "horn's" "horns" "of the horns")
;;   )
