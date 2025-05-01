(ns clj-toolbox.shelltools
  "Tools for making it easier to use clojure as a bash replacement"
  (:require
    [clojure.java.shell :refer [sh *sh-env*]]))

(defn sh!
  "
   Runs a command with clojure.java.shell/sh, throwing if the exit code is not 0
  "
  [& args]
  (let [{:keys [exit out err] :as res} (apply sh args)]
    (if (not= 0 exit)
      (throw (Exception.
               (format "command %s returned non-zero exit code %d\nStdout:\n%s\nStderr:\n%s"
                    (first args)
                    exit
                    out
                    err)))
      res)))

(defn prepare-cmd
  "
   Flattens and removes nils from a collection of arguments.
   Useful for constructing shell commands with conditionals or complex flags
  "
  [cmd]
  (->> cmd
       flatten
       (filter some?)))

(defmacro with-append-sh-env
  "
   Like clojure.java.shell/with-sh-env, but instead of replacing the environment,
   it merges the new env with the old env
  "
  [env & body]
  `(binding [*sh-env* (merge *sh-env* ~env)]
    ~@body))
