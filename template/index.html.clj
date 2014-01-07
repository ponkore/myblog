;; Define template options here
; @layout  default
; @title   home

(my-post-list site)

[:nav {:class "ink-navigation"}
 [:ul {:class "pagination"}
  (if-let [prev-url (:prev-page site)]
    [:li {:class "previous"} (link "Newer" prev-url)]
    [:li {:class "disabled previous"} (link "Newer" "#")])
  (if-let [next-url (:next-page site)]
    [:li {:class "next"} (link "Older" next-url)]
    [:li {:class "disabled next"} (link "Older" "#")])
  ]]
