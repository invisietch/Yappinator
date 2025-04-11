(ns yappinator.utils.ids-test
  (:require [clojure.test :refer [deftest is testing]]
            [yappinator.utils.ids :as ids]))

(deftest test-next-id
  (testing "TSID generation"
    (let [id1 (ids/next-id)
          id2 (ids/next-id)]
      (is (integer? id1) "TSID should be integer")
      (is (integer? id2) "TSID should be integer")
      (is (< id1 id2) "IDs should be time-sortable (id1 < id2)")
      (is (not= id1 id2) "IDs should be unique"))))
