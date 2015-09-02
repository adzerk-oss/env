(ns adzerk.env
  (:refer-clojure :exclude [get set update def]))

(defn- set   [k v] (System/setProperty k v))
(defn- get   [k]   (or (System/getProperty k) (System/getenv k)))

(defmacro def [& ks]
  `(do ~@(for [k ks]
           (let [k' (name k) v (get k')]
             (when (and (not v) (:required (meta k)))
               (throw (AssertionError. (format "Env var not set: '%s'" k))))
             `(do (~'def ~k ~v)
                  ~(when-not (:js-globals &env)
                     `(add-watch (var ~k) (gensym) #(#'set ~k' %4))))))))
