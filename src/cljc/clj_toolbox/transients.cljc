(ns clj-toolbox.transients
  "Additional functions for working with Clojure's transients")

(defn update!
  "Like update, but for transients"
  [m k f]
  (let [orig-val (get m k)]
    (assoc! m k (f orig-val))))

