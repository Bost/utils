(ns corona.heap
  (:require [jvm-alloc-rate-meter.core :as ameter]
            [corona.common :as com]
            [taoensso.timbre :as timbre]))

(defn format-bytes
  "Nicely format `num-bytes` as kilobytes/megabytes/etc.
    (format-bytes 1024) ; -> 2.0 KB
  See
  https://github.com/metabase/metabase
  metabase.util/format-bytes "
  [num-bytes]
  (loop [n num-bytes [suffix & more] ["B" "kB" "MB" "GB"]]
    (if (and (seq more)
             (>= n 1024))
      (recur (/ n 1024) more)
      (format "%.1f %s" (float n) suffix))))

(defn fmap
  "See clojure.algo.generic.functor/fmap"
  [f m]
  (into (empty m) (for [[k v] m] [k (f v)])))

(def am
  (ameter/start-alloc-rate-meter
   (fn [v]
     (timbre/debugf
      "%s; alloc rate %s MB/sec"
      (let [runtime (Runtime/getRuntime)
            ;; current size of heap in bytes
            size (.totalMemory runtime)

            ;; maximum size of heap in bytes. The heap cannot grow
            ;; beyond this size. Any attempt will result in an
            ;; OutOfMemoryException.
            max-size (.maxMemory runtime)

            ;; amount of free memory within the heap in bytes. This size
            ;; will increase after garbage collection and decrease as
            ;; new objects are created.
            free-size (.freeMemory runtime)]
        ((comp
          (partial fmap format-bytes))
         {:size size :max max-size :free free-size}))
      (/ v 1e6)))))

;; Test it out
#_(while true
  (byte-array 1e7)
  (Thread/sleep 100))

;; The meter should report ~100 MB/sec allocation rate into the console.

;; To stop the meter thread - In CIDER press C-c C-c in the repl and enter:
#_(am)
