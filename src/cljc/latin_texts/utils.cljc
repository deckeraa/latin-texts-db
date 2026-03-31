(ns latin-texts.utils
  (:require 
    [clojure.string :as str]))

(defn remove-macrons [s]
  (-> s
      (clojure.string/replace #"ā" "a")
      (clojure.string/replace #"Ā" "A")
      (clojure.string/replace #"ē" "e")
      (clojure.string/replace #"Ē" "E")
      (clojure.string/replace #"ī" "i")
      (clojure.string/replace #"Ī" "I")
      (clojure.string/replace #"ō" "o")
      (clojure.string/replace #"Ō" "O")
      (clojure.string/replace #"ū" "u")
      (clojure.string/replace #"Ū" "U")
      (clojure.string/replace #"ȳ" "y")))

(defn sg-gloss-guess [s]
  (let [xs (clojure.string/split s #", ")
        xs (map #(str % "'s") xs)]
    (clojure.string/join ", " xs)))

(defn pluralize-single-word [s]
  (cond
    (clojure.string/ends-with? s "ty")
    (clojure.string/replace s #"ty$" "ties")
    ;;
    (clojure.string/ends-with? s "ss")
    (str s "es")
    ;;
    :default
    (str s "s")
    ))

(defn pn-gloss-guess [s]
  (let [xs (clojure.string/split s #", ")
        xs (map pluralize-single-word xs)]
    (clojure.string/join ", " xs)))

(defn pg-gloss-guess [s]
  (let [xs (clojure.string/split s #", ")
        xs (map pluralize-single-word xs)]
    (str "of the " (clojure.string/join ", " xs))))
