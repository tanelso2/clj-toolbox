(ns clj-toolbox.strings-test
  (:require
    [clojure.test :refer :all]
    [clj-toolbox.strings :refer :all]
    [clj-toolbox.test-utils :refer :all]))

(defntest-1 box-trim
  "
   abc
   def
   ghi
  "
  "abc\ndef\nghi"

  "
   abc
   def
    hi
  "
  "abc\ndef\n hi"

  "
    bc
   def
     i
  "
  " bc\ndef\n  i"
  ; String of whitespace becomes empty string
  "

  "
  ""
  ; Lines of different lengths
  "
    abc
   def
  ghi
  "
  "  abc\n def\nghi"
  "
           s
    o
           s
  "
  "       s\no\n       s"
  "
           s
    o
               s
  "
  "       s\no\n           s"
  ; Empty lines in the middle 
  "
   server {
     listen %d;
     listen [::]:%d;

     server_name %s;

     location / {
       proxy_pass http://%s:%d/;
       proxy_set_header Host $host;
     }
   }
  "
  "server {\n  listen %d;\n  listen [::]:%d;\n\n  server_name %s;\n\n  location / {\n    proxy_pass http://%s:%d/;\n    proxy_set_header Host $host;\n  }\n}"
  ; Empty string -> empty string
  ""
  "")

(defntest-1 split-whitespace
  "a" ["a"]
  "a bc def" ["a" "bc" "def"]
  "1
   2
   3" ["1" "2" "3"])

(defntest re-has?
  [#"xkcd" "abcdefxkcd"] true
  [#"xkcd" "abcdef"] false
  [#"\d+" "abcdef"] false
  [#"\d+" "123456"] true 
  [#"^[abc]+$" "aaabbbbcccc"] true
  [#"^[abc]+$" "aaabbbbdddd"] false 
  [#"[abc]+" "aaabbbbdddd"] true) 

(defntest-1 parse-int
  "21"  21
  "420" 420
  "83"  83
  "-29" -29
  "x" (thrown? Exception))
