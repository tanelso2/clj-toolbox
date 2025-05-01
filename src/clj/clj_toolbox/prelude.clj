(ns clj-toolbox.prelude
  "Functions that should be in clojure.core (or just functions I use very frequently)"
  (:require
    [clj-toolbox.modules :refer [copy-into-ns]]
    [clj-toolbox.colls]
    [clj-toolbox.derefs]
    [clj-toolbox.maths]
    [clj-toolbox.strings]
    [clj-toolbox.timetools]
    [clj-toolbox.transients]))

(copy-into-ns
  [clj-toolbox.colls [any-in?
                      find-first
                      into-map
                      not-empty?
                      strict-partition
                      take-range]]
  [clj-toolbox.derefs [derefable? unlazy]]
  [clj-toolbox.maths [parse-int
                      square
                      sum
                      sum-by]]
  [clj-toolbox.strings [box-trim re-has? split-whitespace]]
  [clj-toolbox.timetools [now]]
  [clj-toolbox.transients [update!]])
