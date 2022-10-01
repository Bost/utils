(ns utils.num
  (:require
   [utils.reflection :as r]))

(defn round
  "`mode` is one of :high :low :normal; defaults to :normal
  TODO consider using :pre / :hooks. See `wrap-fn-pre-post-hooks`"
  ;; Math/ceil and Math/floor accept only 'double'. Math/round accepts 'float'
  ;; and 'double'.
  ([^double x] (round :normal x))
  ([mode ^double x]
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
  "E.g. 1.0116 rounded to 2 and 3 decimal places:
  (round-precision 1.0116 2) => 1.01
  (round-precision 1.0116 3) => 1.012"
  ;; Math/round accepts float and double, Math/pow accepts double
  [^double value ^long precision]
  (let [multiplier (Math/pow 10.0 precision)]
    (/ (Math/round (* value multiplier)) multiplier)))

(defn round-div-precision
  "Divide and round with precision. dividend = numerator, divisor = denominator.
  E.g. periodical numbers '1 over 3', '2 over 3' rounded to 4 decimal places:
    (round-precision 1 3 4) => 0.3333
    (round-precision 2 3 4) => 0.6667"
  [dividend divisor precision]
  (round-precision (/ (float dividend) divisor) precision))

(defn percentage
  "See https://groups.google.com/forum/#!topic/clojure/nH-E5uD8CY4"
  ([#___ place total-count] (percentage :normal place total-count))
  ([mode place total-count]
   (round mode (/ (* place 100.0) total-count))))
