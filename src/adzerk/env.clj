(ns adzerk.env)

(defmacro def [& ks]
  (let [cljs? (:js-globals &env)]
    (loop [[k & ks] ks, err [], ret []]
      (let [ex `(throw (ex-info ~(str "Required env vars: " err) {:missing-vars '~err}))]
        (when (and cljs? (not k) (seq err)) (eval ex))
        (cond (not k) `(do ~@ret ~(when (seq err) ex))
              :else   (let [k'  (name k)
                            v   (or (System/getProperty k') (System/getenv k'))
                            err (into err (when (and (not v) (:required (meta k))) [k]))]
                        (recur ks err (conj ret `(do (~'def ~k ~v)
                                                     ~(when-not cljs?
                                                        `(add-watch (var ~k) :adzerk.env/w
                                                           #(when %4 (System/setProperty ~k' %4)))))))))))))
