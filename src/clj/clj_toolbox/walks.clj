(ns clj-toolbox.walks
  "Collection of functions to make it easier to walk various clojure structures"
  (:require [clojure.walk :refer [postwalk]]))

(defn walk-with
  [f & {:keys [only]
        :or {only (constantly true)}}]
  (fn [x] (postwalk (fn [y] (if (only y)
                                (f y)
                                y))
                    x)))

(defn format-doubles
  [fmt-str x]
  ((walk-with (partial format fmt-str) :only double?) x))
