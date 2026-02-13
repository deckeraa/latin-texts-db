(ns latin-texts.bulk-adj-insert-one-term-third
  (:require
   [latin-texts.db :refer [do! ll]]))

(defn get-positive-forms-m [{:keys [stem pos-gloss mfn df pl-gen-ium?]}]
  [;; positive masculine
     {:wordform mfn :gloss pos-gloss :part_of_speech "adjective" :number "singular" :gender "masculine" :degree "positive" :case_ "nominative" :lexeme_id (ll df)}
     {:wordform (str stem "is") :gloss pos-gloss :part_of_speech "adjective" :number "singular" :gender "masculine" :degree "positive" :case_ "genitive" :lexeme_id (ll df)}
     {:wordform (str stem "ī") :gloss pos-gloss :part_of_speech "adjective" :number "singular" :gender "masculine" :degree "positive" :case_ "dative" :lexeme_id (ll df)}
     {:wordform (str stem "em") :gloss pos-gloss :part_of_speech "adjective" :number "singular" :gender "masculine" :degree "positive" :case_ "accusative" :lexeme_id (ll df)}
     {:wordform (str stem "ī") :gloss pos-gloss :part_of_speech "adjective" :number "singular" :gender "masculine" :degree "positive" :case_ "ablative" :lexeme_id (ll df)}
     {:wordform (str stem "ēs") :gloss pos-gloss :part_of_speech "adjective" :number "plural" :gender "masculine" :degree "positive" :case_ "nominative" :lexeme_id (ll df)}
     {:wordform (if pl-gen-ium? (str stem "ium") (str stem "um")) :gloss pos-gloss :part_of_speech "adjective" :number "plural" :gender "masculine" :degree "positive" :case_ "genitive" :lexeme_id (ll df)}
     {:wordform (str stem "ibus") :gloss pos-gloss :part_of_speech "adjective" :number "plural" :gender "masculine" :degree "positive" :case_ "dative" :lexeme_id (ll df)}
     {:wordform (str stem "ēs") :gloss pos-gloss :part_of_speech "adjective" :number "plural" :gender "masculine" :degree "positive" :case_ "accusative" :lexeme_id (ll df)}
     {:wordform (str stem "ibus") :gloss pos-gloss :part_of_speech "adjective" :number "plural" :gender "masculine" :degree "positive" :case_ "ablative" :lexeme_id (ll df)}])

(defn get-comparative-forms-m [{:keys [stem comp-gloss mf df pl-gen-ium?]}]
  [;; positive masculine
   {:wordform (str stem "ior") :gloss comp-gloss :part_of_speech "adjective" :number "singular" :gender "masculine" :degree "comparative" :case_ "nominative" :lexeme_id (ll df)}
   {:wordform (str stem "iōris") :gloss comp-gloss :part_of_speech "adjective" :number "singular" :gender "masculine" :degree "comparative" :case_ "genitive" :lexeme_id (ll df)}
   {:wordform (str stem "iōrī") :gloss comp-gloss :part_of_speech "adjective" :number "singular" :gender "masculine" :degree "comparative" :case_ "dative" :lexeme_id (ll df)}
   {:wordform (str stem "iōrem") :gloss comp-gloss :part_of_speech "adjective" :number "singular" :gender "masculine" :degree "comparative" :case_ "accusative" :lexeme_id (ll df)}
   {:wordform (str stem "iōre") :gloss comp-gloss :part_of_speech "adjective" :number "singular" :gender "masculine" :degree "comparative" :case_ "ablative" :lexeme_id (ll df)}
   {:wordform (str stem "iōrēs") :gloss comp-gloss :part_of_speech "adjective" :number "plural" :gender "masculine" :degree "comparative" :case_ "nominative" :lexeme_id (ll df)}
   {:wordform  (str stem "iōrum") :gloss comp-gloss :part_of_speech "adjective" :number "plural" :gender "masculine" :degree "comparative" :case_ "genitive" :lexeme_id (ll df)}
     {:wordform (str stem "iōribus") :gloss comp-gloss :part_of_speech "adjective" :number "plural" :gender "masculine" :degree "comparative" :case_ "dative" :lexeme_id (ll df)}
     {:wordform (str stem "iōrēs") :gloss comp-gloss :part_of_speech "adjective" :number "plural" :gender "masculine" :degree "comparative" :case_ "accusative" :lexeme_id (ll df)}
     {:wordform (str stem "iōribus") :gloss comp-gloss :part_of_speech "adjective" :number "plural" :gender "masculine" :degree "comparative" :case_ "ablative" :lexeme_id (ll df)}])

