; @layout  post
; @title   Raspberry PI 買ったったー
; @tag hardware

;;
;;
(section "Raspberry PI 買いました。")

(p* "タイトルの通り、買いました。")

(img*2x "/img/posts/2013-04-19/IMG_1337.JPG")

(p* "通販ではなく、日本橋のシリコンハウス共立で。数日前に [シリコンハ
ウスのBlog](http://blog.siliconhouse.jp/archives/51988254.html) で入荷
したとの案内があったので、Type-B を買っちゃいました(4,410円)。こういう
ものを買うときは「とりあえず買って用途は後から考える」ことが多く、今回
もそのパターンです。特に何をしたいから、という強い理由があるわけではあ
りません。")

(p* "ちなみに、給電は、USB mini-A で、これに相当する AC アダプタも必要
なのですが、さしあたっては自宅にある **eneloop mobile booster** に付属
していたやつを使うことにして、今回は購入していません。SDカードはちょっ
と贅沢をして 32GB のもの (ヨドバシで￥4,000 強) を購入。")

(p* "家に帰って作業時間を何とか作って、火をいれてみました。SD には、公
式の [ダウンロードページ](http://www.raspberrypi.org/downloads) から落
としてきた **Raspbian wheezy** をインストール(というか焼いたという
か...)。ディスプレイは専用で使えるものの手持ちがないので、リビングのTV
に HDMI 経由でつなぎました。")

(p* "結果は、驚くほどあっさりと起動(GUI は使うつもりがないので試してい
ませんが...)。")

(img*2x "/img/posts/2013-04-19/IMG_1338.JPG")

(p* "ただ、仮設状態のまま運用するわけにもいかないので、Wireless ルータ
のそばに移設をしたところ、ネットワーク経由で反応したりしなかったり、と
いう症状が出てしまいました。電源が不安定なのか、はたまたノイズ等が悪さ
をしているのか...ルータのそばにはディスプレイがないので、ネットワーク
経由での接続ができないと状況がわかりません(がんばれば USB シリアルログ
インとかできそうな気はするのですが、それなりに hardware も要るし...。
というわけで、今日のところはここで時間切れです。")

(p* "でも、いろいろと可能性を感じるおもちゃですw 早くケースを買って(あ
るいは作って)、安定稼働させよう。")
