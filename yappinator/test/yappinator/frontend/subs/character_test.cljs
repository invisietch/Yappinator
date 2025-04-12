(ns yappinator.frontend.subs.character-test
  (:require
   [cljs.test :refer-macros [deftest is testing use-fixtures]]
   [re-frame.core :as rf]
   [day8.re-frame.test :as rf-test]
   [yappinator.frontend.subs.character]
   [yappinator.frontend.events.character]
   [yappinator.frontend.events.core]))

(use-fixtures :each
  {:before
   (fn []
     (rf/dispatch-sync [:empty-db]))})

(deftest character-subs-test
  (rf-test/run-test-sync
   (testing "Initial state"
     (is (empty? @(rf/subscribe [:character/list])))
     (is (nil? @(rf/subscribe [:character/selected-id])))
     (is (nil? @(rf/subscribe [:character/selected]))))

   (testing "Character management"
     (let [char1 {:id "1" :name "Aragorn"}
           char2 {:id "2" :name "Legolas"}]

       (rf-test/run-test-sync
        (rf/dispatch [:character/add char1])
        (is (= [char1] @(rf/subscribe [:character/list])))
        (rf/dispatch [:character/add char2])
        (is (= 2 (count @(rf/subscribe [:character/list])))))

       (rf-test/run-test-sync
        (rf/dispatch [:character/add char2])
        (rf/dispatch [:character/select "2"])
        (is (= "2" @(rf/subscribe [:character/selected-id])))
        (is (= char2 @(rf/subscribe [:character/selected]))))

       (rf-test/run-test-sync
        (rf/dispatch [:character/remove "1"])
        (is (= [] @(rf/subscribe [:character/list])))
        (is (nil? @(rf/subscribe [:character/selected])))))))

  (testing "Non-existent selection"
    (rf-test/run-test-sync
     (rf/dispatch [:character/select "999"])
     (is (nil? @(rf/subscribe [:character/selected]))))))