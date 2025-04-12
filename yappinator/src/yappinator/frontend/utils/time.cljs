(ns yappinator.frontend.utils.time
  (:require ["timeago.js" :as timeago]))

(defn time-ago [timestamp]
  (timeago/format (js/Date. timestamp)))