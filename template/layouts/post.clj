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
(social-buttons site)

[:div {:class "ink-grid vertical-space"}]

;; disqus comment
(disqus-comment site)

;; vertical space
[:div {:class "ink-grid vertical-space"}]
