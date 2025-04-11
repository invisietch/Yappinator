(ns yappinator.frontend.events.auth-test
  (:require
    [cljs.test :refer-macros [deftest is testing use-fixtures]]
    [re-frame.core :as rf]
    [re-frame.subs :as rs]
    [clojure.string :as str]
    [day8.re-frame.test :as rf-test]
    [yappinator.frontend.events.auth]
    [yappinator.frontend.events.core]))

(set! rs/warn-when-not-reactive (constantly nil))
(rf/reg-event-db :http-xhrio-captured (fn [db [_ opts]] (assoc db :last-http opts)))
(rf/reg-sub :http-xhrio-captured (fn [db _] (:last-http db)))
(rf/reg-event-db :websocket/init (fn [db _] db))

(use-fixtures :each
  {:before
   (fn []
     (rf/dispatch-sync [:initialize-db])
     (rf/reg-fx :http-xhrio (fn [opts] (rf/dispatch [:http-xhrio-captured opts]))))})

(deftest login-event-test
  (rf-test/run-test-sync
   (rf/dispatch [:auth/login "user" "pass"])
    (let [http-opts (:last-http @re-frame.db/app-db)]
         (is (true? @(rf/subscribe [:auth/loading?])))
         (is (= :post (:method http-opts)))
         (is (str/includes? (:uri http-opts) "/api/auth/login")))))

(deftest login-success-test
  (rf-test/run-test-sync
   (rf/dispatch [:auth/login-success {:token "test-token"}])
   (is (= "test-token" @(rf/subscribe [:auth/token])))
   (is (false? @(rf/subscribe [:auth/loading?])))))

(deftest login-failure-test
  (rf-test/run-test-sync
   (rf/dispatch [:auth/login-failure {:status-text "Invalid credentials"}])
   (is (= "Invalid credentials" @(rf/subscribe [:auth/error])))
   (is (false? @(rf/subscribe [:auth/loading?])))))