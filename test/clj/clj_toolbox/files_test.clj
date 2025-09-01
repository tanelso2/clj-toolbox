(ns clj-toolbox.files-test
  (:require
    [clojure.test :refer :all]
    [clj-toolbox.files :as files :refer :all]
    [clj-toolbox.test-utils :refer :all]
    [clojure.string :as str]
    [clj-toolbox.colls :refer [not-empty?]]
    [clojure.test.check.generators :as gen]
    [com.gfredericks.test.chuck.clojure-test :refer [checking]])
  (:import
    [java.io FileNotFoundException]
    [java.nio.file.attribute PosixFilePermission]))

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
    (let [dir (test-dir)
          test-path (files/f+ dir "test")]
      (is (false? (file-exists? test-path)))
      (spit test-path "hello")
      (is (true? (file-exists? test-path)))))
  (testing 'file-exists?-false-on-dirs
    (let [dir (test-dir)
          test-path (files/f+ dir "test")]
      (is (false? (file-exists? test-path)))
      (mkdirs test-path)
      (is (false? (file-exists? test-path))))))

(deftest ensure-content-test
  (testing 'ensure-content-file-does-not-exist
    (let [dir (test-dir)
          test-path (files/f!+ dir "doesnotexist")
          s "Hello world"]
      (is (false? (files/file-exists? test-path)))
      (is (true? (files/ensure-content test-path s)))
      (let [content (slurp test-path)]
        (is (= s content)))))
  (testing 'ensure-content-file-has-other-data
    (let [dir (test-dir)
          test-path (files/f!+ dir "test.txt")
          s "Hello world!"]
      (spit test-path "Goodbye")
      (is (true? (files/ensure-content test-path s)))
      (let [content (slurp test-path)]
        (is (= s content)))))
  (testing 'ensure-content-file-already-matches
    (let [dir (test-dir)
          test-path (files/f!+ dir "testing")
          s "Hello world"]
      (spit test-path s)
      (is (false? (files/ensure-content test-path s)))))
  (testing 'ensure-content-function-call
    (let [dir (test-dir)
          test-path (files/f!+ dir "test.txt")
          f (fn [n] (str "Number " n))]
      (is (false? (files/file-exists? test-path)))
      (is (true? (files/ensure-content test-path #(f 9))))
      (let [content (slurp test-path)
            expected "Number 9"] 
        (is (= content
               expected)))
      (is (true? (files/ensure-content test-path #(f 5))))
      (let [content (slurp test-path)
            expected "Number 5"]
        (is (= content expected)))
      (is(false? (files/ensure-content test-path #(f 5))))
      (let [content (slurp test-path)
            expected "Number 5"]
        (is (= content expected))))))

(deftest symlink-exists?-test
  (testing 'symlink-exists?
    (let [dir (test-dir)
          f (files/f!+ dir "target.txt")
          link (files/f!+ dir "link.txt")
          content "abc123"]
      (is (false? (files/file-exists? f)))
      (is (false? (files/file-exists? link)))
      (is (false? (files/symlink-exists? f)))
      (is (false? (files/symlink-exists? link)))
      (spit f content)
      (is (true? (files/file-exists? f)))
      (is (false? (files/file-exists? link)))
      (is (false? (files/symlink-exists? f)))
      (is (false? (files/symlink-exists? link)))
      (files/create-symlink link f)
      (is (true? (files/file-exists? f)))
      (is (true? (files/file-exists? link)))
      (is (false? (files/symlink-exists? f)))
      (is (true? (files/symlink-exists? link)))
      (let [fstr (slurp f)
            linkstr (slurp link)]
        (is (= fstr linkstr))
        (is (= content linkstr))
        (is (= content fstr))))))

(defntest-1 number->perms
  1 [false false true]
  2 [false true false]
  7 [true true true]
  3 [false true true]
  5 [true false true]
  0 [false false false])

(defntest perms->number
  [true nil false] 4
  [:r :w false] 6
  [true true true] 7
  [nil nil nil] 0)

(defntest-1 perms-set->number
  #{:OWNER_READ} 400
  #{:OWNER_READ :OWNER_WRITE :OWNER_EXECUTE} 700
  #{:OWNER_READ :GROUP_READ :OTHERS_READ} 444
  #{:OWNER_READ :OWNER_WRITE :OWNER_EXECUTE
    :GROUP_READ :GROUP_EXECUTE
    :OTHERS_READ :OTHERS_EXECUTE} 755)

(defntest-1 number->perms-set
  400 #{:OWNER_READ}
  700 #{:OWNER_READ :OWNER_WRITE :OWNER_EXECUTE}
  755 #{:OWNER_READ :OWNER_WRITE :OWNER_EXECUTE
        :GROUP_READ :GROUP_EXECUTE
        :OTHERS_READ :OTHERS_EXECUTE})

(defntest-1 perm-keyword->enum
  :OWNER_READ PosixFilePermission/OWNER_READ 
  :OTHERS_EXECUTE PosixFilePermission/OTHERS_EXECUTE)

(deftest posix-perms-test
  (testing 'setting-and-getting
    (let [dir (test-dir)
          test-path (files/f!+ dir "test")]
      (spit test-path "ptooey")
      (files/set-posix-perms! test-path #{:OWNER_READ})
      (let [perms (files/posix-perms test-path)]
        (is (= 1 (count perms)))
        (is (= #{:OWNER_READ} perms)))
      (is (thrown? FileNotFoundException (spit test-path "hwock")))
      (files/set-posix-perms! test-path #{:OWNER_READ :OWNER_WRITE})
      (let [perms (files/posix-perms test-path)]
        (is (= 2 (count perms)))
        (is (= #{:OWNER_READ :OWNER_WRITE} perms)))
      (spit test-path "pfffttt..... nothing but dust")
      (files/set-posix-perms! test-path #{:OWNER_READ :GROUP_READ})
      (let [perms (files/posix-perms test-path)]
        (is (= 2 (count perms)))
        (is (= #{:OWNER_READ :GROUP_READ} perms)))
      (is (thrown? Exception (spit test-path "hwock"))))))
      

(deftest dir-exists?-test
  (testing 'dir-exists?
    (let [dir (test-dir)
          test-path (files/f+ dir "test")]
      (is (false? (dir-exists? test-path)))
      (mkdirs test-path)
      (is (true? (dir-exists? test-path)))))
  (testing 'dir-exists?-false-on-files
    (let [dir (test-dir)
          test-path (files/f+ dir "test")]
      (is (false? (dir-exists? test-path)))
      (spit test-path "Hello")
      (is (false? (dir-exists? test-path))))))

(deftest mkdir-test
  (testing 'mkdir
    (let [dir (test-dir)
          test-dir (abs-path-join dir "test")]
      (is (true? (dir-exists? dir)))
      (is (false? (dir-exists? test-dir)))
      (mkdir test-dir)
      (is (true? (dir-exists? test-dir))))))

(deftest mkdirs-test
  (testing 'mkdirs
    (let [dir (test-dir)
          test-dir (str/join "/" [dir "test" "long" "path"])]
      (is (true? (dir-exists? dir)))
      (is (false? (dir-exists? test-dir)))
      (mkdirs test-dir)
      (is (true? (dir-exists? test-dir))))))

(deftest read-all-test
  (testing 'read-all
    (let [dir (test-dir)
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
    (let [f (temp-file)]
      (is (file-exists? f))
      (is (empty? (slurp f)))
      (spit f "Hello world") ; Check is writable
      (is (not-empty? (slurp f))))))

(deftest abs-path-test
  (testing 'abs-path
    (let [p "project.clj"]
      (is (file-exists? p))
      (let [ap (abs-path p)]
        (is (> (count ap) (count p)))
        (is (str/includes? ap p))))))

(deftest last-modified-test
  (testing 'last-modified
    (let [f (temp-file)]
      (spit f "abc123")
      (let [modified-time-1 (files/last-modified f)]
        (Thread/sleep 10)
        (spit f "xyz987")
        (let [modified-time-2 (files/last-modified f)]
          ;; Should this be >= ? We'll see if it ever breaks
          (is (> modified-time-2 modified-time-1)))))))

(deftest size-test
  (testing 'size
    (let [f (files/temp-file)]
      (spit f "abc")
      (is (= 3 (size f)))
      (spit f "abc123")
      (is (= 6 (size f)))))
  (checking "size-matches-input-string" 200
    [s gen/string-alphanumeric]
    (let [f (temp-file)]
       (spit f s)
       (is (= (count s) (size f)))))
  (testing 'size-does-not-exist
    (let [f "does-not-exist!!!"]
      (is (nil? (size f))))))

(deftest children-test
  (testing 'children
    (let [d (test-dir)
          a (files/f!+ d "a")
          b (files/f!+ d "b")]
      (spit a "Hello")
      (spit b "World")
      (let [c (children d)]
        (is (= 2 (count c)))
        (is (some (partial = a) c))
        (is (some (partial = b) c)))))
  (testing 'children-does-not-exist
    (let [f "does-not-exist"]
      (is (thrown? FileNotFoundException (children f)))))
  (testing 'children-throws-on-file
    (let [f (files/temp-file)]
      (is (thrown? FileNotFoundException (children f))))))
