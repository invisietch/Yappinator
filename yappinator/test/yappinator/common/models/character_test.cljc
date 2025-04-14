(ns yappinator.common.models.character-test
  (:require [clojure.test :refer [deftest is testing]]
            [yappinator.common.models.character :as char]))

(deftest character-card-schema-test
  (testing "Valid character card data"
    (is (char/valid-character-card?
         {:name "Echo"
          :description "Gentle deer"
          :first-message "Hello!"
          :alternate-greetings ["Hi", "Hey"]
          :tags []
          :scenario nil
          :personality nil
          :example-messages nil
          :post-history-instructions nil
          :creator nil
          :creator-notes nil
          :character-version nil
          :system-prompt nil})))

  (testing "Invalid character card data"
    (is (not (char/valid-character-card?
              {:name 123
               :description nil
               :first-message nil
               :alternate-greetings "invalid"
               :tags nil}))))

  (testing "Explain character card errors"
    (let [errors (char/explain-character-errors
                  {:name nil
                   :description nil
                   :first-message nil
                   :alternate-greetings nil
                   :tags nil})]
      (is (some? errors))
      (is (seq (:errors errors))))))

(deftest character-schema-test
  (testing "Valid full character data"
    (is (char/valid-character?
         {:id 123456
          :owner-id 78910
          :character-card {:name "Echo"
                           :description "Gentle deer"
                           :first-message "Hello!"
                           :alternate-greetings ["Hi"]
                           :tags []}
          :avatar {:filename "avatar.png"
                   :content-type "image/png"
                   :url "http://example.com/avatar.png"}}))

    (is (char/valid-character?
         {:character-card {:name "Echo"
                           :description "Gentle deer"
                           :first-message "Hello!"
                           :alternate-greetings ["Hi"]
                           :tags []}}))) ;; minimal required

  (testing "Invalid full character data"
    (is (not (char/valid-character?
              {:id "invalid"
               :owner-id nil
               :character-card nil
               :avatar "invalid"})))

    (let [errors (char/explain-character-errors
                  {:id "invalid"
                   :owner-id nil
                   :character-card nil
                   :avatar {:filename nil :content-type nil}})]
      (is (some? errors))
      (is (seq (:errors errors))))))
