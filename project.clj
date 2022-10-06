(defproject org.clojars.tanelso2/clj-toolbox "0.2.1"
  :description "A collection of tools for use in other Clojure projects"
  :url "https://github.com/tanelso2/clj-toolbox"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/core.async "1.3.618"]
                 [org.clojure/data.json "2.3.1"]]
  :plugins [[lein-cloverage "1.0.13"]
            [lein-shell "0.5.0"]
            [lein-ancient "0.6.15"]
            [lein-changelog "0.3.2"]]
  :profiles {:dev {:dependencies [[org.clojure/clojure "1.10.0"]]}}
  :deploy-repositories [["releases" {:url :clojars :sign-releases false}]
                        ["clojars" {
                                    :sign-releases false}]]
  :source-paths ["src/clj" "src/cljc" "src/cljs"]
  :test-paths ["test/clj" "test/cljc" "test/cljs"]
  :aliases {"update-readme-version" ["shell" "sed" "-i" "s/\\\\[clj-toolbox \"[0-9.]*\"\\\\]/[clj-toolbox \"${:version}\"]/" "README.md"]}
  :release-tasks [["shell" "git" "diff" "--exit-code"]
                  ["change" "version" "leiningen.release/bump-version"]
                  ["change" "version" "leiningen.release/bump-version" "release"]
                  ["changelog" "release"]
                  ["update-readme-version"]
                  ["vcs" "commit"]
                  ["vcs" "tag"]
                  ["deploy"]
                  ["vcs" "push"]])
