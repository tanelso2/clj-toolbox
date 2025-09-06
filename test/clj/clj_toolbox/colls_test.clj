(ns clj-toolbox.colls-test
  (:require
    [clojure.test :refer :all]
    [clj-toolbox.test-utils :refer :all]
    [clj-toolbox.colls :refer :all]))

(defntest into-map
  ;; kf, vf and coll provided
  [:id :val [{:id  :a
              :val :b}
             {:id  :c
              :val :d}]]  {:a :b, :c :d}

  ;; kf and coll provided
  [inc [0 1]] {1 0 2 1}
  [keyword ["a" "b"]] {:a "a", :b "b"}
  ;; Only coll provided
  [[]] {} ; empty coll
  [[[:a :b]]] {:a :b}
  [[[:a :b] [:c :d]]] {:a :b, :c :d})

(defntest strict-partition
  [3 [1 2 3 4 5 6]] [[1 2 3] [4 5 6]]
  [3 [1 2 3 4 5]] (thrown? IllegalArgumentException)
  [2 [1 2 3 4]] [[1 2] [3 4]]
  [2 [1 2 3]] (thrown? IllegalArgumentException))

(defntest take-range
  [1 2 [1 2 3]] [2]
  [5 2 [1 2 3]] (thrown? AssertionError)
  [1 3 [1 2 3 4]] [2 3]
  [1 3 [1 2 3 4] :include-end? true] [2 3 4]
  [1 3 [1 2 3 4] :include-start? false] [3]
  [1 3 [1 2 3 4] :include-start? false
                 :include-end? true] [3 4])

(deftest take-range-test
  (testing 'take-range-zero-to-length
    (let [v [1 2 3]]
      (is (= v (take-range 0 (count v) v))))))

(defntest-1 not-empty?
  [1 2 3] true
  [] false
  {:a 1} true
  {} false
  "abc" true
  "" false)

(defntest any-in?
    [#(= 2 %) [1 2 3]] true
  [#(= 4 %) [1 2 3]] false)

(defntest find-first
  [#(> % 5) [1 2 3 9 6]] 9
  [#(> % 5) [1 2 3]] nil)
