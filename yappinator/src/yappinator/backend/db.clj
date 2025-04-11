(ns yappinator.backend.db
  (:require [xtdb.api :as xt]
            [xtdb.client :as xtc]))

(defonce node
  (xtc/start-client "http://xtdb:3000"))

(defn submit-tx 
  ([ops] (xt/submit-tx node ops))
  ([ops opts] (xt/submit-tx node ops opts)))

(defn query
  ([q] (xt/q node q))
  ([q opts] (xt/q node q opts)))

(defn entity [id]
  (first (query '(from :xt_docs [{:xt/id $id}])
                {:args {:id id}})))
