(ns utils.core
  (:require
   [clojure.string :as s]
   #_[clj-time.core :as time]
   #_[clj-time.format :as timef]))

(def t true)
(def f false)
(def id identity)

(defmacro dbi
  "Identity macro. Meant to be conveniently toggle the dbg macro"
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

(def fst first)
(def snd second)

(defn next-cyclic [idx seq]
  (nth seq (mod (inc idx) (count seq))))

(defn not-empty? [coll] (boolean (seq coll)))
