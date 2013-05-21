; @layout  post
; @title   misaki での画像の Retina 対応について
; @tag misaki

;;
;;
[:h3 "Retina Display でもきれいに表示したい"]

(p "iPhone、iPad、MacBookPro Retina model といったいわゆる ** Retina Display ** では、画像は縦横２倍に自動的にスケーリングされて表示されます。そうすると、画像は dot-by-dot で高精細に表示されるのではなく、少しぼやけた感じに表示されてしまいます。")

(p "これを回避する、すなわち Retina Display でも画像を dot-by-dot できれいに表示するには、img タグに ** width **, ** height ** を真面目に指定して、その各々２倍のサイズの高精細画像を用意する、ということのようです（細かな仕様までは知らないです）。")

(p "ただ、実際の画像の大きさなんていちいち調べるのも面倒なので、[misaki](https://github.com/liquidz/misaki) で HTML を生成する際に、image のサイズを調べて、実際の半分の大きさの width、height を指定するようにしてみました。misaki ではサイトの出力先は `_config.clj` の `:public-dir` で指定するので、そこを起点に image ファイルをちょっと読み込み、image ファイルのサイズを調べて :width、:height を算出してみました。")

#-CLJ
;;
;; 以下を template/layout/default.clj に仕込んでおく
;;
(defn img*2x
  "img tag for Retina display (iOS Device, MacBookPro)"
  ([src] (img*2x {} "" src))
  ([x src]
     (cond
      (string? x) (img*2x {} x src)
      (map? x)    (img*2x x "" src)
      :else       (img*2x {} "" src)))
  ([attr alt src]
     (let [public-dir (:public-dir misaki.config/*config*)          ;; */
           f (java.io.File. (str public-dir src))]
       (if-not (.exists f)
         (img "/img/notfound.png")
         (let [r (javax.imageio.ImageIO/read f)
               w (/ (.getWidth r) 2)
               h (/ (.getHeight r) 2)
               attr (merge attr {:width w :height h})]
           (img attr alt src))))))
;;
;; Usage:
;;
(img*2x "/img/posts/2013-04-19/IMG_1337.JPG")
CLJ

[:h3 "終わりに"]

(p "Retina じゃないディスプレイだと、image サイズが半分になってしまうので、CSS の Media Query をつかって切り分けをしたいところです（そのうちちゃんとやろう）。")

(p "[ここ (Webサイト＆WebアプリのRetina対応方法まとめ) ](http://kray.jp/blog/retina-web/) を部分的に参考にさせていただきました(ちょっと端折り過ぎた、かもw)。")
