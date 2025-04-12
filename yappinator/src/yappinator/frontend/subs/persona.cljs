(ns yappinator.frontend.subs.persona
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 :persona/active-id
 (fn [db _] (get-in db [:ui :active-persona-id])))
