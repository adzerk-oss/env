(ns adzerk.env)

(defmacro env []
  (if (:js-globals &env)
    (throw (IllegalStateException.
             "macro not available in CLJS: see https://github.com/adzerk-oss/env/issues/1"))
    (->> (merge {} (System/getenv) (System/getProperties))
         (reduce #(into %1 (when-not (re-find #"\." (first %2)) [%2])) {}))))

(defn- setenv [k v]
  (when v (assert (string? v)) (System/setProperty k v) v))

(defmacro def [& ks]
  (let [cljs? (:js-globals &env)]
    (loop [[[k v] & ks] (partition 2 ks), err [], ret []]
      (let [ex `(throw (ex-info ~(str "Required env vars: " err) {:missing-vars '~err}))]
        (when (and cljs? (not k) (seq err)) (eval ex))
        (cond (not k) `(do ~@ret ~(when (seq err) ex))
              :else   (let [k'  (name k)
                            v'  (get (env) k')
                            v'' (or v' (when-not (= :required v)
                                         (if cljs? v `(#'setenv ~k' ~v))))
                            err (into err (when (and (not v') (= :required v)) [k]))]
                        (recur ks err (conj ret `(do (~'def ~k ~v'')
                                                     ~(when-not cljs?
                                                        `(add-watch (var ~k) :adzerk.env/w
                                                           #(when %4 (#'setenv ~k' %4)))))))))))))
