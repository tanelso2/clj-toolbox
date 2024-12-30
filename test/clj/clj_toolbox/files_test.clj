(ns clj-toolbox.files-test
  (:require [clojure.test :refer :all]
            [clj-toolbox.files :as files :refer :all]
            [clj-toolbox.test-utils :refer :all]
            [clojure.string :as str])
  (:import
    [java.io File]
    [java.nio.file Files]))

(defntest-1 path->filename
  "/bin/bash" "bash"
  "/bin" "bin"
  "/usr/bin/env" "env"
  "foo/bar/baz.conf" "baz.conf"
  "/etc/nixos/configuration.nix" "configuration.nix"
  "baz2.conf" "baz2.conf")

(defntest-1 path->ext
  "/bin/blah.txt" "txt"
  "/usr/nginx/nginx.conf" "conf"
  "/bin/bash" ""
  "app.py" "py")

(defntest-1 path->dirname
  "/bin/bash" "/bin/"
  "/bin" "/"
  "/usr/bin/env" "/usr/bin/"
  "foo/bar/baz.conf" "foo/bar/"
  "/etc/nixos/configuration.nix" "/etc/nixos/"
  "baz2.conf" "") 

(defntest-1 strip-ext
  "/bin/blah.txt" "/bin/blah"
  "/usr/nginx/nginx.conf" "/usr/nginx/nginx"
  "/bin/bash" "/bin/bash"
  "app.py" "app")

(deftest file-exists?-test
  (testing 'file-exists?
    (let [dir (create-temp-dir)
          test-path (files/f+ dir "test")]
      (is (false? (file-exists? test-path)))
      (spit test-path "hello")
      (is (true? (file-exists? test-path)))))
  (testing 'file-exists?-false-on-dirs
    (let [dir (create-temp-dir)
          test-path (files/f+ dir "test")]
      (is (false? (file-exists? test-path)))
      (mkdirs test-path)
      (is (false? (file-exists? test-path))))))

(deftest dir-exists?-test
  (testing 'dir-exists?
    (let [dir (create-temp-dir)
          test-path (files/f+ dir "test")]
      (is (false? (dir-exists? test-path)))
      (mkdirs test-path)
      (is (true? (dir-exists? test-path)))))
  (testing 'dir-exists?-false-on-files
    (let [dir (create-temp-dir)
          test-path (files/f+ dir "test")]
      (is (false? (dir-exists? test-path)))
      (spit test-path "Hello")
      (is (false? (dir-exists? test-path))))))

(deftest mkdir-test
  (testing 'mkdir
    (let [dir (create-temp-dir)
          test-dir (abs-path-join dir "test")]
      (is (true? (dir-exists? dir)))
      (is (false? (dir-exists? test-dir)))
      (mkdir test-dir)
      (is (true? (dir-exists? test-dir))))))

(deftest mkdirs-test
  (testing 'mkdirs
    (let [dir (create-temp-dir)
          test-dir (str/join "/" [dir "test" "long" "path"])]
      (is (true? (dir-exists? dir)))
      (is (false? (dir-exists? test-dir)))
      (mkdirs test-dir)
      (is (true? (dir-exists? test-dir))))))

(deftest read-all-test
  (testing 'read-all
    (let [dir (create-temp-dir)
          test-file (path-join dir "test.clj")]
      (spit test-file "
            (abc 123)
            {:a 1 :b 2}
            [1 2 3]
            ")
      (let [r (read-all test-file)]
        (is (seq? r))
        (is (= 3 (count r)))
        (let [[l m v] r]
          (is (list? l))
          (is (= 2 (count l)))
          (is (map? m))
          (is (= 2 (count m)))
          (is (= 1 (:a m)))
          (is (= 2 (:b m)))
          (is (vector? v))
          (is (= 3 (count v)))
          (is (= [1 2 3] v)))))))

(deftest temp-file-test
  (testing 'create-temp-file
    (let [f (create-temp-file)]
      (is (file-exists? f))
      (is (empty? (slurp f)))
      (spit f "Hello world") ; Check is writable
      (is (not (empty? (slurp f)))))))

(deftest abs-path-test
  (testing 'abs-path
    (let [p "project.clj"]
      (is (file-exists? p))
      (let [ap (abs-path p)]
        (is (> (count ap) (count p)))
        (is (str/includes? ap p))))))

(deftest last-modified-test
  (testing 'last-modified
    (let [f (create-temp-file)]
      (spit f "abc123")
      (let [modified-time-1 (files/last-modified f)]
        (spit f "xyz987")
        (let [modified-time-2 (files/last-modified f)]
          ;; Should this be >= ? We'll see if it ever breaks
          (is (> modified-time-2 modified-time-1)))))))
