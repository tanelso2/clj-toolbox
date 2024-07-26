(ns clj-toolbox.files
  (:require
    [clojure.java.io :as io]
    [clojure.string :as str])
  (:import 
    [java.io File]
    [java.nio.file Files]))

(defn create-temp-dir
  [& {:keys [prefix]
      :or {prefix ""}}]
  (.toString (Files/createTempDirectory prefix
               (into-array java.nio.file.attribute.FileAttribute []))))

(defn file-exists?
  [filename]
  (let [f (io/file filename)]
    (and (some? f)
         (.exists f))))

(defn file-last-modified
  [filename]
  (.lastModified (io/file filename)))

(defn file-mkdir
  "Equivalent to mkdir <filename>"
  [filename]
  (.mkdir (io/file filename)))

(defn file-mkdirs
  "Equivalent to mkdir -p <filename>"
  [filename]
  (.mkdirs (io/file filename)))

(defn path->filename 
  "Gets the filename from a path
   Assumes unix-style paths e.g. foo/bar/baz.conf"
  [path]
  (-> path
      (str/split #"/")
      last))

(defn path-join
  "Returns a string representing the paths joined"
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
