(ns build
  (:require [clojure.tools.build.api :as b]))

(def lib 'yappinator/backend)
(def version "0.1.0")
(def class-dir "target/classes")
(def uber-file "target/backend.jar")

(defn clean [_]
  (b/delete {:path "target"}))

(defn uberjar [_]
  (clean nil)
  (b/copy-dir {:src-dirs ["src"] :target-dir class-dir})
  (b/compile-clj {:basis (b/create-basis {:project "deps.edn"})
                  :src-dirs ["src"]
                  :class-dir class-dir})
  (b/uber {:class-dir class-dir
           :uber-file uber-file
           :basis (b/create-basis {:project "deps.edn"})
           :main 'yappinator.core})
  (println "Uberjar built:" uber-file))
