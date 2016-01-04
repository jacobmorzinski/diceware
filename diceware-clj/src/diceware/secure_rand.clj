(ns diceware.secure-rand
  (:refer-clojure :exclude [rand rand-int rand-nth])
  (:import [java.security SecureRandom]))

;; I like the secure-rand library at:
;;  https://github.com/killme2008/secure-rand/
;; But I don't like how it pulls in org.apache.commons.codec.binary
;; So here, I just use the pieces that create
;; secure versions of [rand rand-int rand-nth]

(defmacro ^:private wrap-ignore-exception [& body]
  `(try
     ~@body
     (catch Throwable e#)))

(defn- ^SecureRandom new-random
  "Try to create a appropriate SecureRandom.
   http://www.cigital.com/justice-league-blog/2009/08/14/proper-use-of-javas-securerandom/ "
  []
  (doto
      (or
       (wrap-ignore-exception (SecureRandom/getInstance "SHA1PRNG" "SUN"))
       (wrap-ignore-exception (SecureRandom/getInstance "SHA1PRNG"))
       (wrap-ignore-exception (SecureRandom.)))
    (.nextBytes (byte-array 16))))

(defonce ^ThreadLocal ^:private threadlocal-random
  (proxy [ThreadLocal]
         []
         (initialValue [] (new-random))))

(defn rand
  "Returns a secure random floating point number between 0 (inclusive) and
  n (default 1) (exclusive)."
  ([] (.nextDouble ^SecureRandom (.get threadlocal-random)))
  ([n] (* n (rand))))

(defn rand-int
  "Returns a secure random integer between 0 (inclusive) and n (exclusive)."
  [n] (int (rand n)))

(defn rand-nth
  "Return a secure random element of the (sequential) collection. Will have
  the same performance characteristics as nth for the given
  collection."
  [coll]
  (nth coll (rand-int (count coll))))

