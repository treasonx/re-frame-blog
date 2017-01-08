(ns re-frame-blog.events
    (:require [re-frame.core :as re-frame]
              [re-frame-blog.db :as db]))

(defn clear-post [db]
   (assoc db :new-post {:title "" :body ""}))

(re-frame/reg-event-db
 :initialize-db
 (fn  [_ _]
   db/default-db))

(re-frame/reg-event-db
 :set-active-panel
 (fn [db [_ active-panel params]]
   (clear-post db)
   (println params)
   (assoc db :active-panel active-panel)))

(re-frame/reg-event-db
 :save-new-post
 (fn [db [_ _]]
   (def post (:new-post db))
   (clear-post db)
   (assoc db :posts (conj (db :posts) post))))

(re-frame/reg-event-db
 :update-new-post
 (fn [db [_ post]]
   (def new-post {:title (or (post :title) (get-in db [:new-post :title]))
                        :body (or (post :body) (get-in db [:new-post :body]))})
   (assoc db :new-post new-post) 
   ))