(defn get-superlative-forms-m [{:keys [sup-m sup-gloss df]}]
  (let [stem (subs sup-m 0 (- (count sup-m) 2))]
    [ ;; superlative masculine
     {:wordform (str stem "us") :gloss sup-gloss :part_of_speech "adjective" :number "singular" :gender "masculine" :degree "superlative" :case_ "nominative" :lexeme_id (ll df)}
     {:wordform (str stem "ī") :gloss sup-gloss :part_of_speech "adjective" :number "singular" :gender "masculine" :degree "superlative" :case_ "genitive" :lexeme_id (ll df)}
     {:wordform (str stem "ō") :gloss sup-gloss :part_of_speech "adjective" :number "singular" :gender "masculine" :degree "superlative" :case_ "dative" :lexeme_id (ll df)}
     {:wordform (str stem "um") :gloss sup-gloss :part_of_speech "adjective" :number "singular" :gender "masculine" :degree "superlative" :case_ "accusative" :lexeme_id (ll df)}
     {:wordform (str stem "ō") :gloss sup-gloss :part_of_speech "adjective" :number "singular" :gender "masculine" :degree "superlative" :case_ "ablative" :lexeme_id (ll df)}
     {:wordform (str stem "ī") :gloss sup-gloss :part_of_speech "adjective" :number "plural" :gender "masculine" :degree "superlative" :case_ "nominative" :lexeme_id (ll df)}
     {:wordform  (str stem "ōrum") :gloss sup-gloss :part_of_speech "adjective" :number "plural" :gender "masculine" :degree "superlative" :case_ "genitive" :lexeme_id (ll df)}
     {:wordform (str stem "īs") :gloss sup-gloss :part_of_speech "adjective" :number "plural" :gender "masculine" :degree "superlative" :case_ "dative" :lexeme_id (ll df)}
     {:wordform (str stem "ī") :gloss sup-gloss :part_of_speech "adjective" :number "plural" :gender "masculine" :degree "superlative" :case_ "accusative" :lexeme_id (ll df)}
     {:wordform (str stem "īs") :gloss sup-gloss :part_of_speech "adjective" :number "plural" :gender "masculine" :degree "superlative" :case_ "ablative" :lexeme_id (ll df)}]))

(defn get-superlative-forms-f [{:keys [sup-m sup-gloss df]}]
  (let [stem (subs sup-m 0 (- (count sup-m) 2))]
    [ ;; superlative feminine
     {:wordform (str stem "a") :gloss sup-gloss :part_of_speech "adjective" :number "singular" :gender "feminine" :degree "superlative" :case_ "nominative" :lexeme_id (ll df)}
     {:wordform (str stem "ae") :gloss sup-gloss :part_of_speech "adjective" :number "singular" :gender "feminine" :degree "superlative" :case_ "genitive" :lexeme_id (ll df)}
     {:wordform (str stem "ae") :gloss sup-gloss :part_of_speech "adjective" :number "singular" :gender "feminine" :degree "superlative" :case_ "dative" :lexeme_id (ll df)}
     {:wordform (str stem "am") :gloss sup-gloss :part_of_speech "adjective" :number "singular" :gender "feminine" :degree "superlative" :case_ "accusative" :lexeme_id (ll df)}
     {:wordform (str stem "ā") :gloss sup-gloss :part_of_speech "adjective" :number "singular" :gender "feminine" :degree "superlative" :case_ "ablative" :lexeme_id (ll df)}
     {:wordform (str stem "ae") :gloss sup-gloss :part_of_speech "adjective" :number "plural" :gender "feminine" :degree "superlative" :case_ "nominative" :lexeme_id (ll df)}
     {:wordform  (str stem "ārum") :gloss sup-gloss :part_of_speech "adjective" :number "plural" :gender "feminine" :degree "superlative" :case_ "genitive" :lexeme_id (ll df)}
     {:wordform (str stem "īs") :gloss sup-gloss :part_of_speech "adjective" :number "plural" :gender "feminine" :degree "superlative" :case_ "dative" :lexeme_id (ll df)}
     {:wordform (str stem "ās") :gloss sup-gloss :part_of_speech "adjective" :number "plural" :gender "feminine" :degree "superlative" :case_ "accusative" :lexeme_id (ll df)}
     {:wordform (str stem "īs") :gloss sup-gloss :part_of_speech "adjective" :number "plural" :gender "feminine" :degree "superlative" :case_ "ablative" :lexeme_id (ll df)}]))

