(ns clj-toolbox.strings-test
  (:require
    [clojure.test :refer :all]
    [clj-toolbox.strings :refer :all]
    [clj-toolbox.test-utils :refer :all]))

(defntest-1 box-trim
  "
   abc
   def
   ghi
  "
  "abc\ndef\nghi"

  "
   abc
   def
    hi
  "
  "abc\ndef\n hi"

  "
    bc
   def
     i
  "
  " bc\ndef\n  i"
  ; String of whitespace becomes empty string
  "

  "
  ""
  ; Lines of different lengths
  "
    abc
   def
  ghi
  "
  "  abc\n def\nghi"
  "
           s
    o
           s
  "
  "       s\no\n       s"
  "
           s
    o
               s
  "
  "       s\no\n           s"
  ; Empty string -> empty string
  ""
  "")

(defntest-1 split-whitespace
  "a" ["a"]
  "a bc def" ["a" "bc" "def"]
  "1
   2
   3" ["1" "2" "3"])
