(ns diceware.core
  (:require [clojure.java.io :as io]
            [clojure.edn :as edn])
  (:import [java.security SecureRandom])
  (:gen-class))

(def random (SecureRandom.))

(defn one-roll
  "return one random die roll"
  []
  (inc (.nextInt random 6)))

(defn five-rolls
  "return a keyword formed from five random die rolls"
  []
  (keyword (apply str (repeatedly 5 one-roll))))

; You might try to convert the io/resource to io/file ...
; and then a PushbackReader for edn/read ... 
; but that works in the REPL but doesn't work in an Uberjar.
; Something about io/file returning a URL and not a File.
; http://stackoverflow.com/a/32237761/3599738
(def diceware-map
  (-> "diceware-map.edn"
      io/resource
      slurp
      edn/read-string))

(defn -main
  "Print some random die rolls"
  [& args]
  (def n (try
           (Integer/parseInt (first args))
           (catch Exception e 5)))
  (dotimes [_ n]
    (let [roll (five-rolls)]
      (println (roll diceware-map)))))
