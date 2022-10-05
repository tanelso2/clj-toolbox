(ns clj-toolbox.test-utils-test
  (:require [clojure.test :refer :all]
            [clj-toolbox.test-utils :refer :all]))

;; These are my tests for defntest and defntest-1
(defntest max
  [1 2 3] 3
  [5 5] 5)

(defntest-1 inc
  1 2
  3 4
  5 6)
