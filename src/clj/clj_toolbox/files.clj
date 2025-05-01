(ns clj-toolbox.files
  "Functions for manipulating files"
  (:require
    [clojure.java.io :as io]
    [clojure.string :as str])
  (:import
    [java.io FileNotFoundException]
    [java.nio.file Files]))

(defn temp-dir
  "
   Creates a new temporary direction with optional prefix
  "
  [& {:keys [prefix]
      :or {prefix ""}}]
  (.toString (Files/createTempDirectory prefix
               (into-array java.nio.file.attribute.FileAttribute []))))

(defn temp-file
  "
   Creates a new temporary file with optional prefix and suffix
  "
  [& {:keys [prefix suffix]
      :or {prefix ""
           suffix ""}}]
  (.toString (Files/createTempFile prefix suffix
              (into-array java.nio.file.attribute.FileAttribute []))))

(defn file-exists?
  [filename]
  (let [f (io/file filename)]
    (and (some? f)
         (.isFile f)
         (.exists f))))

(defn dir-exists?
  [d]
  (let [f (io/file d)]
    (and (some? f)
         (.isDirectory f)
         (.exists f))))

(defn last-modified
  [filename]
  (.lastModified (io/file filename)))

(defn size
  "
   Returns the size of the file or nil if the file does not exist
  "
  [path]
  (let [f (io/file path)]
    (if (or (nil? f)
            (not (.exists f)))
      nil
      (.length f))))

(defn children
  "
   Returns the paths of the children of the given path as a vector of strings
   If path is not an existing directory, this function throws a FileNotFoundException
  "
  [path]
  (when (not (dir-exists? path))
    (throw (FileNotFoundException. (format "%s does not exist or is not a directory, cannot get children" path))))
  (let [f (io/file path)]
    (->> f
         (.listFiles)
         (seq)
         (mapv #(.getPath %)))))

(defn mkdir
  "Equivalent to mkdir <filename>"
  [filename]
  (.mkdir (io/file filename)))

(defn mkdirs
  "Equivalent to mkdir -p <filename>"
  [filename]
  (.mkdirs (io/file filename)))

(defn path->filename
  "Gets the filename from a path
   Assumes unix-style paths e.g. foo/bar/baz.conf"
  [path]
  (let [i (str/last-index-of path \/)]
    (if (nil? i)
      path
      (subs path (inc i)))))

(defn path->dirname
  "Gets the dirname from a path
   Assumes unix-style paths e.g. foo/bar/baz.conf"
  [path]
  (let [i (str/last-index-of path \/)]
    (if (nil? i)
      ""
      (subs path 0 (inc i)))))

(defn path->ext
  "Gets the extension from a path"
  [path]
  (let [i (str/last-index-of path \.)]
    (if (nil? i)
      ""
      (subs path (inc i)))))

(defn strip-ext
  "Strips the extension from a path"
  [path]
  (let [i (str/last-index-of path \.)]
    (if (nil? i)
      path
      (subs path 0 i))))

(defn path-join
  "Returns a string representing the paths joined
   Return values differ based on platform"
  [& paths]
  (->> paths
       (apply io/file)
       (.getPath)))

(defn abs-path-join
  "Returns a string representing the absolute path of the paths joined"
  [& paths]
  (->> paths
       (apply io/file)
       (.getAbsolutePath)))

(defn abs-path
  "Returns a string representing the absolute path of the given path"
  [path]
  (-> path
      (io/file)
      (.getAbsolutePath)))

(def f+ path-join)

(def f!+ abs-path-join)

(defn read-all
 "
  Reads all Clojure forms from file {f}
 "
 [f]
 (binding [*default-data-reader-fn* (if (nil? *default-data-reader-fn*)
                                      tagged-literal
                                      *default-data-reader-fn*)]
   (with-open [in (java.io.PushbackReader. (clojure.java.io/reader f))]
    (let [obj-seq (repeatedly (partial read {:eof :theend} in))]
      (doall (take-while (partial not= :theend) obj-seq))))))

