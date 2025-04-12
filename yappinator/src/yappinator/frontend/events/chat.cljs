(ns yappinator.frontend.events.chat
  (:require [re-frame.core :as rf]))

(rf/reg-event-db
 :chat/send
 (fn [db [_ conversation-id message]]
   (update-in db [:messages conversation-id] conj message)))