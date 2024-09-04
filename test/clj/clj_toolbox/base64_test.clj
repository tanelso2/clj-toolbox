(ns clj-toolbox.base64-test
  (:require
    [clojure.test :refer :all]
    [clojure.test.check.generators :as gen]
    [clojure.test.check.properties :as prop]
    [com.gfredericks.test.chuck.clojure-test :refer [checking]]
    [clj-toolbox.test-utils :refer :all]
    [clj-toolbox.base64 :refer :all]))

(defntest-1 encode-str
  "hello" "aGVsbG8="
  "testing testing 123" "dGVzdGluZyB0ZXN0aW5nIDEyMw=="
  "" "")

(defntest-1 decode-str
  "" ""
  "aGVsbG8=" "hello")

(deftest base64-encode-decode
  (checking "encode-decode-same" 200
    [s gen/string]
    (is (= s (-> s
                 encode-str
                 decode-str)))))
