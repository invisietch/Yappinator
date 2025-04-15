(ns yappinator.backend.kafka.config)

(defn kafka-host []
  (str "kafka:" (System/getenv "KAFKA_BROKER_PORT")))

(defn consumer-config
  ([group-id] (consumer-config group-id "earliest"))
  ([group-id auto-offset-reset]
   {"bootstrap.servers" (kafka-host)
    "group.id" group-id
    "auto.offset.reset" auto-offset-reset
    "key.deserializer" "org.apache.kafka.common.serialization.StringDeserializer"
    "value.deserializer" "org.apache.kafka.common.serialization.StringDeserializer"}))

(def producer-config
  {"bootstrap.servers" (kafka-host)
   "key.serializer" "org.apache.kafka.common.serialization.StringSerializer"
   "value.serializer" "org.apache.kafka.common.serialization.StringSerializer"})