(defn get-adjective-forms-mf [{:keys [mfn sg-gen pos-gloss comp-gloss sup-gloss pl-gen-ium? df include-comparative? include-superlative?] :as args}]
  (let [stem (subs sg-gen 0 (- (count sg-gen) 2))
        femize (fn [forms] (map (fn [form] (assoc form :gender "feminine")) forms))
        m-pos (get-positive-forms-m (merge args {:stem stem}))
        m-comp (get-comparative-forms-m (merge args {:stem stem}))]
    (concat
     m-pos
     (femize m-pos)
     (when include-comparative? m-comp)
     (when include-comparative? (femize m-comp))
     (when include-superlative?
       (get-superlative-forms-m args))
     (when include-superlative?
       (get-superlative-forms-f args)))
    ))

(defn get-positive-forms-n [{:keys [stem pos-gloss mfn df pl-gen-ium?]}]
  [;; positive neuter
   {:wordform mfn :gloss pos-gloss :part_of_speech "adjective" :number "singular" :gender "neuter" :degree "positive" :case_ "nominative" :lexeme_id (ll df)}
   {:wordform (str stem "is") :gloss pos-gloss :part_of_speech "adjective" :number "singular" :gender "neuter" :degree "positive" :case_ "genitive" :lexeme_id (ll df)}
   {:wordform (str stem "ī") :gloss pos-gloss :part_of_speech "adjective" :number "singular" :gender "neuter" :degree "positive" :case_ "dative" :lexeme_id (ll df)}
   {:wordform mfn :gloss pos-gloss :part_of_speech "adjective" :number "singular" :gender "neuter" :degree "positive" :case_ "accusative" :lexeme_id (ll df)}
   {:wordform (str stem "ī") :gloss pos-gloss :part_of_speech "adjective" :number "singular" :gender "neuter" :degree "positive" :case_ "ablative" :lexeme_id (ll df)}
   {:wordform (str stem "ia") :gloss pos-gloss :part_of_speech "adjective" :number "plural" :gender "neuter" :degree "positive" :case_ "nominative" :lexeme_id (ll df)}
   {:wordform (if pl-gen-ium? (str stem "ium") (str stem "um")) :gloss pos-gloss :part_of_speech "adjective" :number "plural" :gender "neuter" :degree "positive" :case_ "genitive" :lexeme_id (ll df)}
   {:wordform (str stem "ibus") :gloss pos-gloss :part_of_speech "adjective" :number "plural" :gender "neuter" :degree "positive" :case_ "dative" :lexeme_id (ll df)}
   {:wordform (str stem "ia") :gloss pos-gloss :part_of_speech "adjective" :number "plural" :gender "neuter" :degree "positive" :case_ "accusative" :lexeme_id (ll df)}
   {:wordform (str stem "ibus") :gloss pos-gloss :part_of_speech "adjective" :number "plural" :gender "neuter" :degree "positive" :case_ "ablative" :lexeme_id (ll df)}])

(defn get-comparative-forms-n [{:keys [stem comp-gloss n df pl-gen-ium?]}]
  [;; comparative masculine
   {:wordform (str stem "ius") :gloss comp-gloss :part_of_speech "adjective" :number "singular" :gender "neuter" :degree "comparative" :case_ "nominative" :lexeme_id (ll df)}
   {:wordform (str stem "iōris") :gloss comp-gloss :part_of_speech "adjective" :number "singular" :gender "neuter" :degree "comparative" :case_ "genitive" :lexeme_id (ll df)}
   {:wordform (str stem "iōrī") :gloss comp-gloss :part_of_speech "adjective" :number "singular" :gender "neuter" :degree "comparative" :case_ "dative" :lexeme_id (ll df)}
   {:wordform (str stem "ius") :gloss comp-gloss :part_of_speech "adjective" :number "singular" :gender "neuter" :degree "comparative" :case_ "accusative" :lexeme_id (ll df)}
   {:wordform (str stem "iōre") :gloss comp-gloss :part_of_speech "adjective" :number "singular" :gender "neuter" :degree "comparative" :case_ "ablative" :lexeme_id (ll df)}
   {:wordform (str stem "iōra") :gloss comp-gloss :part_of_speech "adjective" :number "plural" :gender "neuter" :degree "comparative" :case_ "nominative" :lexeme_id (ll df)}
   {:wordform  (str stem "iōrum") :gloss comp-gloss :part_of_speech "adjective" :number "plural" :gender "neuter" :degree "comparative" :case_ "genitive" :lexeme_id (ll df)}
     {:wordform (str stem "iōribus") :gloss comp-gloss :part_of_speech "adjective" :number "plural" :gender "neuter" :degree "comparative" :case_ "dative" :lexeme_id (ll df)}
     {:wordform (str stem "iōra") :gloss comp-gloss :part_of_speech "adjective" :number "plural" :gender "neuter" :degree "comparative" :case_ "accusative" :lexeme_id (ll df)}
     {:wordform (str stem "iōribus") :gloss comp-gloss :part_of_speech "adjective" :number "plural" :gender "neuter" :degree "comparative" :case_ "ablative" :lexeme_id (ll df)}])

