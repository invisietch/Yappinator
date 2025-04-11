(ns yappinator.backend.routes.core
  (:require [reitit.ring :as ring]
            [reitit.coercion.malli :as coercion]
            [muuntaja.core :as m]
            [reitit.ring.coercion :as rrc]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [ring.middleware.cors :refer [wrap-cors]]
            [yappinator.backend.routes.auth :refer [auth-routes]]))

(defn health-handler [_req]
  {:status 200
   :body {:status "OK"}})

(def app
  (wrap-cors
   (ring/ring-handler
    (ring/router
     ["/api"
      ["/auth" auth-routes]
      ["/healthz" {:get health-handler}]]
     {:data {:muuntaja m/instance
             :middleware [muuntaja/format-middleware
                          rrc/coerce-exceptions-middleware
                          rrc/coerce-request-middleware
                          rrc/coerce-response-middleware]
             :coercion coercion/coercion}})
    (ring/create-default-handler))
   :access-control-allow-origin [#"http://localhost:4000" #"http://localhost:\d+"]
   :access-control-allow-methods [:get :post :put :delete :options]
   :access-control-allow-headers ["Content-Type" "Authorization"]))
