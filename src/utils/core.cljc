(ns utils.core
  "See https://github.com/weavejester/medley/blob/master/src/medley/core.cljc"
  (:require
   [clojure.string :as cstr]
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
  (case (count args) ;; TODO (if (seq args) args '())
    0 '()
    args))

(defn in?
  "true if `sequence` contains `elem`. See (contains? (set sequence) elem)"
  [sequence elem]
  (boolean (some (fn [e] (= elem e)) sequence)))

(defn union-re-patterns
  "Union regex patters"
  [patterns]
  (re-pattern (cstr/join "|" (map str patterns))))

(defn inclause
  "In SQL, the content of '... in (...)' must be separated by commas and spaces"
  [{:keys [elems contract]}]
  #_(every? contract elems)
  (let [collstr [] ;; list of contracts for strings/varchars
        separator (if (in? collstr contract) "','" ", ")
        ret (cstr/join separator elems)
        s (if (in? collstr contract)
            ;; Use of undeclared Var utils.core/format
            (format "'%s'" ret) ret)]
    (cstr/replace s #"(([\d']+, ){12})" "$1\n")))

(defn sjoin
  "Attention! Order of args differs from cstr/join"
  ([coll] (sjoin coll " "))
  ([coll separator] (cstr/join separator (remove nil? coll))))

(defn sfilter [pred coll] (seq (filter pred coll)))

(defn split
  "Like cstr/split just swap the order of parameters.
  TODO try to write split as a macro"
  ([re s] (cstr/split s re))
  ([re limit s] (cstr/split s re limit)))

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
           ;; Use of undeclared Var utils.core/java
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
   (and (string? v) (not (cstr/blank? v)))))

(defn cleanup-hm [hm]
  (->> hm
       (keep (fn [[k v]] (when (meaningfull? v)
                          {k v})))
       (into {})))

(defn coll?--and--not-empty [v] (and (coll? v) (not-empty v)))
(defn coll?--and--empty?    [v] (and (coll? v) (empty? v)))
(defn if-coll?-then-count   [v] (when (coll? v) (count v)))

(defn what-is
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

    ;; Use of undeclared Var utils.core/future?
    future?
    (fn future?--and--future-done? [f]
      (and
       ;; Use of undeclared Var utils.core/future?
       (future? f)
       ;; Use of undeclared Var utils.core/future-done?
       (future-done? f)))
    (fn future?--and--future-cancelled? [f]
      (and
       ;; Use of undeclared Var utils.core/future?
       (future? f)
       ;; Use of undeclared Var utils.core/future-cancelled?
       (future-cancelled? f)))
    (fn future?--and--realized? [f]
      (and
       ;; Use of undeclared Var utils.core/future?
       (future? f) (realized? f)))

    distinct?
    sequential? associative? sorted? counted? reversible?
    ;; Use of undeclared Var utils.core/bytes?
    bytes?
    indexed? seqable?
    any?
    fn?
    ifn?
    var?
    ;; Use of undeclared Var utils.core/bound?
    (fn var?--and--bount? [v] (and (var? v) (bound? v)))
    (fn var?--and--bount?--and-thread-bound? [v]
      (and (var? v)
           ;; Use of undeclared Var utils.core/bound?
           (bound? v)
           ;; Use of undeclared Var utils.core/thread-bound?
           (thread-bound? v)))
    list?

    vector? set? map? seq? record? map-entry?
    ;; Use of undeclared Var utils.core/class?
    class?
    volatile?
    number?
    (fn number?--and--zero? [n] (and (number? n) (zero? n)))
    true? false? nil? some?
    string?
    (fn string?--and--blank? [s] (and (string? s) (cstr/blank? s)))
    ;; No such namespace: clojure.spec.alpha, could not locate clojure/spec/alpha.cljs, clojure/spec/alpha.cljc, or JavaScript source providing "clojure.spec.alpha" ;; Use of undeclared Var clojure.spec.alpha/spec?
    clojure.spec.alpha/spec?
    special-symbol?
    ;; Use of undeclared Var utils.core/rational?
    rational?
    integer?
    ;; Use of undeclared Var utils.core/ratio?
    ratio?
    ;; Use of undeclared Var utils.core/decimal?
    decimal?
    float? double? int? nat-int? neg-int?
    pos-int? keyword? symbol? ident? qualified-ident? qualified-keyword?
    qualified-symbol? simple-ident? simple-keyword? simple-symbol? boolean?
    inst? uri? uuid?
    ]
   (keep (fn [test-fn]
           (if-let [result (test-fn unknown)]
             {test-fn result})))))

(comment
  (what-is (quote ()))
  (what-is (quote (quote ())))
  (what-is 1)
  (what-is [])
  (what-is ""))

(defn transpose
  "Transpose matrix. See also https://github.com/mikera/core.matrix"
  [m]
  (apply mapv vector m))

#_(let [v [[:a :b :c] [0 1 2]]]
  (= v
     (->> v (transpose) (transpose))))

#_(defn deep-merge
    "Recursively merges maps.
  Thanks to https://dnaeon.github.io/recursively-merging-maps-in-clojure/"
    [& maps]
    (letfn [(m [& xs]
              (if (some #(and (map? %) (not (record? %))) xs)
                (apply merge-with m xs)
                (last xs)))]
      (reduce m maps)))

(defn deep-merge
  "Recursively merges maps. TODO see https://github.com/weavejester/medley
  Thanks to https://gist.github.com/danielpcox/c70a8aa2c36766200a95#gistcomment-2711849"
  [& maps]
  (apply merge-with (fn [& args]
                      (if (every? map? args)
                        (apply deep-merge args)
                        (last args)))
         maps))

(defn last-index-of
  "start-from backwards"
  ([coll elem] (last-index-of coll 0 elem))
  ([coll start-from elem]
   (let [start-from-back (- (dec (count coll)) start-from)]
     ((comp last
            (partial keep-indexed
                     (fn [i v] (when (and (= elem v)
                                         (<= i start-from-back)) i))))
      coll))))

(defn first-index-of
  ([coll elem] (first-index-of coll 0 elem))
  ([coll start-from elem]
   ((comp
     first
     (partial keep-indexed
              (fn [i v] (when (and (= elem v) (>= i start-from)) i))))
    coll)))

(def fns [(fn [] (println "A") true)
          (fn [] (println "B") true)
          (fn [] (println "C") true)])

(defn cont-functions [fns]
  (loop [fs fns]
    (when fs
      (when-let [f (first fs)]
        (when (f)
          (recur (rest fs)))))))

#_(defn deep-merge
    "Might be buggy.
See https://gist.github.com/danielpcox/c70a8aa2c36766200a95#gistcomment-2845162"
    [a & maps]
    (if (map? a)
      (apply merge-with deep-merge a maps)
      (apply merge-with deep-merge maps)))
