(ns clj-toolbox.atoms-test
  (:require
    [clj-toolbox.prelude :refer :all]
    [clojure.test :refer :all]
    [clj-toolbox.test-utils :refer :all]
    [clj-toolbox.files :as files]
    [clj-toolbox.atoms :refer :all]
    [clojure.string :as str]))

(deftest file-backed-atom-test
  (testing 'new-file
    (let [f (files/temp-file)]
      (let [a (make-file-backed-atom f)]
        (is (nil? @a))
        (reset! a :hello)
        (is (files/file-exists? f))
        (is (= ":hello" (str/trim (slurp f))))
        (reset! a 1)
        (is (files/file-exists? f))
        (is (= "1" (str/trim (slurp f))))
        (swap! a inc)
        (is (= 2 @a))
        (is (files/file-exists? f))
        (is (= "2" (str/trim (slurp f))))))
    (testing 'with-default
      (let [f (files/temp-file)
            default -1234
            a (make-file-backed-atom f default)]
        (is (= default @a))
        (is (files/file-exists? f))
        (is (= "-1234" (str/trim (slurp f))))
        (swap! a inc)
        (is (= (inc default) @a))
        (is (= "-1233" (str/trim (slurp f)))))))
  (testing 'reusing-file
    (let [f (files/temp-file)
          a (make-file-backed-atom f)]
      (reset! a 1)
      (is (files/file-exists? f))
      (is (= "1" (str/trim (slurp f))))
      (let [a2 (make-file-backed-atom f)]
        (is (= 1 @a2)))
      (let [default 0
            a3 (make-file-backed-atom f default)]
        ;; Should ignore the default
        (is (= 1 @a3))))
    (testing 'different-types
      (doseq [v [1 1.1 :a "abc" {:a 1} [1 2 3] (list 1 2 3)]]
        (let [f (files/temp-file)
              a (make-file-backed-atom f)]
          (reset! a v)
          (is (files/file-exists? f))
          (let [a2 (make-file-backed-atom f)]
            (is (= v @a2)))
          (let [default 42
                a3 (make-file-backed-atom f default)]
            ;; Should ignore the default
            (is (= v @a3)))))))
  (testing 'file-does-not-exist
    (let [d (files/temp-dir)
          f (files/path-join d "doesnotexist")
          default 42
          a (make-file-backed-atom f default)]
      (is (= default @a))
      (is (files/file-exists? f))
      (is (= (str default) (str/trim (slurp f))))))
  (testing 'dir-does-not-exist
    (let [d (files/temp-dir)
          f (files/path-join d "does" "not" "exist")
          default 42]
      (is (thrown? java.io.FileNotFoundException (make-file-backed-atom f default))))))
      
(deftest watch-changed-timestamp-test
  (testing 'watching
    (let [a (atom nil)
          ta (watch-changed-timestamp a)]
      (is (nil? @a))
      (is (nil? @ta))
      (reset! a 1)
      (is (= 1 @a))
      (is (some? @ta))
      (let [ts1 @ta]
        (Thread/sleep 10)
        (reset! a 2)
        (is (= 2 @a))
        (let [ts2 @ta]
          (is (> ts2 ts1)))))))
        
