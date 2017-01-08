(ns re-frame-blog.db)

(def default-db
  {:title "My Blog"
   :posts []
   :active-panel {:panel :index-panel}
   :new-post {:title "" :body ""}})
