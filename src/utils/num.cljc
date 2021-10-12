(ns utils.num
  (:require
   [utils.reflection :as r]))

(defn round
  "mode is one of :high :low :normal; defaults to :normal
  TODO consider using :pre / :hooks. See `wrap-fn-pre-post-hooks`"
  ([x] (round :normal x))
  ([mode x]
   (let [f (case mode
             :high    (r/static-fn Math/ceil)
             :low     (r/static-fn Math/floor)
             :normal  (r/static-fn Math/round)
             (throw (Exception.
                     "ERROR: round [:high|:low|:normal] <VALUE>")))]
     (long (f x)))))

;; TODO ask about unicode whitespace char
;; Unicode whitespace ;;;;;​ ;;;;;
;; Normal whitespace  ;;;;; ;;;;;

(defn round-precision
  "Floating point precision"
  ([dividend divisor precision]
   (round-precision (/ (float dividend) divisor) precision))
  ([value precision]
   (let [multiplier (Math/pow 10.0 precision)]
     (/ (Math/round (* value multiplier)) multiplier))))

(defn percentage
  "See https://groups.google.com/forum/#!topic/clojure/nH-E5uD8CY4"
  ([#___ place total-count] (percentage :normal place total-count))
  ([mode place total-count]
   (round mode (/ (* place 100.0) total-count))))
