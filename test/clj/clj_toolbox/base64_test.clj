(ns clj-toolbox.base64-test
  (:require
    [clojure.test :refer :all]
    [clj-toolbox.test-utils :refer :all]
    [clj-toolbox.base64 :refer :all]))

(defntest-1 encode-str
  "hello" "aGVsbG8="
  "testing testing 123" "dGVzdGluZyB0ZXN0aW5nIDEyMw==")
