(ns utils.debug)

(defmacro dbi
  "Identity macro. Conveniently toggle it."
  [body]
  `(let [x# ~body]
     x#))

;; TODO 1. (def dbgi dbg) will it work?
;; TODO 2. clojure.contrib.def/defalias
(defmacro dbgi
  "Identity macro. Conveniently toggle it."
  [body]
  `(let [x# ~body]
     x#))

(defmacro dbg
  "Debug value. Doesn't work properly for threading macros?"
  [body]
  `(let [x# ~body]
     (printf "%s: (def %s %s)\n" 'dbg (quote ~body) x#)
     x#))

(defmacro dbgv
  "Debug val. Doesn't work properly for threading macros"
  [body]
  `(let [x# ~body]
     (printf "%s: (def %s %s)\n" 'dbgv (quote ~body) x#)
     x#))

(defmacro dbgt
  "Show type. Doesn't work properly for threading macros"
  [body]
  `(let [x# ~body]
     (printf "%s: (= (type %s) %s)\n" 'dbgt (quote ~body) (type x#))
     x#))

(def spy
  "An alternative to the dbg macro. Also for threading macros tracing.
  See https://curiousprogrammer.net/2017/11/20/clojure-tip-of-the-day-episode-3-threading-macros-tracing/"
  (fn [$] (println "spy:" $) $))

(def spyv
  "An alternative to the dbg macro. Also for threading macros tracing.
  See https://curiousprogrammer.net/2017/11/20/clojure-tip-of-the-day-episode-3-threading-macros-tracing/"
  (fn [$] (println "spyv:" $) $))

(def spyt
  "An alternative to the dbg macro. Also for threading macros tracing.
  See https://curiousprogrammer.net/2017/11/20/clojure-tip-of-the-day-episode-3-threading-macros-tracing/"
(fn [$] (println "spyt:" (type $)) $))

(defmacro trace
  [& params]
  `(let [x# ~params] (println '~params "=" x#) x#))

(defn ttt
  "Measure execution time. TODO make it monadic."
  [fun & args]
  (let [tbeg (System/currentTimeMillis)
        result (doall (apply fun args))]
    {:duration (- (System/currentTimeMillis) tbeg) :fun fun :result result}))
