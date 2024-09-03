(ns clj-toolbox.base64-test
  (:require
    [clojure.test :refer :all]
    [clojure.test.check.generators :as gen]
    [clojure.test.check.properties :as prop]
    [clojure.test.check.clojure-test :refer [defspec]]
    [clj-toolbox.test-utils :refer :all]
    [clj-toolbox.base64 :refer :all]))

(defntest-1 encode-str
  "hello" "aGVsbG8="
  "testing testing 123" "dGVzdGluZyB0ZXN0aW5nIDEyMw=="
  "" "")

(defntest-1 decode-str
  "" ""
  "aGVsbG8=" "hello")

(defspec base64-encode-decode 200
  (prop/for-all [s gen/string]
    (= s (-> s
             encode-str
             decode-str))))
