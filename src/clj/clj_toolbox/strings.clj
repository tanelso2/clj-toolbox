(ns clj-toolbox.strings
  (:require
    [clojure.string :as str]
    [clj-toolbox.colls :refer [not-empty?]]))

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
            (let [len (count v)
                  start (min @left-bound
                             len)
                  end (min (inc @right-bound)
                           len)]
              (subs v start end))))))))

(defn split-whitespace
  [s]
  (str/split s #"\s+"))

(defn re-has?
  "Tests whether re matches any part of s. Returns a boolean"
  [re s]
  (not-empty? (re-seq re s)))

(defn parse-int
  "Shortcut for Integer/parseInt to make it easier to pass around as an argument"
  [s]
  (Integer/parseInt s))
