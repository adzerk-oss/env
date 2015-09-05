(ns adzerk.env
  (:require [clojure.string :refer [join]]))

(defmacro def [& ks]
  `(do ~@(loop [[k & ks] ks, err [], ret []]
           (if-not k
             (into ret (when (seq err)
                         [`(throw (ex-info (str "Required env vars: " '~err)
                                           {:missing-vars '~err}))]))
             (let [k'  (name k)
                   v   (or (System/getProperty k') (System/getenv k'))
                   err (into err (when (and (not v) (:required (meta k))) [k]))]
               (recur ks err (conj ret `(do (~'def ~k ~v)
                                            ~(when-not (:js-globals &env)
                                               `(add-watch (var ~k) :adzerk.env/w
                                                  #(when %4 (System/setProperty ~k' %4))))))))))))
