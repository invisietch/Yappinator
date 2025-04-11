(ns frontend.events
  (:require [re-frame.core :as rf]))

(rf/reg-event-db
 :initialize-db
 (fn [_ _]
   {:message "Hello from Yappinator!"}))