; @title   Ponkore's Blog
; @format  html5

;;; banner
(defn misaki-banner
  "link to misaki official(?) banner."
  []
  (link {:target "_blank"} (img "http://liquidz.github.com/img/misaki_banner.png") "https://github.com/liquidz/misaki"))

(defn my-date->string
  "Convert org.joda.time.DateTime to String"
  [date]
  (if date (.toString date "yyyy-MM-dd")))

(defn p-hack
  "ちょっとバグあり..."
  [v]
  (if (and (vector? v) (= :p (first v)))
    (let [attr (filter map? (rest v))
          contents (->> (remove map? (rest v)) (map first))]
      (into [] (concat '(:p) attr contents)))
    v))

;;; experimental
(defn cut-after-read-more
  "引数の tree 構造の中にある :read-more 以前の要素 tree を返す。"
  [sexp]
  (letfn [(cut-after-read-more* [tree]
            (let [tree (p-hack tree)]
              (loop [[x & xs] tree result ()]
                (let [[node search-continue] (if (sequential? x) (cut-after-read-more* x) [x (not= x :read-more)])
                      tmp-arr (vec (concat result (list node)))]
                  (if search-continue
                    (if (empty? xs) [tmp-arr true] (recur xs tmp-arr))
                    [tmp-arr false])))))]
    (first (cut-after-read-more* sexp))))

(defn my-post-list
  "Make default all posts unordered list."
  [site]
  (let [list-fn
        (fn [post]
          (concat (str (my-date->string (:date post)) "&nbsp;-&nbsp;")
                  (link (:title post) (:url post))
                  (cut-after-read-more (force (:lazy-content-without-htmlize post)))))
        ]
    (ul list-fn (:posts site))
    ))

;;; facebook button
(defn facebook-like-button
  "facebook like button"
  [site]
  (let [params ;; TODO: ぐだぐだなので、なんとかしなくちゃ
        {"href" "http%3A%2F%2Fponkore.github.com%2Fi",
         "layout" "standard",
         "show_faces" "true",
         "width" 300,
         "action" "like",
         "colorscheme" "light",
         "height" 80}
        param-str (clojure.string/join "&" (map (fn [[k v]] (str k "=" v)) params))]
    [:iframe {:src (str "http://www.facebook.com/plugins/like.php?" param-str)
              :scrolling "no"
              :frameborder "0"
              :style "border:none; overflow:hidden; height:41px;"
              :allowTransparency "true"}]))

;;; Tumblr+ ボタン
(defn tumblr-share-button
  "Tumblr share button"
  [site]
  (link {:title "Share on Tumblr"
         :style "display:inline-block; text-indent:-9999px; overflow:hidden; width:81px; height:20px; background:url('http://platform.tumblr.com/v1/share_1.png') top left no-repeat transparent;"}
        "http://www.tumblr.com/share"))

;;; hatena bookmark button
(defn hatena-bookmark-button
  [site]
  "hatena bookmark button"
  (list 
   (link {:class "hatena-bookmark-button"
          :data-hatena-bookmark-title (:title site)
          :data-hatena-bookmark-layout "standard"
          :title "このエントリーをはてなブックマークに追加"}
         (img {:alt ""
               :width "20"
               :height "20"
               :style "border: none;"}
              "http://b.st-hatena.com/images/entry-button/button-only.gif")
         (str "http://b.hatena.ne.jp/entry/" (:site-url site))) ;; TODO: this is my **BUG**!!
   [:script {:type "text/javascript"
             :src "http://b.st-hatena.com/js/bookmark_button.js"
             :charset "utf-8"
             :async "async"}]))

;;; social buttons
(defn social-buttons
  "define social buttons"
  [site]
  [:div {:class "social-buttons"}
   [:div {:class "social-buttons-btn"}
    (hatena-bookmark-button site)]
   [:div {:class "social-buttons-btn"}
    (tumblr-share-button site)]
   [:div {:class "social-buttons-btn"}
    (tweet-button :lang "ja" :label "ツイート")]
   [:div {:class "social-buttons-btn"}
    (facebook-like-button site)]
   ])

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; disqus comment
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; disqus comment and thread
(defn disqus-comment
  "disqus comment and thread"
  [site]
  [:div
   [:div {:id "disqus_thread"}]
   [:script {:type "text/javascript"}
   "(function() {
        var dsq = document.createElement('script'); dsq.type = 'text/javascript'; dsq.async = true;
        dsq.src = 'http://' + '" (:disqus-shortname site)"' + '.disqus.com/embed.js';
        (document.getElementsByTagName('head')[0] || document.getElementsByTagName('body')[0]).appendChild(dsq);
    })();"]
   [:noscript
    "Please enable JavaScript to view the "
    (link "comments powered by Disqus." "http://disqus.com/?ref_noscript")]
   [:a {:href "http://disqus.com" :class "dsq-brlink"}
    "comments powered by " [:span {:class "logo-disqus"} "Disqus"]]])

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Embed hogehoge
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn gist [gistno & filename]
  [:script {:src (str "https://gist.github.com/" gistno ".js" (if (empty? filename) "" (str "?file=" filename)))}])

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Slideshare
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn slideshare [embed-code]
  [:iframe {:src (str "http://www.slideshare.net/slideshow/embed_code/" embed-code)
            :width "427" :height "356"
            :style "border:1px solid #CCC;border-width:1px 1px 0;margin-bottom:5px"
            :allowfullscreen true
            }])
