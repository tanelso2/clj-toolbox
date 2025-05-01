(ns clj-toolbox.maths-test
  (:require
    [clojure.test :refer :all]
    [clj-toolbox.test-utils :refer :all]
    [clj-toolbox.maths :refer :all]))

(defntest-1 square
  0 0
  1 1
  2 4
  3 9
  4 16
  10 100
  -10 100
  -11 121
  0.5 0.25)

(defntest-1 exp-or-throw!
  0 1.0
  2 (Math/exp 2)
  5000 (thrown? Exception)
  ##Inf (thrown? Exception))

(defntest-1 sum
  [1 2 3] 6
  [] 0
  [5 5] 10)

(defntest sum-by
  [second [[:a 1] [:b 1]]] 2
  [inc [0 1 2]] 6)

(defntest-1 average
  [1 2 3] 2
  [1 2] 3/2)

(defntest average-by
  [second [[:a 1] [:b 2] [:c 3]]] 2
  [inc [0 1 2]] 2)
