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
  [1 2 3]          {:exn clojure.lang.ArityException}
  [nil]            {:exn clojure.lang.ArityException}
  [ret ret ret]    {:exn clojure.lang.ArityException})

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
