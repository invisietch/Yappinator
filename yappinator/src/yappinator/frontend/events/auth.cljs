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
   {:db (-> db
            (assoc-in [:auth :token] token)
            (assoc-in [:auth :loading?] false)
            (assoc-in [:auth :error] nil))
    :dispatch [:websocket/init token]}))

(rf/reg-event-db
 :auth/login-failure
 (fn [db [_ error]]
   (-> db
       (assoc-in [:auth :error] (:status-text error))
       (assoc-in [:auth :loading?] false))))
