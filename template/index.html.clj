;; Define template options here
; @layout  default
; @title   home

(my-post-list site)

(if-let [url (:prev-page site)]
  (link "previous page" url))

(if-let [url (:next-page site)]
  (link "next page" url))
