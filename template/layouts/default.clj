; @title   Ponkore's Blog
; @format  html5

;;; banner
(defn misaki-banner
  "link to misaki official(?) banner."
  []
  (link {:target "_blank"} (img "http://liquidz.github.io/img/misaki_banner.svg") "https://github.com/liquidz/misaki"))

(defn my-date->string
  "Convert org.joda.time.DateTime to String"
  [date]
  (if date (.toString date "yyyy-MM-dd")))

(defn my-post-list
  "Make default all posts unordered list."
  [site]
  (let [list-fn
        #(list (str (my-date->string (:date %)) "&nbsp;-&nbsp;")
               (link (:title %) (:url %)))]
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
  [:ul {:class "social-buttons"}
   [:li (hatena-bookmark-button site)]
   [:li (tumblr-share-button site)]
   [:li (tweet-button :lang "ja" :label "ツイート")]
   [:li (facebook-like-button site)]])

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

(defn img*2x
  "img tag for Retina display (iOS Device, MacBookPro)"
  ([src] (img*2x {} "" src))
  ([x src]
     (cond
      (string? x) (img*2x {} x src)
      (map? x)    (img*2x x "" src)
      :else       (img*2x {} "" src)))
  ([attr alt src]
     (let [public-dir (:public-dir misaki.config/*config*)
           f (java.io.File. (str public-dir src))]
       (if-not (.exists f)
         (img "/img/notfound.png")
         (let [r (javax.imageio.ImageIO/read f)
               w (/ (.getWidth r) 2)
               h (/ (.getHeight r) 2)
               attr (merge attr {:width w :height h})]
           (img attr alt src))))))

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
 [:meta {:name "author" :content (str "@" (:twitter site))}]

 [:link {:rel "shortcut icon" :href "/favicon.ico"}]

 (css [(:css site ())])
]

[:body

 [:div {:class "ink-grid"}
  ;; header
  [:header {:class "ink-grid"}
   [:div {:class "vertical-space"}
    [:h2 {:id "title"} [:a {:href "/"} (:site-title site)]]
    ]]

  ;; menu
  [:nav {:class "ink-navigation"}
   [:ul {:class "menu horizontal flat blue"}
    [:li
     [:a {:href "/"} "Home"]]
    [:li
     [:a {:href "/archives.html"} "Archives"]]
    [:li
     [:a {:href "/about.html"} "About me"]]
   ]]

  ;; main container
  [:div {:class "ink-grid vertical-space"}]

  contents ;; main contents

  [:footer
   [:div {:class "ink-grid" :style "float:right;"}
    (misaki-banner)]]]

 [:div {:class "ink-grid vertical-space"}]

 (js [(:js site ())])

 ;; code prettify
 (if (:enable-prettify (meta contents))
   (js [(:js-prettify site ())]))

]
