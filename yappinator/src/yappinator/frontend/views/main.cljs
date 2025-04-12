(ns yappinator.frontend.views.main
  (:require [re-frame.core :as rf]
            [yappinator.frontend.views.login :refer [login-view]]
            [yappinator.frontend.components.top-bar :refer [top-bar]]
            [yappinator.frontend.components.character-sidebar :refer [character-sidebar]]
            [yappinator.frontend.components.conversation-list :refer [conversation-list]]
            [yappinator.frontend.components.conversation-panel :refer [conversation-panel]]))

(defn main-app-panel []
  (let [token @(rf/subscribe [:auth/token])]
    (if-not token
      [login-view]
      [:div.flex.flex-col.h-screen.bg-gruvbox-dark0
       [top-bar]
       [:div.flex.flex-1.overflow-hidden
        [character-sidebar]
        [conversation-list]
        [conversation-panel]]])))

(defn main-view []
  (let [token (rf/subscribe [:auth/token])]
    (fn []
      (if @token
        [main-app-panel]
        [login-view]))))
