(ns yappinator.frontend.events.chat-test
  (:require
   [cljs.test :refer-macros [deftest is use-fixtures]]
   [re-frame.core :as rf]
   [day8.re-frame.test :as rf-test]
   [yappinator.frontend.events.chat]
   [yappinator.frontend.events.core]))

(use-fixtures :each
  {:before
   (fn []
     (rf/dispatch-sync [:empty-db]))})

(def sample-conversation
  {:id "conv-1"
   :title "Journey to Mordor"
   :character-id "char-1"
   :tags ["tag-1"]})

(def sample-message
  {:id "msg-1"
   :author {:id "char-1" :type :assistant
            :name "Gandalf"
            :avatar-url "/avatars/gandalf.png"}
   :content "You shall not pass!"
   :timestamp "2025-04-11T10:30:00Z"
   :status {:ack :success :stored :success :clean :success :ok :success}})

(deftest send-message-test
  (rf-test/run-test-sync
   (rf/dispatch [:conversation/create sample-conversation])
   (rf/dispatch [:chat/send "conv-1" sample-message])
   (let [messages (vec @(rf/subscribe [:chat/messages "conv-1"]))]
     (is (= 1 (count messages)))
     (is (= sample-message (first messages))))))