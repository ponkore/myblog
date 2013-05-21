; @layout  post
; @title   GISについてぼちぼち書いていこう。
; @tag gis

;;
;;
[:h3 "Why GIS?"]

(p "自分の日常の仕事の中で、いろんなデータを見る機会が増えてきたように思います。大量のRDB上の生データが主だったりするのですが、いわゆる * Visualization * を真面目にやらないと、いろいろと「ハマる」ことが増えてきました。今までは Excel でなんとかグラフを書いたりセルを色分けしたりで見渡せていたのですが、限界が来るのも近い、といった感じです。")

(p "最近は [d3.js](http://d3js.org/) のようにド派手なやつとか、他にもググれば JavaScript ライブラリならたくさん出てきます。ただ、今自分が直面している課題としては、地図とか地理情報に密接に関係しているものがあるので、自分の道具として GIS を使えるようにしておきたいのです。")

;;
;;
[:h3 "開発のベースとして何を使う？"]

(p "基本的に GIS のライブラリそのもの（内部実装）とかには深く立ち入りたくはありません（他にやることが山ほどあるので）。というわけで、オープンソースかつコミュニティの大きそうな（情報の多そうな）ものをベースに考えざるを得ません。今のところ [OpenLayers](http://www.openlayers.org/) をメインで考えています。ただ最近 [leaflet](http://leafletjs.com/) というやつも結構イケてそうな気もしてきたので、どっちも触りながら考えていこうと思っています。")

;;
;;
[:h3 "地図データは？"]
(p "地図を表示するには当然地図のデータが必要です。先ほどの OpenLayers、Leaflet にしても、背景の地図、それにオーバーレイするデータ、それぞれ必要になってきます。")

(p "背景の地図については、[OpenStreetMap](http://www.openstreetmap.org/) であれば、OpenLayers からでも Leaflet からでも自由につかうことができそうです( [Googleマップ](https://maps.google.co.jp/)、[Yahoo!地図](http://maps.loco.yahoo.co.jp/)、[bing の地図](http://www.bing.com/maps/?FORM=Z9LH1)、とかは、利用条件がややこしそうなのでちょっと遠慮しときます(基本、社内のオレオレツール用に使うので商用利用前提で考えます))。")

(p "あと選択肢としては、国土交通省が公開している [電子国土](http://portal.cyberjapan.jp/index.html) というのが OpenStreetMap 同様、使えそうな感じです。")

;;
[:h3 "表示するデータは？"]
(p "「何を表示したいか？」については、まあいろいろとあるわけですが、実はさしあたって必要となる情報があったりします。鉄道の路線情報です。駅の情報については、[駅データ.jp](http://www.ekidata.jp/) さんなら駅の位置情報(緯度、経度)とつながり(どの路線にどの順でつながっている) がわかります。ただ、路線の線形（どんなポリゴンで構成されているか）まではわかりません。")

(p "線形までは考えない、という手もあるのですが、実は国土交通省が公開している[国土数値情報ダウンロードサービス](http://nlftp.mlit.go.jp/ksj/) というのがあり、その中に鉄道の路線のベクタデータが公開されています。幸いなことに、アンケートにきちんと回答し地図上に「国土数値情報を使って表示している」旨を示しさえすれば、(悪いことをしない限り商用でも)利用することができます([約款](http://nlftp.mlit.go.jp/ksj/other/yakkan.html) )。")

;;
[:h3 "鉄道路線のデータについて"]
(p "先ほど国土数値情報ダウンロードサービスから入手できる、と書きましたが、入手できるのは、** JPGIS2.1(GML)準拠及びSHAPE形式 ** です。")

(p "JPGIS は日本国内における地理情報の標準規格、とでも言えばよいのでしょうか。詳細はぐぐってください。(GML) となっているのは、JPGIS における符号化形式として XML をベースとした Markup Language になります（がこちらも詳細はぐぐってくださいw)。")

(p "SHAPE 形式は、GIS でわりと標準的に用いられる、バイナリー形式のファイルフォーマットです。バイナリーなので直接いじるには面倒ですが、ちょっとしたデータベースのような感じになっており、コンパクトかつ高速にアクセスできる、というメリットがあります。")

(p "鉄道の路線情報は、JPGIS2.1(GML)とSHAPE形式が一緒になった ZIP ファイル(N05-11_GML.zip) になります。")

(p "入手方法は、[国土数値情報ダウンロードサービス](http://nlftp.mlit.go.jp/ksj/gml/gml_datalist.html) のページの * 国土骨格 * というグループにある * 鉄道時系列（線、点） * となっているところからダウンロードします。途中のアンケートとかは、ちゃんと回答しましょう。入手できるファイルは、11.61MB 程です。")

(p "入手したファイルを解凍すると、以下のファイルが含まれています。全国の鉄道路線情報になります。")

(table {:class "ink-table ink-zebra ink-bordered"}'["ファイル" "内容"]
 [["KSJ-META-N05-11.xml" "下記の N05-11.xml (国土数値情報（鉄道時系列）データ) の XML スキーマ"]
  ["N05-11.xml" "国土数値情報（鉄道時系列）データ"]
  ["N05-11_RailroadSection2.dbf, N05-11_RailroadSection2.shp, N05-11_RailroadSection2.shx" "路線の線形情報(SHAPEファイル)"]
  ["N05-11_Station2.dbf,N05-11_Station2.shp,N05-11_Station2.shx" "駅の位置情報のSHAPEファイル"]])

