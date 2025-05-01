(ns clj-toolbox.prelude
  (:require
    [clj-toolbox.modules :refer [copy-into-ns]]))

(copy-into-ns
  [clj-toolbox.colls [any-in?
                      find-first
                      into-map
                      not-empty?
                      strict-partition
                      take-range]]
  [clj-toolbox.derefs [derefable? unlazy]]
  [clj-toolbox.maths [square sum sum-by]]
  [clj-toolbox.strings [box-trim re-has? split-whitespace parse-int]]
  [clj-toolbox.timetools [now]]
  [clj-toolbox.transients [update!]])
