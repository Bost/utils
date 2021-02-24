(defproject org.clojars.bost/utils :lein-v
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies
  [[org.clojure/clojure "1.10.2"]
   [org.clojure/core.rrb-vector "0.1.2"]
   ;; clj-time-ext as well as clj-time produces java bytecode, no clojurescript
   [org.clojars.bost/clj-time "0.6.0-289-0xe68a"]
   ;; File system utilities
   [me.raynes/fs "1.4.6"]]
  ;; can't use the lein-v from ~/.lein/profiles.clj
  :java-source-paths ["java"]
  :plugins
  [
   ;; leiningen project versions from git
   [com.roomkey/lein-v "7.2.0"]
   ;; autorecompile changed java files
   [lein-virgil "0.1.9"]
   ]
  :deploy-repositories [["clojars" {:url "https://repo.clojars.org"
                                    :username "bost"
                                    :password :env/CLOJARS_TOKEN
                                    :sign-releases false}]]
  )
