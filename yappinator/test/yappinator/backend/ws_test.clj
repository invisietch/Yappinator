(ns yappinator.backend.ws-test
  (:require [clojure.test :refer :all]
            [yappinator.backend.ws :as ws]
            [yappinator.backend.test-fixtures :refer [with-xtdb-node]]))

(use-fixtures :each with-xtdb-node)

(deftest sente-setup-test
  (testing "Sente server setup"
    (is (some? ws/sente-system))
    (is (fn? ws/chsk-send!))
    (is (some? ws/ring-ajax-get-or-ws-handshake))
    (is (some? ws/ring-ajax-post))))
