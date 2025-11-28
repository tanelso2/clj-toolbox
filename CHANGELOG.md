# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).


## [Unreleased]
This release exists because I messed up the release process for 2.1.0

## [2.1.0] — 2025-11-28
* Add inclusive/exclusive options to clj-toolbox.colls/take-range
* Convert some extraneous macros in clj-toolbox.test-utils to be a function instead
* Add clj-toolbox.files/copy
* Add clj-toolbox.files/backup!
* Add clj-toolbox.files/home

## [2.0.0] — 2025-04-30
* Remove deprecated functions from clj-toolbox.files
* Added clj-toolbox.modules for copying vars from other modules
* Broke out everything from clj-toolbox.prelude into relevant modules - copied back in to prelude by using clj-toolbox.modules/copy-into-ns
* Removed defnmem from clj-toolbox.prelude (I did not actually use it much and the clj-toolbox.modules macros can't handle other macros yet)
* Moved most cljc modules to being clj modules
* Add clj-toolbox.test-utils/test-dir
* Added namespace documentation

## [1.0.1] — 2025-04-23
* Switch clj-toolbox.test-utils to use box-trim from clj-toolbox.strings

## [1.0.0] — 2025-04-23
* Fully remove clj-toolbox.string-tools
* Add clj-toolbox.maths/square
* Add clj-toolbox.maths/exp-or-throw!
* Add clj-toolbox.functools/inverse and mul-inverse
* Add clj-toolbox.walks/walk-with
* Add clj-toolbox.walks/format-doubles

## [0.11.1] — 2025-03-27
* Fix exception thrown when clj-toolbox.strings/box-trim input has empty lines

## [0.11.0] — 2025-02-22
* Add clj-toolbox.prelude/re-has?
* Add clj-toolbox.prelude/unlazy

## [0.10.1] — 2025-01-29
* Add clj-http to dependencies

## [0.10.0] — 2025-01-29
* Add clj-toolbox.prelude/not-empty?
* Add files/size and files/children
* Add clj-toolbox.atoms
* Add clj-toolbox.shelltools
* Rename clj-toolbox.string-tools to clj-toolbox.strings
* Add clj-toolbox.strings/split-whitespace
* Add clj-toolbox.urls

## [0.9.0] — 2024-12-30
* Add property tests using clojure.test.check for streams and base64
* Add base64/decode-str
* Deprecated files/[file-mkdir, file-mkdirs, file-last-modified]
* Added files/[mkdir, mkdirs, last-modified, f+, f!+, abs-path, path->dirname]

## [0.8.3] — 2024-09-02
* Add streams/with-out-stream
* Add base64/encode-str
* Bump version of clojure/core.async

## [0.8.2] — 2024-08-24
* Fix string-tools/box-trim throwing exceptions when some lines are bigger than others
* Add test-utils/with-expected-output to be similar to expect tests in OCaml

## [0.8.1] — 2024-08-20
* Switch string-tools/box-trim to use volatiles internally instead of atoms

## [0.8.0] — 2024-08-15
* Add string-tools/box-trim
* Add prelude/take-range
* Generate a more unique name for tests defined with defntest

## [0.7.3] — 2024-08-14
* Add files/create-temp-file
* Add prelude/strict-partition
* Fix test-utils/defntest-1 from silently dropping a mismatched test-pair

## [0.7.2] — 2024-08-13
* Added files/read-all

## [0.7.1] — 2024-08-07
* Add files/strip-ext

## [0.7.0] — 2024-08-06
* Change output of test-utils so they produce readable errors

## [0.6.0] — 2024-08-05
* Added `clj-toolbox.files/[dir-exists? path->ext]`

## [0.5.1] — 2024-07-26

## [0.5.0] — 2024-07-26

## [0.4.0] — 2023-04-05
- Figured out how the release process should work


[0.4.0]: https://github.com/tanelso2/clj-toolbox/compare/0.0.0...0.4.0
[0.5.0]: https://github.com/tanelso2/clj-toolbox/compare/0.4.0...0.5.0
[0.5.1]: https://github.com/tanelso2/clj-toolbox/compare/0.5.0...0.5.1
[0.6.0]: https://github.com/tanelso2/clj-toolbox/compare/0.5.1...0.6.0
[0.7.0]: https://github.com/tanelso2/clj-toolbox/compare/0.6.0...0.7.0
[0.7.1]: https://github.com/tanelso2/clj-toolbox/compare/0.7.0...0.7.1
[0.7.2]: https://github.com/tanelso2/clj-toolbox/compare/0.7.1...0.7.2
[0.7.3]: https://github.com/tanelso2/clj-toolbox/compare/0.7.2...0.7.3
[0.8.0]: https://github.com/tanelso2/clj-toolbox/compare/0.7.3...0.8.0
[0.8.1]: https://github.com/tanelso2/clj-toolbox/compare/0.8.0...0.8.1
[0.8.2]: https://github.com/tanelso2/clj-toolbox/compare/0.8.1...0.8.2
[0.8.3]: https://github.com/tanelso2/clj-toolbox/compare/0.8.2...0.8.3
[0.9.0]: https://github.com/tanelso2/clj-toolbox/compare/0.8.3...0.9.0
[0.10.0]: https://github.com/tanelso2/clj-toolbox/compare/0.9.0...0.10.0
[0.10.1]: https://github.com/tanelso2/clj-toolbox/compare/0.10.0...0.10.1
[0.11.0]: https://github.com/tanelso2/clj-toolbox/compare/0.10.1...0.11.0
[0.11.1]: https://github.com/tanelso2/clj-toolbox/compare/0.11.0...0.11.1
[1.0.0]: https://github.com/tanelso2/clj-toolbox/compare/0.11.1...1.0.0
[1.0.1]: https://github.com/tanelso2/clj-toolbox/compare/1.0.0...1.0.1
[2.0.0]: https://github.com/tanelso2/clj-toolbox/compare/1.0.1...2.0.0
[2.1.0]: https://github.com/tanelso2/clj-toolbox/compare/2.0.0...2.1.0
[2.1.0]: https://github.com/tanelso2/clj-toolbox/compare/2.1.0...2.1.0
[Unreleased]: https://github.com/tanelso2/clj-toolbox/compare/2.1.0...HEAD
