(defproject utils :lein-v
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies
  [[org.clojure/clojure "1.10.0"]
   [org.clojure/core.rrb-vector "0.0.13"]
   ;; clj-time-ext as well as clj-time produces java bytecode, no clojurescript
   [clj-time-ext "0.0.0-32-0x20c9"]
   ;; File system utilities
   [me.raynes/fs "1.4.6"]]
   ;; can't use the lein-v from ~/.lein/profiles.clj
  :plugins [[com.roomkey/lein-v "7.0.0"]])
