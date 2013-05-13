; @layout  default
; @title   post default title
; @enable-prettify true

;; page header
[:div {:class "page-header"}
 ;; post title
 [:h2 [:a {:href (:url site)} (:title site)]]
 ;; post date
 [:div {:class "tag-and-date"}
  [:div {:class "date"} (-> site :date my-date->string)]]
 ;; tags
 [:nav {:class "ink-navigation"}
  (post-tags :class "pills")]]

[:article
 ;; contents
 [:div {:class "post ink-vspace"} contents]]

;; social buttons
[:div {:class "clearfix"} (social-buttons site)]

;; disqus comment
[:div {:class "clearfix"} (disqus-comment site)]
