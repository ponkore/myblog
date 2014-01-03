; @layout  post
; @title   Wikipedia の駅のページからキロ程情報を抽出する
; @tag clojure

;;
;;
(section "Wikipedia の駅のページからキロ程情報を抽出する")

(p* "この記事は、[lisp アドベントカレンダー2013 12/14 の記事](http://qiita.com/ponkore/items/5309023186353de49172)
の続きです。ほったらかしにしておくのももったいないので、ぼちぼち続けていこうと思っています。
まずはその続きの ** 第一弾 ** ということで。")

(subsection "やりたいこと")

(p* "最終的にやりたいことは、前述のアドベントカレンダーの記事に書きましたが、駅のキロ程情報を Wikipedia から
拾ってこよう、ということです。前述の記事を書いた時には、** 「lisp アドベントカレンダーらしく」 ** というのを
意識したかったので、HTML の木構造を S式でちゃっちゃとつくって、[clojure.data.zip](http://clojure.github.io/data.zip/)
あたりを使ってノードをトラバースして、という方向で考えていました(が時間切れに...)。
ですが、もう今となってはそういう縛りは一旦とっぱらって、情報抽出に専念してみます。")

(p* "今回は、[HTML Cleaner](http://htmlcleaner.sourceforge.net/) のもつ機能をそのまま使う方針で考えます。
`HTML Cleaner` のオブジェクトは、HTML に沿った形の木構造をしているので、「キロ程」というキーワードのあるノード
 (`HTML Cleaner` の`TagNode`) を上位にさかのぼって探す、といった操作が簡単に行えます。具体的には、`HTML Cleaner`
で取得した `TagNode` の以下のメソッド等を使ってみました。")

[:ul
 [:li (p* "`TagNode#getElementByName`：自分のノードの子の要素を名前指定で探す。今回、「キロ程」という
キーワードは `&lt;th&gt;` に記述されていたので、`'th'`に記述されている「キロ程」という文字列、を探すことで良さそうです。 ")]
 [:li (p* "`TagNode#getParent`: 自分のノードの親要素のノードを返します。") ]]

(subsection "書いたコード")

(p* "書いたコードを晒しておきます。")

#-CLOJURE
(ns html-parser.core
  (:import [org.htmlcleaner HtmlCleaner]))

(defn- html->node
  [cleaner html-src]
  (doto (.getProperties cleaner)
    (.setOmitComments true)        ;; HTML のコメントは無視する
    (.setPruneTags "script,style") ;; <script>, <style> タグは無視する
    (.setOmitXmlDeclaration true))
  (.clean cleaner html-src)) ;; cleaner.clean(string) でパース

(defn- station?
  [node]
  (->> node .getText .toString (re-find #".+駅\**$"))) ;; 駅によっては XX駅* のような表記あり

(defn- parse-page-and-extract-kilotei
  [page-src]
  (let [cleaner (HtmlCleaner.)
        rootnode (html->node cleaner page-src)]
    (->> (.getElementListByName rootnode "th" true) ;; <th> を探す
         (filter station?) ;; <th>の内容が XX 駅 のものに限定
         (map #(-> % .getParent .getParent .getText .toString)) ;; 2つ親 (<th> -> <thead> -> <table>) の <table> タグ以下のテキストを取得
         (mapcat #(re-seq #"所属路線■*([^*キ]+)\**キロ程(\d+\.?\d*km)" %)) ;; 路線名、キロ程を抽出
         (map rest)
         distinct)))

(defn get-kilotei-from-wikipedia
  [url]
  (->> (slurp url) ;; TODO: cache
       parse-page-and-extract-kilotei))

;; (get-kilotei-from-wikipedia "http://ja.wikipedia.org/wiki/新大阪駅")
;; => (("東海道新幹線" "552.6km") ("山陽新幹線" "0.0km") ("東海道本線（JR京都線）" "552.6km") ("東海道本線貨物支線\n（梅田貨物線）" "3.8km") ("御堂筋線" "2.9km"))

CLOJURE

(p* "正規表現あたり限りなくあやしいですが、そのうちまじめに見直そうと思います。
トライアルとして書く分には、まあ良しとします。")

(subsection "今後")

(p* "今回は単純に、目当ての情報をピンポイントで抽出する、というだけのことをしていますが、
そのうちに、")

[:ul
 [:li (p* "一旦取得した Wikipedia のページはキャッシュする")]
 [:li (p* "指定した駅から順次次の駅のリンクも合わせて取得する")]
 [:li (p* "路線の関連付けまで合わせてデータベース化する")]
 [:li (p* "(clojure からは離れるが) 国土数値情報の路線の線形と重ねあわせてみる")]]

(p* "といったことをやってみようと思います。")
