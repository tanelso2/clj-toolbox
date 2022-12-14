(ns clj-toolbox.streams-test
  (:require
    [clojure.test :refer :all]
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
             (stream->string in-stream))))))
