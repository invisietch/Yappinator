(ns yappinator.frontend.events.auth-test
  (:require
    [cljs.test :refer-macros [deftest is testing use-fixtures]]
    [re-frame.core :as rf]
    [re-frame.subs :as rs]
    [day8.re-frame.test :as rf-test]
    [clojure.string :as str]
    [yappinator.frontend.events.auth]
    [yappinator.frontend.events.core]))

(set! rs/warn-when-not-reactive (constantly nil))
(def ^:private local-storage-calls (atom []))

(defn- mock-local-storage []
  #js {:setItem (fn [k v] (swap! local-storage-calls conj [:set k v]))
        :removeItem (fn [k] (swap! local-storage-calls conj [:remove k]))})

(use-fixtures :each
  {:before
   (fn []
     (reset! local-storage-calls [])
     (set! js/localStorage (mock-local-storage))
     (rf/dispatch-sync [:initialize-db])
     (rf/reg-fx :http-xhrio (fn [opts] (rf/dispatch [:http-xhrio-captured opts])))
     (rf/reg-event-fx :websocket/init (fn [_ _] {})))})

(rf/reg-event-db :http-xhrio-captured (fn [db [_ opts]] (assoc db :last-http opts)))
(rf/reg-sub :http-xhrio-captured (fn [db _] (:last-http db)))

(deftest login-event-test
  (rf-test/run-test-sync
    (rf/dispatch [:auth/login "user" "pass"])
    (let [http-opts (:last-http @re-frame.db/app-db)]
      (is (true? @(rf/subscribe [:auth/loading?])))
      (is (= :post (:method http-opts)))
      (is (str/includes? (:uri http-opts) "/api/auth/login")))))

(deftest login-success-test
  (rf-test/run-test-sync
    (let [token "test-token"]
      (rf/dispatch [:auth/login-success {:token token}])
      (is (= token @(rf/subscribe [:auth/token])))
      (is (false? @(rf/subscribe [:auth/loading?])))
      (is (= [:set "auth-token" token] (last @local-storage-calls))))))

(deftest set-token-local-storage-test
  (rf-test/run-test-sync
    (let [token "test-token"]
      (rf/dispatch [:auth/set-token-local-storage token])
      (is (= [:set "auth-token" token] (last @local-storage-calls))))))

(deftest logout-test
  (rf-test/run-test-sync
    (rf/dispatch [:auth/login-success {:token "test-token"}])
    (rf/dispatch [:auth/logout])
    (is (nil? @(rf/subscribe [:auth/token])))
    (is (= [:remove "auth-token"] (last @local-storage-calls)))))

(deftest remove-token-local-storage-test
  (rf-test/run-test-sync
    (rf/dispatch [:auth/remove-token-local-storage])
    (is (= [:remove "auth-token"] (last @local-storage-calls)))))

(deftest login-failure-test
  (rf-test/run-test-sync
    (rf/dispatch [:auth/login-failure {:status-text "Invalid credentials"}])
    (is (= "Invalid credentials" @(rf/subscribe [:auth/error])))
    (is (false? @(rf/subscribe [:auth/loading?])))))