(ns utils.num)

(defn round
  "TODO consider using :pre / :hooks. See `wrap-fn-pre-post-hooks`"
  ([x] (round :normal x))
  ([mode x]
   (let [f (case mode
             :high    (static-fn Math/ceil)
             :low     (static-fn Math/floor)
             :normal  (static-fn Math/round)
             (throw (Exception.
                     "ERROR: round [:high|:low|:normal] <VALUE>")))]
     (long (f x)))))

(defn percentage
  "See https://groups.google.com/forum/#!topic/clojure/nH-E5uD8CY4"
  ([place total-count] (percentage :normal place total-count))
  ([mode place total-count]
   (round mode (/ (* place 100.0) total-count))))

;; TODO ask about unicode whitespace char
;; Unicode whitespace ;;;;;​ ;;;;;
;; Normal whitespace  ;;;;; ;;;;;

(defn round-precision
  [value precision]
  (let [multiplier (Math/pow 10.0 precision)]
    (/ (Math/round (* value multiplier)) multiplier)))

(defn round-div-precision
  [dividend divisor precision]
  (round-precision (/ (float dividend) divisor) precision))
