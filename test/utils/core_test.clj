(ns utils.core-test
  (:require
   [clojure.test :refer :all]
   [utils.core :refer :all]
   [utils.reflection :refer :all]))

#_(deftest a-test
  (testing "FIXME, I fail."
    (is (= 0 0))))

(deftest test-various
  (def class-name "org.domain.Main")
  (clojure.lang.Reflector/getStaticField class-name "FIELD")
  ;; => Main.class: public static String FIELD = "..."
  (type (Class/forName class-name))
  ;; => java.lang.Class
  (type (.newInstance (Class/forName class-name)))
  ;; => org.domain.Main
  (instance? (Class/forName class-name) (.newInstance (Class/forName class-name)))
  ;; => true
  (.instanceFn1 (new org.domain.Main) "foo")
  ;; => instanceFn0: arg0: foo

  ;; compiler emit warnings when reflection is needed to resolve Java method calls
  ;; or field accesses
  ;; (set! *warn-on-reflection* true)
  (set! *warn-on-reflection* false)

  (.instanceFn1 (.newInstance (Class/forName class-name)) "foo")
  ;; => instanceFn0: arg0: foo

  (clojure.lang.Reflector/invokeStaticMethod class-name "staticFn0" (to-array nil))
  ;; => staticFn0: no-args

  (clojure.lang.Reflector/invokeInstanceMethod
   (.newInstance (Class/forName class-name))
   "instanceFn1"
   (to-array ["foo"]))
  ;; => instanceFn0: arg0: foo
  )

(deftest test-get-static-fn
  ((get-static-fn org.domain.Main "staticFn0"))
  ;; => staticFn0: no-args

  ((get-static-fn org.domain.Main "staticFn1") "foo")
  ;; => staticFn1: arg0: foo

  ((get-static-fn org.domain.Main "staticFn2") "foo" "bar")
  ;; => staticFn1: arg0: foo, arg1: bar
  )

(deftest test-get-instance-fn
  ((get-instance-fn org.domain.Main "instanceFn0"))
  ;; => instanceFn0: no-args

  ((get-instance-fn org.domain.Main "instanceFn1") "foo")
  ;; => instanceFn0: arg0: foo

  ((get-instance-fn org.domain.Main "instanceFn2") "foo" "bar")
  ;; => instanceFn0: arg0: foo, arg1: bar

  ((get-static-fn org.domain.Main "main") (into-array String ["aaa" "bbb"]))
  ;; => main: args: aaa, bbb
  ;; => nil

  (org.domain.Main/main (into-array String ["foo" "bar"]))
  ;; => main: args: foo, bar
  ;; => nil
  )
