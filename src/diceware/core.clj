(ns diceware.core
  (:gen-class))

(import '[java.security SecureRandom])

(require '[clojure.java.io :as io]
         '[clojure.edn])

(def random (SecureRandom.))

(defn one-roll
  "return one random die roll"
  []
  (+ 1 (int (Math/floor (* 6 (. random nextFloat))))))

(defn five-rolls
  "return a keyword formed from five random die rolls"
  []
  (keyword (clojure.string/join "" (repeatedly 5 one-roll))))

; You might try to convert the io/resource to io/file ...
; and then a PushbackReader for clojure.edn/read ... 
; but that works in the REPL but doesn't work in an Uberjar.
; Something about io/file returning a URL and not a File.
; http://stackoverflow.com/a/32237761/3599738
(def diceware-map
  (-> "diceware-map.edn"
      io/resource
      slurp
      clojure.edn/read-string))

(defn -main
  "Print some random die rolls"
  [& args]
  (def n (try
           (Integer/parseInt (first args))
           (catch Exception e 5)))
  (dorun
    (for [i (range n)]
      (let [roll (five-rolls)]
        (println "roll" roll "makes word:" (roll diceware-map))))))
