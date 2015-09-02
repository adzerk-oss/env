# env
Clojure environment configuration library.

[](dependency)
```clojure
[adzerk/env "0.1.0"] ;; latest release
```
[](/dependency)

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

- This defines the vars `FOO`, `BAR`, and `BAZ`
- The values are set to:
  - the system property of the same name, or
  - the environment variable of the same name otherwise
- The `^:required` metadata will trigger an exception if no system property
  or environment variable can be found with that name.

#### Set

```clojure
;; clojure only
(alter-var-root #'FOO (constantly "new value"))
```

The underlying system property will be updated as well as the var itself.
