# env

> The twelve-factor app stores config in environment variables (often
> shortened to env vars or env). Env vars are easy to change between
> deploys without changing any code; unlike config files, there is
> little chance of them being checked into the code repo accidentally;
> and unlike custom config files, or other config mechanisms such as
> Java System Properties, they are a language- and OS-agnostic standard.
>
> Another aspect of config management is grouping. Sometimes apps batch
> config into named groups (often called “environments”) named after
> specific deploys, such as the development, test, and production
> environments in Rails. This method does not scale cleanly: as more
> deploys of the app are created, new environment names are necessary,
> such as staging or qa. As the project grows further, developers may
> add their own special environments like joes-staging, resulting in a
> combinatorial explosion of config which makes managing deploys of the
> app very brittle.
>
> In a twelve-factor app, env vars are granular controls, each fully
> orthogonal to other env vars. They are never grouped together as
> “environments”, but instead are independently managed for each deploy.
> This is a model that scales up smoothly as the app naturally expands
> into more deploys over its lifetime.
>
> &mdash; [The Twelve-Factor App](http://12factor.net/config)

This library is a thin wrapper around the JVM's environment variables and
system properties, in the spirit of the Twelve-Factor App philosophy.

Supported:

- get value of env var from environment
- override value of env var globally, for entire jvm
- throw exception if required env var is not set

Not supported:

- env as a map (see grouping, above)
- configuration files (see config files, above)
- keywordized names (not portable across classloaders)
- non-string values (not portable across classloaders)

## Usage

[](dependency)
```clojure
[adzerk/env "0.1.2"] ;; latest release
```
[](/dependency)

```clojure
;; clojure
(ns foo
  (:require [adzerk.env :as env]))

;; clojurescript
(ns foo
  (:require-macros [adzerk.env :as env]))
```

### Get

```clojure
;; both clojure and clojurescript
(env/def FOO, ^:required BAR, BAZ)
```

- This defines the vars `FOO`, `BAR`, and `BAZ`
- The values are set to:
  - the system property of the same name if it is set, or
  - the environment variable of the same name otherwise
- Add `^:required` meta to throw an exception if no value is set

### Set

```clojure
;; clojure only
(alter-var-root #'FOO (constantly "new value"))
```

- The underlying system property will be updated as well as the var itself.
- Subsequent calls to `env/def` will reflect the updated value.
