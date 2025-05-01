(ns clj-toolbox.streams
  "Functions for working with Streams"
  (:import
    [java.io PipedInputStream
             PipedOutputStream
             OutputStreamWriter]))

(defn string->stream
  [s]
  (-> s
      (.getBytes)
      (java.io.ByteArrayInputStream.)))

(defn stream->string
  [in-stream]
  (slurp in-stream))

(defn make-paired-streams
  []
  (let [in-stream (PipedInputStream.)
        out-stream (PipedOutputStream. in-stream)]
    [in-stream out-stream]))

(defmacro with-out-stream
  [s & body]
  `(with-open [s# (OutputStreamWriter. ~s)]
     (binding [*out* s#]
        ~@body)))
