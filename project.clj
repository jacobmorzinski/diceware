(defproject diceware "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]]
  :main ^:skip-aot diceware.core
  :target-path "target/%s"
;;; Jar Output
  ;; Leave the contents of :source-paths out of jars (for AOT projects).
  :omit-source true
  ;; Files with names matching any of these patterns will be excluded from jars.
  ;;:jar-exclusions [#"(?:^|/).svn/"]
  ;; Same thing, but for uberjars.
  ;;:uberjar-exclusions [#"META-INF/DUMMY.SF"]

  :profiles {:uberjar {:aot :all}})
