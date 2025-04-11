(ns yappinator.models.users
  (:require [yappinator.utils.ids :as ids]
            [buddy.hashers :as hashers]
            [yappinator.db :as db]))

(defn create-user! [{:keys [username password]}]
  (let [user {:xt/id (ids/next-id)
              :user/username username
              :user/password-hash (hashers/derive password)}]
    (db/submit-tx [[:put-docs :users user]])
    user))

(defn find-by-username [username]
  (first (db/query '(-> (from :users [xt/id user/username user/password-hash])
                        (where (= user/username $username))
                        (return xt/id user/username user/password-hash))
                   {:args {:username username}})))

(defn check-password [username password]
  (when-let [user (find-by-username username)]
    (hashers/check password (:user/password-hash user))))

(defn ensure-user! [{:keys [username password]}]
  (when-not (find-by-username username)
    (create-user! {:username username :password password})))