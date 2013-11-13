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
  (ul #(link (:name %) (:url %)) {:class "pills"} (:tag site))]]

[:article
 ;; contents
 [:div {:class "post"} contents]]

;; social buttons
[:div {:class "ink-grid"} (social-buttons site)]

;; disqus comment
[:div {:class "ink-grid"} (disqus-comment site)]
