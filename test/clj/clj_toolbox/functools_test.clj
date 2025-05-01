(ns clj-toolbox.functools-test
  (:require [clojure.test :refer :all]
            [clj-toolbox.functools :refer :all]
            [clj-toolbox.test-utils :refer :all]))

(defntest apply-n-times
  [3 inc 0] 3
  [5 #(conj % :a) []] [:a :a :a :a :a])

(deftest map-paginated-test
  (testing map-paginated
    (let [m {nil {:token :a
                  :vals [1 2 3]}
             :a {:token :b
                 :vals [4 5]}
             :b {:token :c
                 :vals [6]}
             :c {:token nil
                 :vals [7 8 9]}}]
        (is (= [1 2 3 4 5 6 7 8 9]
             (map-paginated #(get m %)
                           :get-token :token
                           :get-vals :vals))))))

(def ^:private ret :return-val)

(defn- bare-ignore-args-test-fn
  []
  ret)

(defntest bare-ignore-args-test-fn
  []               ret
  [1 2 3]          (thrown? clojure.lang.ArityException)
  [nil]            (thrown? clojure.lang.ArityException)
  [ret ret ret]    (thrown? clojure.lang.ArityException))

(def wrapped-ignore-args-test-fn (ignore-args bare-ignore-args-test-fn))

(defntest wrapped-ignore-args-test-fn
  []               ret
  [1 2 3]          ret
  [nil]            ret
  [ret ret ret]    ret)

(defmacro make-background-inc-fn
  [a & body]
  `(fn []
    (swap! ~a inc)
    ~@body))

(deftest loop-in-background-thread!-test
  (testing 'basic-inc
    (let [a (atom 0)
          v (atom {})
          f (make-background-inc-fn a)
          get-val #(get @v %)
          record-val #(swap! v assoc % @a)
          stop! (loop-in-background-thread! f
                                            0
                                            10)
          wait (fn [] (Thread/sleep 50))]
      (record-val :first)
      (wait)
      (record-val :second)
      (is (> (get-val :second)
             (get-val :first)))
      (stop!)
      (record-val :end)
      (wait)
      (record-val :end2)
      (is (= (get-val :end)
             (get-val :end2)))))
  (testing 'throwing-inc
    ;; Should continue to work even if inside function is throwing
    (let [a (atom 0)
          v (atom {})
          f (make-background-inc-fn a
              (throw (Exception. "Throwing after every iteration")))
          record-val #(swap! v assoc % @a)
          get-val #(get @v %)
          stop! (loop-in-background-thread! f
                                            0
                                            10)
          wait (fn [] (Thread/sleep 50))]
      (record-val :first)
      (wait)
      (record-val :second)
      (is (> (get-val :second)
             (get-val :first)))
      (stop!)
      (record-val :end)
      (wait)
      (record-val :end2)
      (is (= (get-val :end)
             (get-val :end2))))))

(deftest inverse-test
  (testing 'constantly
    (let [f (constantly 2)
          invf (inverse f) 
          check (fn [& args] (is (= 0 (+ (apply f args)
                                         (apply invf args)))))]
      (check)
      (is (= -2 (invf)))))
      
  (testing '+
    (let [f +
          invf (inverse f)
          check (fn [& args] (is (= 0 (+ (apply f args)
                                         (apply invf args)))))]
      (is (= -2 (invf 1 1)))
      (is (= 10 (invf -5 -5)))
      (is (= 4  (invf -10 6)))
      (is (= 0  (invf -1 1)))
      (check 2 2)
      (check 1 2 3)
      (check -1 -2 3 -4))))
  ;; TODO: Checking test that (= 0 (+ (f x) ((inverse f) x)))

(deftest mul-inverse-test
  (testing 'constantly
    (let [f (constantly 2)
          invf (mul-inverse f)
          check (fn [& args] (is (= 1.0 (* (apply f args)
                                           (apply invf args)))))]
      (check)
      (is (= 0.5 (invf))))))

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

