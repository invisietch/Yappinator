(ns yappinator.frontend.db)

(defn get-token-from-storage []
  (when (exists? js/localStorage)
    (.getItem js/localStorage "auth-token")))

(def default-db
  {:auth {:token (get-token-from-storage)
          :error nil
          :loading? false}
   :ui {:context :characters
        :selected-filter nil
        :selected-conversation-id nil
        :selected-character-id nil
        :selected-tags #{}}
   :characters [{:id "char-1" :name "Gandalf" :avatar-url "/avatars/gandalf.png"}
                {:id "char-2" :name "Aragorn" :avatar-url "/avatars/aragorn.png"}]
   :tags [{:id "tag-1" :name "Fantasy"}
          {:id "tag-2" :name "Adventure"}]
   :conversations [{:id "conv-1"
                    :title "Journey to Mordor"
                    :character-id "char-1"
                    :tags ["tag-1"]}
                   {:id "conv-2"
                    :title "Battle of Helm's Deep"
                    :character-id "char-2"
                    :tags ["tag-1" "tag-2"]}]
   :messages {"conv-1" [{:id "msg-1"
                         :author {:id "char-1" :type :assistant
                                  :name "Gandalf"
                                  :avatar-url "/avatars/gandalf.png"}
                         :content "You shall not pass!"
                         :timestamp "2025-04-11T10:30:00Z"
                         :status {:ack :success :stored :success :clean :success :ok :success}}]
              "conv-2" [{:id "msg-2"
                         :author {:id "char-2" :type :assistant
                                  :name "Aragorn"
                                  :avatar-url "/avatars/aragorn.png"}
                         :content "Hold your ground!"
                         :timestamp "2025-04-11T11:00:00Z"
                         :status {:ack :pending :stored :pending :clean :pending :ok :pending}}]}})
