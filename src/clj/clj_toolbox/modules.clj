(ns clj-toolbox.modules
  "Macros for copying vars from another module into the current one")

(defn- copy-var-fn
  [^clojure.lang.Symbol from-ns
   ^clojure.lang.Symbol sym]
  (assert (symbol? from-ns))
  (assert (symbol? sym))
  (let [orig-var (intern from-ns sym)
        m (meta orig-var)
        v @orig-var
        orig-doc (get m :doc "")
        new-doc (str orig-doc \newline \newline
                       "  Copied from " from-ns \/ sym)]
    (list `def sym new-doc v)))

(defmacro copy-var
  [from-ns sym]
  (copy-var-fn from-ns sym))

(defmacro copy-vars
  [from-ns syms]
  (let [var-calls (for [sym syms]
                    `(copy-var ~from-ns ~sym))]
    `(do (require (quote [~from-ns]))
         ~@var-calls)))

(defmacro copy-into-ns
  [& ns-defs]
  (let [copy-vars-calls (for [[from-ns vars] ns-defs]
                          `(copy-vars ~from-ns ~vars))]
    `(do ~@copy-vars-calls)))
