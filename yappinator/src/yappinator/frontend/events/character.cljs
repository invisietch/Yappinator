(ns yappinator.frontend.events.character
  (:require [re-frame.core :as rf]))

(rf/reg-event-db
 :character/select
 (fn [db [_ character-id]]
   (assoc-in db [:ui :selected-character-id] character-id)))

(rf/reg-event-db
 :character/add
 (fn [db [_ character]]
   (update db :characters conj character)))

(rf/reg-event-db
 :character/remove
 (fn [db [_ character-id]]
   (update db :characters #(remove (fn [c] (= (:id c) character-id)) %))))
