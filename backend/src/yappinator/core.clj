(ns yappinator.core
  (:require [yappinator.models.users :as users]
            [ring.adapter.jetty :as jetty]))

(defn wait-for-xtdb [retries interval-ms]
  (loop [attempt 0]
    (let [ready? (try
                   (users/find-by-username "healthcheck")
                   true
                   (catch Exception e
                     (println "⚠️ Waiting for XTDB..." (.getMessage e))
                     false))]
      (if ready?
        (println "✅ XTDB connection established.")
        (if (< attempt retries)
          (do
            (Thread/sleep interval-ms)
            (recur (inc attempt)))
          (throw (Exception. "❌ XTDB not ready after retries")))))))

(defn seed-admin-user! []
  (users/ensure-user! {:username (System/getenv "ADMIN_USER_USERNAME")
                       :password (System/getenv "ADMIN_USER_PASSWORD")}))

(defn handler [_request]
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body "Hello from Ring + XTDB 2!"})

(defn -main [& _args]
  (wait-for-xtdb 20 5000)
  (seed-admin-user!)
  (println "Server running at http://localhost:8080")
  (jetty/run-jetty handler {:port 8080 :join? true}))
