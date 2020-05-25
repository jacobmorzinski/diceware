(ns diceware.core
  (:refer-clojure :exclude [rand rand-int rand-nth])
  (:require [clojure.java.io :as io]
            [clojure.edn :as edn]
            [clojure.tools.cli :as cli]
            [clojure.string :as string]
            [clojure.pprint]
            [diceware.secure-rand :as securerandom])
  (:gen-class))

(defn one-roll
  "return an int from a roll of a single die"
  []
  (inc (securerandom/rand-int 6)))

(defn five-rolls
  "return a keyword formed from a roll of five dice, such as :12346"
  []
  (->> (repeatedly 5 one-roll) ; e.g, => (1 2 3 4 6)
       (apply str)             ; e.g, => "12346"
       (keyword)))

;; You might try to convert the io/resource to io/file ...
;; and then a PushbackReader for edn/read ... 
;; but that works in the REPL but doesn't work in an Uberjar.
;; Something about io/file returning a URL and not a File.
;; http://stackoverflow.com/a/32237761/3599738
(def diceware-map
  (-> "diceware-map.edn"
      io/resource
      slurp
      edn/read-string))

(defn join-character [s]
  (-> s
      str
      first))

(defn parse-int [n]
  (-> n
      edn/read-string
      int))

(def cli-options
  [["-j" "--join CHARACTER" "Join words with CHARACTER (default \\n)"
    :default \newline
    :parse-fn join-character]
   ["-n" "--number NUMBER" "Number of words to generate"
    :default 5
    :parse-fn parse-int]
   ["-h" "--help"]])

(defn usage [options-summary]
  (->> ["Usage: clj -m diceware.main [options]"
        ""
        "Options:"
        options-summary]
       (string/join \newline)))

(defn error-msg [errors]
  (str "The following errors occurred while parsing your command:\n\n"
       (string/join \newline errors)))

(defn -main
  "Print some random die rolls"
  [& args]
  (let [{:keys [options arguments errors summary]}
        (cli/parse-opts args cli-options)]
    (cond
      (:help options) (println (usage summary))
      (some? errors) (println (error-msg errors))
      :else
      (let [{:keys [join number]} options]
        (dotimes [_ number]
          (let [roll (five-rolls)]
            (println (roll diceware-map))))))))
