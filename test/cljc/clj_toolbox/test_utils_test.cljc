(ns clj-toolbox.test-utils-test
  (:require [clojure.test :refer :all]
            [clj-toolbox.test-utils :refer :all]))

(defntest max
  [1 2 3] 3
  [5 5] 5
  [] (thrown? clojure.lang.ArityException)
  ;; [1] (thrown? clojure.lang.ArityException) ; supposed to fail
  [] (thrown-with-msg? clojure.lang.ArityException #"Wrong number"))
  ;; [] (thrown-with-msg? clojure.lang.ArityException #"Hey there Delilah")) ; supposed to fail

(defntest-1 inc
  1 2
  3 4
  5 6)
  ;; 6 6) ; supposed to fail

(def inc_-called (atom false))

(defn inc_
  [x]
  (if (nil? x)
    @inc_-called
    (do
      (reset! inc_-called true)
      (inc x))))

(defntest-1 inc_
  1 2
  2 3
  'do (thrown? java.lang.ClassCastException))

(def max_-called (atom false))

(defn max_
  [& args]
  (if (empty? args)
    @max_-called
    (do
      (reset! max_-called true)
      (apply max args))))

(defntest max_
  [1 2 3] 3)

(deftest test-functions-get-called
  (testing "defntest-1"
    (inc_-test)
    (testing "functions get called"
      (is (true? @inc_-called))))
  (testing "defntest"
    (max_-test)
    (testing "functions get called"
      (is (true? @max_-called)))))
