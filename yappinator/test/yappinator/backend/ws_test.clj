(ns yappinator.backend.ws-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [yappinator.backend.ws :as ws]
            [yappinator.backend.test-fixtures :refer [with-xtdb-node]]
            [yappinator.backend.models.users :as users]
            [yappinator.backend.auth.jwt :as jwt]
            [yappinator.backend.utils.ids :as ids]
            [yappinator.backend.kafka.producer :as kafka]
            [yappinator.common.models.character :as char]))

(use-fixtures :each with-xtdb-node)

(def test-user {:username "test-user" :password "test-pass"})

(defn setup-test-user []
  (users/ensure-user! test-user)
  (users/find-by-username (:username test-user)))

(deftest character-create-ws-handler-test
  (testing "Character creation WS event with valid character schema and JWT auth"
    (let [user (setup-test-user)
          user-id (:xt/id user)
          token (jwt/generate-token {:user-id user-id})
          mock-character-data {:character-card {:name "Test"
                                                :description "Desc"
                                                :first-message "Hello!"
                                                :alternate-greetings ["Hi"]
                                                :tags []}
                               :avatar {:filename "avatar.png"
                                        :content-type "image/png"}}

          mock-ring-req {:params {:token token}}
          resolved-uid (ws/user-id-fn mock-ring-req)

          mock-event {:id :character/create
                      :uid resolved-uid
                      :event {:data mock-character-data}}

          kafka-called-with (atom nil)
          ack-called-with (atom nil)]

      (with-redefs [kafka/enqueue!
                    (fn [_ payload]
                      (reset! kafka-called-with payload))
                    ws/chsk-send!
                    (fn [_ msg]
                      (reset! ack-called-with msg))
                    ids/next-id (constantly 123456789)]
        (ws/event-msg-handler mock-event)

        (is (= user-id resolved-uid))
        (is (char/valid-character? @kafka-called-with))
        (is (= (:owner-id @kafka-called-with) user-id))

        (is (= @ack-called-with
               [:character/create-ack {:character-id 123456789 :status :acknowledged}]))))))



