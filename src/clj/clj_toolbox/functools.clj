(ns clj-toolbox.functools
  (:require
    [clojure.core.async :as a :refer [go-loop timeout <!]]))

(defn apply-n-times
  "Applies function {f} {n} times with a starting value of {x}
   For example:
    (apply-n-times 2 inc 0) is equivalent to (inc (inc 0))
    (apply-n-times 3 inc 5) is equivalent to (inc (inc (inc 5)))
  "
  [n f x]
  (reduce
    (fn [v _] (f v))
    x
    (range n)))

(defn ignore-args
  "Takes a zero-arity function and wraps it to take any args and ignore them"
  [f]
  (fn [& _] (f)))

(defn cache-most-recent-ret
  "
   Returns a fn that when called, passes the args to f and caches the return value in the atom a
  "
  [a f]
  (fn [& args]
    (let [r (apply f args)]
      (reset! a r)
      r)))

(defn loop-in-background-thread!
  ([f sleep-seconds]
   (loop-in-background-thread! f sleep-seconds 0))
  ([f sleep-seconds sleep-millis]
   (let [sleep-time (+ (* sleep-seconds 1000) sleep-millis)
          continue? (atom true)   
          stop!-fn  (fn []  (reset! continue? false))]
      (go-loop []
        (if @continue?
          (do
            (try
              (f)
              (catch Exception _)) ;we want to keep running even if something fails
            (<! (timeout sleep-time))
            (recur))
          nil))
      stop!-fn)))

(defn map-paginated
  "Maps a function of one argument continuously

   This is intended to be used with api calls that
   are paginated to get the full list of results.

   Args:
    f - function of one argument, either nil or a value to be used as a continuation token
    :get-token - extract the next continuation token from the return value of (f token)
    :get-vals - extract the values to be returned from the return value of (f token)
  "
  [f
   & {:keys [get-token
             get-vals]
      :or {get-token identity
           get-vals identity}}]
  (letfn
    [(helper [token acc]
       (let [next-val (f token)
             next-token (get-token next-val)
             vs (get-vals next-val)
             next-acc (concat acc vs)]
         (if (some? next-token)
           (recur next-token next-acc)
           next-acc)))]
    (helper nil nil)))
