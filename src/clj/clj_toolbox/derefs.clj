(ns clj-toolbox.derefs)

(defn derefable?
  "Returns true if x is an instance of clojure.langIDeref"
  [x]
  (instance? clojure.lang.IDeref x))

(defn unlazy
  "Unwraps x if it is a clojure.lang.IDeref.
   Otherwise just returns x"
  [x]
  (if (derefable? x)
    @x
    x))
