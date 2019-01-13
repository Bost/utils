(ns utils.fs
  (:require
   [utils.core :as ut]
   [clj-time.coerce :as tc]
   [clj-time-ext.core :as tce]
   ;; https://github.com/Raynes/fs
   [me.raynes.fs :as rfs]))

;; usable only on the JVM, not on clojurescript
;; (defn file-ago-diff
;;   "e.g. (file-ago-diff filepath {:verbose true :desc-length :short})"
;;   [filepath {:keys [verbose desc-length]
;;              :or {verbose false desc-length :long} :as prm-map}]
;;   (tce/ago-diff (tc/from-long (.lastModified (new java.io.File filepath)))
;;                 prm-map))

;; usable only on the JVM, not on clojurescript
(defn file-ago-diff
  "e.g. (file-ago-diff filepath {:verbose true :desc-length :short})"
  [filepath {:keys [verbose desc-length]
             :or {verbose false desc-length :long} :as prm-map}]
  (tce/ago-diff (tc/from-long (.lastModified (new java.io.File filepath)))
                prm-map))

;; (require '[clojure.java.io :as io])
;; (defn find-files
;;   "Thanx to https://gist.github.com/ZucchiniZe/f24fdcb346ed070ec2f3"
;;   [path]
;;   (let [directory (clojure.java.io/file path)
;;         dir? #(.isDirectory %)]
;;     (map #(.getPath %)
;;          (filter (comp not dir?)
;;                  (tree-seq dir? #(.listFiles %) directory)))))

(defn find-files
  "Find only files (not dirs) matching given `pattern`."
  [path pattern]
  (->> (rfs/find-files path pattern)
       (filter rfs/file?)))

(defn sort-by-size-desc
  [path pattern]
  (->> (find-files path pattern)
       ;; (take 2)
       (map #(into {:name % :size (rfs/size %)}))
       (sort-by :size)
       reverse
       #_(map (fn [m] (str (:name m) " " (:size m))))
       #_count))
