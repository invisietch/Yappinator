(ns yappinator.backend.handlers.character
  (:require [yappinator.backend.kafka.producer :as kafka]
            [yappinator.backend.utils.ids :as ids]
            [yappinator.common.models.character :as schema]))

(defn handle-create-character [{:keys [uid event send-fn]}]
  (let [{:keys [head body]} event
        {:keys [request-id correlation-id]} head]

    (if-not (schema/valid-character? body)
      (send-fn uid [:character/status {:request-id request-id
                                       :status :invalid
                                       :errors (schema/explain-character-errors body)}])

      (let [character-id (ids/next-id)
            payload {:head {:request-id request-id
                            :correlation-id correlation-id}
                     :body (assoc body :id character-id :owner-id uid)}]
        (kafka/enqueue! "character-create" payload)
        (send-fn uid [:character/status {:request-id request-id
                                         :character-id character-id
                                         :status :acknowledged}])))))

