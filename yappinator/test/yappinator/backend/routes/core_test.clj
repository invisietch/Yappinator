(ns yappinator.backend.routes.core-test
  (:require [clojure.test :refer [deftest is testing]]
            [yappinator.backend.routes.core :refer [app]]
            [ring.mock.request :as mock]
            [muuntaja.core :as m]))

(defn parse-json [body]
  (m/decode m/instance "application/json" body))

(deftest healthz-route-test
  (testing "/api/healthz explicitly returns 200 OK"
    (let [response (app (mock/request :get "/api/healthz"))]
      (is (= 200 (:status response)))
      (is (= {:status "OK"} (parse-json (:body response)))))))
