(ns clj-toolbox.string-tools-test
  (:require
    [clojure.test :refer :all]
    [clj-toolbox.string-tools :refer :all]
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
  "       s\no\n           s")
