(ns clj-toolbox.urls-test
  (:require
    [clojure.test :refer :all]
    [clj-toolbox.urls :refer :all]
    [clj-toolbox.test-utils :refer :all]))

(defntest-1 get-query-params
  "https://example.com?a=b&x=y" {:a "b" :x "y"}
  "http://localhost:9090?abc=y&code=342#" {:abc "y" :code "342"}
  "https://example.com/foo/bar?a=b&x=y" {:a "b" :x "y"}
  "https://example.com/foo/bar/?a=b&x=y" {:a "b" :x "y"}) 

(defntest-1 query->map
  nil nil
  "a=b&x=y" {:a "b" :x "y"}
  "abc=y&code=342" {:abc "y" :code "342"})

(defntest construct
  ["https://example.com" {:a "b" :x "y"}] "https://example.com?a=b&x=y"
  ["https://example.com" {:token "xyz" :redirect_uri "localhost:8888"}] "https://example.com?token=xyz&redirect_uri=localhost%3A8888")

(defntest-1 extract-host
  "https://example.com?a=b&x=y" "example.com"
  "http://localhost:9090?abc=y&code=342#" "localhost:9090" 
  "https://example.com/foo/bar?a=b&x=y" "example.com"
  "https://example.com/foo/bar/?a=b&x=y" "example.com")

(defntest-1 split
  "http://localhost:9090?abc=y&code=342#" {:host "localhost" :port 9090 
                                           :path "" :fragment "" 
                                           :query "abc=y&code=342" 
                                           :scheme "http" :user-info nil}
  "https://example.com/foo/bar/?a=b&x=y" {:host "example.com" :port nil 
                                          :path "/foo/bar/" :fragment nil 
                                          :query "a=b&x=y" :scheme "https" 
                                          :user-info nil}
  "https://user:pword@example.com/foo/bar#abc123" {:host "example.com" :port nil 
                                                   :path "/foo/bar" :fragment "abc123" 
                                                   :query nil :scheme "https" 
                                                   :user-info "user:pword"}
  "https://example.com" {:host "example.com" :port nil :path ""
                         :fragment nil :query nil 
                         :scheme "https" :user-info nil}
  ;; This is the example in the docstring. Make sure to update that if this
  ;; ever changes!!!
  "https://user:pword@localhost:9000/foo?a=b&x=y#helloworld" {:host "localhost" :port 9000
                                                              :path "/foo" :fragment "helloworld"
                                                              :query "a=b&x=y" :scheme "https"
                                                              :user-info "user:pword"})
  
