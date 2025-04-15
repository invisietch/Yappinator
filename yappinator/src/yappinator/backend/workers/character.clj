(ns yappinator.backend.workers.character
  (:require [jackdaw.client :as jc]
            [cheshire.core :as json]
            [yappinator.backend.models.characters :as character-model]
            [yappinator.backend.kafka.config :as kafka-config]
            [yappinator.backend.kafka.consumer :as consumer]
            [yappinator.backend.kafka.retry :as retry]
            [yappinator.backend.ws :refer [connected-uids chsk-send!]]))

(def max-retries 5)
(def base-delay-ms 5000)

(defn notify-client [owner-id request-id character-id status]
  (when (contains? @connected-uids owner-id)
    (chsk-send! owner-id [:character/status {:request-id request-id
                                             :character-id character-id
                                             :status status}])))

(defn attempt-persist [data]
  (try
    {:result (character-model/create-character! data)}
    (catch Exception e
      {:error (.getMessage e)})))

(defn retry-or-fail! [producer topic original-msg retries error-message]
  (if (< retries max-retries)
    (let [delay-ms (* base-delay-ms retries)]
      (retry/enqueue-with-delay! producer topic
                                 (update-in (update-in original-msg [:head :retry-count] (fnil inc 0)) [:head :last-error] error-message)
                                 delay-ms))
    (notify-client (get-in original-msg [:body :owner-id])
                   (get-in original-msg [:head :request-id])
                   (get-in original-msg [:body :id])
                   :error)))

(defn process-message [producer topic message]
  (let [parsed-msg (json/parse-string (:value message) true)
        retries (get-in parsed-msg [:head :retry-count] 0)]
    (if-not (retry/should-process? parsed-msg)
      (jc/produce! producer {:topic-name topic
                             :key (str (get-in parsed-msg [:body :id]))
                             :value (json/generate-string parsed-msg)})

      (let [{:keys [head body]} parsed-msg
            {:keys [request-id]} head
            {:keys [result error]} (attempt-persist body)]
        (if result
          (notify-client (:character/owner-id result) request-id (:xt/id result) :persisted)
          (do
            (notify-client (:owner-id body) request-id (:id body) :persistence-retrying)
            (retry-or-fail! producer topic parsed-msg (inc retries) error)))))))

(defn start-worker
  ([]
   (start-worker #(consumer/create-consumer (kafka-config/consumer-config "character-create-worker"))
                 #(jc/producer kafka-config/producer-config)))
  ([consumer-factory producer-factory]
   (let [consumer (consumer-factory)
         producer (producer-factory)
         topic "character-create"]
     (with-open [consumer consumer
                 producer producer]
       (consumer/subscribe! consumer [topic])
       (loop []
         (let [records (consumer/poll! consumer 1000)]
           (doseq [record records]
             (process-message producer topic record))
           (consumer/commit-sync! consumer))
         (recur))))))



