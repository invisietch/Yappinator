(ns yappinator.frontend.components.conversation-list
  (:require [re-frame.core :as rf]
            [yappinator.frontend.components.tag-filter :refer [tag-filter-dropdown]]))

(defn conversation-list []
  (let [conversations (or @(rf/subscribe [:conversation/filtered-list]) [])
        selected-conv @(rf/subscribe [:conversation/selected-id])]
    [:div.flex.flex-col.bg-gruvbox-dark2.text-gruvbox-fg.w-64.p-2
     [:div.flex.justify-between.items-center.mb-2
      [:span.font-semibold.text-sm "Conversations"]
      [tag-filter-dropdown]]

     (for [{:keys [id title]} conversations]
       ^{:key id}
       [:div.cursor-pointer.px-2.py-1.rounded.text-sm.hover:bg-gruvbox-dark3
        {:class (if (= id selected-conv) "bg-gruvbox-dark3" "")
         :on-click #(rf/dispatch [:conversation/select id])}
        title])]))



