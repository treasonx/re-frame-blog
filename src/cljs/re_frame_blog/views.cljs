(ns re-frame-blog.views
    (:require [reagent.core  :as reagent]
              [re-frame.core :as re-frame]
              [re-com.core :as re-com]))

;; home

(defn title [title]
  [re-com/title
   :label title
   :level :level1])

(defn index-title [posts]
      (if (pos? (count @posts))
        (title (str "Wow I have " (count @posts) " posts!"))
        (title (str "You don't have any posts!"))))

(defn goto-new []
  (re-frame/dispatch [:set-active-panel :new-panel]))

(defn goto-index []
  (re-frame/dispatch [:set-active-panel :index-panel]))

(defn new-button []
  [re-com/button
   :label "New Post"
   :on-click goto-new])

(defn convert-post-to-list-item [index post] 
  ^{:key index}[:li [re-com/hyperlink-href :label (post :title) :href (str "#/blog/" index)]])

(defn list-posts [posts]
  [re-com/v-box
   :gap "1em"
   :children [[:ul (map-indexed convert-post-to-list-item @posts)]]])

(defn index-panel []
  (let [posts (re-frame/subscribe [:posts])]
  [re-com/v-box
   :gap "1em"
   :children [[index-title posts] [new-button] [list-posts posts]]]))

;; about

(defn link-to-home-page []
  [re-com/hyperlink-href
   :label "Index page"
   :href "#/"])

(defn blog-panel []
  [re-com/v-box
   :gap "1em"
   :children [[title "Blog page placeholder"] [link-to-home-page]]])

;; new 

(defn new-title-field []
  [re-com/v-box
   :gap "1em"
   :children [[re-com/label :label "Title"] [re-com/input-text 
               :model ""
               :change-on-blur? false
               :on-change #(re-frame/dispatch [:update-new-post {:title %}])]]])

(defn new-body-field []
  [re-com/v-box
   :gap "1em"
   :children [[re-com/label :label "Body"] [re-com/input-textarea
               :model ""
               :change-on-blur? false
               :on-change #(re-frame/dispatch [:update-new-post {:body %}])]]])

(defn save-new-post []
  (re-frame/dispatch [:save-new-post])
  (goto-index))

(defn save-new-button []
  [re-com/button
   :label "Save"
   :on-click save-new-post])

(defn new-form []
  [re-com/v-box
   :gap "1em"
   :children [[new-title-field] [new-body-field] [save-new-button]]])
   
(defn new-panel []
  [re-com/v-box
   :gap "1em"
   :children [[title "Create new post."] [new-form] [link-to-home-page]]])

;; main

(defn- panels [panel-name]
  (case panel-name
    :index-panel [index-panel]
    :blog-panel [blog-panel]
    :new-panel [new-panel]
    [:div]))

(defn main-panel []
  (let [active-panel (re-frame/subscribe [:active-panel])]
    (fn []
      (println @active-panel)
      [re-com/v-box
       :height "100%"
       :children [[panels @active-panel]]])))
