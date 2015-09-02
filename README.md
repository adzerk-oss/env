# env
Clojure / ClojureScript environment configuration library.

## Overview

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

## Usage

[](dependency)
```clojure
[adzerk/env "0.1.0"] ;; latest release
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
