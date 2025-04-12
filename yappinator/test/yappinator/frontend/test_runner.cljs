(ns yappinator.frontend.test-runner
  (:require [re-frame.core :as rf]))

;; Register subscriptions to capture effects
(rf/reg-sub :http-xhrio-captured (fn [db _] (:last-http db)))
(rf/reg-sub :dispatched (fn [db _] (:last-dispatch db)))

(rf/reg-event-db :http-xhrio-captured
                 (fn [db [_ opts]] (assoc db :last-http opts)))

(rf/reg-event-db :dispatch
                 (fn [db [_ event]] (assoc db :last-dispatch event)))
