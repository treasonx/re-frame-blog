(ns re-frame-blog.core
    (:require [reagent.core :as reagent]
              [re-frame.core :as re-frame]
              [re-frame-blog.events]
              [re-frame-blog.subs]
              [re-frame-blog.routes :as routes]
              [re-frame-blog.views :as views]
              [re-frame-blog.config :as config]))


(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (routes/app-routes)
  (re-frame/dispatch-sync [:initialize-db])
  (dev-setup)
  (mount-root))
