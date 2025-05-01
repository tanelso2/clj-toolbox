(ns clj-toolbox.derefs-test
  (:require
    [clojure.test :refer :all]
    [clj-toolbox.test-utils :refer :all]
    [clj-toolbox.derefs :refer :all]))

(defntest-1 derefable?
  + false
  1 false
  :x false
  'y false
  (atom 1) true
  (future 1) true)

(defntest-1 unlazy
  1 1
  :x :x
  {} {}
  + +
  (atom 1) 1
  (future 1) 1
  (delay ::unlazy) ::unlazy)
