(ns yappinator.backend.kafka.producer
  (:require [jackdaw.client :as jc]
            [clojure.data.json :as json]))

(def producer-config
  {"bootstrap.servers" (str "kafka:" (System/getenv "KAFKA_BROKER_PORT"))})

(def producer (delay (jc/producer producer-config)))

(defn enqueue!
  [topic payload]
  (let [record {:topic-name topic
                :key (str (:character-id payload))
                :value (json/write-str payload)}]
    @(jc/produce! @producer record)))



