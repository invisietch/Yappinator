(ns yappinator.backend.ws
  (:require [taoensso.sente :as sente]
            [taoensso.sente.server-adapters.http-kit :refer [get-sch-adapter]]
            [yappinator.backend.auth.jwt :as jwt]
            [yappinator.backend.handlers.character :as character]))

(defn user-id-fn [ring-req]
  (let [token (get-in ring-req [:params :token])]
    (:user-id (jwt/verify-token token))))

(defonce sente-system
  (sente/make-channel-socket-server!
   (get-sch-adapter)
   {:user-id-fn user-id-fn}))

(def chsk-send! (:send-fn sente-system))
(def connected-uids (:connected-uids sente-system))
(def ring-ajax-get-or-ws-handshake (:ajax-get-or-ws-handshake-fn sente-system))
(def ring-ajax-post (:ajax-post-fn sente-system))

(defn wrap-auth [handler]
  (fn [{:keys [_ uid] :as ev-msg}]
    (if uid
      (handler ev-msg)
      (chsk-send! uid [:auth/error {:error "Unauthorized"}]))))

(defmulti event-msg-handler :id)

(defmethod event-msg-handler :character/create [ev-msg]
  (character/handle-create-character (assoc ev-msg :send-fn chsk-send!)))

(defn start-router! []
  (sente/start-server-chsk-router!
   (:ch-recv sente-system)
   (wrap-auth event-msg-handler)))





