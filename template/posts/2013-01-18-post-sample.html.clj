; @layout  post
; @title   いろんな言語でのサンプル(google code prettify のテスト)
; @summary misakiで google code prettify するパターンを書いてみます。

(p (:summary (meta contents)))

[:h3 "clojure"]
#-CLJ
; CLJ
(defn fact[n]
  "factorial"
  (if (= n 0) 1
      (* n (fact (dec n)))))
CLJ
#-CLOJURE
; CLOJURE
(defn fact[n]
  "factorial"
  (if (= n 0) 1
      (* n (fact (dec n)))))
CLOJURE


[:h3 "lisp"]
#-LISP
; LISP
(defun fact (n)
  "factorial"
  (if (= n 0) 1
      (* n (fact (1- n)))))
LISP


[:h3 "sql"]
#-SQL
-- SQL
select
  user_name, count(*)
from user_tables
where user_name like 'SCOT%'
group by user_name
having count(*) > 1
order by 1
/
SQL


[:h3 "java"]
#-JAVA
// JAVA
public class Foo extends Hoge {
    // 適当な定数
    private static final int BOO = 0;
    // メインだけ書いてみる
    public static void main(String[] args) {
        System.out.println("Hello, misaki!");
    }
}
JAVA


[:h3 "html"]
#-HTML
<!-- HTML -->
<table>
  <tbody>
    <tr>
      <td>1</td>
      <td>2</td>
    </tr>
  </tbody>
</table>
<pre>
  hoge fuga
</pre>
Hello, <B><I>misaki!</I></B><br>
HTML


[:h3 "css"]
#-CSS
/* =============================================================================
   HTML5 Boilerplate CSS: h5bp.com/css
   ========================================================================== */

article, aside, details, figcaption, figure, footer, header, hgroup, nav, section { display: block; }
audio, canvas, video { display: inline-block; *display: inline; *zoom: 1; }
audio:not([controls]) { display: none; }
[hidden] { display: none; }
CSS


[:h3 "shell"]
#-SH

bash$ cat ./hello.sh
#!/bin/sh
echo "hello, misaki."
bash$ chmod +x ./hello.sh
bash$ ./hello.sh
hello, misaki.
bash$
SH


[:h3 "ruby"]
#-RUBY

module Nendo


RUBY

(p "まだまだ調整が必要ですねぇ。WebDesignって楽しいけど難しいですね。")
