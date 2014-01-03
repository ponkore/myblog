; @layout  post
; @title   random-docs (2014年最初の記事)
; @tag clojure

;;
;;
(section "random-docs (2014年の最初の記事)")

(p* "あけましておめでとうございます。")

(p* "昨年末は F# の勉強ばかりしていて、そろそろ Clojure 熱も上がってきたので
なんとなく思いつきで書いてみました。小ネタです。")

(p* "clojure.core の doc string をたまには思い出そうと。本当は twitter
の bot にでもすれば一番いいのですが、doc string が140文字を楽勝で超えてしまう
関数もいっぱいあるので、「時折思い出させる」仕掛けをどうするかは、別途考え中です。")

#-CLOJURE
(defn get-doc-string
  [ns sym]
  (let [wrt (java.io.StringWriter.)]
    (binding [*out* wrt]
      (->> (ns-resolve ns sym)
           (meta)
           (#'clojure.repl/print-doc)))
    (.toString wrt)))

(def syms (-> 'clojure.core ns-map keys sort))

(def clojure-core-docs (->> syms (map #(get-doc-string 'clojure.core %))))

(defn random-docs
  []
  (println (rand-nth clojure-core-docs)))
CLOJURE

(p* "`clojure.doc/doc` 相当のことをやろうとして、`clojure.repl/doc` を見てみたら、
private な関数 `clojure.repl/print-doc` を呼んでいたので、`#'` をつけています。")

(p* "Usage:")

#-SH
user> (random-docs)
-------------------------
clojure.core/not-every?
([pred coll])
  Returns false if (pred x) is logical true for every x in
  coll, else true.

nil
user> (random-docs)
-------------------------
clojure.core/rational?
([n])
  Returns true if n is a rational number

nil
user>
SH

(p* "新年一発目は小ネタということで。今年もよろしくお願いします。")
