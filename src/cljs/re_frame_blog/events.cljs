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
 (fn [db [_ active-panel]]
   (clear-post db)
   (assoc db :active-panel active-panel)))

(re-frame/reg-event-db
 :set-active-post
 (fn [db [_ index]]
   (clear-post db)
   (def selected-post (nth (:posts db) index))
   (def post-with-index (assoc selected-post :index index))
   (assoc db :selected-post post-with-index)))

(re-frame/reg-event-db
 :save-new-post
 (fn [db [_ _]]
   (def post (:new-post db))
   (clear-post db)
   (def index (count (db :posts)))
   (assoc db :posts (concat (db :posts) [(assoc post :id index)]))))

(re-frame/reg-event-db
 :save-post
 (fn [db [_ index]]
   (let [post (:new-post db)]
     (clear-post db)
     (assoc db :posts (assoc (db :posts) index (assoc post :id index))))))

(re-frame/reg-event-db
 :delete-post
 (fn [db [_ index]]
   (let [post (:new-post db)]
     (clear-post db)
     (assoc db :posts (keep-indexed #( if (not= %1 index) %2) (db :post))))))

(re-frame/reg-event-db
 :update-new-post
 (fn [db [_ post]]
   (def new-post {:title (or (post :title) (get-in db [:new-post :title]))
                        :body (or (post :body) (get-in db [:new-post :body]))})
   (assoc db :new-post new-post) 
   ))

