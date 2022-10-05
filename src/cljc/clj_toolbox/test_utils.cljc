(ns clj-toolbox.test-utils
  (:require [clojure.test :refer :all]))

(defn matches-expectations
  [expected f input]
  (try
    (let [actual (apply f input)]
      (= expected actual))
    (catch Exception e
      (and (contains? expected :exn)
           (instance? (:exn expected) e)))))

(defmacro defntest
 "
  Defines a series of tests for function f
  Each test-pair consists of 
    args expected
  where args is a vector of arguments to f and expected is the expected return value of (apply f args)
 "
 [f & test-pairs]
 (let [funcname (str f)
       sym (symbol (str f "-test"))]
   `(deftest ~sym
      (testing ~funcname
        (are [input expected] (matches-expectations expected ~f input)
          ~@test-pairs)))))

(defn- wrap-first-arg-for-apply
  [coll]
  (->> coll
       (partition 2)
       (mapcat (fn [[x y]] [[x] y]))))

(defmacro defntest-1
  "
  Defines a series of tests for 1-arity function f
  Each test-pair consists of 
    arg expected
  where arg is a single argument to f and expected is the expected return value of (apply f [arg])
  "
  [f & test-pairs]
  `(defntest ~f
     ~@(wrap-first-arg-for-apply test-pairs)))
