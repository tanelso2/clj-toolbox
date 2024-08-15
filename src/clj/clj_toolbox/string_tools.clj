(ns clj-toolbox.string-tools
  (:require
    [clojure.string :as str]))

(defn box-trim
  [s]
  (let [first-line (atom nil)
        last-line (atom nil)
        left-bound (atom nil)
        right-bound (atom nil)
        lines (str/split-lines s)
        found-char (atom false)]
    (doseq [[^int row ^String line] (map-indexed vector lines)
            [^int col ^char c] (map-indexed vector line)]
      (when (not (Character/isWhitespace c))
        (if (not @found-char)
          (do
            (reset! first-line row)
            (reset! last-line row)
            (reset! left-bound col)
            (reset! right-bound col)
            (reset! found-char true))
          (do
            (swap! last-line max row)
            (swap! left-bound min col)
            (swap! right-bound max col)))))
    (if (not @found-char)
      ""
      (let [valid-lines (->> lines
                             (drop @first-line)
                             (take (+ 1 (- @last-line @first-line))))]
        (str/join "\n"
          (for [v valid-lines]
            (subs v @left-bound (inc @right-bound))))))))
