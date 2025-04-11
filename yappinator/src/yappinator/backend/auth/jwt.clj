(ns yappinator.backend.auth.jwt
  (:require [buddy.sign.jwt :as jwt]))

(def secret (or (System/getenv "JWT_SECRET") "default-secret"))

(defn generate-token [claims]
  (jwt/sign claims secret {:alg :hs256}))

(defn verify-token [token]
  (try
    (jwt/unsign token secret {:alg :hs256})
    (catch Exception _
      nil)))
