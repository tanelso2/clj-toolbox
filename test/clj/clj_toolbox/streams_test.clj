(ns clj-toolbox.streams-test
  (:require
    [clojure.test :refer :all]
    [clojure.test.check.generators :as gen]
    [clojure.test.check.properties :as prop]
    [com.gfredericks.test.chuck.clojure-test :refer [checking]]
    [clj-toolbox.streams :refer :all]
    [clj-toolbox.test-utils :refer :all]
    [clojure.java.io :as io]))

(def ^:private test-val "hello world")

(deftest conversion-test
  (testing "There and back again"
    (is (= test-val
           (-> test-val
               string->stream
               stream->string))))
  (checking "There and back again - qc" 200
    [s gen/string]
    (is (= s (-> s
                 string->stream
                 stream->string))))
  (testing "stream objects"
    (let [stream (string->stream test-val)]
      (is (instance? java.io.InputStream stream))
      (is (not= stream test-val)))))

(deftest paired-streams-test
  (testing 'paired-streams
    (let [[in-stream out-stream] (make-paired-streams)]
      (with-open [writer (io/writer out-stream)]
        (.write writer test-val))
      (is (= test-val
             (stream->string in-stream)))))
  (checking "paired-streams - qc" 200
    [s gen/string]
    (let [[in-stream out-stream] (make-paired-streams)]
      (with-open [writer (io/writer out-stream)]
        (.write writer s))
      (is (= s
             (stream->string in-stream))))))

(deftest with-out-stream-test
  (testing 'with-out-stream
    (let [[istream ostream] (make-paired-streams)]
      (with-out-stream ostream
        (println "Hello")
        (println "Hmm")
        (println "I've forgotten your name"))
      (is (= "Hello\nHmm\nI've forgotten your name\n"
             (stream->string istream))))))
