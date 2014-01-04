; @layout  post
; @title   tree-seq のメモ
; @tag clojure

;;
(section "tree-seq のメモ")

(p* "[昨日の記事](/2014-01/kilotei-from-wikipedia/)で一応やりたいことはできたのだが、もう少し Clojure っぽく見なおしてみます。
ついでといってはなんですが `clojure.core/tree-seq` についてメモっておきます。")

(subsection "tree-seq とは？")

(p* "tree-seq は、木構造を深さ優先検索でトラバースして各ノードのリストを Lazy Sequence にして返します。")

#-CLOJURE
(tree-seq branch? children root)
CLOJURE

[:ul
 [:li (p* "`branch?`：引数を一つとり子要素があるかないかを調べて返す関数を指定します。")]
 [:li (p* "`children`：自分の子要素のリストを返す関数を指定します。")]
 [:li (p* "`root`：木構造の root ノードを指定します。")]]

(subsection "使ってみる")

(p* "前回書いたコードのうち、`TagNode#getElementListByName` を Clojure で置き換えてみます。
考え方としては root からたどれるノードを `tree-seq` ですべてたどって、あとは filter で処理します。")

#-CLOJURE
(defn- branch?
  [node]
  (.hasChildren node))

(defn- children
  [node]
  (when (.hasChildren node)
    (.getChildTagList node)))

(defn get-element-list-by-name
  [element-name node]
  (->> node
       (tree-seq branch? children)
       (filter #(= element-name (.getName %)))))

;; Usage: (get-element-list-by-name "h2" rootnode)
CLOJURE

(p* "これを使って、前回の処理を少し書き換えます。")

#-CLOJURE
(defn- parse-page-and-extract-kilotei
  [page-src]
  (let [cleaner (HtmlCleaner.)]
    (->> (html->node cleaner page-src)
         (get-element-list-by-name "th")  ;; 元々は (.getElementListByName rootnode "th" true) としていた。
         (filter station?)
         (map #(-> % .getParent .getParent .getText .toString)) ;; 2つ親 (<th> -> <thead> -> <table>) の <table> タグ以下のテキストを取得
         (mapcat #(re-seq #"所属路線■*([^*キ]+)\**キロ程(\d+\.?\d*km)" %)) ;; キロ程を抽出
         (map rest))))
CLOJURE

(p* "あまり見た目は変わりませんが、ちょっと Clojure 成分が増えましたw。
ただ残念ながら少し遅くなってしまっています。Java で書かれた元の `TagNode#getElementListByName`
を見てみないとわかりませんが、tree-seq でざっくり処理を置き換えただけで特にチューニング
的なことはしていないので無駄なメモリ消費もあるのかもしれません。")

(p* "まあ、`tree-seq`をちょっと使ってみたかっただけかもしれません...")
