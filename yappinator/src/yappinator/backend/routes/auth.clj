(ns yappinator.backend.routes.auth
  (:require [yappinator.backend.models.users :as users]
            [yappinator.backend.auth.jwt :as jwt]))

(defn login-handler [{:keys [parameters]}]
  (let [{:keys [username password]} (:body parameters)
        user (users/find-by-username username)]
    (if (and user (users/check-password username password))
      {:status 200
       :body {:token (jwt/generate-token {:user-id (:xt/id user)})}}
      {:status 401
       :body {:error "Invalid credentials"}})))

(def auth-routes
  [["/login" {:post login-handler}]])