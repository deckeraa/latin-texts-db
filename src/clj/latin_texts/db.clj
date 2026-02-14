(ns latin-texts.db
  (:require [next.jdbc :as jdbc]
            [migratus.core :as migratus]
            [honey.sql :as sql]
            [honey.sql.helpers :as h]
            [latin-texts.migrations.basic-tables]))

;; useful HoneySQL ref: https://github.com/seancorfield/honeysql/blob/develop/doc/clause-reference.md

(def db-spec
  {:dbtype "sqlite"
   :dbname "resources/db/latin.db"})   ;; relative to project root

(def config {:store         :database
             :migration-dir "migrations"
             :db            db-spec})

(def ds (jdbc/get-datasource db-spec))

(defn migrate! [] (migratus/migrate config))
(defn rollback! [] (migratus/rollback config))

(defn do! [stmt]
  (try 
    (jdbc/execute! ds (sql/format stmt) {:return-keys true})
    (catch Exception e
      (println "Failed to execute stmt: " stmt)
      (println (sql/format stmt))
      (throw e))))

(defn ll "lookup/load lexeme" [dictionary-form]
  (let [lexeme-id (-> (do! {:select [:lexeme_id]
                            :from :lexemes
                            :where [:= :dictionary-form dictionary-form]})
                      first
                      :lexemes/lexeme_id)]
    (if lexeme-id
      lexeme-id
      ;; else insert a new lexeme
      (-> (do! {:insert-into [:lexemes]
                :values [{:dictionary-form dictionary-form}]
                :returning :lexeme_id})
          first
          :lexemes/lexeme_id))))

(defn load-lexeme-with-all-associated-meanings [dictionary-form]
  (let [lexeme (-> (do! {:select [:*]
                         :from :lexemes
                         :where [:= :dictionary-form dictionary-form]})
                   first)
        meanings (-> (do! {:select [:*]
                         :from :meanings
                         :where [:= :lexeme_id (:lexemes/lexeme_id lexeme)]}))]
    {:lexeme lexeme
     :meanings meanings}))

