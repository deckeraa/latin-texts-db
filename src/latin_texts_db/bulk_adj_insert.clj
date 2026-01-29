(ns latin-texts-db.bulk-adj-insert
  (:require
   [next.jdbc :as jdbc]
   [migratus.core :as migratus]
   [honey.sql :as sql]
   [honey.sql.helpers :as h]
   [latin-texts-db.migrations.basic-tables]
   [latin-texts-db.db :refer [ds do! ll]]
   [latin-texts-db.bulk-adj-insert-three-term-first-and-second]))

(defn quickprint [wordform]
  (clojure.string/join " " [(:wordform wordform) (:gloss wordform) (:gender wordform)]))

;; (defn num-terms [dictionary-form]
;;   (count (clojure.string/split dictionary-form #",")))

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

(defn insert-adj-meanings! [{:keys [dictionary-form sup-m pos-gloss comp-gloss sup-gloss include-comparative? include-superlative? gen-ius?] :as args}]
  (let [meanings
        (cond
          (= 3 (count (clojure.string/split dictionary-form #", ")))
          (latin-texts-db.bulk-adj-insert-three-term-first-and-second/insert-single-adj-from-args! args))]
    (doseq [meaning meanings]
      (insert-adj-meaning! meaning))))

;; (insert-adj-meanings! {:dictionary-form "sōlus, sōla, sōlum" :pos-gloss "alone" :gen-ius? true})

;; (defn insert-single-adj-from-args! [args]
;;   (let [meanings (apply get-verb-forms args)]
;;       (doseq [meaning meanings]
;;         (insert-adj-meaning! meaning))))

;; (defn insert-all! []
;;   (doseq [args wordlist]
;;     (let [meanings (apply get-verb-forms args)]
;;       (doseq [meaning meanings]
;;         (insert-ajd-meaning! meaning)))))
