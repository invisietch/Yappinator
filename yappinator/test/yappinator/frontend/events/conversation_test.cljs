(ns yappinator.frontend.events.conversation-test
  (:require
   [cljs.test :refer-macros [deftest is testing use-fixtures]]
   [re-frame.core :as rf]
   [day8.re-frame.test :as rf-test]
   [yappinator.frontend.events.conversation]
   [yappinator.frontend.subs.conversation]
   [yappinator.frontend.events.core]))

(def sample-conversation
  {:id "conv-1"
   :title "Council of Elrond"
   :character-id "char-1"
   :tags ["urgent", "mission"]})

(use-fixtures :each
  {:before
   (fn []
     (rf/dispatch-sync [:empty-db])
     (rf/dispatch-sync [:init-conversation-state]))})

(rf/reg-event-db
 :init-conversation-state
 (fn [db _]
   (-> db
       (assoc :conversations [])
       (assoc :tags [{:id "casual" :name "casual"} {:id "urgent" :name "urgent"} {:id "mission" :name "mission"}])
       (assoc-in [:ui :selected-tags] #{}))))

(deftest select-conversation-test
  (rf-test/run-test-sync
   (rf/dispatch [:conversation/select "conv-123"])
   (is (= "conv-123" @(rf/subscribe [:conversation/selected-id])))))

(deftest create-conversation-test
  (rf-test/run-test-sync
   (rf/dispatch [:conversation/create sample-conversation])
   (let [convos @(rf/subscribe [:conversation/list])]
     (is (= 1 (count convos)))
     (is (= sample-conversation (first convos))))))

(deftest toggle-tag-test
  (rf-test/run-test-sync
   (testing "Add tag"
     (rf/dispatch [:conversation/toggle-tag "urgent"])
     (is (contains? @(rf/subscribe [:conversation/selected-tags]) "urgent")))

   (testing "Remove tag"
     (rf/dispatch [:conversation/toggle-tag "urgent"])
     (is (not (contains? @(rf/subscribe [:conversation/selected-tags]) "urgent"))))))