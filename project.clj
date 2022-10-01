(defproject org.clojars.bost/utils :lein-v
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies
  [[org.clojure/clojure "1.11.1"]
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
   ;; 'lein test' doesn't work with when lein-virgil is plugged-in
   ;; See also https://github.com/ztellman/virgil/pull/31
   [org.clojars.bost/virgil "0.0.0-72-0x260b"]
   ]
  ;; https://github.com/practicalli/clojure-deps-edn
  ;; clojure -M:project/clojars project.jar
  :deploy-repositories [["clojars" {:url "https://repo.clojars.org"
                                    :username "bost"
                                    :password :env/CLOJARS_TOKEN
                                    :sign-releases false}]]
  )