;; "<iframe src=\"http://www.slideshare.net/slideshow/embed_code/13782976\" width=\"427\" height=\"356\" frameborder=\"0\" marginwidth=\"0\" marginheight=\"0\" scrolling=\"no\" style=\"border:1px solid #CCC;border-width:1px 1px 0;margin-bottom:5px\" allowfullscreen> </iframe> <div style=\"margin-bottom:5px\"> <strong> <a href=\"http://www.slideshare.net/masa0kato/clojure-programmingchapter2-13782976\" title=\"Clojure programming-chapter-2\" target=\"_blank\">Clojure programming-chapter-2</a> </strong> from <strong><a href=\"http://www.slideshare.net/masa0kato\" target=\"_blank\">Masao Kato</a></strong> </div>"


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Main layout starts here.
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
[:head
 [:meta {:charset (:charset site)}]
 [:meta {:http-equiv "Content-Language" :content "ja"}]
 [:meta {:http-equiv "Content-Type" :content "text/html; charset=UTF-8"}]
 [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge,chrome=1"}]
 [:title (if (= (:title site) "home")
           (:site-title site)
           (str (:site-title site) " - " (:title site)))]
 [:link {:rel "alternate" :type "application/atom+xml" :title (:title site) :href "/atom.xml"}]
 [:meta {:name "description" :content (:site-meta-description site)}]
 [:meta {:name "viewport" :content "width=device-width; initial-scale=1.0; maximum-scale=1.0; user-scalable=0;"}]
 [:meta {:name "HandheldFriendly" :content "True"}]
 [:meta {:name "MobileOptimized" :content "320"}]
 [:meta {:name "author" :content (:site-meta-author site)}]

 [:link {:rel "shortcut icon" :href "/favicon.ico"}]

 (css [(:css site ())])
]

[:body
 ;; header
 [:header {:class "ink-container"}
  [:div {:class "ink-vspace"}
   [:h3 {:id "title"} [:a {:href "/"} (:site-title site)]]
   ]]

 ;; menu
 [:nav {:class "ink-container ink-navigation"}
  [:ul {:class "horizontal menu"}
   [:li
    [:a {:href "/"} "Home"]]
   [:li
    [:a {:href "/archives.html"} "Archives"]]
   [:li
    [:a {:href "/about.html"} "About me"]]
   ]]

 ;; main container
 [:div {:class "ink-container ink-vspace"}
  contents ;; main contents
  ]

 [:footer
  [:div {:class "ink-container"}
   [:div {:style "float:right;"} (misaki-banner)]]]

 (js [(:js site ())])

 ;; code prettify
 (if (:enable-prettify (meta contents))
   (js [(:js-prettify site ())]))

]
