{
 ;; directory setting
 :public-dir   "../ponkore.github.com/"
 :tag-out-dir  "tag/"
 :template-dir "template/"
 :post-dir     "posts/"
 :layout-dir   "layouts/"

 ;; posts and tags url setting
 ;;   default value: "/"
 ;;   ex)
 ;;     "/"    => "/YYYY-MM/POST.html"
 ;;     "/foo" => "/foo/YYYY-MM/POST.html"
 :url-base     "/"

 ;; dev server port
 :port 8080

 ;; site language
 :lang "ja"

 ;; default site data
 :site {:atom-base  "http://ponkore.github.io" ; please change "localhost:8080" to your blog domain
        :charset "utf-8"
        :site-title "(カッコの中の小人)"
        :site-subtitle "主にProgrammingなネタ集です。"
        :site-meta-description "This is @ponkore's blog"
        :site-meta-author "@ponkore"
        :site-url "http://ponkore.github.io"
        :twitter    "ponkore"
        :disqus-shortname "ponkoresblog"
        :css ["/css/github.css"
              "http://fastly.ink.sapo.pt/3.1.1/css/ink.css"
              "/css/main.css"]
        :js ["//ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"
             "http://fastly.ink.sapo.pt/3.1.1/js/ink-all.js"
             "/js/retina-1.1.0.min.js"]
        :js-prettify ["/js/script.js"
                      "/js/highlight.pack.js"]
        :cljs ["js/cljs-main.js"]
        }

 ;; post file compile hook
 :compile-with-post ["index.html.clj" "atom.xml.clj"]

 ;; tag setting
 :tag-layout "tag"

 ;; post setting
 :post-filename-regexp #"(\d{4})[-_](\d{1,2})[-_](\d{1,2})[-_](.+)\.html.clj$"
 :post-filename-format "$(year)-$(month)/$(filename)/index.html"

 ;; 1ページあたりのポスト数
 ;;    デフォルト値: nil (ページネーション無効)
 :posts-per-page 10

 ;; 2ページ目以降のページ分けされたトップページのファイル名フォーマット
 ;;   デフォルト値: "page$(page)/$(filename)"
 ;;   利用可能な変数:
 ;;     @page    : ページ番号(1..N)
 ;;     @filename: ファイル名
 ;;     @name    : 拡張子抜きのファイル名
 ;;     @ext     : 拡張子
 ;; :page-filename-format "page$(page)/$(filename)"
 :page-filename-format "pages/$(page)/$(filename)"

 ;; post sort type (:date :name :title :date-desc :name-desc :title-desc)
 :post-sort-type :date-desc

 ;; clojurescript compile options
 ;; src-dir base is `:template-dir`
 ;; output-dir base is `:public-dir`
 :cljs {:src-dir       "cljs"
        :output-to     "js/cljs-main.js"
        ;; :optimizations :simple
        ;; :pretty-print  true
        :optimizations :advanced
        :pretty-print  false
        }

 ;; highlight setting
 :code-highlight {:CLJ     "clojure"
                  :CLOJURE "clojure"
                  :LISP    "lisp"
                  :SQL     "sql"
                  :CSS     "css"
                  :JAVA    "java"
                  :HTML    "html"
                  :RUBY    "ruby"
                  :SH      "bash"
                  }

 ;; flag for detailed log
 :detailed-log true

 ;; flag for error notification
 ;;   default value: false
 :notify? true

 ;; notify setting(OPTIONAL)
 :notify-setting {;; title for fixing notification
                  :fixed-title  "$(filename)"
                  ;; message for fixing notication
                  :fixed        "FIXED"
                  ;; title for failing notification
                  :failed-title "$(filename) : $(line)"
                  ;; message for failing notification
                  :failed       "$(message)"}

 ;; compiler setting
 :compiler ["default" "cljs"]
}
