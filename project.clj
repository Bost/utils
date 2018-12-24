(defproject utils "0.10.0"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies
  [
   [org.clojure/clojure "1.10.0"]
   ;; java bytecode, no clojurescript
   #_[clj-time-ext "0.16.0"]
   ]
  :plugins
  [
   ;; collection of nREPL middleware designed to enhance CIDER
   [cider/cider-nrepl "0.18.0" :exclusions [org.clojure/tools.nrepl]]])
