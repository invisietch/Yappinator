(ns yappinator.frontend.components.character-sidebar
  (:require [re-frame.core :as rf]))

(defn character-sidebar []
  (let [characters (or @(rf/subscribe [:character/list]) [])
        selected-char-id @(rf/subscribe [:character/selected-id])]
    [:div.flex.flex-col.bg-gruvbox-dark1.text-gruvbox-fg.w-16.p-2
     (for [{:keys [id avatar-url name]} characters]
       ^{:key id}
       [:div.cursor-pointer.mb-2
        {:on-click #(rf/dispatch [:character/select id])}
        [:img {:src avatar-url
               :alt name
               :title name
               :class (str "rounded-xl w-12 h-12 object-cover border-2 "
                           (if (= id selected-char-id)
                             "border-gruvbox-yellow"
                             "border-transparent"))}]])]))
