# env
Clojure environment configuration library.

[](dependency)
```clojure
[adzerk/boot-cljs "1.7.48-2"] ;; latest release
```

#### Setup

```clojure
;; clojure
(ns foo
  (:require [adzerk.env :as env]))

;; clojurescript
(ns foo
  (:require-macros [adzerk.env :as env]))
```

#### Get

```clojure
;; both clojure and clojurescript
(env/def FOO, ^:required BAR, BAZ)
```

This defines the vars `#'foo/FOO`, `#'foo/BAR`, etc. with values obtained from:

- System property `"FOO"` etc., or
- Environment variable `FOO` etc., otherwise.

The `^:required` metadata on the var instructs env to throw an exception if
neither the system property nor the environment variable is set.

#### Set

```clojure
;; clojure only
(alter-var-root #'FOO (constantly "new value"))
```

The underlying system property will be updated as well as the var itself.
