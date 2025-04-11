(ns yappinator.frontend.subs.auth
  (:require [re-frame.core :as rf]))

(rf/reg-sub :auth/token
  (fn [db _]
    (get-in db [:auth :token])))

(rf/reg-sub :auth/error
  (fn [db _]
    (get-in db [:auth :error])))

(rf/reg-sub :auth/loading?
  (fn [db _]
    (get-in db [:auth :loading?])))
