(ns yappinator.frontend.subs.conversation-test
  (:require
   [cljs.test :refer-macros [deftest is testing use-fixtures]]
   [re-frame.core :as rf]
   [day8.re-frame.test :as rf-test]
   [yappinator.frontend.subs.conversation]
   [yappinator.frontend.events.conversation]
   [yappinator.frontend.events.core]))

(def test-conversations
  [{:id "conv-1" :character-id "char-1" :tags ["urgent" "mission"]}
   {:id "conv-2" :character-id "char-2" :tags ["casual"]}
   {:id "conv-3" :character-id "char-1" :tags ["mission"]}])

(use-fixtures :each
  {:before
   (fn []
     (rf/dispatch-sync [:empty-db])
     (rf/dispatch-sync [:init-conversation-state])
     (doseq [conv test-conversations]
       (rf/dispatch-sync [:conversation/create conv])))})

(rf/reg-event-db
 :init-conversation-state
 (fn [db _]
   (-> db
       (assoc :conversations [])
       (assoc :tags [{:id "casual" :name "casual"} {:id "urgent" :name "urgent"} {:id "mission" :name "mission"}])
       (assoc-in [:ui :selected-tags] #{}))))

(deftest conversation-subs-test
  (rf-test/run-test-sync
   (testing "Basic subscriptions"
     (is (= 3 (count @(rf/subscribe [:conversation/list])))))

   (testing "Selected conversation"
     (rf/dispatch [:conversation/select "conv-2"])
     (is (= "conv-2" @(rf/subscribe [:conversation/selected-id]))))

   (testing "Filtered list"
     (rf-test/run-test-sync
      (rf/dispatch [:character/select "char-1"])
      (rf/dispatch [:conversation/toggle-tag "mission"])
      (let [filtered @(rf/subscribe [:conversation/filtered-list])]
        (is (= 2 (count filtered))))

      (rf/dispatch [:conversation/toggle-tag "urgent"])
      (let [filtered @(rf/subscribe [:conversation/filtered-list])]
        (is (= 1 (count filtered))))

      (rf/dispatch [:conversation/toggle-tag "mission"])
      (let [filtered @(rf/subscribe [:conversation/filtered-list])]
        (is (= 1 (count filtered)))))

     (testing "Selected conversation details"
       (rf/dispatch [:conversation/select "conv-1"])
       (let [selected @(rf/subscribe [:conversation/selected])]
         (is (= "conv-1" (:id selected)))

         (rf/dispatch [:conversation/select "invalid-id"])
         (is (empty? @(rf/subscribe [:conversation/selected]))))))))

(deftest tags-subs-test
  (rf-test/run-test-sync
   (testing "Tag management"
     (rf/dispatch [:conversation/toggle-tag "new-tag"])
     (let [selected-tags @(rf/subscribe [:conversation/selected-tags])]
       (is (contains? selected-tags "new-tag"))

       (rf/dispatch [:conversation/toggle-tag "new-tag"])
       (let [selected-tags @(rf/subscribe [:conversation/selected-tags])]
         (is (not (contains? selected-tags "new-tag"))))))))