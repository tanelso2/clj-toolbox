(ns clj-toolbox.string-tools
  (:require
    [clojure.string :as str]))

(defn box-trim
  [s]
  (let [first-line (volatile! nil)
        last-line (volatile! nil)
        left-bound (volatile! nil)
        right-bound (volatile! nil)
        found-char (volatile! false)
        lines (str/split-lines s)]
    (doseq [[^int row ^String line] (map-indexed vector lines)
            [^int col ^char c] (map-indexed vector line)]
      (when (not (Character/isWhitespace c))
        (if (not @found-char)
          (do
            (vreset! first-line row)
            (vreset! last-line row)
            (vreset! left-bound col)
            (vreset! right-bound col)
            (vreset! found-char true))
          (do
            (vswap! last-line max row)
            (vswap! left-bound min col)
            (vswap! right-bound max col)))))
    (if (not @found-char)
      ""
      (let [valid-lines (->> lines
                             (drop @first-line)
                             (take (+ 1 (- @last-line @first-line))))]
        (str/join "\n"
          (for [v valid-lines]
            (subs v @left-bound (inc @right-bound))))))))
