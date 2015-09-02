# env
Clojure environment configuration library.

[](dependency)
```clojure
[adzerk/boot-cljs "1.7.48-2"] ;; latest release
```

### Setup

Clojure:

```clojure
(ns foo
  (:require [adzerk.env :as env]))
```

ClojureScript:

```clojure
(ns foo
  (:require-macros [adzerk.env :as env]))
```

### Get Environment Vars

Clojure and ClojureScript:

```clojure
(env/def FOO, ^:required BAR, BAZ)
```

This defines the vars `#'foo/FOO`, `#'foo/BAR`, etc. with values obtained from:

- System property `"FOO"` etc., or
- Environment variable `FOO` otherwise.

The `^:required` metadata on the var instructs env to throw an exception if
neither the system property nor the environment variable is set.

### Set Environment Vars

Clojure only:

```clojure
(alter-var-root #'FOO (constantly "new value"))
```

The underlying system property will be updated as well as the var itself.
