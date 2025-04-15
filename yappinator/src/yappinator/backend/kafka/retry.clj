(ns yappinator.backend.kafka.retry
  (:require [jackdaw.client :as jc]
            [clojure.data.json :as json]))

(defn current-timestamp []
  (System/currentTimeMillis))

(defn enqueue-with-delay!
  [producer topic message delay-ms]
  (let [process-after (+ (current-timestamp) delay-ms)
        updated-message (assoc-in message [:head :process-after] process-after)]
    @(jc/produce! producer {:topic-name topic
                            :key (str (get-in message [:body :id]))
                            :value (json/write-str updated-message)})))

(defn should-process? [message]
  (let [process-after (get-in message [:head :process-after] 0)]
    (<= process-after (current-timestamp))))

