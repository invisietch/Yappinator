(ns yappinator.frontend.events.auth
  (:require [re-frame.core :as rf]
            [ajax.core :as ajax]
            [yappinator.frontend.config :as config]))

(rf/reg-event-fx
 :auth/login
 (fn [{:keys [db]} [_ username password]]
   {:db (assoc-in db [:auth :loading?] true)
    :http-xhrio {:method :post
                 :uri (str config/API_URL "/api/auth/login")
                 :params {:username username
                          :password password}
                 :format (ajax/json-request-format)
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [:auth/login-success]
                 :on-failure [:auth/login-failure]}}))

(rf/reg-event-fx
 :auth/login-success
 (fn [{:keys [db]} [_ {:keys [token]}]]
   {:db (assoc-in db [:auth :token] token)
    :fx [[:dispatch [:websocket/init token]]
         [:dispatch [:auth/set-token-local-storage token]]]}))

(rf/reg-event-fx
 :auth/login-failure
 (fn [{:keys [db]} [_ error]]
   {:db (-> db
            (assoc-in [:auth :error] (:status-text error))
            (assoc-in [:auth :loading?] false))}))

(rf/reg-event-fx
 :auth/set-token-local-storage
 (fn [_ [_ token]]
   (.setItem js/localStorage "auth-token" token)
   {}))

(rf/reg-event-fx
 :auth/logout
 (fn [{:keys [db]} _]
   {:db (assoc-in db [:auth :token] nil)
    :fx [[:dispatch [:auth/remove-token-local-storage]]]}))

(rf/reg-event-fx
 :auth/remove-token-local-storage
 (fn [_ _]
   (.removeItem js/localStorage "auth-token")
   {}))

