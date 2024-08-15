(ns clj-toolbox.test-utils
  (:require [clojure.test :refer :all]
            [clj-toolbox.prelude :refer [strict-partition]]))

(defmacro make-test-body
  [expected f input]
  (cond
    (and
      (list? expected)
      (= 'thrown? (first expected)))
    (let [exn (second expected)]
      ; We build lists manually instead of using quasiquoting
      ; so that `is` has more readable error messages
      (list `is (list 'thrown?
                      exn
                      (list 'apply f input))))
    (and
      (list? expected)
      (= 'thrown-with-msg? (first expected)))
    (let [exn (second expected)
          re (nth expected 2)]
      (list `is (list 'thrown-with-msg?
                      exn
                      re
                      (list 'apply f input))))
    :else (list `is (list '= expected (list 'apply f input)))))

(defmacro defntest
  "
   Defines a series of tests for function f
   Each test-pair consists of
     args expected
   where args is a vector of arguments to f and expected is the expected return value of (apply f args)
  "
  [f & test-pairs]
  (let [c (count test-pairs)]
    (assert (even? c)
            (str "Need even number of arguments to defntest. Actual: " c)))
  (let [funcname (str f)
        sym (symbol (str f "-defntest-test"))
        cases (partition 2 test-pairs)
        v (map-indexed (fn [i [input expected]] [i input expected])
                       cases)]
   `(deftest ~sym
      (testing ~funcname
        ~@(for [[i in exp] v]
            `(testing ~(str "case" i)
                (make-test-body ~exp ~f ~in)))))))

(defn- wrap-first-arg-for-apply
  [coll]
  (->> coll
       (strict-partition 2)
       (mapcat (fn [[x y]] [[x] y]))))

(defmacro defntest-1
  "
  Defines a series of tests for 1-arity function f
  Each test-pair consists of
    arg expected
  where arg is a single argument to f and expected is the expected return value of (apply f [arg])
  "
  [f & test-pairs]
  (let [c (count test-pairs)]
    (assert (even? c)
            (str "Need even number of arguments to defntest-1. Actual: " c)))
  `(defntest ~f
     ~@(wrap-first-arg-for-apply test-pairs)))
