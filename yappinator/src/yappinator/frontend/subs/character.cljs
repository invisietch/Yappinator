(ns yappinator.frontend.subs.character
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 :character/list
 (fn [db _] (get db :characters [])))

(rf/reg-sub
 :character/selected-id
 (fn [db _] (get-in db [:ui :selected-character-id])))

(rf/reg-sub
 :character/selected
 :<- [:character/selected-id]
 :<- [:character/list]
 (fn [[selected-id characters] _]
   (some #(when (= (:id %) selected-id) %) characters)))
