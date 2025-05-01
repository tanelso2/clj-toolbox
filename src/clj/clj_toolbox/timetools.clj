(ns clj-toolbox.timetools
  (:import
    [java.util Date]))

(defn now
  "
    Gets the time in milliseconds since unix epoch as a Long
  "
  []
  (.getTime (Date.)))
