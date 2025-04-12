(ns yappinator.frontend.subs.chat
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 :chat/messages
 (fn [db [_ conversation-id]]
   (get-in db [:messages conversation-id] [])))
