(defproject diceware "0.2.0"
  :description "Generate passwords using the Diceware system."
  :url "http://example.com/FIXME"
  :license {:name "MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.6.0"]]
  :main diceware.core
  :target-path "target/%s"
;;; Jar Output
  ;; Leave the contents of :source-paths out of jars (for AOT projects).
  :omit-source true
  ;; Files with names matching any of these patterns will be excluded from jars.
  ;;:jar-exclusions [#"(?:^|/).svn/"]
  ;; Same thing, but for uberjars.
  ;;:uberjar-exclusions [#"META-INF/DUMMY.SF"]

  :profiles {:uberjar {:aot :all}})
