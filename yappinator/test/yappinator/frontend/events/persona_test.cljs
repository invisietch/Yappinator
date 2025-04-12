(ns yappinator.frontend.events.persona-test
  (:require
   [cljs.test :refer-macros [deftest is testing use-fixtures]]
   [re-frame.core :as rf]
   [day8.re-frame.test :as rf-test]
   [yappinator.frontend.events.persona]
   [yappinator.frontend.events.core]))

(use-fixtures :each
  {:before
   (fn []
     (rf/dispatch-sync [:empty-db]))})

(deftest set-active-persona-test
  (rf-test/run-test-sync
   (testing "Set active persona"
     (let [test-id "persona-123"]
       (rf/dispatch [:persona/set-active test-id])
       (is (= test-id @(rf/subscribe [:persona/active-id])))))

   (testing "Clear active persona"
     (rf/dispatch [:persona/set-active nil])
     (is (nil? @(rf/subscribe [:persona/active-id]))))))