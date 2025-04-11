(ns yappinator.frontend.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 :message
 (fn [db _]
   (:message db)))

(rf/reg-sub
 :auth/token
 (fn [db _]
   (:auth/token db)))

(rf/reg-sub
 :auth/error
 (fn [db _]
   (:auth/error db)))