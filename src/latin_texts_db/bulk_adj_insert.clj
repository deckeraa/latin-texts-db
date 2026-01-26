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

;; (defn insert-verb-meanings! [dict-entry gender sn-gloss sg-gloss pn-gloss pg-gloss]
;;   (let [[nom gen] (clojure.string/split dict-entry #", ")
;;         declension (genitive->declension gen)
;;         meanings (case declension
;;                    1 (get-noun-forms-first-declension nom gen gender sn-gloss sg-gloss pn-gloss pg-gloss)
;;                    2 (case gender 
;;                        "masculine" (get-noun-forms-second-declension-mf nom gen "masculine" sn-gloss sg-gloss pn-gloss pg-gloss)
;;                        "neuter"    (get-noun-forms-second-declension-neuter    nom gen      sn-gloss sg-gloss pn-gloss pg-gloss)
;;                        "feminine"  (get-noun-forms-second-declension-mf nom gen "feminine"  sn-gloss sg-gloss pn-gloss pg-gloss)
;;                        ))]
;;     (doseq [meaning meanings]
;;       (insert-verb-meaning! meaning)))
;;   )

;; (defn insert-single-adj-from-args! [args]
;;   (let [meanings (apply get-verb-forms args)]
;;       (doseq [meaning meanings]
;;         (insert-adj-meaning! meaning))))

;; (defn insert-all! []
;;   (doseq [args wordlist]
;;     (let [meanings (apply get-verb-forms args)]
;;       (doseq [meaning meanings]
;;         (insert-ajd-meaning! meaning)))))
