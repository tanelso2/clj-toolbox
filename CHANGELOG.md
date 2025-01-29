# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).


## [Unreleased]
* Add clj-toolbox.prelude/not-empty?
* Add files/size and files/children

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
[Unreleased]: https://github.com/tanelso2/clj-toolbox/compare/0.9.0...HEAD
