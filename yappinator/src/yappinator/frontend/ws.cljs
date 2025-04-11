(ns yappinator.frontend.ws
  (:require [taoensso.sente :as sente]
            [re-frame.core :as rf]))

(defonce sente-client (atom nil))

(rf/reg-event-fx
 :init-websocket
 (fn [{:keys [db]} [_ token]]
   (let [socket (sente/make-channel-socket-client!
                 "/chsk"
                 {:type :auto
                  :params {:token token}})]
     (reset! sente-client socket)
     {:db db})))

(defn send-event! [event]
  (when-let [send-fn (:send-fn @sente-client)]
    (send-fn event)))
