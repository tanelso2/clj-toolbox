(ns clj-toolbox.timetools
  "Functions for working with time"
  (:import
    [java.util Date]))

(defn now
  "
    Gets the time in milliseconds since unix epoch as a Long
  "
  []
  (.getTime (Date.)))
