## Usage

```fish
# On Guix set JAVA_CMD when this error appears:
#   Java compiler not found; Be sure to use java from a JDK
#   rather than a JRE by modifying PATH or setting JAVA_CMD.
set --export JAVA_CMD $HOME/.guix-profile/bin/java
lein install
lein deploy clojars
# lein do install, deploy clojars
```

```clj
[utils.core :refer :all]
```
## License

Copyright © 2017, 2018, 2019, 2020, 2021, 2022

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
