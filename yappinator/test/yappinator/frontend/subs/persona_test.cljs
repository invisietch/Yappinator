(ns yappinator.frontend.subs.persona-test
  (:require
   [cljs.test :refer-macros [deftest is testing use-fixtures]]
   [re-frame.core :as rf]
   [day8.re-frame.test :as rf-test]
   [yappinator.frontend.subs.persona]
   [yappinator.frontend.events.persona]
   [yappinator.frontend.events.core]))

(use-fixtures :each
  {:before
   (fn []
     (rf/dispatch-sync [:empty-db]))})

(deftest persona-subs-test
  (rf-test/run-test-sync
   (testing "Initial state"
     (is (nil? @(rf/subscribe [:persona/active-id]))))

   (testing "Active persona changes"
     (rf-test/run-test-sync
      (rf/dispatch [:persona/set-active "persona-1"])
      (is (= "persona-1" @(rf/subscribe [:persona/active-id])))

      (rf-test/run-test-sync
       (rf/dispatch [:persona/set-active "persona-2"])
       (is (= "persona-2" @(rf/subscribe [:persona/active-id])))

       (rf-test/run-test-sync
        (rf/dispatch [:persona/set-active nil])
        (is (nil? @(rf/subscribe [:persona/active-id])))))))))