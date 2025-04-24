(ns clj-toolbox.walks-test
  (:require
    [clojure.test :refer :all]
    [clj-toolbox.test-utils :refer :all]
    [clj-toolbox.walks :refer :all]))

(defn- format-doubles-2-decimals
  [x]
  (format-doubles "%.2f" x))

(defntest-1 format-doubles-2-decimals
  nil nil
  1 1
  1.0 "1.00"
  3.5 "3.50"
  7.8943 "7.89"
  [1.0 2.0 3] ["1.00" "2.00" 3]
  {:name "John Smith"
   :age 42
   :weight 189.2}
  {:name "John Smith"
   :age 42
   :weight "189.20"})