(defn split-preceding-trailing-punctuation [s]
  (let [s* (clojure.string/trim s)
        [_ lead wordform trail]
        (re-matches #"^(\p{P}+)?(.*?)(\p{P}+)?$" s*)]
    {:punctuation_preceding
     (str lead
          (when (clojure.string/starts-with? s "\t") "\t"))
     :wordform wordform
     :punctuation_trailing
     (str trail ;; TODO respect the number of returns and newlines
          (when (clojure.string/ends-with? s "\n") "\n"))}))

;; (defn split-preceding-trailing-punctuation [s]
;;   (let [[_ lead wordform trail]
;;         (re-matches #"^(\p{P}+)?([\s\S]*?)(\p{P}+)?$"
;;                     s)]
;;     {:punctuation_preceding lead
;;      :wordform wordform
;;      :punctuation_trailing trail}))

;; (defn split-preceding-trailing-punctuation [s]
;;   (let [[_ lead wordform trail]
;;         (re-matches #"^(\p{P}+)?(\p{L}[\p{L}\p{M}]*?)(\p{P}+)?$" s)]
;;     {:punctuation_preceding lead
;;      :wordform wordform
;;      :punctuation_trailing trail}))

(defn insert-token-into-db* [text-id prev-id tokens]
  (println (str "On " (vec (first tokens)) ", next token " (str (first (rest tokens)))))
  (let [{:keys [:punctuation_preceding :wordform :punctuation_trailing]} (split-preceding-trailing-punctuation (first tokens))
        _ (println (first tokens) " split into " punctuation_preceding " - " wordform " - " punctuation_trailing)
        insert-result (do! {:insert-into [:tokens]
                            :values [{:text_id text-id
                                      :wordform wordform
                                      :prev_token_id prev-id
                                      :punctuation_preceding punctuation_preceding
                                      :punctuation_trailing punctuation_trailing
                                      }]
                            :returning :token_id})
        new-token-id (:tokens/token_id (first insert-result))]
    (do! {:update :tokens
          :set {:next_token_id new-token-id}
          :where [:= :token_id prev-id]})
    (println "new-token-id" new-token-id)
    (when (not (empty? (rest tokens)))
      (insert-token-into-db* text-id new-token-id (rest tokens))
      )))

(defn id->meaning* [meaning-or-meaning-id]
  (if (map? meaning-or-meaning-id)
    meaning-or-meaning-id
    (-> (do! {:select [:*]
              :from :meanings
              :where [:= :meaning_id meaning-or-meaning-id]})
        first)))

(defn id->token [token-or-token-id]
  (if (map? token-or-token-id)
    token-or-token-id
    (-> (do! {:select [:*]
              :from :tokens
              :where [:= :token_id token-or-token-id]})
        first)))

(defn get-lexeme-for-meaning [meaning-or-meaning-id]
  (let [lexeme-id (:meanings/lexeme_id (id->meaning* meaning-or-meaning-id))
        lexeme (first (do! {:select [:*]
                            :from :lexemes
                            :where [:= :lexeme_id lexeme-id]}))]
    lexeme))

(defn id->meaning [meaning-or-meaning-id]
  (as-> meaning-or-meaning-id $
    (id->meaning* $)
    (assoc $ :lexeme (get-lexeme-for-meaning $))))

(defn id->footnote [footnote-or-footnote-id]
  (if (map? footnote-or-footnote-id)
    footnote-or-footnote-id
    (-> (do! {:select [:*]
              :from :footnotes
              :where [:= :footnote_id footnote-or-footnote-id]})
        first)))

(defn token->footnotes [token-or-token-id]
  (let [token-id (or (:tokens/token_id token-or-token-id) token-or-token-id)]
    (do! {:select [:*]
          :from :footnotes
          :where [:= :token_id token-id]})))

(defn token->meaning [token-or-token-id]
  (let [token (id->token token-or-token-id)]
    (when (and token (:tokens/meaning_id token))
      (id->meaning (:tokens/meaning_id token)))))

(defn text->tokens [text-id]
  ;; TODO should probably make a 'first token' column in texts and use that instead of assuming the the first token will be the first in SQL row order
  (do! {:select [:*]
        :from :tokens
        :where [:= :text_id text-id]}))

(defn get-token-range [start-id end-id]
  (when (and start-id end-id)
    (let [step (fn step [id]
                 (when id
                   (when-let [token (id->token id)]
                     (cons token
                           (when-not (= id end-id)
                             (step (:tokens/next_token_id token)))))))]
      (step start-id))))

(defn get-lexeme-for-token [token-or-token-id]
  (let [meaning-id (:tokens/meaning_id (id->token token-or-token-id))
        
        lexeme (get-lexeme-for-meaning meaning-id)]
    lexeme))

(defn remove-enclitic-ne [s]
  (when (clojure.string/ends-with? s "ne")
    (subs s 0 (- (count s) 2))))

(defn remove-enclitic-que [s]
  (when (clojure.string/ends-with? s "que")
    (subs s 0 (- (count s) 3))))

(defn remove-enclitic-nam [s]
  (when (clojure.string/ends-with? s "nam")
    (subs s 0 (- (count s) 3))))

(defn get-potential-meanings-of-wordform [wordform]
  (let [wordforms-to-search
        (remove
         nil?
         [wordform
          (clojure.string/lower-case wordform)
          (remove-enclitic-que wordform)
          (remove-enclitic-que
           (clojure.string/lower-case wordform))
          (remove-enclitic-ne wordform)
          (remove-enclitic-ne
           (clojure.string/lower-case wordform))
          (remove-enclitic-nam wordform)
          (remove-enclitic-nam
           (clojure.string/lower-case wordform))])
        ;; TODO the SQL query for looking up potential meanings is somewhat slow ~3ms per call when I measured. This is on SQLite with an index for meanings/wordform.
        potential-meanings
        (do! {:select [:*]
              :from :meanings
              :where [:in :wordform wordforms-to-search]})]
    (mapv (fn [meaning]
            (assoc meaning :lexeme (get-lexeme-for-meaning meaning))) potential-meanings)))

(defn decorate-token [token]
  (as-> token $
    (if-let [v (:tokens/meaning_id $)]
      (let [meaning (id->meaning v)]
        (assoc $ :meaning
               (assoc meaning
                      :lexeme
                      (get-lexeme-for-meaning meaning))))
      ;; only look up potential meanings if a meaning isn't already set on the token
      (assoc $ :potential-meanings
             (get-potential-meanings-of-wordform
              (:tokens/wordform token))))
    (assoc $ :lexeme (get-lexeme-for-token token))
    (assoc $ :footnotes (token->footnotes token))))

(defn get-token [token-id]
  (let [token (-> (do! {:select [:*]
                        :from :tokens
                        :where [:= :token_id token-id]})
                  first)]
    (decorate-token token)))

(defn update-token-field! [token-id field value]
  (let [k (keyword "tokens" field)]
    (when (nil? (id->token token-id))
      (throw (Exception. (str "Token " token-id " not found."))))
    (do! {:update :tokens
          :set {k value}
          :where [:= :token_id token-id]})))

(defn set-meaning-for-token! [token-id meaning-id]
  (let [meaning (first (do! {:select [:meaning_id]
                             :from :meanings
                             :where [:= :meaning_id meaning-id]}))]
    (if meaning
      (do! {:update :tokens
            :set {:meaning_id meaning-id}
            :where [:= :token_id token-id]})
      (println "meaning " meaning-id " not found in db."))))

(defn unset-meaning-for-token! [token-id]
  (println "In unset-meaning-for-token!" token-id)
  (do! {:update :tokens
        :set {:meaning_id nil}
        :where [:= :token_id token-id]}))

(defn insert-meaning! [lexeme-dictionary-form meaning]
  (let [lexeme_id (ll lexeme-dictionary-form)]
    (-> (do! {:insert-into [:meanings]
              :values [(assoc meaning :lexeme_id lexeme_id)]
              :returning :*})
        first)))

(defn get-lexemes []
  (do! {:select [:*]
        :from :lexemes}))

(defn get-texts []
  (do! {:select [:*]
        :from :texts}))
