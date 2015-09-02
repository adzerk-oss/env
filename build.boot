(set-env!
  :resource-paths #{"src"}
  :dependencies   '[[org.clojure/clojure       "1.7.0"          :scope "provided"]
                    [adzerk/bootlaces          "0.1.10"         :scope "test"]])

(require
  '[clojure.java.io :as io]
  '[adzerk.bootlaces :refer :all])

(def +version+ "0.1.0")

(bootlaces! +version+)

(task-options!
  pom  {:project     'adzerk/env
        :version     +version+
        :description "Clojure environment configuration library."
        :url         "https://github.com/adzerk-oss/env"
        :scm         {:url "https://github.com/adzerk-oss/env"}
        :license     {"Eclipse Public License" "http://www.eclipse.org/legal/epl-v10.html"}})

