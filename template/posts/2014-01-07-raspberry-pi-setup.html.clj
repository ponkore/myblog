; @layout  post
; @title   久しぶりに Raspberry PI さわってみた
; @tag hardware raspberry-pi

;;
(section "久しぶりに Raspberry PI さわってみた")

(p* "以前([過去の記事](/2013-04/raspberry-pi/))購入した **Raspberry PI** ですが、
忙しかったことと他にやりたかったこともあり、長らく放置していました。で、先日(1/5(日))日本橋に
ぶらっと寄った時に、ケースとか小型ヒートシンクとかが某店に売っていたのを見つけついつい
購入してしまい、久しぶりに自宅サーバに仕込んでやろう、と思い立った次第です。")

(subsection "OS のインストール等")

(p* "Raspbian を選択しました。Debian 好きだし。FreeBSD はまだ安定していないとか
ちらほら聞いたことがあるので今回はパス(安定したら FreeBSD に置き換えるかもしれません)。")

(p* "ダウンロードページ([ここ](http://www.raspberrypi.org/downloads))から Raspbian を
ダウンロード、と思ったら思いの外回線速度が遅く、torrent 経由で get。Mac に SD カードを
つないで、")

#-SH
bash$ sudo diskutil umount /dev/disk1s1
SH

(p* "などしてから")

#-SH
bash$ sudo time dd bs=1m if=2013-12-20-wheezy-raspbian.img of=/dev/rdisk1
SH

(p* "で書き込みました。作業した日が 2014/01/05(日) だったので 2013-12-20 はかなり
新しくていい感じです。Debian なのでインストールされているパッケージ等はそれなりの古さ
だと思っていましたが、")

#-SH
bash$ uname -a
Linux fubuki 3.10.24+ #614 PREEMPT Thu Dec 19 20:38:42 GMT 2013 armv6l GNU/Linux
bash$ java -version
java version "1.7.0_40"
Java(TM) SE Runtime Environment (build 1.7.0_40-b43)
Java HotSpot(TM) Client VM (build 24.0-b56, mixed mode)
bash$
SH

(p* "といった具合です。hostname は **fubuki** にしました([暁の水平線に勝利をきざむあのゲーム](http://www.dmm.com/netgame/feature/kancolle.html) を参考にしました。すのでな。)。")

(subsection "起動とか")

(p* "初回起動時は、`raspi-config` が勝手に立ち上がってるのね...。リモートで ssh
経由でログインして ps して初めて気がつきました。一旦シャットダウンして、リビングの TV に
HDMI 経由でつないでコンソールを見てみます。あまり込み入った設定はせずに(なにか設定した
とは思うのだけどメモってないので忘れました)、リブート等何度かしてみて安定していることを
念のため確認しました。その後、crontab で、heroku に仕込んである自分の Web アプリを
定期的に叩く仕掛けを仕込んで1/5(日)の作業は終了。")

(subsection "timezone 設定")

(p* "すっかり忘れていました、timezone 設定。`date` コマンド叩いたら時刻ずれてました...。")

#-SH
bash$ sudo dpkg-reconfigure tzdata
# ... (ごにょごにょ)

Current default time zone: 'Asia/Tokyo'
Local time is now:      Mon Jan  6 21:51:04 JST 2014.
Universal Time is now:  Mon Jan  6 12:51:04 UTC 2014.

bash$ date
2014年  1月  6日 月曜日 21:51:10 JST
bash$
SH

(p* "このコマンド、毎回忘れて毎回ググる...。良い時代になったものです。")

(subsection "いろいろ入れてみる(PostGIS 編)")

(p* "マシンスペック的にまともに動くとは思ってませんが、ちょっと興味があったのでトライしてみました。
といっても、この日(1/6)は入れてサービスが起動しただけで、データベースには何も仕込んでません。")

#-SH
bash$ sudo apt-get install postgresql-9.1-postgis
パッケージリストを読み込んでいます... 完了
依存関係ツリーを作成しています
状態情報を読み取っています... 完了
以下の特別パッケージがインストールされます:
  libgeos-3.3.3 libgeos-c1 libpq5 libproj0 lsb-release postgis postgresql-9.1 postgresql-client-9.1 postgresql-client-common postgresql-common proj-data
  ssl-cert
提案パッケージ:
  proj-bin lsb oidentd ident-server locales-all postgresql-doc-9.1 openssl-blacklist
以下のパッケージが新たにインストールされます:
  libgeos-3.3.3 libgeos-c1 libpq5 libproj0 lsb-release postgis postgresql-9.1 postgresql-9.1-postgis postgresql-client-9.1 postgresql-client-common
  postgresql-common proj-data ssl-cert
アップグレード: 0 個、新規インストール: 13 個、削除: 0 個、保留: 0 個。
10.5 MB のアーカイブを取得する必要があります。
この操作後に追加で 36.4 MB のディスク容量が消費されます。
続行しますか [Y/n]? y
取得:1 http://mirrordirector.raspbian.org/raspbian/ wheezy/main libgeos-3.3.3 armhf 3.3.3-1.1 [576 kB]
取得:2 http://mirrordirector.raspbian.org/raspbian/ wheezy/main libgeos-c1 armhf 3.3.3-1.1 [165 kB]
取得:3 http://mirrordirector.raspbian.org/raspbian/ wheezy/main libpq5 armhf 9.1.11-0wheezy1 [520 kB]
取得:4 http://mirrordirector.raspbian.org/raspbian/ wheezy/main lsb-release all 4.1+Debian8+rpi1 [27.1 kB]
取得:5 http://mirrordirector.raspbian.org/raspbian/ wheezy/main postgis armhf 1.5.3-2+b1 [573 kB]
取得:6 http://mirrordirector.raspbian.org/raspbian/ wheezy/main postgresql-client-common all 134wheezy4 [63.3 kB]
取得:7 http://mirrordirector.raspbian.org/raspbian/ wheezy/main postgresql-client-9.1 armhf 9.1.11-0wheezy1 [1,337 kB]
取得:8 http://mirrordirector.raspbian.org/raspbian/ wheezy/main ssl-cert all 1.0.32 [19.5 kB]
取得:9 http://mirrordirector.raspbian.org/raspbian/ wheezy/main postgresql-common all 134wheezy4 [138 kB]
取得:10 http://mirrordirector.raspbian.org/raspbian/ wheezy/main postgresql-9.1 armhf 9.1.11-0wheezy1 [3,339 kB]
取得:11 http://mirrordirector.raspbian.org/raspbian/ wheezy/main proj-data armhf 4.7.0-2 [2,940 kB]
取得:12 http://mirrordirector.raspbian.org/raspbian/ wheezy/main libproj0 armhf 4.7.0-2 [114 kB]
取得:13 http://mirrordirector.raspbian.org/raspbian/ wheezy/main postgresql-9.1-postgis armhf 1.5.3-2+b1 [665 kB]
10.5 MB を 12秒 で取得しました (842 kB/s)
パッケージを事前設定しています ...
以前に未選択のパッケージ libgeos-3.3.3 を選択しています。
(データベースを読み込んでいます ... 現在 71054 個のファイルとディレクトリがインストールされています。)
(.../libgeos-3.3.3_3.3.3-1.1_armhf.deb から) libgeos-3.3.3 を展開しています...
以前に未選択のパッケージ libgeos-c1 を選択しています。
(.../libgeos-c1_3.3.3-1.1_armhf.deb から) libgeos-c1 を展開しています...
以前に未選択のパッケージ libpq5 を選択しています。
(.../libpq5_9.1.11-0wheezy1_armhf.deb から) libpq5 を展開しています...
以前に未選択のパッケージ lsb-release を選択しています。
(.../lsb-release_4.1+Debian8+rpi1_all.deb から) lsb-release を展開しています...
以前に未選択のパッケージ postgis を選択しています。
(.../postgis_1.5.3-2+b1_armhf.deb から) postgis を展開しています...
以前に未選択のパッケージ postgresql-client-common を選択しています。
(.../postgresql-client-common_134wheezy4_all.deb から) postgresql-client-common を展開しています...
以前に未選択のパッケージ postgresql-client-9.1 を選択しています。
(.../postgresql-client-9.1_9.1.11-0wheezy1_armhf.deb から) postgresql-client-9.1 を展開しています...
以前に未選択のパッケージ ssl-cert を選択しています。
(.../ssl-cert_1.0.32_all.deb から) ssl-cert を展開しています...
以前に未選択のパッケージ postgresql-common を選択しています。
(.../postgresql-common_134wheezy4_all.deb から) postgresql-common を展開しています...
'postgresql-common による /usr/bin/pg_config から /usr/bin/pg_config.libpq-dev への退避 (divert)' を追加しています
以前に未選択のパッケージ postgresql-9.1 を選択しています。
(.../postgresql-9.1_9.1.11-0wheezy1_armhf.deb から) postgresql-9.1 を展開しています...
以前に未選択のパッケージ proj-data を選択しています。
(.../proj-data_4.7.0-2_armhf.deb から) proj-data を展開しています...
以前に未選択のパッケージ libproj0 を選択しています。
(.../libproj0_4.7.0-2_armhf.deb から) libproj0 を展開しています...
以前に未選択のパッケージ postgresql-9.1-postgis を選択しています。
(.../postgresql-9.1-postgis_1.5.3-2+b1_armhf.deb から) postgresql-9.1-postgis を展開しています...
man-db のトリガを処理しています ...
libgeos-3.3.3 (3.3.3-1.1) を設定しています ...
libgeos-c1 (3.3.3-1.1) を設定しています ...
libpq5 (9.1.11-0wheezy1) を設定しています ...
lsb-release (4.1+Debian8+rpi1) を設定しています ...
postgis (1.5.3-2+b1) を設定しています ...
postgresql-client-common (134wheezy4) を設定しています ...
postgresql-client-9.1 (9.1.11-0wheezy1) を設定しています ...
update-alternatives: /usr/share/man/man1/psql.1.gz (psql.1.gz) を提供するために 自動モード で /usr/share/postgresql/9.1/man/man1/psql.1.gz を使います
ssl-cert (1.0.32) を設定しています ...
postgresql-common (134wheezy4) を設定しています ...
ユーザ postgres をグループ ssl-cert に追加
Building PostgreSQL dictionaries from installed myspell/hunspell packages...
insserv: warning: script 'mathkernel' missing LSB tags and overrides
postgresql-9.1 (9.1.11-0wheezy1) を設定しています ...
Creating new cluster (configuration: /etc/postgresql/9.1/main, data: /var/lib/postgresql/9.1/main)...
Moving configuration file /var/lib/postgresql/9.1/main/postgresql.conf to /etc/postgresql/9.1/main...
Moving configuration file /var/lib/postgresql/9.1/main/pg_hba.conf to /etc/postgresql/9.1/main...
Moving configuration file /var/lib/postgresql/9.1/main/pg_ident.conf to /etc/postgresql/9.1/main...
Configuring postgresql.conf to use port 5432...
update-alternatives: /usr/share/man/man1/postmaster.1.gz (postmaster.1.gz) を提供するために 自動モード で /usr/share/postgresql/9.1/man/man1/postmaster.1.gz を使います
[ ok ] Starting PostgreSQL 9.1 database server: main.
proj-data (4.7.0-2) を設定しています ...
libproj0 (4.7.0-2) を設定しています ...
postgresql-9.1-postgis (1.5.3-2+b1) を設定しています ...
bash$
SH

(p* "Debian 使うと自分がどんどんアホになりますね。時間はそれなりにかかりましたが、仕込むだけなら楽すぎです。")

(subsection "いろいろ入れてみる(Clojure 編)")

(p* "Clojure については、[Leiningen](http://leiningen.org/) が無いと話にならないので、
apt-get ではなく `lein` コマンドからインストール(というか勝手にインストールされます)。
これが簡単なようで、時間がかかってイライラしてしまった...。どうせ時間がかかるだろうなぁ、
と思っていたので、time コマンドで時間計測。")

#-SH
bash$ time ./lein help
Downloading Leiningen to /home/pi/.lein/self-installs/leiningen-2.3.4-standalone.jar now...
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
100 13.0M  100 13.0M    0     0   399k      0  0:00:33  0:00:33 --:--:--  422k
Retrieving org/clojure/tools.nrepl/0.2.3/tools.nrepl-0.2.3.pom from central
Retrieving org/clojure/pom.contrib/0.1.2/pom.contrib-0.1.2.pom from central
Retrieving org/sonatype/oss/oss-parent/7/oss-parent-7.pom from central
Retrieving clojure-complete/clojure-complete/0.2.3/clojure-complete-0.2.3.pom from clojars
Retrieving org/clojure/tools.nrepl/0.2.3/tools.nrepl-0.2.3.jar from central
Retrieving clojure-complete/clojure-complete/0.2.3/clojure-complete-0.2.3.jar from clojars
Leiningen is a tool for working with Clojure projects.

Several tasks are available:
check               Check syntax and warn on reflection.
classpath           Write the classpath of the current project to output-file.
clean               Remove all files from paths in project's clean-targets.
compile             Compile Clojure source into .class files.
deploy              Deploy jar and pom to remote repository.
deps                Show details about dependencies.
do                  Higher-order task to perform other tasks in succession.
help                Display a list of tasks or help for a given task or subtask.
install             Install current project to the local repository.
jar                 Package up all the project's files into a jar file.
javac               Compile Java source files.
new                 Generate scaffolding for a new project based on a template.
plugin              DEPRECATED. Please use the :user profile instead.
pom                 Write a pom.xml file to disk for Maven interoperability.
repl                Start a repl session either with the current project or standalone.
retest              Run only the test namespaces which failed last time around.
run                 Run the project's -main function.
search              Search remote maven repositories for matching jars.
show-profiles       List all available profiles or display one if given an argument.
test                Run the project's tests.
trampoline          Run a task without nesting the project's JVM inside Leiningen's.
uberjar             Package up the project files and all dependencies into a jar file.
update-in           Perform arbitrary transformations on your project map.
upgrade             Upgrade Leiningen to specified version or latest stable.
version             Print version for Leiningen and the current JVM.
with-profile        Apply the given task with the profile(s) specified.

Run `lein help $TASK` for details.

Global Options:
  -o             Run a task offline.
  -U             Run a task after forcing update of snapshots.
  -h, --help     Print this help.
  -v, --version  Print Leiningen's version.

See also: readme, faq, tutorial, news, sample, profiles, deploying, gpg, mixed-source, templates, and copying.

real    3m11.842s
user    2m21.790s
sys     0m4.190s
bash$
SH

(p* "ダウンロードして help 見るだけ、で３分。ちなみに、おそらくメモリのキャッシュも
効いているだろうと思われる２回目の `./lein help` は、")

#-SH
bash$ time ./lein help
Leiningen is a tool for working with Clojure projects.

-- snip --

real    2m26.445s
user    2m20.650s
sys     0m2.180s
bash$
SH

(p* "2分半...まあ良いでしょう。本格的に開発に使うわけじゃなし。ちなみに、`lein help`は遅いのですが、
`lein new &lt;project&gt;` はまだましでした。")

#-SH
bash$ time ~/bin/lein new hello
Generating a project called hello based on the 'default' template.
To see other templates (app, lein plugin, etc), try `lein help new`.

real    0m37.365s
user    0m36.000s
sys     0m1.100s
bash$
SH

(subsection "いろいろ入れてみる(Emacs 編)")

(p* "さて、ここからがめんどくさかったです(時間的に)。apt-get で入る奴は Emacs-23 っぽかったの
ですが、常用している Emacs のバージョンが 24.3 で、Emacs-23 では動かない elisp もちらほら
あるようだったので、思い切ってソースからビルドしてインストールすることにしました。")

(p* "手順はほぼ[ここ(Compile Emacs 24.2 on Raspberry Pi (Rasbian) )](https://coderwall.com/p/uztyfw)にある通りでした(感謝!!)。
バージョンこそ違いますが、インストール手順としては完璧でした。ざっくりまとめると、")

#-SH
bash$ sudo apt-get install texinfo libncurses5-dev
bash$ mkdir src
bash$ cd ./src
bash$ wget http://ftp.gnu.org/pub/gnu/emacs/emacs-24.3.tar.xz
bash$ tar -xJvf emacs-24.3.tar.xz
bash$ cd ./emacs-24.3
bash$ time ./configure --prefix=/opt/emacs --without-x
#  -- snip --
real    7m49.623s
user    2m42.200s
sys     0m32.040s
bash$ time make
#  -- snip --
real    51m49.290s
user    41m39.060s
sys     1m22.130s
bash$ time sudo make install
real    9m25.840s
user    3m44.570s
sys     1m3.480s
bash$
SH

(p* "こんな感じでした。すでに日付が変わってしまっていた(つまり深夜)だったので、その間
テレビを見ながら待ちぼうけ。ここ数年 Emacs のビルドなんて大した手間じゃない、という
認識だったのですが、完全に間違ってました。単にハード(PC)が速くなっただけ、ということです。
**Emacsは巨大** でした。")

(p* "この後、自分の elisp なり package なりを入れていきます。自分が使う
elisp の package のダウンロード、インストールは自動化しているのですが、この処理が
これまた非常に遅い。さすがに待ちきれず、そのまま放置して寝ました（朝にはちゃんと終わってました）。")

(subsection "いったんまとめ")

(p* "そもそも環境全体が SD カード上に載っているので(激しい書き込みに弱い)、Emacs を
コンパイルしてインストールするとか、PostGIS 入れてしまうとか、いろいろ間違ってます。
ですが、ちょっとずつ自分の環境になっていくというのは嬉しいものですし、やってみて初めて
気がつくようなこともあるかと。")

(p* "ちなみに、自宅のルータを dyndns.org に登録して、外部から SSH でログイン
できるようにしました(ポートはもちろん変えてますが)。電車の中で iPhone からログイン
してみたりして遊んでます。")
