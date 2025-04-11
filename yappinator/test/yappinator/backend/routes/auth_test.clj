(ns yappinator.backend.routes.auth-test
  (:require [clojure.test :refer :all]
            [yappinator.backend.models.users :as users]
            [yappinator.backend.routes.auth :as auth]
            [yappinator.backend.auth.jwt :as jwt]
            [yappinator.backend.test-fixtures :refer [with-xtdb-node]]))

(use-fixtures :each with-xtdb-node)

(deftest login-handler-test
  (users/create-user! {:username "testuser" :password "testpass"})

  (testing "successful login"
    (let [response (auth/login-handler {:parameters {:body {:username "testuser"
                                                            :password "testpass"}}})]
      (is (= 200 (:status response)))
      (is (string? (get-in response [:body :token])))))

  (testing "unsuccessful login - wrong password"
    (let [response (auth/login-handler {:parameters {:body {:username "testuser"
                                                            :password "wrongpass"}}})]
      (is (= 401 (:status response)))
      (is (contains? (:body response) :error))))

  (testing "unsuccessful login - no user"
    (let [response (auth/login-handler {:parameters {:body {:username "nosuchuser"
                                                            :password "testpass"}}})]
      (is (= 401 (:status response)))
      (is (contains? (:body response) :error)))))

(deftest jwt-generation-and-verification
  (testing "JWT generation and verification"
    (let [claims {:user-id "test-user-id"}
          token (jwt/generate-token claims)
          verified (jwt/verify-token token)]
      (is (string? token))
      (is (= claims verified))))

  (testing "JWT verification failure"
    (let [invalid-token "invalid.token.here"]
      (is (nil? (jwt/verify-token invalid-token))))))
