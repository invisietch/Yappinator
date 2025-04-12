(ns yappinator.frontend.subs.core
  (:require [re-frame.core :as rf]))

(rf/reg-sub
  :ui/context
  (fn [db _]
    (get-in db [:ui :context])))

(rf/reg-sub
  :tags
  (fn [db _]
    (:tags db)))
