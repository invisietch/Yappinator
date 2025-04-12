(ns yappinator.frontend.events.conversation
  (:require [re-frame.core :as rf]))

(rf/reg-event-db
 :conversation/select
 (fn [db [_ conversation-id]]
   (assoc-in db [:ui :selected-conversation-id] conversation-id)))

(rf/reg-event-db
 :conversation/create
 (fn [db [_ new-conversation]]
   (update db :conversations conj new-conversation)))

(rf/reg-event-db
 :conversation/toggle-tag
 (fn [db [_ tag-id]]
   (update-in db [:ui :selected-tags] (fnil #(if (contains? % tag-id) (disj % tag-id) (conj % tag-id)) #{}))))
