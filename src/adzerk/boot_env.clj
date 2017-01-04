(ns adzerk.boot-env
  (:require
    [clojure.java.io :as io]
    [boot.core       :as boot]
    [boot.util       :as util]
    [adzerk.env      :as env]))

(boot/deftask init
  "Load java properties from disk to set system variables."
  [f file FILE str "Path on disk (NOT fileset) to Java properties file to load"]
  (boot/with-pass-thru _
    (let [env-file (io/file file)]
      (if-not (.exists env-file)
        (util/fail "env: file \"%s\" doesn't exist\n" file)
        (with-open [rdr (io/reader file)]
          (util/info "Loading \"%s\"...\n" file)
          (doseq [[k v] (doto (java.util.Properties.) (.load rdr))]
            (#'env/setenv k v)))))))
