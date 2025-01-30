(ns clj-toolbox.urls
  (:require
    [clj-http.client :as http]
    [clojure.string :as str])
  (:import
    [java.net URI]))

(defn construct
  "
   Takes a url and a hash-map of query-params

   Returns a string of the combined url and query-params
  "
  [url query-params]
  (let [query-str (http/generate-query-string query-params)]
    (str url "?" query-str)))

(defn query->map
  "
   Takes a query string and turns it into a hashmap of keywords to strings
   Returns nil if input is nil
  "
  [query-str]
  (if (nil? query-str)
    nil
    (->> (str/split query-str #"&")
         (map #(str/split % #"="))
         (flatten)
         (map-indexed (fn [idx val]
                        (if (even? idx)
                          (keyword val)
                          val)))
         (apply hash-map))))

(defn get-query-params
  "
    Gets the query params from a url and returns as a hash-map
  "
  [url]
  (let [uri (URI. url)
        query-str (.getQuery uri)]
    (query->map query-str)))

(defn extract-host
  [url]
  (let [uri (URI/create url)
        host (.getHost uri)
        port (.getPort uri)]
    (if (not= -1 port)
      (format "%s:%d" host port)
      host)))

(defn split
  "
   Given a url String, split-url will return a hashmap with the following keys
   parsed from the given url (or nil if the url does not feature that part of the url spec)
  The keys in the returned map are:
   :host - host of the url
   :port - the port number of the url, or nil if not given
   :path - the path of the url. If the url is the root of that domain, the path will be an empty string
   :fragment - the fragment of url or nil if not given
   :query - the query string of the url or nil if not given. This is a String, but can be turned into a map with clj-toolbox.urltools/query->map
   :scheme - the scheme of the query e.g. \"https\"
   :user-info - the user info string of the url, or nil if not given

   For an example url that includes every element:
   ```
   (split-url \"https://user:pword@localhost:9000/foo?a=b&x=y#helloworld\")

   {:host \"localhost\" :port 9000
    :path \"/foo\" :fragment \"helloworld\"
    :query \"a=b&x=y\" :scheme \"https\"
    :user-info \"user:pword\"}
   ```
  "
  [url]
  (let [uri (URI/create url)
        host (.getHost uri)
        port (let [p (.getPort uri)]
                (if (= -1 p)
                    nil
                    p))
        path (.getPath uri)
        fragment (.getFragment uri)
        query (.getQuery uri)
        scheme (.getScheme uri)
        user-info (.getUserInfo uri)]
    {:host host :port port :path path :fragment fragment
     :query query :scheme scheme :user-info user-info}))

