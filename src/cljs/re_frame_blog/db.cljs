(ns re-frame-blog.db)

(def default-db
  {:title "My Blog"
   :posts (list)
   :active-panel {:panel :index-panel}
   :new-post {:title "" :body ""}})
