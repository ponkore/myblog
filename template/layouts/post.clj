; @layout  default
; @title   post default title

[:article
 ;; page header
 [:div {:class "page-header"}
  ;; title
  [:h3 (link (:title site) "#")]
  ;; post date and tags
  [:div {:class "tag-and-date"}
   [:p {:class "date"} (-> site :date my-date->string)] ; (post-date)
   (post-tags)]]

 ;; contents
 [:div {:class "post"} contents]

 ;; social buttons
 [:div {:class "clearfix"} (social-buttons site)]

 ;; disqus comment
 [:div {:class "clearfix"} (disqus-comment site)]

]
