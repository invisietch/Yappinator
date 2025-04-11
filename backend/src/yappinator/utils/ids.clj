(ns yappinator.utils.ids
  (:import [com.github.f4b6a3.tsid TsidCreator]))

(defn next-id []
  (.toLong (TsidCreator/getTsid)))
