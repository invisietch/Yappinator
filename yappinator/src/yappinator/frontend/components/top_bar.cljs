(ns yappinator.frontend.components.top-bar
  (:require [yappinator.frontend.icons :as icons]))

(defn top-bar []
  [:div.flex.items-center.justify-between.p-3.bg-gruvbox-dark3.text-gruvbox-fg.shadow-md
   [:span.font-bold.text-lg "Yappinator"]
   [:button.hover:text-gruvbox-blue
    [icons/cog-icon {:class "h-6 w-6"}]]])
