(ns yappinator.frontend.core
  (:require [reagent.dom.client :as rdom]
            [re-frame.core :as rf]
            [day8.re-frame.http-fx]

            [yappinator.frontend.events.core]
            [yappinator.frontend.events.auth]
            [yappinator.frontend.events.chat]
            [yappinator.frontend.events.conversation]
            [yappinator.frontend.events.character]
            [yappinator.frontend.events.persona]

            [yappinator.frontend.subs.auth]
            [yappinator.frontend.subs.chat]
            [yappinator.frontend.subs.conversation]
            [yappinator.frontend.subs.persona]
            [yappinator.frontend.subs.character]

            [yappinator.frontend.views.main :refer [main-view]]))

(defonce root (rdom/create-root (.getElementById js/document "app")))

(defn mount-root []
  (rf/dispatch-sync [:initialize-db])
  (rdom/render root [main-view]))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn init [] (mount-root))
