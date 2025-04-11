(ns yappinator.frontend.events.core
  (:require [re-frame.core :as rf]))

(rf/reg-event-db
 :initialize-db
 (fn [_ _]
   {:auth {:token nil
           :error nil
           :loading? false}}))
