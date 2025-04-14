(ns yappinator.common.models.avatar
  (:require [malli.core :as m]))

(def AvatarSchema
  [:map
   [:filename :string]
   [:content-type :string]
   [:url {:optional true} :string]])

(defn valid-avatar? [data]
  (m/validate AvatarSchema data))

(defn explain-avatar-errors [data]
  (m/explain AvatarSchema data))
