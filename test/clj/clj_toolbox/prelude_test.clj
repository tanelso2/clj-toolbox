(ns clj-toolbox.prelude-test
  (:require [clojure.test :refer :all]
            [clj-toolbox.prelude :refer :all]
            [clj-toolbox.test-utils :refer :all]))

(defntest-1 sum
  [1 2 3] 6
  [] 0
  [5 5] 10)

(defntest sum-by
  [second [[:a 1] [:b 1]]] 2
  [inc [0 1 2]] 6)

(defntest any-in?
  [#(= 2 %) [1 2 3]] true
  [#(= 4 %) [1 2 3]] false)

(defntest find-first
  [#(> % 5) [1 2 3 9 6]] 9
  [#(> % 5) [1 2 3]] nil)

(defntest-1 parse-int
  "21"  21
  "420" 420
  "83"  83
  "-29" -29)

(defonce fibonacci-calls (atom []))
(defonce fibonacci-max-input (atom -1))

(defnmem fibonacci [x]
  (swap! fibonacci-calls conj x)
  (swap! fibonacci-max-input max x)
  (if (or (= 0 x)
          (= 1 x))
    1
    (+ (fibonacci (- x 1))
       (fibonacci (- x 2)))))

(deftest defnmem-test
  (testing 'defnmem
    (is (= 2
           (fibonacci 2)))
    (is (= 3
           (fibonacci 3)))
    (is (= 5
           (fibonacci 4)))
    (is (= 21
           (fibonacci 7)))
    ; A call of (fibonacci max-input)
    ; should require calling fibonacci for all 0..max-input
    ; Since this is memoized, each one should only be called once
    (is (= (+ 1 @fibonacci-max-input)
           (count @fibonacci-calls)))
    ; Make sure there are no repeats since it should be memoizing everything
    (is (apply distinct? @fibonacci-calls))))

(defntest-1 derefable?
  + false
  1 false
  :x false
  'y false
  (atom 1) true
  (future 1) true)

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
