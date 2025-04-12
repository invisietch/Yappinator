(ns yappinator.frontend.icons
  (:require ["@heroicons/react/24/outline" :refer [UserIcon TagIcon CogIcon FunnelIcon CloudIcon CircleStackIcon CheckCircleIcon ClipboardDocumentCheckIcon]]
            [reagent.core :as r]))

(def user-icon (r/adapt-react-class UserIcon))
(def tag-icon (r/adapt-react-class TagIcon))
(def cog-icon (r/adapt-react-class CogIcon))
(def filter-icon (r/adapt-react-class FunnelIcon))
(def cloud-icon (r/adapt-react-class CloudIcon))
(def database-icon (r/adapt-react-class CircleStackIcon))
(def check-circle-icon (r/adapt-react-class CheckCircleIcon))
(def clipboard-check-icon (r/adapt-react-class ClipboardDocumentCheckIcon))
