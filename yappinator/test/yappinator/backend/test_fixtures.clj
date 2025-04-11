(ns yappinator.backend.test-fixtures
  (:require [clojure.test :refer :all]
            [xtdb.api :as xt]
            [xtdb.node :as xtn]
            [yappinator.backend.db :as db]))

(defn with-xtdb-node [f]
  (let [node (xtn/start-node {})]
    (with-redefs [db/node node
                  db/submit-tx (fn
                                 ([ops] (xt/execute-tx node ops))
                                 ([ops opts] (xt/execute-tx node ops opts)))]
      (try
        (f)
        (finally
          (.close node))))))
