(defproject globe "0.1.0-SNAPSHOT"
  :description "A cljoure libary for dealing with world state and updates"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :core.typed {:check [globe.core globe.engine globe.entities]}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/core.incubator "0.1.3"]
                 [org.clojure/core.typed "0.2.19"]
                 [lein-light-nrepl "0.0.6"] ;;Make sure to check what the latest version of lein-light-nrepl is
                 [org.clojure/core.async "0.1.267.0-0d7780-alpha"]]
  :repl-options {:nrepl-middleware [lighttable.nrepl.handler/lighttable-ops]})












