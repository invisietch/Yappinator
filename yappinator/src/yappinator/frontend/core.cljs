(ns yappinator.frontend.core
  (:require [reagent.core :as r]
            [reagent.dom.client :as rdom]
            [re-frame.core :as rf]
            [day8.re-frame.http-fx]
            [yappinator.frontend.events.core]
            [yappinator.frontend.events.auth]
            [yappinator.frontend.subs.auth]
            [yappinator.frontend.views.main :refer [main-view]]))

(defonce root (rdom/create-root (.getElementById js/document "app")))

(defn mount-root []
  (rf/dispatch-sync [:initialize-db])
  (rdom/render root [main-view]))

(defn init []
  (mount-root))
