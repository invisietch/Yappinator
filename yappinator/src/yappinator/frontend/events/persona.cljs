(ns yappinator.frontend.events.persona
  (:require [re-frame.core :as rf]))

(rf/reg-event-db
 :persona/set-active
 (fn [db [_ persona-id]]
   (assoc-in db [:ui :active-persona-id] persona-id)))
