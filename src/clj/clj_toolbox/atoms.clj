(ns clj-toolbox.atoms
  "Tools for atoms with extra effects"
  (:require
    [clj-toolbox.prelude :refer :all]))

(defn make-file-backed-atom
  "
   Takes a filename and an optional default value.
   Returns an atom that will write its value to the file every time it is updated
   The initial value of the atom will be either the contents of the file if it exists, the default-value, or nil
  "
  ([filename]
   (make-file-backed-atom filename nil))
  ([filename default-value]
   (let [initial-file-value (try
                              (read-string (slurp filename))
                              (catch Exception _
                                 nil))
         initial-value (if (some? initial-file-value)
                          initial-file-value
                          default-value)
         write-file (fn [x] (spit filename (prn-str x)))
         a (atom nil)]
      (add-watch a (keyword (str "file-write-" (gensym)))
                   (fn [_k _ref old-state new-state]
                     (when (not= old-state new-state)
                        (write-file new-state))))
      (reset! a initial-value)
      a)))

(defn watch-changed-timestamp
  "
   Takes an atom and returns another atom that will be updated with a timestamp (long, number of milliseconds since UNIX epoch) every time the input atom is updated
  "
  [a]
  (let [ts-atom (atom nil)]
    (add-watch a (keyword (str "watch-changed-" (gensym)))
                 (fn [_k _ref old-state new-state]
                    (when (not= old-state new-state)
                      (reset! ts-atom (now)))))
    ts-atom))
