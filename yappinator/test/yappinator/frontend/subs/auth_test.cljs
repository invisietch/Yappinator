(ns yappinator.frontend.subs.auth-test
  (:require
    [cljs.test :refer-macros [deftest is testing use-fixtures]]
    [re-frame.core :as rf]
    [re-frame.subs :as rs]
    [day8.re-frame.test :as rf-test]
    [yappinator.frontend.subs.auth]
    [yappinator.frontend.events.auth]
    [yappinator.frontend.events.core]))

(set! rs/warn-when-not-reactive (constantly nil))
(rf/reg-event-db :websocket/init (fn [db _] db))

(use-fixtures :each
  {:before
   (fn []
     (rf/dispatch-sync [:initialize-db]))})

(deftest auth-subs-test
  (rf-test/run-test-sync
    (testing "Initial state"
      (is (nil? @(rf/subscribe [:auth/token])))
      (is (nil? @(rf/subscribe [:auth/error])))
      (is (false? @(rf/subscribe [:auth/loading?]))))

    (testing "Login flow"
      (rf/dispatch [:auth/login "user" "pass"])
      (is (true? @(rf/subscribe [:auth/loading?])))
      
      (rf/dispatch [:auth/login-success {:token "secret"}])
      (is (= "secret" @(rf/subscribe [:auth/token])))
      (is (false? @(rf/subscribe [:auth/loading?])))
      
      (rf/dispatch [:auth/login-failure {:status-text "Error"}])
      (is (= "Error" @(rf/subscribe [:auth/error]))))))