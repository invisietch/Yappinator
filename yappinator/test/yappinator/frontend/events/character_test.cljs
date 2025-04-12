(ns yappinator.frontend.events.character-test
  (:require
   [cljs.test :refer-macros [deftest is use-fixtures]]
   [re-frame.core :as rf]
   [day8.re-frame.test :as rf-test]
   [yappinator.frontend.events.character]
   [yappinator.frontend.events.core]))

(use-fixtures :each
  {:before
   (fn []
     (rf/dispatch-sync [:initialize-db]))})

(deftest select-character-test
  (rf-test/run-test-sync
   (let [test-id "char-123"]
     (rf/dispatch [:character/select test-id])
     (is (= test-id @(rf/subscribe [:character/selected-id]))))))

(deftest add-character-test
  (rf-test/run-test-sync
   (let [character {:id "char-456" :name "Gandalf"}]
     (rf/dispatch [:character/add character])
     (is (some #(= character %) @(rf/subscribe [:character/list]))))))

(deftest remove-character-test
  (rf-test/run-test-sync
   (let [char-id "char-789"
         character {:id char-id :name "Frodo"}]
     (rf/dispatch [:character/add character])
     (rf/dispatch [:character/remove char-id])
     (is (not-any? #(= char-id (:id %)) @(rf/subscribe [:character/list]))))))