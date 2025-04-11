(ns yappinator.models.users-test
  (:require [clojure.test :refer :all]
            [yappinator.models.users :as users]
            [yappinator.test-fixtures :refer [with-xtdb-node]]))

(use-fixtures :each with-xtdb-node)

(deftest create-and-find-user
  (testing "creating and retrieving a user explicitly"
    (let [user (users/create-user! {:username "testuser"
                                    :password "testpass"})
          fetched (users/find-by-username "testuser")]
      (is (= (:xt/id user) (:xt/id fetched)))
      (is (= "testuser" (:user/username fetched))))))

(deftest ensure-user-idempotent
  (testing "ensure-user! explicitly doesn't duplicate users"
    (users/ensure-user! {:username "admin" :password "adminpass"})
    (users/ensure-user! {:username "admin" :password "newpass"}) ;; should not duplicate
    (let [users-found (users/find-by-username "admin")]
      (is (some? users-found))
      (is (= "admin" (:user/username users-found))))))

(deftest check-password-test
  (testing "password verification explicitly"
    (users/create-user! {:username "authuser" :password "secret123"})
    (is (true? (users/check-password "authuser" "secret123")))
    (is (false? (users/check-password "authuser" "wrongpass")))
    (is (nil? (users/check-password "nosuchuser" "secret123")))))