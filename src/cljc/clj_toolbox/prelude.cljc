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

(defn ^{:see-also clojure.core/empty?}
  not-empty?
  "
    Returns true if x is not empty, else false.

    Exists because doing (not (empty? x)) is inefficient
  "
  [x]
  (boolean (seq x)))

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

#?(:clj
   (do
    (defn derefable?
      "Returns true if x is an instance of clojure.langIDeref"
      [x]
      (instance? clojure.lang.IDeref x))

    (defn unlazy
      "Unwraps x if it is a clojure.lang.IDeref.
       Otherwise just returns x"
      [x]
      (if (derefable? x)
        @x
        x))))

(defn into-map
  "
    Transforms coll into a hash-map by using (kf x) as the key and (vf x) as the value for each x in coll
    vf defaults to identity if not provided
    if only coll is provided, will assume coll is a list of pairs
  "
  ([coll] (into {} coll))
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

#?(:clj
    (defn ^{:see-also clojure.core/partition}
      strict-partition
      "
        Like parition, but throws an exception if the number of elements in coll
        are not evenly divisible by n, instead of partition's behavior of having
        the last group be too small.
      "
      [n coll]
      (let [c (count coll)]
        (when (not= 0 (mod c n))
          (throw
            (IllegalArgumentException.
              (str "Cannot partition " c " elements into lists of size " n " evenly.")))))
      (partition n coll)))

(defn take-range
  "
    Returns the part of the coll from index start (inclusive) to end (exclusive).
  "
  [start end coll]
  (assert (>= end start) "Can't take a range from a higher number to a lower number")
  (->> coll
       (drop start)
       (take (- end start))))

(defn re-has?
  "Tests whether re matches any part of s. Returns a boolean"
  [re s]
  (not-empty? (re-seq re s)))
