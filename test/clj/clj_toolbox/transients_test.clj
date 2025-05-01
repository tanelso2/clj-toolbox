(ns clj-toolbox.transients-test
  (:require
    [clojure.test :refer :all]
    [clj-toolbox.test-utils :refer :all]
    [clj-toolbox.transients :refer :all]))

(deftest update!-test
  (testing 'update!
    (let [m (transient {:a 1 :b 2})
          a1 (get m :a)
          b1 (get m :b)
          m (update! m :a inc)
          a2 (get m :a)
          b2 (get m :b)
          m (persistent! m)
          a3 (get m :a)
          b3 (get m :b)]
      (are [actual expected] (= actual expected)
        a1 1
        b1 2
        a2 2
        b2 2
        a3 2
        b3 2))))
