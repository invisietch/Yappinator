(ns yappinator.frontend.views.login
  (:require [re-frame.core :as rf]
            [reagent.core :as r]))

(defn login-view []
  (let [username (r/atom "")
        password (r/atom "")
        loading? (rf/subscribe [:auth/loading?])
        error (rf/subscribe [:auth/error])]
    (fn []
      [:div {:class "flex items-center justify-center min-h-screen bg-gruvbox-dark0"}
       [:div {:class "max-w-sm w-full bg-gruvbox-dark1 rounded-xl shadow-lg p-8"}
        [:h2 {:class "text-2xl font-bold text-gruvbox-fg mb-6"} "Login"]
        [:input {:type "text"
                 :placeholder "Username"
                 :value @username
                 :class "w-full mb-4 p-2 rounded bg-gruvbox-dark2 text-gruvbox-fg"
                 :on-change #(reset! username (-> % .-target .-value))}]
        [:input {:type "password"
                 :placeholder "Password"
                 :value @password
                 :class "w-full mb-4 p-2 rounded bg-gruvbox-dark2 text-gruvbox-fg"
                 :on-change #(reset! password (-> % .-target .-value))}]
        [:button {:class (str "w-full p-2 rounded font-bold "
                              (if @loading? "bg-gruvbox-gray cursor-not-allowed"
                                  "bg-gruvbox-blue"))
                  :on-click #(when-not @loading?
                               (rf/dispatch [:auth/login @username @password]))}
         (if @loading? "Signing in..." "Sign In")]
        (when @error
          [:div {:class "mt-4 text-gruvbox-red"}
           "Error: " @error])]])))
