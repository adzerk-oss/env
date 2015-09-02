(ns adzerk.env
  (:refer-clojure :exclude [get set update def]))

(defn get    [k]          (or (System/getProperty k) (System/getenv k)))
(defn set    [k v]        (System/setProperty k v))
(defn update [k f & args] (set k (apply f (get k) args)))

(defmacro def [& ks]
  `(do ~@(for [k ks]
           (let [v (get (name k))]
             (when (and (not v) (:required (meta k)))
               (throw (AssertionError. (format "Env var not set: '%s'" k))))
             (list 'def k v)))))
