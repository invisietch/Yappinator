(ns yappinator.frontend.views
  (:require [re-frame.core :as rf]
            [reagent.core :as r]))

(defn login-panel []
  (let [username (r/atom "")
        password (r/atom "")
        error (rf/subscribe [:auth/error])]
    (fn []
      [:div
       [:input {:type "text"
                :placeholder "Username"
                :value @username
                :on-change #(reset! username (-> % .-target .-value))}]
       [:input {:type "password"
                :placeholder "Password"
                :value @password
                :on-change #(reset! password (-> % .-target .-value))}]
       [:button {:on-click #(rf/dispatch [:login @username @password])}
        "Login"]
       (when @error
         [:div.error "Error: " @error])])))

(defn main-view []
  [:div
   [login-panel]])
