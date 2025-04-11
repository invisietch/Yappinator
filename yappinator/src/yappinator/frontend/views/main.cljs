(ns yappinator.frontend.views.main
  (:require [re-frame.core :as rf]
            [yappinator.frontend.views.login :refer [login-panel]]))

(defn main-app-panel []
  [:div {:class "text-gruvbox-fg p-4"}
   [:h1 "Welcome to Yappinator!"]])

(defn main-view []
  (let [token (rf/subscribe [:auth/token])]
    (fn []
      (if @token
        [main-app-panel]
        [login-panel]))))
