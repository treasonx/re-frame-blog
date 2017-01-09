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
  (aset js/location "hash" "/new"))

(defn goto-post [index]
  (aset js/location "hash" (str "/blog/" index)))

(defn goto-index []
  (aset js/location "hash" ""))

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

;; blog detail
(defn link-to-home-page []
  [re-com/hyperlink-href
   :label "Index page"
   :href "#/"])

(defn link-to-edit [index]
  [re-com/hyperlink-href
   :label "Edit"
   :href (str "#/edit/" index)])

(defn blog-panel []
  (let [selected-post (re-frame/subscribe [:selected-post])]
  [re-com/v-box
   :gap "1em"
   :children [[title (@selected-post :title)] 
              [:p (@selected-post :body)] 
              [link-to-home-page]
              [link-to-edit (@selected-post :index)]]]))

;; edit fields 
(defn title-field [value on-change]
  [re-com/v-box
   :gap "1em"
   :children [[re-com/label :label "Title"] [re-com/input-text 
               :model value
               :change-on-blur? false
               :on-change on-change]]])

(defn body-field [value on-change]
  [re-com/v-box
   :gap "1em"
   :children [[re-com/label :label "Body"] [re-com/input-textarea
               :model value
               :change-on-blur? false
               :on-change on-change]]])

;; blog edit

(defn save-post [index]
  (re-frame/dispatch [:save-post index])
  (goto-post index))

(defn delete-post [index]
  (re-frame/dispatch [:delete-post index])
  (goto-index))

(defn save-button [index]
  [re-com/button
   :label "Save"
   :on-click #(save-post index)])

(defn delete-button [index]
  [re-com/button
   :label "Delete"
   :on-click #(delete-post index)])


(defn edit-panel []
  (let [selected-post (re-frame/subscribe [:selected-post])]
  [re-com/v-box
   :gap "1em"
   :children [[title (str "Edit: " (@selected-post :title))] 
              [title-field (@selected-post :title) #(re-frame/dispatch [:update-new-post {:title %}])] 
              [body-field (@selected-post :body) #(re-frame/dispatch [:update-new-post {:body %}])]
              [save-button (@selected-post :index)]
              [delete-button]
              [link-to-home-page]]]))

;; new 
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
   :children [[title-field "" #(re-frame/dispatch [:update-new-post {:title %}])] 
              [body-field "" #(re-frame/dispatch [:update-new-post {:body %}])] 
              [save-new-button]]])
   
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
    :edit-panel [edit-panel]
    [:div]))

(defn main-panel []
  (let [active-panel (re-frame/subscribe [:active-panel])]
    (fn []
      [re-com/v-box
       :height "100%"
       :children [[panels @active-panel]]])))
