(ns yappinator.common.models.avatar-test
  (:require [clojure.test :refer [deftest is testing]]
            [yappinator.common.models.avatar :as avatar]))

(deftest avatar-schema-test
  (testing "Valid avatar data"
    (is (avatar/valid-avatar?
         {:filename "avatar.png"
          :content-type "image/png"
          :url "http://example.com/avatar.png"}))

    (is (avatar/valid-avatar?
         {:filename "avatar.jpg"
          :content-type "image/jpeg"}))) ;; url optional

  (testing "Invalid avatar data"
    (is (not (avatar/valid-avatar?
              {:filename nil
               :content-type "image/png"})))

    (is (not (avatar/valid-avatar?
              {:filename "avatar.png"
               :content-type nil}))))

  (testing "Explain avatar errors"
    (let [errors (avatar/explain-avatar-errors {:filename nil :content-type nil})]
      (is (some? errors))
      (is (seq (:errors errors))))))