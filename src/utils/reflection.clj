(ns utils.reflection
  (:require
   [utils.core :refer [sjoin]]
   [utils.fs :as fs])
  (:import
   [org.domain Main]
   [java.util.jar JarFile]
   #_[org.apache.catalina.startup Catalina CatalinaProperties]))

#_(iterator-seq (.iterator (.keySet (java.lang.System/getProperties))))

#_(->> java.lang.reflect.Method bean :methods (map #(.getName %)))

;; class bean
#_(:enum
   :interfaces
   :declaredConstructors
   :simpleName
   :package
   :packageName
   :constructors
   :typeName
   :interface
   :declaredFields
   :protectionDomain
   :enumConstants
   :typeParameters
   :declaredMethods
   :name
   :superclass
   :fields
   :enclosingMethod
   :nestHost
   :nestMembers
   :annotatedInterfaces
   :array
   :anonymousClass
   :canonicalName
   :module
   :memberClass
   :primitive
   :modifiers
   :signers
   :methods
   :declaredClasses
   :class
   :classLoader
   :declaredAnnotations
   :annotations
   :genericSuperclass
   :annotatedSuperclass
   :declaringClass
   :synthetic
   :componentType
   :genericInterfaces
   :annotation
   :classes
   :localClass
   :enclosingConstructor
   :enclosingClass)

(def method-desc-fns
  {:methods
   (fn [name]
     (sjoin [(.getName name)
             (->> name .getParameterTypes java.util.Arrays/toString)
             "->"
             (.getReturnType name)]))
   :constructors
   (fn [name]
     (sjoin [(.getName name)
             (->> name .getParameterTypes java.util.Arrays/toString)]))})

(defn class-bean [{:keys [name bean-kw] :as prm}]
  (->> name symbol resolve bean bean-kw
       (map (bean-kw method-desc-fns))))

(defn class-bean-ret [{:keys [bean-kw] :as prm}]
  (conj prm
        {bean-kw
         (->> (class-bean prm)
              (map (fn [elem] {:name elem}))
              (take 1)
              (into []))}))

(defn classes [{:keys [name] :as prm}]
  (->> (new JarFile name)
       .entries iterator-seq
       (map str)
       (filter (fn [entry] (.endsWith (str entry) ".class")))
       (map (fn [entry] (.replaceAll entry "/" ".")))
       (map (fn [entry] (.replaceAll entry ".class" "")))
       (take 2)))

(defn classes-ret [prm]
  (conj prm
        {:classes
         (->> (classes prm)
              (take 2)
              (map (fn [elem] {:name elem}))
              (map (fn [prm] (class-bean-ret (conj prm {:bean-kw :methods}))))
              (map (fn [prm] (class-bean-ret (conj prm {:bean-kw :constructors}))))
              (into []))}))

#_(->> all-jars
     (take 1)
     (map (fn [elem] {:name elem}))
     (map classes-ret)
     (into []))

;; java.util.Date - jump to defintion:  , g g  /  g d

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

(defn get-static-fn [^Class class method]
  (fn [& args]
    (clojure.lang.Reflector/invokeStaticMethod
     (.getName class)
     (str method)
     (to-array args))))

(defmacro static-fn [f] `(fn [x#] (~f x#)))

;; TODO accessing static method - see:
;; => Math/ceil  ; leads to a syntax error
;; https://groups.google.com/forum/#!topic/clojure/nH-E5uD8CY4
;; https://stackoverflow.com/a/8623141
;; ​(defmacro static-fn [f] `(fn [x#] (~f x#)))
;; (static-fn Math/ceil) ;; => #function[...]

((get-static-fn org.domain.Main "staticFn0"))
;; => staticFn0: no-args

((get-static-fn org.domain.Main "staticFn1") "foo")
;; => staticFn1: arg0: foo

((get-static-fn org.domain.Main "staticFn2") "foo" "bar")
;; => staticFn1: arg0: foo, arg1: bar

(defn get-instance-fn [^Class class method]
  (fn [& args]
    (clojure.lang.Reflector/invokeInstanceMethod
     (.newInstance class)
     (str method)
     (to-array args))))

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
