(ns yappinator.backend.utils.keys)

(defn namespace-keys-recursive
  ([m ns] (namespace-keys-recursive m ns #{}))
  ([m ns exclude-keys]
   (cond
     (map? m)
     (reduce-kv (fn [acc k v]
                  (let [new-k (if (exclude-keys k) k (keyword ns (name k)))
                        new-v (namespace-keys-recursive v ns exclude-keys)]
                    (assoc acc new-k new-v)))
                {}
                m)

     (vector? m)
     (mapv #(namespace-keys-recursive % ns exclude-keys) m)

     :else
     m)))

(defn denamespace-keys-recursive [m]
  (cond
    (map? m)
    (reduce-kv (fn [acc k v]
                 (assoc acc (keyword (name k))
                        (denamespace-keys-recursive v)))
               {}
               m)

    (vector? m)
    (mapv denamespace-keys-recursive m)

    :else m))


