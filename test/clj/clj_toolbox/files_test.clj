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
  "foo/bar/baz.conf" "baz.conf"
  "/etc/nixos/configuration.nix" "configuration.nix")

(defntest-1 path->ext
  "/bin/blah.txt" "txt"
  "/usr/nginx/nginx.conf" "conf"
  "/bin/bash" ""
  "app.py" "py")

(defntest-1 strip-ext
  "/bin/blah.txt" "/bin/blah"
  "/usr/nginx/nginx.conf" "/usr/nginx/nginx"
  "/bin/bash" "/bin/bash"
  "app.py" "app")

(deftest file-exists?-test
  (testing 'file-exists?
    (let [dir (create-temp-dir)
          test-path (str/join "/" [dir "test"])]
      (is (false? (file-exists? test-path)))
      (spit test-path "hello")
      (is (true? (file-exists? test-path)))))
  (testing 'file-exists?-false-on-dirs
    (let [dir (create-temp-dir)
          test-path (str/join "/" [dir "test"])]
      (is (false? (file-exists? test-path)))
      (file-mkdirs test-path)
      (is (false? (file-exists? test-path))))))

(deftest dir-exists?-test
  (testing 'dir-exists?
    (let [dir (create-temp-dir)
          test-path (str/join "/" [dir "test"])]
      (is (false? (dir-exists? test-path)))
      (file-mkdirs test-path)
      (is (true? (dir-exists? test-path)))))
  (testing 'dir-exists?-false-on-files
    (let [dir (create-temp-dir)
          test-path (str/join "/" [dir "test"])]
      (is (false? (dir-exists? test-path)))
      (spit test-path "Hello")
      (is (false? (dir-exists? test-path))))))

(deftest file-mkdir-test
  (testing 'file-mkdir
    (let [dir (create-temp-dir)
          test-dir (str/join "/" [dir "test"])]
      (is (true? (dir-exists? dir)))
      (is (false? (dir-exists? test-dir)))
      (file-mkdir test-dir)
      (is (true? (dir-exists? test-dir))))))

(deftest file-mkdirs-test
  (testing 'file-mkdirs
    (let [dir (create-temp-dir)
          test-dir (str/join "/" [dir "test" "long" "path"])]
      (is (true? (dir-exists? dir)))
      (is (false? (dir-exists? test-dir)))
      (file-mkdirs test-dir)
      (is (true? (dir-exists? test-dir))))))
