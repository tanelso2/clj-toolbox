(ns clj-toolbox.shelltools-test
  (:require
    [clojure.test :refer :all]
    [clj-toolbox.test-utils :refer :all]
    [clj-toolbox.shelltools :refer :all]
    [clojure.java.shell :refer [*sh-env*]]
    [clojure.string :as str]
    [clj-toolbox.files :as files]))

(deftest sh!-test
  (testing 'true
    ;; true should return 0 and have no output
    (let [{:keys [exit out err]} (sh! "true")]
      (is (= 0 exit))
      (is (= "" out))
      (is (= "" err))))
  (testing 'false
    ;; false should throw
    (is (thrown-with-msg? Exception #".*non-zero exit code.*" (sh! "false"))))
  (testing 'cat
    (let [f (files/temp-file)]
      (spit f "hello")
      (let [{:keys [exit out err]} (sh! "cat" f)]
        (is (= 0 exit))
        (is (= "" err))
        (is (= "hello" out))))))

(defntest-1 prepare-cmd
  ["a" "b" "c"] ["a" "b" "c"]
  ["a" nil "c"] ["a" "c"]
  ["a" ["b" nil "d"] "e"] ["a" "b" "d" "e"])

(defn- env-contains?
  [expected missing]
  (let [{:keys [exit out]} (sh! "env")]
    (is (= 0 exit))
    (doseq [x expected]
      (is (str/includes? out x)))
    (doseq [x missing]
      (is (not (str/includes? out x))))))

(deftest with-append-sh-env-test
  (testing 'appending
    (let [env-1 *sh-env*]
      (is (empty? env-1))
      (env-contains? [] ["HELLO=world"])
      (with-append-sh-env {:HELLO "world"}
        (let [env-2 *sh-env*]
          (is (= 1 (count env-2)))
          (env-contains? ["HELLO=world"] ["foo=bar"])
          (with-append-sh-env {:foo "bar"}
            (let [env-3 *sh-env*]
              (is (= 2 (count env-3))
                (env-contains? ["HELLO=world" "foo=bar"]
                               []))))
          (env-contains? ["HELLO=world"] ["foo=bar"])))
      (env-contains? [] ["HELLO=world"]))))
