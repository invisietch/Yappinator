(ns frontend.core
  (:require [reagent.core :as r]
            [reagent.dom.client :as rdom]
            [re-frame.core :as rf]
            [frontend.events]
            [frontend.subs]))

(defn main-panel []
  (let [msg @(rf/subscribe [:message])]
    [:div [:h1 msg]]))

(defonce root (rdom/create-root (.getElementById js/document "app")))

(defn mount-root []
  (rf/dispatch-sync [:initialize-db])
  (rdom/render root [main-panel]))

(defn init []
  (mount-root))