(ns yappinator.backend.kafka.consumer
  (:require [jackdaw.client :as jc]))

(defprotocol IKafkaConsumer
  (poll! [this timeout-ms])
  (subscribe! [this topics])
  (commit-sync! [this])
  (close! [this]))

(defrecord KafkaConsumer [consumer]
  IKafkaConsumer
  (poll! [_ timeout-ms] (jc/poll consumer timeout-ms))
  (subscribe! [_ topics] (jc/subscribe consumer topics))
  (commit-sync! [_] (.commitSync consumer))
  (close! [_] (.close consumer)))

(defn create-consumer [config]
  (->KafkaConsumer (jc/consumer config)))

