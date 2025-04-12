(ns yappinator.frontend.subs.conversation
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 :conversation/list
 (fn [db _] (get db :conversations [])))

(rf/reg-sub
 :conversation/selected-id
 (fn [db _] (get-in db [:ui :selected-conversation-id])))

(rf/reg-sub
 :conversation/selected-tags
 (fn [db _] (get-in db [:ui :selected-tags] #{})))

(rf/reg-sub
 :conversation/filtered-list
 (fn [db _]
   (let [selected-char (get-in db [:ui :selected-character-id])
         selected-tags (set (get-in db [:ui :selected-tags] #{}))]
     (vec (filter (fn [{:keys [character-id tags]}]
                    (and (= character-id selected-char)
                         (or (empty? selected-tags)
                             (every? #(contains? (set tags) %) selected-tags))))
                  (:conversations db []))))))

(rf/reg-sub
 :conversation/tags
 (fn [db _] (get db :tags [])))

(rf/reg-sub
 :conversation/selected
 :<- [:conversation/selected-id]
 :<- [:conversation/list]
 (fn [[selected-id conversations] _]
   (or (some #(when (= (:id %) selected-id) %) conversations) {})))
