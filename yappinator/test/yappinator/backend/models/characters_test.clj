(ns yappinator.backend.models.characters-test
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [yappinator.backend.models.characters :as chars]
            [yappinator.backend.utils.ids :as ids]
            [yappinator.backend.test-fixtures :refer [with-xtdb-node]]))

(use-fixtures :each with-xtdb-node)

(deftest test-create-character!
  (testing "Creates and retrieves character correctly"
    (let [character-id (ids/next-id)
          character-data {:id character-id
                          :owner-id 42
                          :character-card {:name "Test Character"
                                           :description "Test description"
                                           :first-message "Hello there!"
                                           :alternate-greetings ["Hi!"]
                                           :tags ["tag1" "tag2"]}}
          stored-doc (chars/create-character! character-data)
          fetched-doc (chars/find-by-id character-id)]

      (is (= (:xt/id stored-doc) character-id))
      (is (= (:id fetched-doc) character-id))
      (is (= (:owner-id fetched-doc) 42))
      (is (= (get-in fetched-doc [:character-card :name]) "Test Character")))))

(deftest test-validate-character!
   (testing "Validation success"
     (is (chars/validate-character! {:id 1 :owner-id 2
                                     :character-card {:name "Valid"
                                                      :description "Valid desc"
                                                      :first-message "Hi!"
                                                      :alternate-greetings []
                                                      :tags []}})))

   (testing "Validation failure"
     (is (thrown? Exception
                  (chars/validate-character! {:invalid "data"})))))



