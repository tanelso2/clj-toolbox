(ns clj-toolbox.prelude
  (:import
    #?(:clj [java.util Date])))

(defmacro defnmem
  "
    Defines a memoized function
     Examples:
      (defnmem fibonacci [x]
        (if (or (= 0 x)
                (= 1 x))
          1
          (+ (fibonacci (- x 1))
             (fibonacci (- x 2)))))
  "
  [name & fndef]
  `(def ~name
     (memoize
       (fn ~@fndef))))

(defn ^{:see-also clojure.core/not-any?}
  any-in?
  "
    Returns true if (pred x) is logical true for any x in coll, else false.
  "
  [pred coll]
  (apply (complement not-any?) [pred coll]))

(defn  ^{:see-also clojure.core/some}
  find-first
  "
    Returns the first x in coll for which (pred x) returns true.
    returns nil if none is found
  "
  [pred coll]
  (some (fn [x] (if (pred x)
                  x
                  nil))
        coll))

(defn derefable?
  "Returns true if x is an instance of clojure.langIDeref"
  [x]
  (instance? clojure.lang.IDeref x))

(defn into-map
  "
    Transforms coll into a hash-map by using (kf x) as the key and (vf x) as the value for each x in coll
    vf defaults to identity if not provided
  "
  ([kf coll]
   (into-map kf identity coll))
  ([kf vf coll]
   (into {} (map
                 (fn [x] [(kf x) (vf x)])
                 coll))))
#?(:clj
    (defn now
      "
        Gets the time in milliseconds since unix epoch as a Long
      "
      []
      (.getTime (java.util.Date.))))
#?(:clj
    (defn parse-int
      "Shortcut for Integer/parseInt to make it easier to pass around as an argument"
      [s]
      (Integer/parseInt s)))

(defn update!
  "Like update, but for transients"
  [m k f]
  (let [orig-val (get m k)]
    (assoc! m k (f orig-val))))

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