[:h3 "データを加工する(QGIS)"]
(p "自分としては、JR西日本のデータだけが欲しいので、それ以外のデータは取り除きたいところです。プログラムを書いて GML データから抽出する、という手段も考えられますが、ここはひとつ勉強も兼ねて [QGIS](http://www.qgis.org/) を使ってみます。")

(p "** QGIS(Quantum GIS) ** は、いわゆる「デスクトップGISツール」で、Windows/Linux/MacOSX/FreeBSD等のプラットフォームに対応しているオープンソースのツールです(ライセンスはGPL)。ありがたいことに、GUI のメニューがほとんど日本語に翻訳されているので、初心者にもとっつきやすいです。")

(p "2013/05月時点での最新は v1.8 です。")

;; (defn img@2x [uri]
;;   (let [file (find-file uri)
;;         [w h] (image-size file)
;;         w (/ w 2)   ;; int に丸める
;;         h (/ h 2)]  ;; int に丸める
;;     (img {:width w :height h} uri)))
;;
(img*2x "/img/posts/2013-05-12/2013-05-11_11.15.01.png")

(p "ここでは、QGIS を使って、鉄道時系列データをいじって、JR西日本のみのデータを作成してみます(QGISのインストールについては割愛します)。")

[:h3 "SHAPEファイルの読み込み"]

(p "SHAPE ファイルは、QGIS上では 1レイヤとして取り込まれます。ということで、[レイヤ]メニューから[ベクタレイヤの追加...]を選択します。")

(img*2x "/img/posts/2013-05-12/2013-05-11_11.22.30.png")

(p "あとはダイアログに従ってソースタイプ：ファイル、データセット：.shp ファイル、を指定します(エンコーディングは Shift_JIS としましたが、Shift_JIS 以外で OK かどうかはちょっと不明です)。まずは駅のほう(N05-11_Station2.shp)からやってみましょう。")

(img*2x "/img/posts/2013-05-12/2013-05-11_11.20.50.png")

(p "Open を実行すると、「空間参照システム選択」というダイアログが表示されます。ここではとりあえずデフォルトどおり、** WGS84 EPSG:4326 ** を選択しておきます(というかデフォルトのまま変更しない)。")

(img*2x "/img/posts/2013-05-12/2013-05-11_11.21.25.png")

(p "そのまま実行すると、* N05-11_Station2 * というレイヤが作成され、駅の位置を表している点が画面に表示されます。")

(p "同様に、路線情報(N05-11_RailroadSection2.shp)も取り込んでみます。")

(img*2x "/img/posts/2013-05-12/2013-05-11_11.22.46.png")

[:h3 "属性テーブル"]

(p "SHAPEファイルは地理情報をもつデータベースファイルのようなものです。QGIS にはこのデータベースを直接操作する機能があります。")

(p "まずはレイヤを選択して[レイヤ]メニューから[属性テーブルのオープン]を選択します。")

(img*2x "/img/posts/2013-05-12/2013-05-11_11.22.02.png")

(p "すると、以下のようなダイアログが表示されます。ベクタレイヤを構成する属性情報が一覧として表示されます。")

(img*2x "/img/posts/2013-05-12/2013-05-11_11.22.15.png")

[:h3 "属性テーブルの操作(不要な情報の削除)"]

(p "属性テーブルの右下に[アドバンストサーチ]というボタンがあります。これを押すと選択条件を指定するダイアログが表示されます。ここで、「西日本旅客鉄道」以外の行を検索し、行削除をすることで、JR西日本だけのデータが出来上がることになります。（メンドクサイので説明は省略w）")

(p "で、削除が終わった状態で画面を表示させると、以下のような感じになります。")

(img*2x "/img/posts/2013-05-12/2013-05-11_11.45.39.png")

(p "大阪近辺を表示させたのですが、味も素っ気も無いですね。")

(p "ただ、この状態だと、「すでに廃線となっている路線およびその駅」とか、「ある時期まであった路線が少し移動した」といった情報も一緒に表示されているはずです(時系列情報として持っています)。ですので、(自分ツール用としては)「今生きている路線データ」のみにして他は削除してしまいました。")

[:h3 "ラベリング"]

(p "QGIS にはラベリングという機能があります。レイヤの属性情報に応じて、地物(地図上に表示するブツ)に文字列を表示したり色分けしたり、結構こまかな表示制御を行うことができます。[レイヤ]-[ラベリング]を選択すると、レイヤラベリング指定ダイアログが表示されます。")

(img*2x "/img/posts/2013-05-12/2013-05-11_11.48.19.png")

(p "デフォルトでは、一番上の「このレイヤのラベル」のチェックボックスが off になっているのでまず on にします。次に、ラベル表示するテキストをドロップダウンリストから選択します。この SHAPE ファイルではぱっと見わかりにくいのですが、N05_011 というのが駅名を表しているのでこれを選択します。ちなみに路線のレイヤでは、路線名は N05_002 なのでそちらを選択します。今回は路線名を赤くしてみました。")

(img*2x "/img/posts/2013-05-12/2013-05-11_11.52.21.png")

(p "で、できあがったのが下図になります。")

(img*2x "/img/posts/2013-05-12/2013-05-11_11.52.31.png")

(p "もう少し引いてみると、下のような感じです。")

(img*2x "/img/posts/2013-05-12/2013-05-11_13.04.08.png")

[:h3 "いったんまとめ"]

(p "今回は、GIS、国土数値情報、QGISでのデータ加工、と駆け足で書いてしまいました(よって内容が薄いorz)。次回からは、SHAPE ファイルからデータを GeoJSON 形式に抽出して、遊んでみたいと思います。")
