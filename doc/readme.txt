blancoDb Enterprise Edition (以降 blancoDb) は SQL定義書から Javaソースコードを自動生成するツールです。
SQL定義書という *.xlsファイル形式の記述内容にしたがって、データベース入出力をおこなうソースコードを自動生成することができます。
blancoDbを使えば、データベース入出力の ルーチンワーク的で しかし厄介なプログラミングを人間が担当する必要はありません。Excelなどの表計算ソフトを使って、SQL定義書 に必要項目を記入するだけでよいのです。

チュートリアルや定義書記入要領などは、下記のURLで入手することができます。
●http://hp.vector.co.jp/authors/VA027994/blanco/blancodb.html

自動生成されたソースコードは、それらが独立して動作するようになっています。ランタイムライブラリなどは必要ありません。
安全で確実で高速なデータベース入出力処理が必要な方は、ぜひ blancoDbを試してみてください。
Eclipseプラグイン形式かAntタスクで実行することが出来ます。

利用のおおまかなステップは下記のようになります。
 1.Eclipseプラグインをインストールする。
 2.blancoDbプラグインを起動する。
 3.blancoDbプラグインで SQL(*.xls)ファイルを作成する。
 4.Excelなどの表計算ソフトを使って、SQL定義書を記入する。
 5.blancoDbプラグインで ソースコードの自動生成をおこなう。
 6.自動生成されたソースコードを使って データベース入出力をするプログラムを作成する。

[ポイント]
・blancoDbのインストールに際して、他のblanco Frameworkプラグインの場合とは異なり、プラグインを解凍したうえで Eclipseプラグインとして登録してください。
  これは blancoDbがソースコードを自動生成する際に、JDBCドライバが必要になるからです。
  プラグイン内の所定のフォルダにJDBCドライバjarファイルを配置してください。

[特徴]
・ごく普通のSQL文を そのまま利用できます。
  利用しているリレーショナルデータベースのSQL文を そのまま利用できます。
・よくありがちなバグを予防するための仕組みをもっています。
   (1)一意制約違反などが特別な例外として扱われていて、処理忘れを確実に防ぐことができます。
   (2)SQLインジェクションを発生させにくい構造になっています。blancoDbの利用は SQLインジェクション対策として非常に効果的です。 
・リレーショナルデータベースのカーソルが利用できます。
・実行時に特別なクラスライブラリを必要としません。生成したソースコードだけで完結して動作します。
・ごく普通のリレーショナルデータベースの機能がそのまま利用できます。
   (1)カーソル、ロック、NULLなどを ごく普通に利用することができます。
   (2)トランザクションを適切に扱うことができます。コミットとロールバックを任意のタイミングで呼び出すことができます。
      もちろん、トランザクション分離レベルを利用することもできます。
・大量のデータを扱うことができます。
   (1)処理件数が増えてもメモリ消費量が線形には増えません。
      blancoDbが自動生成したソースコードは、数千万件のデータ処理などにも普通に対応することができます。
・ストアドプロシージャ呼び出しを利用できます。

[開発者]
 1.中西保夫 (Saisse) : 初期バージョン(blancoDb)の開発。
 2.伊賀敏樹 (Tosiki Iga / いがぴょん): blancoDb Enterprise Editionにフォークした後の開発および維持メンテ担当。
 3.山本耕司 (Y-moto) : リリース判定。
 4.久保征人          : 試験およびリリース判定。

[ライセンス]
 1.blancoDb Enterprise Edition は ライセンス として GNU Lesser General Public License を採用しています。

[依存するライブラリ]
blancoDbは下記のライブラリを利用しています。
※各オープンソース・プロダクトの提供者に感謝します。
 1.JExcelApi - Java Excel API - A Java API to read, write and modify Excel spreadsheets
     http://jexcelapi.sourceforge.net/
     http://sourceforge.net/projects/jexcelapi/
     http://www.andykhan.com/jexcelapi/ 
   概要: JavaからExcelブック形式を読み書きするためのライブラリです。
   ライセンス: GNU Lesser General Public License
 2.blancoCg
   概要: ソースコード生成ライブラリ
   ライセンス: GNU Lesser General Public License
 3.その他の blanco Framework
   概要: このプロダクトは それ自身が blanco Frameworkにより自動生成されています。
         このプロダクトは 実行時に blanco Framework各種プロダクトに依存して動作します。
   ライセンス: GNU Lesser General Public License
   