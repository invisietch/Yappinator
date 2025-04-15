(ns yappinator.backend.handlers.character-test
  (:require [clojure.test :refer [deftest is testing]]
            [yappinator.backend.handlers.character :as character]
            [yappinator.common.models.character :as schema]
            [yappinator.backend.kafka.producer :as kafka]))

(deftest test-handle-create-character
  (testing "Valid character request"
    (let [event {:head {:request-id 1}
                 :body {:name "Gandalf"
                        :description "Wizard"
                        :first-message "Fly, you fools!"
                        :alternate-greetings ["Greetings!"]
                        :tags []}}
          kafka-called (atom nil)
          send-called (atom nil)]

      (with-redefs [schema/valid-character? (constantly true)
                    kafka/enqueue! (fn [_ payload] (reset! kafka-called payload))]
        (character/handle-create-character {:uid 42 :event event :send-fn (fn [_ msg] (reset! send-called msg))})

        (is (some? @kafka-called))
        (is (= (:request-id (:head @kafka-called)) 1))
        (is (= (:owner-id (:body @kafka-called)) 42))
        (is (= (second @send-called)
               {:request-id 1
                :character-id (:id (:body @kafka-called))
                :status :acknowledged})))))

  (testing "Invalid character request"
    (let [event {:head {:request-id 2}
                 :body {:invalid-key true}}
          send-called (atom nil)]

      (with-redefs [schema/valid-character? (constantly false)
                    schema/explain-character-errors (constantly {:errors "invalid schema"})]
        (character/handle-create-character {:uid 42 :event event :send-fn (fn [_ msg] (reset! send-called msg))})

        (is (= (second @send-called)
               {:request-id 2
                :status :invalid
                :errors {:errors "invalid schema"}}))))))

