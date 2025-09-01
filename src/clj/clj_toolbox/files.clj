(ns clj-toolbox.files
  "Functions for manipulating files"
  (:require
    [clojure.java.io :as io]
    [clojure.string :as str])
  (:import
    [java.io FileNotFoundException]
    [java.nio.file Files LinkOption]
    [java.nio.file.attribute FileAttribute PosixFilePermission PosixFilePermissions]))

(defn to-path [f]
  (-> f (io/file) (.toPath)))

(defn temp-dir
  "
   Creates a new temporary direction with optional prefix
  "
  [& {:keys [prefix]
      :or {prefix ""}}]
  (.toString (Files/createTempDirectory prefix
               (into-array FileAttribute []))))

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

(defn symlink-exists?
  [path]
  (let [p (to-path path)]
    (and (some? p)
         (Files/isSymbolicLink p))))

(defn create-symlink
  [symlink-path target]
  (let [l (to-path symlink-path)
        t (to-path target)
        attrs (into-array FileAttribute [])]
    (Files/createSymbolicLink l t attrs)))

(defn read-symlink
  [path]
  (Files/readSymbolicLink (to-path path)))

(defn ensure-symlink
  "
    TODO
  "
  [link target]
  ; TODO
  nil)

(defn- enum->keyword
  [x]
  (-> x str keyword))

(defn perm-keyword->enum
  [k]
  (-> k
      (name)
      (PosixFilePermission/valueOf)))

(defn posix-perms
  [path]
  (let [opts (into-array LinkOption [])
        path' (to-path path)
        perms (Files/getPosixFilePermissions path' opts)]
    (->> perms
        (map enum->keyword)
        (into #{}))))

(defn set-posix-perms!
  [f perms]
  (let[p (to-path f)
       perms' (->> perms
                   (map perm-keyword->enum)
                   (into #{}))]
    (Files/setPosixFilePermissions p perms')))

(defn perms->number
  [r w x]
  (cond-> 0
    r (+ 4)
    w (+ 2)
    x (+ 1)))

(defn number->perms
  [n]
  (let [x (bit-test n 0)
        w (bit-test n 1)
        r (bit-test n 2)]
    [r w x]))

(defn perms-set->number
  [perms]
  (let [user-perms (perms->number (get perms :OWNER_READ)
                                  (get perms :OWNER_WRITE)
                                  (get perms :OWNER_EXECUTE))
        group-perms (perms->number (get perms :GROUP_READ)
                                   (get perms :GROUP_WRITE)
                                   (get perms :GROUP_EXECUTE))
        others-perms (perms->number (get perms :OTHERS_READ)
                                    (get perms :OTHERS_WRITE)
                                    (get perms :OTHERS_EXECUTE))]
    (+ (* 100 user-perms)
       (* 10 group-perms)
       (* 1 others-perms))))

(defn number->perms-set
  [n]
  (let [[u g o] (->> (str n)
                     (map int))
        [ur uw ux] (number->perms u)
        [gr gw gx] (number->perms g)
        [or' ow ox] (number->perms o)]
    (cond-> #{}
      ur (conj :OWNER_READ)
      uw (conj :OWNER_WRITE)
      ux (conj :OWNER_EXECUTE)
      gr (conj :GROUP_READ)
      gw (conj :GROUP_WRITE)
      gx (conj :GROUP_EXECUTE)
      or' (conj :OTHERS_READ)
      ow (conj :OTHERS_WRITE)
      ox (conj :OTHERS_EXECUTE))))

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

(defn ensure-content
  "
    Ensures the content of the file matches expected.
    Expected can be either a string or a 0-arg function.
    Returns true if this function modified the file.
  "
  [path expected]
  (let [expected-str (cond (string? expected) expected
                           (fn? expected) (expected))]
    (if (file-exists? path)
      (let [actual (slurp path)]
        (if (not= expected-str actual)
          (do
            (spit path expected-str)
            true)
          false))
      (do
        (spit path expected-str)
        true))))

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

