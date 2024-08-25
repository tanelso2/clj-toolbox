(ns clj-toolbox.base64
  (:import
    [java.util Base64]))

(defn encode-str
  "base64 encodes the input string. Returns a string"
  [^String s]
  (.encodeToString (Base64/getEncoder) (.getBytes s)))
