(ns utils.core
  (:require
   [clojure.string :as s]
   ))

(def t true)
(def f false)
(def i identity)

(defn id
  "Identity function returning a list of its args:
    (id)           ; => ()
    (id 1)         ; => (1)
    (id 1 2)       ; => (1 2)

  Compare with:
    (identity)     ; => exception
    (identity 1)   ; => 1
    (identity 1 2) ; => exception
  "
  [& args]
  (case (count args)
    0 '()
    args))

(defmacro dbi
  "Identity macro. Conveniently toggle the dbg macro"
  [body]
  `(let [x# ~body]
     x#))

(defmacro dbgi
  "Identity macro. Conveniently toggle the dbgv macro"
  [body]
  `(let [x# ~body]
     x#))

(defmacro dbg [body]
  "debug val. Doesn't work properly for threading macros"
  `(let [x# ~body]
     (println (str "dbg: (def " (quote ~body) " " x# ")"))
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
(fn [$] (println "spyt: " (type $)) $))

(defmacro dbgv [body]
  "debug val. Doesn't work properly for threading macros"
  `(let [x# ~body]
     (println (str "dbgv: (def " (quote ~body) " " x# ")"))
     x#))

(defmacro dbgt [body]
  "debug type. Doesn't work properly for threading macros"
  `(let [x# ~body]
     (println (str "dbgt: (type " (quote ~body) ") = " (type x#)))
     x#))

(defmacro trace
  [& params]
  `(let [x# ~params] (println '~params "=" x#) x#))

(defn in?
  "true if `sequence` contains `elem`. See (contains? (set sequence) elem)"
  [sequence elem]
  (boolean (some (fn [e] (= elem e)) sequence)))

(defn union-re-patterns
  "Union regex patters"
  [patterns]
  (re-pattern (s/join "|" (map str patterns))))

;; TODO inclausestr: improve surrounding by "'"
(defn inclause
  "The content of '... in (...)' must be separated by comma and space"
  [{:keys [elems contract] :as prm}]
  #_(every? contract elems)
  (let [collstr [] ;; list of contracts for strings/varchars
        separator (if (in? collstr contract) "','" ", ")
        ret (clojure.string/join separator elems)]
    (let [s (if (in? collstr contract) (str "'" ret "'") ret)]
      (clojure.string/replace s #"(([\d']+, ){12})" "$1\n"))))

(defn sjoin [coll] (s/join " " (remove nil? coll)))
(defn sfilter [pred coll] (seq (filter pred coll)))
(defn split
  "Like clojure.string/split just swap param order"
  ([re s] (s/split s re))
  ([re limit s] (s/split s re limit)))

(def fst first)
(def snd second)

(defn _0th [coll] (nth coll 0))
(defn _1th [coll] (nth coll 1))
(defn _2th [coll] (nth coll 2))
(defn _3th [coll] (nth coll 3))
(defn _4th [coll] (nth coll 4))
(defn _5th [coll] (nth coll 5))

(defn next-cyclic [idx seq]
  (nth seq (mod (inc idx) (count seq))))

(defn pwd
  "Print user home and current working directory"
  []
  #?(;; :default nil
     ;; :cljs js/NaN
     :clj (println "(System/getProperty \"user.dir\")"
                   (System/getProperty "user.dir")))
  (println "(-> (java.io.File. \".\") .getAbsolutePath)"
           (-> (java.io.File. ".") .getAbsolutePath)))

#_(defn not-empty? [coll] (boolean (seq coll)))
(def not-empty?
  "TODO create test to compare the implementations"
  (complement empty?))

(defn coll-and-empty? [x] (and (coll? x) (empty? x)))

(defn coll-and-not-empty? [x] (and (coll? x) (not-empty? x)))

(defn meaningfull? [v]
  (or
   ;; (boolean v) is true and v is not a collection. Note (boolean 0) => true
   (and v (not (coll? v)))
   (seq v)  ;; true if v is a not-empty collection
   (and (string? v) (not (clojure.string/blank? v)))))

(defn cleanup-hm [hm]
  (->> hm
       (keep (fn [[k v]] (if (meaningfull? v)
                           {k v})))
       (into {})))

(defn coll?--and--not-empty [v] (and (coll? v) (not-empty v)))
(defn coll?--and--empty?[v] (and (coll? v) (empty? v)))
(defn if-coll?-then-count [v] (if (coll? v) (count v)))

(defn whatisit
  "A.k.a:
  What is it?
  Qu'est-ce que c'est?
  Was ist das?
  Cos'è?
  Что́ это?
  O que é isso?"
  [unknown]
  (->>
   #_[(fn coll?--count [v] (if (coll? v) (count v)))]
   [
    #_(fn [n] (if (clojure.zip/node n) (clojure.zip/branch? n)))
    #_clojure.zip/end?

    ;; seq
    boolean
    type
    coll?

    ;; coll?--and--not-empty
    coll?--and--empty?
    if-coll?-then-count

    future?
    (fn future?--and--future-done? [f] (and (future? f) (future-done? f)))
    (fn future?--and--future-cancelled? [f] (and (future? f) (future-cancelled? f)))
    (fn future?--and--realized? [f] (and (future? f) (realized? f)))

    distinct?
    sequential? associative? sorted? counted? reversible?
    bytes?
    indexed? seqable?
    any?
    fn?
    ifn?
    var?
    (fn var?--and--bount? [v] (and (var? v) (bound? v)))
    (fn var?--and--bount?--and-thread-bound? [v] (and (var? v) (bound? v) (thread-bound? v)))
    list?

    vector? set? map? seq? record? map-entry?
    class?
    volatile?
    number?
    (fn number?--and--zero? [n] (and (number? n) (zero? n)))
    true? false? nil? some?
    string?
    (fn string?--and--blank? [s] (and (string? s) (clojure.string/blank? s)))
    clojure.spec.alpha/spec?
    special-symbol?
    rational?
    integer?
    ratio? decimal? float? double? int? nat-int? neg-int?
    pos-int? keyword? symbol? ident? qualified-ident? qualified-keyword?
    qualified-symbol? simple-ident? simple-keyword? simple-symbol? boolean?
    inst? uri? uuid?
    ]
   (keep (fn [test-fn]
           (if-let [result (test-fn unknown)]
             {test-fn result})))))

(comment
  (cotoje (quote ()))
  (cotoje (quote (quote ())))
  (cotoje 1)
  (cotoje [])
  (cotoje ""))

(defn transpose
  "Transpose matrix. See also https://github.com/mikera/core.matrix"
  [m]
  (apply mapv vector m))

#_(let [v [[:a :b :c] [0 1 2]]]
  (= v
     (->> v (transpose) (transpose))))