(defn get-superlative-forms-n [{:keys [sup-m sup-gloss df]}]
  (let [stem (subs sup-m 0 (- (count sup-m) 2))]
    [ ;; superlative neuter
     {:wordform (str stem "um") :gloss sup-gloss :part_of_speech "adjective" :number "singular" :gender "neuter" :degree "superlative" :case_ "nominative" :lexeme_id (ll df)}
     {:wordform (str stem "ī") :gloss sup-gloss :part_of_speech "adjective" :number "singular" :gender "neuter" :degree "superlative" :case_ "genitive" :lexeme_id (ll df)}
     {:wordform (str stem "ō") :gloss sup-gloss :part_of_speech "adjective" :number "singular" :gender "neuter" :degree "superlative" :case_ "dative" :lexeme_id (ll df)}
     {:wordform (str stem "um") :gloss sup-gloss :part_of_speech "adjective" :number "singular" :gender "neuter" :degree "superlative" :case_ "accusative" :lexeme_id (ll df)}
     {:wordform (str stem "ō") :gloss sup-gloss :part_of_speech "adjective" :number "singular" :gender "neuter" :degree "superlative" :case_ "ablative" :lexeme_id (ll df)}
     {:wordform (str stem "a") :gloss sup-gloss :part_of_speech "adjective" :number "plural" :gender "neuter" :degree "superlative" :case_ "nominative" :lexeme_id (ll df)}
     {:wordform  (str stem "ōrum") :gloss sup-gloss :part_of_speech "adjective" :number "plural" :gender "neuter" :degree "superlative" :case_ "genitive" :lexeme_id (ll df)}
     {:wordform (str stem "īs") :gloss sup-gloss :part_of_speech "adjective" :number "plural" :gender "neuter" :degree "superlative" :case_ "dative" :lexeme_id (ll df)}
     {:wordform (str stem "a") :gloss sup-gloss :part_of_speech "adjective" :number "plural" :gender "neuter" :degree "superlative" :case_ "accusative" :lexeme_id (ll df)}
     {:wordform (str stem "īs") :gloss sup-gloss :part_of_speech "adjective" :number "plural" :gender "neuter" :degree "superlative" :case_ "ablative" :lexeme_id (ll df)}]))

(defn get-adjective-forms-n [{:keys [mfn sg-gen pos-gloss comp-gloss sup-gloss pl-gen-ium? df include-comparative? include-superlative? sg-gen] :as args}]
  (let [stem (subs sg-gen 0 (- (count sg-gen) 2))]
    (concat
     (get-positive-forms-n (merge args {:stem stem}))
     (when include-comparative?
       (get-comparative-forms-n (merge args {:stem stem})))
     (when include-superlative?
       (get-superlative-forms-n (merge args))))))

(defn get-adjective-forms [{:keys [mfn sg-gen sup-m pos-gloss comp-gloss sup-gloss pl-gen-ium? include-comparative? include-superlative?] :as args}]
  (let [mfn (clojure.string/trim (:dictionary-form args))
        df mfn
        include-comparative? (or comp-gloss (true? include-comparative?))
        include-superlative? (or sup-gloss (true? include-superlative?))
        comp-gloss (or comp-gloss (str "more " pos-gloss))
        sup-gloss (or sup-gloss (str "very " pos-gloss))
        mf-forms (get-adjective-forms-mf (merge {:mfn mfn :df df :include-comparative? include-comparative? :include-superlative? include-superlative?} args))
        n-forms (get-adjective-forms-n (merge {:mfn mfn :df df :include-comparative? include-comparative? :include-superlative? include-superlative?} args))]
    (concat
     mf-forms
     n-forms)
    ))

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
  (let [meanings (get-adjective-forms args-map)]
    (doseq [meaning meanings]
      (insert-adj-meaning! meaning))))
