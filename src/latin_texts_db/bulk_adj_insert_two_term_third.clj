(ns latin-texts-db.bulk-adj-insert-two-term-third
  (:require
   [latin-texts-db.db :refer [do! ll]]))

(defn get-adjective-forms-m [{:keys [mf gloss pl-gen-ium? df]}]
  (let [stem (subs mf 0 (- (count mf) 2))]
    [
     ;; positive masculine
     {:wordform mf :gloss gloss :part_of_speech "adjective" :number "singular" :gender "masculine" :degree "positive" :case_ "nominative" :lexeme_id (ll df)}
     {:wordform mf :gloss gloss :part_of_speech "adjective" :number "singular" :gender "masculine" :degree "positive" :case_ "genitive" :lexeme_id (ll df)}
     {:wordform (str stem "ī") :gloss gloss :part_of_speech "adjective" :number "singular" :gender "masculine" :degree "positive" :case_ "dative" :lexeme_id (ll df)}
     {:wordform (str stem "em") :gloss gloss :part_of_speech "adjective" :number "singular" :gender "masculine" :degree "positive" :case_ "accusative" :lexeme_id (ll df)}
     {:wordform (str stem "ī") :gloss gloss :part_of_speech "adjective" :number "singular" :gender "masculine" :degree "positive" :case_ "ablative" :lexeme_id (ll df)}
     {:wordform (str stem "ēs") :gloss gloss :part_of_speech "adjective" :number "plural" :gender "masculine" :degree "positive" :case_ "nominative" :lexeme_id (ll df)}
     {:wordform (if pl-gen-ium? (str stem "ium") (str stem "um")) :gloss gloss :part_of_speech "adjective" :number "plural" :gender "masculine" :degree "positive" :case_ "genitive" :lexeme_id (ll df)}
     {:wordform (str stem "ibus") :gloss gloss :part_of_speech "adjective" :number "plural" :gender "masculine" :degree "positive" :case_ "dative" :lexeme_id (ll df)}
     {:wordform (str stem "ēs") :gloss gloss :part_of_speech "adjective" :number "plural" :gender "masculine" :degree "positive" :case_ "accusative" :lexeme_id (ll df)}
     {:wordform (str stem "ibus") :gloss gloss :part_of_speech "adjective" :number "plural" :gender "masculine" :degree "positive" :case_ "ablative" :lexeme_id (ll df)}]))

(defn get-adjective-forms-n [{:keys [n gloss pl-gen-ium? df]}]
  (let [stem (subs n 0 (- (count n) 1))]
    [
     ;; positive masculine
     {:wordform n :gloss gloss :part_of_speech "adjective" :number "singular" :gender "masculine" :degree "positive" :case_ "nominative" :lexeme_id (ll df)}
     {:wordform (str stem "is") :gloss gloss :part_of_speech "adjective" :number "singular" :gender "masculine" :degree "positive" :case_ "genitive" :lexeme_id (ll df)}
     {:wordform (str stem "ī") :gloss gloss :part_of_speech "adjective" :number "singular" :gender "masculine" :degree "positive" :case_ "dative" :lexeme_id (ll df)}
     {:wordform n :gloss gloss :part_of_speech "adjective" :number "singular" :gender "masculine" :degree "positive" :case_ "accusative" :lexeme_id (ll df)}
     {:wordform (str stem "ī") :gloss gloss :part_of_speech "adjective" :number "singular" :gender "masculine" :degree "positive" :case_ "ablative" :lexeme_id (ll df)}
     {:wordform (str stem "ia") :gloss gloss :part_of_speech "adjective" :number "plural" :gender "masculine" :degree "positive" :case_ "nominative" :lexeme_id (ll df)}
     {:wordform (if pl-gen-ium? (str stem "ium") (str stem "um")) :gloss gloss :part_of_speech "adjective" :number "plural" :gender "masculine" :degree "positive" :case_ "genitive" :lexeme_id (ll df)}
     {:wordform (str stem "ibus") :gloss gloss :part_of_speech "adjective" :number "plural" :gender "masculine" :degree "positive" :case_ "dative" :lexeme_id (ll df)}
     {:wordform (str stem "ia") :gloss gloss :part_of_speech "adjective" :number "plural" :gender "masculine" :degree "positive" :case_ "accusative" :lexeme_id (ll df)}
     {:wordform (str stem "ibus") :gloss gloss :part_of_speech "adjective" :number "plural" :gender "masculine" :degree "positive" :case_ "ablative" :lexeme_id (ll df)}]))

(defn get-adjective-forms [{:keys [mf n pos-gloss pl-gen-ium?]}]
  (let [df (clojure.string/join ", " [mf n])
        masc-forms (get-adjective-forms-m {:mf mf :gloss pos-gloss :df df :pl-gen-ium? pl-gen-ium?})
        neut-forms (get-adjective-forms-n {:n n :gloss pos-gloss :df df :pl-gen-ium? pl-gen-ium?})
        ]
    (concat
     masc-forms
     (map (fn [form] (assoc form :gender "feminine")) masc-forms)
     neut-forms)))

(defn insert-adj-meaning! [meaning-values]
  ;; TODO consolidate these into a common file
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

(defn insert-single-adj-from-args! [args-map]
  (let [[mf n] (clojure.string/split (:dictionary-form args-map) #", ")
        args-map (assoc args-map :mf mf :n n)
        meanings (get-adjective-forms args-map)]
    (doseq [meaning meanings]
      (insert-adj-meaning! meaning))))
