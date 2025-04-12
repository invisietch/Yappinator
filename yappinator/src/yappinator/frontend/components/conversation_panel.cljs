(ns yappinator.frontend.components.conversation-panel
  (:require [re-frame.core :as rf]
            [yappinator.frontend.icons :as icons]
            [yappinator.frontend.utils.time :refer [time-ago]]))

(def status-colors {:success "text-gruvbox-green"
                    :failed  "text-gruvbox-red"
                    :pending "text-gruvbox-gray"})

(defn status-icon [status icon-component]
  [icon-component {:class (str "w-4 h-4 " (get status-colors status "text-gruvbox-gray"))}])

(defn message-status-icons [{:keys [ack stored clean ok]}]
  [:div.flex.space-x-1.items-center
   [status-icon ack icons/cloud-icon]
   [status-icon stored icons/database-icon]
   [status-icon clean icons/check-circle-icon]
   [status-icon ok icons/clipboard-check-icon]])

(defn message [{:keys [author timestamp content status]}]
  [:div.flex.mb-4
   [:img.rounded-full.w-10.h-10.object-cover.mr-2 {:src (:avatar-url author) :alt (:name author)}]
   [:div.flex-1
    [:div.flex.justify-between.items-start
     [:div.flex.flex-col
      [:span.font-bold (:name author)]
      [:span.text-xs.text-gruvbox-gray {:title (subs timestamp 0 16)} (time-ago timestamp)]]
     [message-status-icons status]]
    [:div.mt-1 {:style {:white-space "pre-wrap"}} content]]])

(defn conversation-panel []
  (let [selected-conv @(rf/subscribe [:conversation/selected])
        conversation-id (when selected-conv (:id selected-conv))
        messages (if conversation-id (or @(rf/subscribe [:chat/messages conversation-id]) []) [])]
    [:div.flex.flex-col.flex-1.bg-gruvbox-dark0.text-gruvbox-fg.overflow-auto
     [:div.flex-1.p-4
      (if selected-conv
        (for [msg messages]
          ^{:key (:id msg)} [message msg])
        [:div.text-gruvbox-gray "Select a conversation"])]
     [:div.p-2.bg-gruvbox-dark1
      [:input.bg-gruvbox-dark2.text-gruvbox-fg.rounded.p-2.w-full
       {:placeholder "Type your message..."}]]]))
