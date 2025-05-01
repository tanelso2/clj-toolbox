(ns clj-toolbox.colls
  "Functions for manipulating and traversing collections")

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
  (partition n coll))

(defn take-range
  "
    Returns the part of the coll from index start (inclusive) to end (exclusive).
  "
  [start end coll]
  (assert (>= end start) "Can't take a range from a higher number to a lower number")
  (->> coll
       (drop start)
       (take (- end start))))

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
