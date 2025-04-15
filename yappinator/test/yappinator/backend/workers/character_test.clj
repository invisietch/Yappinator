(ns yappinator.backend.workers.character-test
  (:require [clojure.test :refer [deftest is testing]]
            [yappinator.backend.workers.character :as worker]
            [yappinator.backend.models.characters :as character-model]
            [yappinator.backend.kafka.consumer :as consumer]
            [cheshire.core :as json]
            [yappinator.backend.ws :as ws])
)

(deftest test-process-message
  (testing "Successful processing and notification"
    (let [message {:value (json/generate-string {:head {:request-id 123}
                                                 :body {:id 1
                                                        :owner-id 42
                                                        :character-card {:name "Bob"
                                                                         :description "Test"
                                                                         :first-message "Hi!"
                                                                         :alternate-greetings []
                                                                         :tags []}}})}
          notification-called (atom nil)]
      (with-redefs [character-model/create-character!
                    (fn [data]
                      {:xt/id (:id data)
                       :character/owner-id (:owner-id data)
                       :character/character-card (:character-card data)})
                    ws/connected-uids (atom #{42})
                    ws/chsk-send! (fn [uid msg] (reset! notification-called [uid msg]))]

        (worker/process-message nil "character-create" message)

        (is (= @notification-called
               [42 [:character/status {:request-id 123
                                       :character-id 1
                                       :status :persisted}]]))))))

(deftest test-worker-loop-commits-offsets
  (testing "Worker loop commits offsets after processing batch"
    (let [mock-record {:value (json/generate-string {:head {:request-id 123}
                                                     :body {:id 1
                                                            :owner-id 42
                                                            :character-card {:name "Bob"
                                                                             :description "Test"
                                                                             :first-message "Hi!"
                                                                             :alternate-greetings []
                                                                             :tags []}}})}
          notification-called (atom nil)
          commit-called (atom 0)
          poll-counter (atom 0)

          mock-consumer
          (reify consumer/IKafkaConsumer
            (poll! [_ _]
              (if (< @poll-counter 1)
                (do (swap! poll-counter inc) [mock-record])
                []))
            (subscribe! [_ _topics] nil)
            (commit-sync! [_] (swap! commit-called inc))
            (close! [_] nil))

          mock-producer (reify java.io.Closeable
                          (close [_] nil))]

      (with-redefs [character-model/create-character!
                    (fn [data]
                      {:xt/id (:id data)
                       :character/owner-id (:owner-id data)
                       :character/character-card (:character-card data)})
                    ws/connected-uids (atom #{42})
                    ws/chsk-send! (fn [uid msg] (reset! notification-called [uid msg]))]

        (let [worker-future (future (worker/start-worker
                                     (constantly mock-consumer)
                                     (constantly mock-producer)))]
          (Thread/sleep 500)
          (future-cancel worker-future))

        (is (= @notification-called
               [42 [:character/status {:request-id 123
                                       :character-id 1
                                       :status :persisted}]]))

        (is (pos? @commit-called))))))


