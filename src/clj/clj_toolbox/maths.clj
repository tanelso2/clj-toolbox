(ns clj-toolbox.maths
  "Functions for math and numbers")

(defn square
  "Returns the square of a number"
  [x]
  (* x x))

(defn exp-or-throw!
  "
   A wrapper around Math/exp that throws if the result would be infinite
  "
  [a]
  (let [r (Math/exp a)]
    (if (infinite? r)
      (throw (Exception. (str "Math/exp overflowed on input value " a)))
      r)))

(defn sum
  "
    Gets the sum of every element in coll
  "
  [coll]
  (reduce + 0 coll))

(defn sum-by
  "
    Gets the sum of (f x) for every x in coll
  "
  [f coll]
  (->> coll
       (map f)
       (sum)))

(defn average
  "
    Gets the average of every element in coll
  "
  [coll]
  (/ (sum coll)
     (count coll)))

(defn average-by
  "
    Gets the average of (f x) for every x in coll
  "
  [f coll]
  (->> coll
       (map f)
       (average)))

(defn parse-int
  "Shortcut for Integer/parseInt to make it easier to pass around as an argument"
  [s]
  (Integer/parseInt s))
