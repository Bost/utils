(defproject utils :lein-v
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies
  [[org.clojure/clojure "1.10.0"]
   [org.clojure/core.rrb-vector "0.0.13"]
   ;; java bytecode, no clojurescript
   [clj-time-ext "0.0.0-31-0x6e56"]
   ;; File system utilities
   [me.raynes/fs "1.4.6"]]
  :plugins
  [
   ;; Drive leiningen project version from git instead of the other way around
   [com.roomkey/lein-v "7.0.0"]
   ;; collection of nREPL middleware designed to enhance CIDER
   [cider/cider-nrepl "0.18.0" :exclusions [org.clojure/tools.nrepl]]])
