(ns latin-texts-db.bulk-adj-insert-three-term-first-and-second
  (:require
   [latin-texts-db.db :refer [do! ll]]))

(defn get-adjective-forms
  ([m f n sup-m gloss]
   (get-adjective-form m f n gloss (str "more " gloss) (str "very " gloss)))
  ([m f n sup-m pos-gloss comp-gloss sup-gloss ]
   (let [stem (subs f 0 (dec (count f)))
         sup-stem  (subs sup-m 0 (- (count sup-m) 2))
         df (clojure.string/join ", " [m f n])]
     [
      ;; positive masculine
      {:wordform m :gloss pos-gloss :part_of_speech "adjective" :number "singular" :gender "masculine" :degree "positive" :case_ "nominative" :lexeme_id (ll df)}
      {:wordform (str stem "ī") :gloss pos-gloss :part_of_speech "adjective" :number "singular" :gender "masculine" :degree "positive" :case_ "genitive" :lexeme_id (ll df)}
      {:wordform (str stem "ō") :gloss pos-gloss :part_of_speech "adjective" :number "singular" :gender "masculine" :degree "positive" :case_ "dative" :lexeme_id (ll df)}
      {:wordform (str stem "um") :gloss pos-gloss :part_of_speech "adjective" :number "singular" :gender "masculine" :degree "positive" :case_ "accusative" :lexeme_id (ll df)}
      {:wordform (str stem "ō") :gloss pos-gloss :part_of_speech "adjective" :number "singular" :gender "masculine" :degree "positive" :case_ "ablative" :lexeme_id (ll df)}
      {:wordform (str stem "ī") :gloss pos-gloss :part_of_speech "adjective" :number "plural" :gender "masculine" :degree "positive" :case_ "nominative" :lexeme_id (ll df)}
      {:wordform (str stem "ōrum") :gloss pos-gloss :part_of_speech "adjective" :number "plural" :gender "masculine" :degree "positive" :case_ "genitive" :lexeme_id (ll df)}
      {:wordform (str stem "īs") :gloss pos-gloss :part_of_speech "adjective" :number "plural" :gender "masculine" :degree "positive" :case_ "dative" :lexeme_id (ll df)}
      {:wordform (str stem "ōs") :gloss pos-gloss :part_of_speech "adjective" :number "plural" :gender "masculine" :degree "positive" :case_ "accusative" :lexeme_id (ll df)}
      {:wordform (str stem "īs") :gloss pos-gloss :part_of_speech "adjective" :number "plural" :gender "masculine" :degree "positive" :case_ "ablative" :lexeme_id (ll df)}

      ;; positive feminine
      {:wordform (str stem "a") :gloss pos-gloss :part_of_speech "adjective" :number "singular" :gender "feminine" :degree "positive" :case_ "nominative" :lexeme_id (ll df)}
      {:wordform (str stem "ae") :gloss pos-gloss :part_of_speech "adjective" :number "singular" :gender "feminine" :degree "positive" :case_ "genitive" :lexeme_id (ll df)}
      {:wordform (str stem "ae") :gloss pos-gloss :part_of_speech "adjective" :number "singular" :gender "feminine" :degree "positive" :case_ "dative" :lexeme_id (ll df)}
      {:wordform (str stem "am") :gloss pos-gloss :part_of_speech "adjective" :number "singular" :gender "feminine" :degree "positive" :case_ "accusative" :lexeme_id (ll df)}
      {:wordform (str stem "ā") :gloss pos-gloss :part_of_speech "adjective" :number "singular" :gender "feminine" :degree "positive" :case_ "ablative" :lexeme_id (ll df)}
      {:wordform (str stem "ae") :gloss pos-gloss :part_of_speech "adjective" :number "plural" :gender "feminine" :degree "positive" :case_ "nominative" :lexeme_id (ll df)}
      {:wordform (str stem "ārum") :gloss pos-gloss :part_of_speech "adjective" :number "plural" :gender "feminine" :degree "positive" :case_ "genitive" :lexeme_id (ll df)}
      {:wordform (str stem "īs") :gloss pos-gloss :part_of_speech "adjective" :number "plural" :gender "feminine" :degree "positive" :case_ "dative" :lexeme_id (ll df)}
      {:wordform (str stem "ās") :gloss pos-gloss :part_of_speech "adjective" :number "plural" :gender "feminine" :degree "positive" :case_ "accusative" :lexeme_id (ll df)}
      {:wordform (str stem "īs") :gloss pos-gloss :part_of_speech "adjective" :number "plural" :gender "feminine" :degree "positive" :case_ "ablative" :lexeme_id (ll df)}

      ;; positive neuter
      {:wordform (str stem "um") :gloss pos-gloss :part_of_speech "adjective" :number "singular" :gender "neuter" :degree "positive" :case_ "nominative" :lexeme_id (ll df)}
      {:wordform (str stem "ī") :gloss pos-gloss :part_of_speech "adjective" :number "singular" :gender "neuter" :degree "positive" :case_ "genitive" :lexeme_id (ll df)}
      {:wordform (str stem "ō") :gloss pos-gloss :part_of_speech "adjective" :number "singular" :gender "neuter" :degree "positive" :case_ "dative" :lexeme_id (ll df)}
      {:wordform (str stem "um") :gloss pos-gloss :part_of_speech "adjective" :number "singular" :gender "neuter" :degree "positive" :case_ "accusative" :lexeme_id (ll df)}
      {:wordform (str stem "ō") :gloss pos-gloss :part_of_speech "adjective" :number "singular" :gender "neuter" :degree "positive" :case_ "ablative" :lexeme_id (ll df)}
      {:wordform (str stem "a") :gloss pos-gloss :part_of_speech "adjective" :number "plural" :gender "neuter" :degree "positive" :case_ "nominative" :lexeme_id (ll df)}
      {:wordform (str stem "ōrum") :gloss pos-gloss :part_of_speech "adjective" :number "plural" :gender "neuter" :degree "positive" :case_ "genitive" :lexeme_id (ll df)}
      {:wordform (str stem "īs") :gloss pos-gloss :part_of_speech "adjective" :number "plural" :gender "neuter" :degree "positive" :case_ "dative" :lexeme_id (ll df)}
      {:wordform (str stem "a") :gloss pos-gloss :part_of_speech "adjective" :number "plural" :gender "neuter" :degree "positive" :case_ "accusative" :lexeme_id (ll df)}
      {:wordform (str stem "īs") :gloss pos-gloss :part_of_speech "adjective" :number "plural" :gender "neuter" :degree "positive" :case_ "ablative" :lexeme_id (ll df)}

      ;; comparative masculine
      {:wordform (str stem "ior") :gloss comp-gloss :part_of_speech "adjective" :number "singular" :gender "masculine" :degree "comparative" :case_ "nominative" :lexeme_id (ll df)}
      {:wordform (str stem "iōris") :gloss comp-gloss :part_of_speech "adjective" :number "singular" :gender "masculine" :degree "comparative" :case_ "genitive" :lexeme_id (ll df)}
      {:wordform (str stem "iōrī") :gloss comp-gloss :part_of_speech "adjective" :number "singular" :gender "masculine" :degree "comparative" :case_ "dative" :lexeme_id (ll df)}
      {:wordform (str stem "iōrem") :gloss comp-gloss :part_of_speech "adjective" :number "singular" :gender "masculine" :degree "comparative" :case_ "accusative" :lexeme_id (ll df)}
      {:wordform (str stem "iōre") :gloss comp-gloss :part_of_speech "adjective" :number "singular" :gender "masculine" :degree "comparative" :case_ "ablative" :lexeme_id (ll df)}
      {:wordform (str stem "iōrēs") :gloss comp-gloss :part_of_speech "adjective" :number "plural" :gender "masculine" :degree "comparative" :case_ "nominative" :lexeme_id (ll df)}
      {:wordform (str stem "iōrum") :gloss comp-gloss :part_of_speech "adjective" :number "plural" :gender "masculine" :degree "comparative" :case_ "genitive" :lexeme_id (ll df)}
      {:wordform (str stem "iōribus") :gloss comp-gloss :part_of_speech "adjective" :number "plural" :gender "masculine" :degree "comparative" :case_ "dative" :lexeme_id (ll df)}
      {:wordform (str stem "iōrēs") :gloss comp-gloss :part_of_speech "adjective" :number "plural" :gender "masculine" :degree "comparative" :case_ "accusative" :lexeme_id (ll df)}
      {:wordform (str stem "iōribus") :gloss comp-gloss :part_of_speech "adjective" :number "plural" :gender "masculine" :degree "comparative" :case_ "ablative" :lexeme_id (ll df)}

      ;; comparative feminine
      {:wordform (str stem "ior") :gloss comp-gloss :part_of_speech "adjective" :number "singular" :gender "feminine" :degree "comparative" :case_ "nominative" :lexeme_id (ll df)}
      {:wordform (str stem "iōris") :gloss comp-gloss :part_of_speech "adjective" :number "singular" :gender "feminine" :degree "comparative" :case_ "genitive" :lexeme_id (ll df)}
      {:wordform (str stem "iōrī") :gloss comp-gloss :part_of_speech "adjective" :number "singular" :gender "feminine" :degree "comparative" :case_ "dative" :lexeme_id (ll df)}
      {:wordform (str stem "iōrem") :gloss comp-gloss :part_of_speech "adjective" :number "singular" :gender "feminine" :degree "comparative" :case_ "accusative" :lexeme_id (ll df)}
      {:wordform (str stem "iōre") :gloss comp-gloss :part_of_speech "adjective" :number "singular" :gender "feminine" :degree "comparative" :case_ "ablative" :lexeme_id (ll df)}
      {:wordform (str stem "iōrēs") :gloss comp-gloss :part_of_speech "adjective" :number "plural" :gender "feminine" :degree "comparative" :case_ "nominative" :lexeme_id (ll df)}
      {:wordform (str stem "iōrum") :gloss comp-gloss :part_of_speech "adjective" :number "plural" :gender "feminine" :degree "comparative" :case_ "genitive" :lexeme_id (ll df)}
      {:wordform (str stem "iōribus") :gloss comp-gloss :part_of_speech "adjective" :number "plural" :gender "feminine" :degree "comparative" :case_ "dative" :lexeme_id (ll df)}
      {:wordform (str stem "iōrēs") :gloss comp-gloss :part_of_speech "adjective" :number "plural" :gender "feminine" :degree "comparative" :case_ "accusative" :lexeme_id (ll df)}
      {:wordform (str stem "iōribus") :gloss comp-gloss :part_of_speech "adjective" :number "plural" :gender "feminine" :degree "comparative" :case_ "ablative" :lexeme_id (ll df)}

      ;; comparative neuter
      {:wordform (str stem "ius") :gloss comp-gloss :part_of_speech "adjective" :number "singular" :gender "neuter" :degree "comparative" :case_ "nominative" :lexeme_id (ll df)}
      {:wordform (str stem "iōris") :gloss comp-gloss :part_of_speech "adjective" :number "singular" :gender "neuter" :degree "comparative" :case_ "genitive" :lexeme_id (ll df)}
      {:wordform (str stem "iōrī") :gloss comp-gloss :part_of_speech "adjective" :number "singular" :gender "neuter" :degree "comparative" :case_ "dative" :lexeme_id (ll df)}
      {:wordform (str stem "ius") :gloss comp-gloss :part_of_speech "adjective" :number "singular" :gender "neuter" :degree "comparative" :case_ "accusative" :lexeme_id (ll df)}
      {:wordform (str stem "iōre") :gloss comp-gloss :part_of_speech "adjective" :number "singular" :gender "neuter" :degree "comparative" :case_ "ablative" :lexeme_id (ll df)}
      {:wordform (str stem "iōra") :gloss comp-gloss :part_of_speech "adjective" :number "plural" :gender "neuter" :degree "comparative" :case_ "nominative" :lexeme_id (ll df)}
      {:wordform (str stem "iōrum") :gloss comp-gloss :part_of_speech "adjective" :number "plural" :gender "neuter" :degree "comparative" :case_ "genitive" :lexeme_id (ll df)}
      {:wordform (str stem "iōribus") :gloss comp-gloss :part_of_speech "adjective" :number "plural" :gender "neuter" :degree "comparative" :case_ "dative" :lexeme_id (ll df)}
      {:wordform (str stem "iōra") :gloss comp-gloss :part_of_speech "adjective" :number "plural" :gender "neuter" :degree "comparative" :case_ "accusative" :lexeme_id (ll df)}
      {:wordform (str stem "iōribus") :gloss comp-gloss :part_of_speech "adjective" :number "plural" :gender "neuter" :degree "comparative" :case_ "ablative" :lexeme_id (ll df)}

      ;; superlative masculine
      {:wordform (str sup-stem "us") :gloss sup-gloss :part_of_speech "adjective" :number "singular" :gender "masculine" :degree "superlative" :case_ "nominative" :lexeme_id (ll df)}
      {:wordform (str sup-stem "ī") :gloss sup-gloss :part_of_speech "adjective" :number "singular" :gender "masculine" :degree "superlative" :case_ "genitive" :lexeme_id (ll df)}
      {:wordform (str sup-stem "ō") :gloss sup-gloss :part_of_speech "adjective" :number "singular" :gender "masculine" :degree "superlative" :case_ "dative" :lexeme_id (ll df)}
      {:wordform (str sup-stem "um") :gloss sup-gloss :part_of_speech "adjective" :number "singular" :gender "masculine" :degree "superlative" :case_ "accusative" :lexeme_id (ll df)}
      {:wordform (str sup-stem "ō") :gloss sup-gloss :part_of_speech "adjective" :number "singular" :gender "masculine" :degree "superlative" :case_ "ablative" :lexeme_id (ll df)}
      {:wordform (str sup-stem "ī") :gloss sup-gloss :part_of_speech "adjective" :number "plural" :gender "masculine" :degree "superlative" :case_ "nominative" :lexeme_id (ll df)}
      {:wordform (str sup-stem "ōrum") :gloss sup-gloss :part_of_speech "adjective" :number "plural" :gender "masculine" :degree "superlative" :case_ "genitive" :lexeme_id (ll df)}
      {:wordform (str sup-stem "īs") :gloss sup-gloss :part_of_speech "adjective" :number "plural" :gender "masculine" :degree "superlative" :case_ "dative" :lexeme_id (ll df)}
      {:wordform (str sup-stem "ōs") :gloss sup-gloss :part_of_speech "adjective" :number "plural" :gender "masculine" :degree "superlative" :case_ "accusative" :lexeme_id (ll df)}
      {:wordform (str sup-stem "īs") :gloss sup-gloss :part_of_speech "adjective" :number "plural" :gender "masculine" :degree "superlative" :case_ "ablative" :lexeme_id (ll df)}

      ;; superlative feminine
      {:wordform (str sup-stem "a") :gloss sup-gloss :part_of_speech "adjective" :number "singular" :gender "feminine" :degree "superlative" :case_ "nominative" :lexeme_id (ll df)}
      {:wordform (str sup-stem "ae") :gloss sup-gloss :part_of_speech "adjective" :number "singular" :gender "feminine" :degree "superlative" :case_ "genitive" :lexeme_id (ll df)}
      {:wordform (str sup-stem "ae") :gloss sup-gloss :part_of_speech "adjective" :number "singular" :gender "feminine" :degree "superlative" :case_ "dative" :lexeme_id (ll df)}
      {:wordform (str sup-stem "am") :gloss sup-gloss :part_of_speech "adjective" :number "singular" :gender "feminine" :degree "superlative" :case_ "accusative" :lexeme_id (ll df)}
      {:wordform (str sup-stem "ā") :gloss sup-gloss :part_of_speech "adjective" :number "singular" :gender "feminine" :degree "superlative" :case_ "ablative" :lexeme_id (ll df)}
      {:wordform (str sup-stem "ae") :gloss sup-gloss :part_of_speech "adjective" :number "plural" :gender "feminine" :degree "superlative" :case_ "nominative" :lexeme_id (ll df)}
      {:wordform (str sup-stem "ārum") :gloss sup-gloss :part_of_speech "adjective" :number "plural" :gender "feminine" :degree "superlative" :case_ "genitive" :lexeme_id (ll df)}
      {:wordform (str sup-stem "īs") :gloss sup-gloss :part_of_speech "adjective" :number "plural" :gender "feminine" :degree "superlative" :case_ "dative" :lexeme_id (ll df)}
      {:wordform (str sup-stem "ās") :gloss sup-gloss :part_of_speech "adjective" :number "plural" :gender "feminine" :degree "superlative" :case_ "accusative" :lexeme_id (ll df)}
      {:wordform (str sup-stem "īs") :gloss sup-gloss :part_of_speech "adjective" :number "plural" :gender "feminine" :degree "superlative" :case_ "ablative" :lexeme_id (ll df)}

      ;; superlative neuter
      {:wordform (str sup-stem "um") :gloss sup-gloss :part_of_speech "adjective" :number "singular" :gender "neuter" :degree "superlative" :case_ "nominative" :lexeme_id (ll df)}
      {:wordform (str sup-stem "ī") :gloss sup-gloss :part_of_speech "adjective" :number "singular" :gender "neuter" :degree "superlative" :case_ "genitive" :lexeme_id (ll df)}
      {:wordform (str sup-stem "ō") :gloss sup-gloss :part_of_speech "adjective" :number "singular" :gender "neuter" :degree "superlative" :case_ "dative" :lexeme_id (ll df)}
      {:wordform (str sup-stem "um") :gloss sup-gloss :part_of_speech "adjective" :number "singular" :gender "neuter" :degree "superlative" :case_ "accusative" :lexeme_id (ll df)}
      {:wordform (str sup-stem "ō") :gloss sup-gloss :part_of_speech "adjective" :number "singular" :gender "neuter" :degree "superlative" :case_ "ablative" :lexeme_id (ll df)}
      {:wordform (str sup-stem "a") :gloss sup-gloss :part_of_speech "adjective" :number "plural" :gender "neuter" :degree "superlative" :case_ "nominative" :lexeme_id (ll df)}
      {:wordform (str sup-stem "ōrum") :gloss sup-gloss :part_of_speech "adjective" :number "plural" :gender "neuter" :degree "superlative" :case_ "genitive" :lexeme_id (ll df)}
      {:wordform (str sup-stem "īs") :gloss sup-gloss :part_of_speech "adjective" :number "plural" :gender "neuter" :degree "superlative" :case_ "dative" :lexeme_id (ll df)}
      {:wordform (str sup-stem "a") :gloss sup-gloss :part_of_speech "adjective" :number "plural" :gender "neuter" :degree "superlative" :case_ "accusative" :lexeme_id (ll df)}
      {:wordform (str sup-stem "īs") :gloss sup-gloss :part_of_speech "adjective" :number "plural" :gender "neuter" :degree "superlative" :case_ "ablative" :lexeme_id (ll df)}
      ])))

(defn insert-adj-meaning! [meaning-values]
  (let [existing-match (do! {:select [:meaning_id]
                             :from :meanings
                             :where [:and
                                     [:= :gloss (:gloss meaning-values)]
                                     [:= :wordform (:wordform meaning-values)]
                                     [:= :case_ (:case_ meaning-values)]
                                     [:= :number (:number meaning-values)]
                                     [:= :gender (:gender meaning-values)]
                                     [:= :degree (:degree meaning-values)]]})
        match-id (:meanings/meaning_id (first existing-match))]
    (if match-id
      (println "Meaning is already present in the database: " match-id)
      (do! {:insert-into [:meanings]
            :values [meaning-values]}))))

(def wordlist
  [["ruber, rubra, rubrum" "ruberrimus" "red" "redder" "reddest"]
   ])

(defn insert-single-adj-from-args! [args]
  (let [[m f n] (clojure.string/split (first args) #", ")
        meanings (apply get-adjective-forms (concat [m f n] (rest args)))]
    (doseq [meaning meanings]
      (insert-adj-meaning! meaning))))

(defn insert-all! []
  (doseq [args wordlist]
    (insert-single-adj-from-args! args)))
