(ns yappinator.frontend.subs.chat-test
  (:require
    [cljs.test :refer-macros [deftest is testing use-fixtures]]
    [re-frame.core :as rf]
    [day8.re-frame.test :as rf-test]
    [yappinator.frontend.subs.chat]
    [yappinator.frontend.events.chat]
    [yappinator.frontend.events.core]))

(use-fixtures :each
  {:before
   (fn []
     (rf/dispatch-sync [:empty-db]))})

(def test-conversation
  {:id "test-conv"
   :title "Council of Elrond"
   :character-id "char-2"
   :tags ["urgent"]})

(def test-message
  {:id "msg-2"
   :author {:id "char-2" :type :user
            :name "Frodo"
            :avatar-url "/avatars/frodo.png"}
   :content "I will take the ring to Mordor"
   :timestamp "2025-04-11T11:00:00Z"
   :status {:ack :pending}})

(deftest chat-subs-test
  (rf-test/run-test-sync
    (testing "Empty initial state"
      (is (empty? @(rf/subscribe [:chat/messages "non-existent"]))))

    (testing "Message handling"
      (rf-test/run-test-sync
        (rf/dispatch [:conversation/create {:id "test-conv" :title "First"}])
        (rf/dispatch [:chat/send "test-conv" test-message])
        (let [messages (vec @(rf/subscribe [:chat/messages "test-conv"]))]
          (is (= 1 (count messages)))
          (is (= test-message (first messages))))
        (rf/dispatch [:chat/send "test-conv" (assoc test-message :id "msg-3")])
        (is (= 2 (count (vec @(rf/subscribe [:chat/messages "test-conv"])))))))

    (testing "Multiple conversations"
      (rf-test/run-test-sync
        (rf/dispatch [:conversation/create {:id "convo-1" :title "First"}])
        (rf/dispatch [:conversation/create {:id "convo-2" :title "Other"}])
        (rf/dispatch [:chat/send "convo-2" test-message])
        (let [messages (vec @(rf/subscribe [:chat/messages "convo-2"]))]
          (is (= 1 (count messages)))
          (is (= (:content test-message) (:content (first messages)))))
        (is (empty? @(rf/subscribe [:chat/messages "convo-3"])))))))