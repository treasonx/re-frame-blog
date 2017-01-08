(ns re-frame-blog.subs
    (:require-macros [reagent.ratom :refer [reaction]])
    (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 :name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
 :active-panel
 (fn [db _]
   (:active-panel db)))

(re-frame/reg-sub
 :posts
 (fn [db]
   (:posts db)))

(re-frame/reg-sub
 :new-post
 (fn [db]
   (:new-post db)))

