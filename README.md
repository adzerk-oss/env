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

## Motivation

Very small applications are self-contained. They start with a clean
slate, live in a single classloader, and have no persistent mutable
state. These kinds of programs can be configured simply via command
line arguments or by pulling env vars into a Clojure var and passed
to individual components of the application as function parameters.

When the application gets more complex it might be running in multiple
Clojure runtimes across multiple machines and performing side effects
on external resources like databases, message queues, caches, etc.
Furthermore, users inevitably interact with the application via a web
application frontend that needs to perform side effects on the backend
application. These external linkages all need to be configured in the
environment in which the applications are built and deployed.

Finally, complex applications need to be able to bootstrap themselves.
That is to say, the program needs to coordinate the bootstrapping of
its various components when it starts up. This generally takes the
form of the application entry point _setting_ environment vars that
the rest of the application components will use to configure themselves.
Clojure vars are not the answer here because they cannot be accessed
from different Clojure runtimes in the same JVM, and so defeat any
classloader isolation mechanism in use.

So, we can compile a list of requirements:

- Obtain a map of environment variable names and values (both strings)
  from the global env.
- Set or override environment variables such that the new values are
  in effect for the whole JVM.
- Usable in both Clojure and ClojureScript programs.

and some additional features that might be nice to have:

- Define Clojure vars with values from the environment:
  - optionally assign default values for missing env vars, and
  - persist defaults to the JVM global env.
- Optionally throw exception when required env vars are missing
  - with a nice, descriptive error message,
  - at runtime in Clojure, and
  - at compile time in ClojureScript.

and some features we _don't_ want:

- configuration files (see config files, above)
- non-string names or values (not portable across classloaders)

## Usage

[](dependency)
```clojure
[adzerk/env "0.3.0"] ;; latest release
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
(env/def
  FOO nil
  BAR :required
  BAZ "supergood"
  BAF (or BAZ "justokay"))
```

- This defines the vars `FOO`, `BAR`, `BAZ` and `BAF`
- The values are read from:
  - the system property of the same name if it is set, or
  - the environment variable of the same name otherwise
- The righthand side provides default values and can be:
  - `:required` (throw an exception), or
  - an expression that evaluates to a string or `nil`.
- If a default is applied it is persisted in the env
  - subsequent calls to `env/def` or `env/env` reflect the updated value.

> **Note:** Setting or overriding env vars is not currently supported
> in the ClojureScript implementation. The env is read-only there and
> default or overridden values are not persisted in the env.

```clojure
;; clojure only (see https://github.com/adzerk-oss/env/issues/1)
(env/env)
```

- Returns a map of all env vars, with system property overrides as above.

### Set

```clojure
;; clojure only
(alter-var-root #'FOO (constantly "new value"))
```

- The underlying system property will be updated as well as the var itself.
- Subsequent calls to `env/def` and `env/env` will reflect the updated value.

## Hacking

```
# build and install locally
boot build-jar
```
```
# push snapshot
boot build-jar push-snapshot
```
```
# push release
boot build-jar push-release
```

## License

Copyright © 2014 Adzerk

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
