(ns latin-texts-db.migrations.basic-tables
  (:require
   [next.jdbc :as jdbc]
   [honey.sql :as sql]
   [honey.sql.helpers :as h :refer [create-table with-columns drop-table]]))

;; TODO consolidate db-spec and config into one place
;; Will probably do this once I get migratus working
(def db-spec
  {:dbtype "sqlite"
   :dbname "resources/db/latin.db"})   ;; relative to project root

(def config {:store         :database
             :migration-dir "migrations"
             :db            db-spec})

(def ds (jdbc/get-datasource db-spec))

(defn run-statements! [conn stmts]
  (doseq [stmt stmts]
    (try
      ;; (println "About to execute " stmt)
      (jdbc/execute! conn (sql/format stmt))
      (catch Exception e
        (println "Failed to execute " stmt (sql/format stmt))
        (throw e)))))

(defn migrate-up [conn]
  (run-statements!
   conn
   [
    (create-table
     :genders
     (with-columns
       [:gender_key  :text    [:primary-key]]
       [:label       :text    :not-null]
       [:description :text]))             

    (create-table
     :numbers
     (with-columns
       [:number_key  :text    [:primary-key]]
       [:label       :text    :not-null])) 

    (create-table
     :cases
     (with-columns
       [:case_key    :text    [:primary-key]]
       [:label       :text    :not-null]   
       [:abbreviation :text]))               

    (create-table
     :persons
     (with-columns
       [:person_key  :integer [:primary-key]]
       [:label       :text    :not-null])) 

    (create-table
     :tenses
     (with-columns
       [:tense_key   :text    [:primary-key]]
       [:label       :text    :not-null])) 

    (create-table
     :moods
     (with-columns
       [:mood_key    :text    [:primary-key]]
       [:label       :text    :not-null])) 

    (create-table
     :voices
     (with-columns
       [:voice_key   :text    [:primary-key]]
       [:label       :text    :not-null])) 

    (create-table
     :degrees
     (with-columns
       [:degree_key  :text    [:primary-key]]
       [:label       :text    :not-null])) 

    (create-table
     :parts_of_speech
     (with-columns
       [:pos_key     :text    [:primary-key]]
       [:label       :text    :not-null]))                

    (create-table
     :texts
     (with-columns
       [:text_id     :integer :primary-key :autoincrement]
       [:title       :text    :not-null]
       [:author      :text]
       [:description :text]
       [:reference-url :text]
       ))

       (create-table
     :lexemes
     (with-columns
       [:lexeme_id       :integer :primary-key :autoincrement]
       [:dictionary-form :text]))

    (create-table
     :meanings
     (with-columns
       [:meaning_id     :integer :primary-key :autoincrement]
       [:lexeme_id      :integer :references [:lexemes :lexeme_id]]
       [:wordform       :text]     ; e.g. "haec", "ferre", "tulit"
       ;; Core semantic content
       [:gloss          :text]     ; short English translation / gloss
       [:definition     :text] ; longer explanation or dictionary-style definition
       [:usage_note     :text] ; e.g. "poetic", "post-Augustan", "with dative", "rare"

       ;; Morphological / grammatical properties (most important for Latin)
       [:part_of_speech :text :references [:parts_of_speech :pos_key]]
       [:person         :integer :references [:persons :person_key]]
       [:number         :text :references [:numbers :number_key]]
       [:gender         :text :references [:genders :gender_key]]
       ;; called case_ to avoid conflict with a SQL keyword
       [:case_           :text :references [:cases :case_key]]
       [:tense          :text :references [:tenses :tense_key]]
       [:mood           :text :references [:moods :mood_key]]
       [:voice          :text :references [:voices :voice_key]]

       ;; Additional useful flags / properties
       [:degree         :text :references [:degrees :degree_key]]
       [:declension     :integer]       ; 1–5 or NULL
       [:conjugation    :integer]       ; 1–4, or 0 for irregular

       ;; Metadata
       [:confidence     :integer]       ; 0–100, optional
       [:source         :text] ; "Lewis&Short", "OLD", "Gaffiot", "user", etc.
       [:created_at     :timestamp [:default [:raw "CURRENT_TIMESTAMP"]]]
       [:updated_at     :timestamp]))
    
    (create-table
     :tokens
     (with-columns
       [:token_id       :integer :primary-key :autoincrement]
       [:text_id        :integer :references [:texts   :text_id]]
       [:witness_id     :integer]
       [:prev_token_id  :integer :references [:tokens :token_id]]
       [:next_token_id  :integer :references [:tokens :token_id]]
       [:punctuation_preceding :text]
       [:punctuation_trailing :text]
       [:wordform       :text    :not-null]
       [:meaning_id     :integer :references [:meanings :meaning-id]]
       [:gloss_override :text]))

    (create-table
     :footnotes
     (with-columns
       [:footnote_id :integer :primary-key :autoincrement]
       [:token_id    :integer :references [:tokens :token_id]]
       [:text        :text]
       [:start_token_id  :integer :references [:tokens :token_id]]
       [:end_token_id  :integer :references [:tokens :token_id]]))

    {:insert-into [:genders]
     :values [{:gender-key "masculine"              :label "Masculine"              :description "masculine gender"}
              {:gender-key "feminine"               :label "Feminine"               :description "feminine gender"}
              {:gender-key "neuter"                 :label "Neuter"                 :description "neuter gender"}
              {:gender-key "common"                 :label "Common"                 :description "common gender (masc or fem)"}
              {:gender-key "masculine-or-feminine"  :label "Masculine or Feminine"  :description "either masculine or feminine depending on referent"}]}

    {:insert-into [:numbers]
     :values [{:number-key "singular" :label "Singular"}
              {:number-key "plural"   :label "Plural"}]}

    {:insert-into [:cases]
     :values [{:case-key "nominative" :label "Nominative" :abbreviation "nom."}
              {:case-key "genitive"    :label "Genitive"    :abbreviation "gen."}
              {:case-key "dative"      :label "Dative"      :abbreviation "dat."}
              {:case-key "accusative"  :label "Accusative"  :abbreviation "acc."}
              {:case-key "ablative"    :label "Ablative"    :abbreviation "abl."}
              {:case-key "vocative"    :label "Vocative"    :abbreviation "voc."}
              {:case-key "locative"    :label "Locative"    :abbreviation "loc."}]}

    {:insert-into [:persons]
     :values [{:person-key 1 :label "first person"}
              {:person-key 2 :label "second person"}
              {:person-key 3 :label "third person"}]}

    {:insert-into [:tenses]
     :values [{:tense-key "present"        :label "Present"}
              {:tense-key "imperfect"      :label "Imperfect"}
              {:tense-key "future"         :label "Future"}
              {:tense-key "perfect"        :label "Perfect"}
              {:tense-key "pluperfect"     :label "Pluperfect"}
              {:tense-key "future-perfect" :label "Future Perfect"}]}

    {:insert-into [:moods]
     :values [{:mood-key "indicative"  :label "Indicative"}
              {:mood-key "subjunctive" :label "Subjunctive"}
              {:mood-key "imperative"  :label "Imperative"}
              {:mood-key "infinitive"  :label "Infinitive"}]}

    {:insert-into [:voices]
     :values [{:voice-key "active"        :label "Active"}
              {:voice-key "passive"       :label "Passive"}
              {:voice-key "deponent"      :label "Deponent"}
              {:voice-key "semi-deponent" :label "Semi-deponent"}]}

    {:insert-into [:degrees]
     :values [{:degree-key "positive"    :label "Positive"}
              {:degree-key "comparative" :label "Comparative"}
              {:degree-key "superlative" :label "Superlative"}]}

    {:insert-into [:parts_of_speech]
     :values [{:pos-key "noun"        :label "Noun"}
              {:pos-key "verb"        :label "Verb"}
              {:pos-key "adjective"   :label "Adjective"}
              {:pos-key "participle"  :label "Participle"}
              {:pos-key "adverb"      :label "Adverb"}
              {:pos-key "pronoun"     :label "Pronoun"}
              {:pos-key "preposition" :label "Preposition"}
              {:pos-key "conjunction" :label "Conjunction"}
              {:pos-key "particle"    :label "Particle"}
              {:pos-key "interjection":label "Interjection"}]}
    ]))

(defn migrate-down [conn]
  (run-statements!
   conn
   [(h/drop-table :meanings)
    (h/drop-table :lexemes)
    (h/drop-table :tokens)
    (h/drop-table :texts)
    (h/drop-table :parts_of_speech)
    (h/drop-table :degrees)
    (h/drop-table :voices)
    (h/drop-table :moods)
    (h/drop-table :tenses)
    (h/drop-table :persons)
    (h/drop-table :cases)
    (h/drop-table :numbers)
    (h/drop-table :genders)
    ]))

