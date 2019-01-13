(ns utils.core
  (:require
   [clojure.string :as s]
   ;; clj-time is for JVM, not for clojurescript
   #_[clj-time.coerce :as tc]
   #_[clj-time-ext.core :as tce]
   ))

(def t true)
(def f false)
(def id identity)

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

(defmacro dbgv [body]
  "debug val"
  `(let [x# ~body]
     (println (str "dbgv: (def " (quote ~body) " " x# ")"))
     x#))

(defmacro dbgt [body]
  "debug type"
  `(let [x# ~body]
     (println (str "dbgt: (type " (quote ~body) ") = " (type x#)))
     x#))

(defmacro trace
  [& params]
  `(let [x# ~params] (println '~params "=" x#) x#))

(defn in?
  "true if seq contains elm"
  [seq elm]
  (boolean (some (fn [e] (= elm e)) seq)))

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

#_(defn tnow []
  (timef/unparse (timef/formatter "HHmmss.SSS") (time/now)))
#_(defn fntime [v]
  (timef/unparse (timef/formatter "HH:mm dd.MM.yy")
                 (time/date-time v)))

(defn sjoin [coll] (s/join " " (remove nil? coll)))
(defn sfilter [pred coll] (seq (filter pred coll)))
(defn split
  "Like clojure.string/split just swap param order"
  ([re s] (s/split s re))
  ([re limit s] (s/split s re limit)))

(def fst first)
(def snd second)

(defn _0th [coll] (nth 0 coll))
(defn _1th [coll] (nth 1 coll))
(defn _2th [coll] (nth 2 coll))
(defn _3th [coll] (nth 3 coll))
(defn _4th [coll] (nth 4 coll))
(defn _5th [coll] (nth 5 coll))

(defn next-cyclic [idx seq]
  (nth seq (mod (inc idx) (count seq))))

(defn not-empty? [coll] (boolean (seq coll)))

(defn pwd
  "Print user home and current working directory"
  []
  #?(;; :default nil
     ;; :cljs js/NaN
     :clj (println "(System/getProperty \"user.dir\")"
                   (System/getProperty "user.dir")))
  (println "(-> (java.io.File. \".\") .getAbsolutePath)"
           (-> (java.io.File. ".") .getAbsolutePath)))

