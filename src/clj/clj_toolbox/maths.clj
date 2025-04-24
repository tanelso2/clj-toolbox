(ns clj-toolbox.maths)

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
