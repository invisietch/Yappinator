(ns yappinator.backend.core
  (:require [ring.adapter.jetty :as jetty]
            [yappinator.backend.routes.core :refer [app]]
            [yappinator.backend.models.users :as users]
            [clojure.tools.logging :as log]))

(defn wait-for-xtdb [retries interval-ms]
  (loop [attempt 0]
    (let [ready? (try
                   (users/find-by-username "healthcheck")
                   true
                   (catch Exception _
                     (log/info "🔄 Waiting for XTDB...")
                     false))]
      (if ready?
        (log/info "✅ XTDB connection established.")
        (if (< attempt retries)
          (do
            (Thread/sleep interval-ms)
            (recur (inc attempt)))
          (throw (Exception. "❌ XTDB not ready after retries")))))))

(defn seed-admin-user! []
  (log/info "🌱 Seeding admin user...")
  (users/ensure-user! {:username (System/getenv "ADMIN_USER_USERNAME")
                       :password (System/getenv "ADMIN_USER_PASSWORD")}))

(defn -main [& _args]
  (wait-for-xtdb 20 5000)
  (seed-admin-user!)
  (log/info "🚀 Server running at http://localhost:8080")
  (jetty/run-jetty app {:port 8080 :join? true}))
