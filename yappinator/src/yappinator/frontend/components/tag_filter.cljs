(ns yappinator.frontend.components.tag-filter
  (:require [re-frame.core :as rf]
            [reagent.core :as r]
            [yappinator.frontend.icons :as icons]))

(defn tag-pill [tag selected-tags toggle-tag]
  (let [tag-id (:id tag)
        selected? (contains? selected-tags tag-id)]
    [:span.cursor-pointer.rounded-full.px-2.py-1.text-xs.font-semibold.m-1
     {:class (if selected?
               "bg-gruvbox-blue text-gruvbox-dark0"
               "bg-gruvbox-dark3 text-gruvbox-fg hover:bg-gruvbox-dark4")
      :on-click #(toggle-tag tag-id)}
     (:name tag)]))

(defn tag-filter-dropdown []
  (let [open? (r/atom false)]
    (fn []
      (let [tags @(rf/subscribe [:conversation/tags])
            selected-tags @(rf/subscribe [:conversation/selected-tags])
            toggle-tag (fn [tag-id]
                         (rf/dispatch [:conversation/toggle-tag tag-id]))]
        [:div.relative
         [:button.p-1.rounded.hover:bg-gruvbox-dark2
          {:on-click #(swap! open? not)}
          [icons/filter-icon {:class "h-5 w-5 text-gruvbox-fg"}]]

         (when @open?
           [:div.absolute.right-0.z-10.bg-gruvbox-dark1.border.border-gruvbox-dark2.rounded-lg.shadow-lg.p-2.mt-1.w-48
            (for [tag tags]
              ^{:key (:id tag)}
              [tag-pill tag selected-tags toggle-tag])])]))))
