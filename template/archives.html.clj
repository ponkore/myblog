; @layout default
; @title Archives

(defn date->str [d] (str (month d) "-" (day d)))

(defn make-post-list [posts]
  (let [list-fn
        #(list (str (date->str (:date %)) "&nbsp;-&nbsp;")
               (link (:title %) (:url %)))]
    (ul list-fn posts)))

[:article {:class "archives"}
 (let [post-group (group-by #(year (:date %)) (:posts site))]
   (for [year (keys post-group)]
     (list
      [:h2 (str year)]
      (make-post-list (get post-group year)))))]
