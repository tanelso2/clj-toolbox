(defproject org.clojars.tanelso2/clj-toolbox "0.11.1-SNAPSHOT"
  :description "A collection of tools for use in other Clojure projects"
  :url "https://github.com/tanelso2/clj-toolbox"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/core.async "1.6.681"]
                 [org.clojure/data.json "2.3.1"]]
  :plugins [[lein-cloverage "1.2.4"]
            [lein-shell "0.5.0"]
            [lein-ancient "0.6.15"]
            [lein-changelog "0.3.2"]]
  :profiles {:dev {:dependencies [[org.clojure/clojure "1.11.4"]
                                  [org.clojure/test.check "1.1.1"]
                                  [com.gfredericks/test.chuck "0.2.13"]
                                  [clj-http "3.12.2"]]}}
  :deploy-repositories [["releases" {:url "https://repo.clojars.org"
                                     :sign-releases false
                                     :username :env/clojars_username
                                     :password :env/clojars_deploy_key}]
                        ["snapshots" :clojars]
                        ["clojars" {:sign-releases false
                                    :username :env/clojars_username
                                    :password :env/clojars_deploy_key}]]
  :source-paths ["src/clj" "src/cljc" "src/cljs"]
  :test-paths ["test/clj" "test/cljc" "test/cljs"]
  :aliases {"bump-version" ["change" "version" "leiningen.release/bump-version"]
            "update-readme-version" ["shell" "sed" "-i" "s/\\\\[clj-toolbox \"[0-9.]*\"\\\\]/[clj-toolbox \"${:version}\"]/" "README.md"]}
  :release-tasks [["vcs" "assert-committed"]
                  ; TODO:
                  ; I think this does the same thing as assert-committed
                  ["shell" "git" "diff" "--exit-code"]
                  ["bump-version" "release"]
                  ["changelog" "release"]
                  ["update-readme-version"]
                  ["vcs" "commit"]
                  ["vcs" "tag" "--no-sign"]
                  ["deploy"]
                  ["bump-version" "patch"]
                  ["vcs" "commit"]
                  ["vcs" "push"]])
