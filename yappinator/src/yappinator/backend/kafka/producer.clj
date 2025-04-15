(ns yappinator.backend.kafka.producer
  (:require [jackdaw.client :as jc]
            [cheshire.core :as json]
            [yappinator.backend.kafka.config :refer [producer-config]]))

(def producer (delay (jc/producer producer-config)))

(defn enqueue!
  [topic payload]
  (let [record {:topic-name topic
                :key (str (:character-id payload))
                :value (json/generate-string payload)}]
    @(jc/produce! @producer record)))





