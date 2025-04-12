(ns yappinator.frontend.events.core
  (:require [re-frame.core :as rf]
            [yappinator.frontend.db :as db]))

(rf/reg-event-db
 :initialize-db
 (fn [_ _]
   db/default-db))

(rf/reg-event-db
 :empty-db
 (fn [_ _]
   {}))

(rf/reg-event-db
 :select-character
 (fn [db [_ character-id]]
   (assoc-in db [:ui :selected-character-id] character-id)))
