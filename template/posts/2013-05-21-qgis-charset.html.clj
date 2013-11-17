; @layout  post
; @title   QGIS 1.8 の SHAPE ファイル文字化け対策について
; @tag gis

;;
;;
(section "QGIS 1.8 の SHAPE ファイルの読み込み時の文字化けについて")

(p* "国土数値情報で公開されている SHAPE ファイル等、日本語の属性情報を
もつ SHAPE ファイルを QGIS に読み込むときには、SHAPE ファイルのエンコー
ディングに合わせたエンコーディングを指定する必要があります。国土数値情
報の場合は、**Shift_JIS** を指定すればよいです(Shift_JIS、SJIS 以外
を指定すると文字化けした、はず)。")

(p* "ところが、無事に読み込みに成功しても、別の SHAPE ファイルに保存し
たりすると、保存先の SHAPE ファイルが文字化けしてしまうことがあります。
というか、「読み込みに成功してるのに何で？？？」とずっと思っていました。
")

(p* "が、同じ悩みを抱えている人が他にもおられたようで、解決策がありま
した。qgis に環境変数として `SHAPE_ENCODING=\"\"` を指定する、という
方法です。これを指定すると、ベクタレイヤーを別 SHAPEFILE に保存したり
する際に文字化けすることがなくなりました(自分は保存時には UTF-8 を指定
するようにしています)。")

#-SH

#
# QGIS 1.8 では Shapefile 読み書き時に文字コード変換が変にかかってしまうので、
# 環境変数 SHAPE_ENCODING を空白にして、QGIS システム内では文字コード変換を
# させずに、ダイアログにて明示的に指定する。
#
 
bash$ export SHAPE_ENCODING=""
bash$ open -a qgis

SH

;;
;;
(section "Python のバージョンによる問題")

(p* "ところが問題はこれだけではありませんでした。今度は QGIS 起動時に
エラーダイアログが出ます。")

#-SH
# メッセージはこんな感じ
Couldn't load plugin fTools due an error when calling its classFactory() method...
SH

(p* "SHAPE_ENCODING を指定した時に限り必ず表示されるようになってしまい
ました。")

(p* "これはどうも python のバージョンによる問題のようです。
 [ここ](http://gis.stackexchange.com/questions/48364/couldnt-load-plugin-ftools-due-an-error-when-calling-its-classfactory-method)
 に類似のエラーがあったので参考にしました。自分の環境には MacPorts の
 python と OSX(10.7) 標準の python が入っております。")

#-SH
# MacPorts python
bash$ python --version
Python 2.7.5
bash$ /usr/bin/python
Python 2.7.1
bash$
SH

(p* "正確にはインストールされているライブラリ等の差異だと思うのですが、
qgis が使用する python を /usr/bin/python になるよう、/usr/bin のパス
を前に来るように指定すると、エラーダイアログが出なくなりました。")

#-SH
# 自分の自宅環境(OSX Lion) だと上記で動かなかった。MacPorts の python (2.7.3) だとダメで
# /usr/bin/python (2.7.1) だと動く。なので、PATH 環境変数にも依存...。
# 結局、最終的には、
 
bash$ export SHAPE_ENCODING=""
bash$ PATH=/usr/bin:$PATH
bash$ open -a qgis
SH

(p* "Python 詳しくないし、原因を真面目に探して修正しだしたりしたらキリ
がないので、とりあえずはこれでしばらく運用してみます。")
