(ns yappinator.backend.models.characters
  (:require [yappinator.backend.db :as db]
            [yappinator.backend.utils.keys :refer [namespace-keys-recursive denamespace-keys-recursive]]
            [yappinator.common.models.character :as schema]
            [malli.core :as m]))

(defn validate-character! [character-data]
  (when-not (m/validate schema/CharacterSchema character-data)
    (throw (ex-info "Invalid character data" {:errors (m/explain schema/CharacterSchema character-data)})))
  character-data)

(defn create-character! [character-data]
  (validate-character! character-data)
  (let [doc (-> character-data
                (assoc :xt/id (:id character-data))
                (dissoc :id)
                (namespace-keys-recursive "character" #{:xt/id}))]
    (db/submit-tx [[:put-docs :characters doc]])
    doc))

(defn find-by-id [character-id]
  (some-> (db/query '(-> (from :characters [xt/id
                                           character/owner-id
                                           character/character-card
                                           character/avatar])
                        (where (= xt/id $id))
                        (return xt/id
                                character/owner-id
                                character/character-card
                                character/avatar))
                   {:args {:id character-id}})
          first
          denamespace-keys-recursive))


