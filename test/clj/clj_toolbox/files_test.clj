(ns clj-toolbox.files-test
  (:require [clojure.test :refer :all]
            [clj-toolbox.files :refer :all]
            [clj-toolbox.test-utils :refer :all]
            [clojure.string :as str])
  (:import 
    [java.io File]
    [java.nio.file Files]))

(defntest-1 path->filename
  "/bin/bash" "bash"
  "/bin" "bin"
  "/usr/bin/env" "env"
  "foo/bar/baz.conf" "baz.conf")

(deftest file-exists?-test
  (testing 'file-exists?
    (let [dir (create-temp-dir)
          test-path (str/join "/" [dir "test"])]
      (is (false? (file-exists? test-path)))
      (spit test-path "hello")
      (is (true? (file-exists? test-path))))))
      
          

