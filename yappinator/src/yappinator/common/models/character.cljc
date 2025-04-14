(ns yappinator.common.models.character
  (:require [malli.core :as m]
            [yappinator.common.models.avatar :refer [AvatarSchema]]))

(def CharacterCardSchema
  [:map
   [:name :string]
   [:description :string]
   [:scenario {:optional true} [:maybe :string]]
   [:personality {:optional true} [:maybe :string]]
   [:first-message :string]
   [:example-messages {:optional true} [:maybe :string]]
   [:post-history-instructions {:optional true} [:maybe :string]]
   [:creator {:optional true} [:maybe :string]]
   [:creator-notes {:optional true} [:maybe :string]]
   [:character-version {:optional true} [:maybe :string]]
   [:system-prompt {:optional true} [:maybe :string]]
   [:alternate-greetings [:vector :string]]
   [:tags [:vector :any]]])

(def CharacterSchema
  [:map
   [:id {:optional true} :int]
   [:owner-id {:optional true} :int]
   [:character-card CharacterCardSchema]
   [:avatar {:optional true} [:maybe AvatarSchema]]])

(defn valid-character-card? [data]
  (m/validate CharacterCardSchema data))

(defn valid-character? [data]
  (m/validate CharacterSchema data))

(defn explain-character-errors [data]
  (m/explain CharacterSchema data))