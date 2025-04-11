(ns yappinator.frontend.events
  (:require [re-frame.core :as rf]
            [ajax.core :as ajax]))

(rf/reg-event-db
 :initialize-db
 (fn [_ _]
   {:message "Hello from Yappinator!"}))

(rf/reg-event-db
 :set-token
 (fn [db [_ token]]
   (assoc db :auth/token token)))

(rf/reg-event-fx
 :login-success
 (fn [{:keys [db]} [_ response]]
   {:db (assoc db :auth/token (:token response))
    :dispatch [:init-websocket (:token response)]}))

(rf/reg-event-db
 :login-failure
 (fn [db [_ error]]
   (assoc db :auth/error (:status-text error))))

(rf/reg-event-fx
 :login
 (fn [_ [_ username password]]
   {:http-xhrio {:method :post
                 :uri "/api/auth/login"
                 :params {:username username
                          :password password}
                 :format (ajax/json-request-format)
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [:login-success]
                 :on-failure [:login-failure]}}))
