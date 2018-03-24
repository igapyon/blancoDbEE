/*
 * このソースコードは blanco Frameworkにより自動生成されました。
 */
package blanco.db.task;

import java.io.IOException;

import blanco.db.task.valueobject.BlancoDbProcessInput;

/**
 * バッチ処理クラス [BlancoDbBatchProcess]。
 *
 * <P>バッチ処理の呼び出し例。</P>
 * <code>
 * java -classpath (クラスパス) blanco.db.task.BlancoDbBatchProcess -help
 * </code>
 */
public class BlancoDbBatchProcess {
    /**
     * 正常終了。
     */
    public static final int END_SUCCESS = 0;

    /**
     * 入力異常終了。内部的にjava.lang.IllegalArgumentExceptionが発生した場合。
     */
    public static final int END_ILLEGAL_ARGUMENT_EXCEPTION = 7;

    /**
     * 入出力例外終了。内部的にjava.io.IOExceptionが発生した場合。
     */
    public static final int END_IO_EXCEPTION = 8;

    /**
     * 異常終了。バッチの処理開始に失敗した場合、および内部的にjava.lang.Errorまたはjava.lang.RuntimeExceptionが発生した場合。
     */
    public static final int END_ERROR = 9;

    /**
     * コマンドラインから実行された際のエントリポイントです。
     *
     * @param args コンソールから引き継がれた引数。
     */
    public static final void main(final String[] args) {
        final BlancoDbBatchProcess batchProcess = new BlancoDbBatchProcess();

        // バッチ処理の引数。
        final BlancoDbProcessInput input = new BlancoDbProcessInput();

        boolean isNeedUsage = false;
        boolean isFieldJdbcdriverProcessed = false;
        boolean isFieldJdbcurlProcessed = false;
        boolean isFieldJdbcuserProcessed = false;
        boolean isFieldJdbcpasswordProcessed = false;
        boolean isFieldMetadirProcessed = false;
        boolean isFieldBasepackageProcessed = false;

        // コマンドライン引数の解析をおこないます。
        for (int index = 0; index < args.length; index++) {
            String arg = args[index];
            if (arg.startsWith("-verbose=")) {
                input.setVerbose(Boolean.valueOf(arg.substring(9)).booleanValue());
            } else if (arg.startsWith("-jdbcdriver=")) {
                input.setJdbcdriver(arg.substring(12));
                isFieldJdbcdriverProcessed = true;
            } else if (arg.startsWith("-jdbcurl=")) {
                input.setJdbcurl(arg.substring(9));
                isFieldJdbcurlProcessed = true;
            } else if (arg.startsWith("-jdbcuser=")) {
                input.setJdbcuser(arg.substring(10));
                isFieldJdbcuserProcessed = true;
            } else if (arg.startsWith("-jdbcpassword=")) {
                input.setJdbcpassword(arg.substring(14));
                isFieldJdbcpasswordProcessed = true;
            } else if (arg.startsWith("-jdbcdriverfile=")) {
                input.setJdbcdriverfile(arg.substring(16));
            } else if (arg.startsWith("-metadir=")) {
                input.setMetadir(arg.substring(9));
                isFieldMetadirProcessed = true;
            } else if (arg.startsWith("-tmpdir=")) {
                input.setTmpdir(arg.substring(8));
            } else if (arg.startsWith("-targetdir=")) {
                input.setTargetdir(arg.substring(11));
            } else if (arg.startsWith("-basepackage=")) {
                input.setBasepackage(arg.substring(13));
                isFieldBasepackageProcessed = true;
            } else if (arg.startsWith("-runtimepackage=")) {
                input.setRuntimepackage(arg.substring(16));
            } else if (arg.startsWith("-schema=")) {
                input.setSchema(arg.substring(8));
            } else if (arg.startsWith("-table=")) {
                input.setTable(arg.substring(7));
            } else if (arg.startsWith("-sql=")) {
                input.setSql(arg.substring(5));
            } else if (arg.startsWith("-failonerror=")) {
                input.setFailonerror(arg.substring(13));
            } else if (arg.startsWith("-log=")) {
                input.setLog(arg.substring(5));
            } else if (arg.startsWith("-logmode=")) {
                input.setLogmode(arg.substring(9));
            } else if (arg.startsWith("-logsql=")) {
                input.setLogsql(arg.substring(8));
            } else if (arg.startsWith("-statementtimeout=")) {
                input.setStatementtimeout(arg.substring(18));
            } else if (arg.startsWith("-executesql=")) {
                input.setExecutesql(arg.substring(12));
            } else if (arg.startsWith("-encoding=")) {
                input.setEncoding(arg.substring(10));
            } else if (arg.startsWith("-convertStringToMsWindows31jUnicode=")) {
                input.setConvertStringToMsWindows31jUnicode(arg.substring(36));
            } else if (arg.startsWith("-cache=")) {
                input.setCache(arg.substring(7));
            } else if (arg.equals("-?") || arg.equals("-help")) {
                usage();
                System.exit(END_SUCCESS);
            } else {
                System.out.println("BlancoDbBatchProcess: 入力パラメータ[" + arg + "]は無視されました。");
                isNeedUsage = true;
            }
        }

        if (isNeedUsage) {
            usage();
        }

        if( isFieldJdbcdriverProcessed == false) {
            System.out.println("BlancoDbBatchProcess: 処理開始失敗。入力パラメータ[input]の必須フィールド値[jdbcdriver]に値が設定されていません。");
            System.exit(END_ILLEGAL_ARGUMENT_EXCEPTION);
        }
        if( isFieldJdbcurlProcessed == false) {
            System.out.println("BlancoDbBatchProcess: 処理開始失敗。入力パラメータ[input]の必須フィールド値[jdbcurl]に値が設定されていません。");
            System.exit(END_ILLEGAL_ARGUMENT_EXCEPTION);
        }
        if( isFieldJdbcuserProcessed == false) {
            System.out.println("BlancoDbBatchProcess: 処理開始失敗。入力パラメータ[input]の必須フィールド値[jdbcuser]に値が設定されていません。");
            System.exit(END_ILLEGAL_ARGUMENT_EXCEPTION);
        }
        if( isFieldJdbcpasswordProcessed == false) {
            System.out.println("BlancoDbBatchProcess: 処理開始失敗。入力パラメータ[input]の必須フィールド値[jdbcpassword]に値が設定されていません。");
            System.exit(END_ILLEGAL_ARGUMENT_EXCEPTION);
        }
        if( isFieldMetadirProcessed == false) {
            System.out.println("BlancoDbBatchProcess: 処理開始失敗。入力パラメータ[input]の必須フィールド値[metadir]に値が設定されていません。");
            System.exit(END_ILLEGAL_ARGUMENT_EXCEPTION);
        }
        if( isFieldBasepackageProcessed == false) {
            System.out.println("BlancoDbBatchProcess: 処理開始失敗。入力パラメータ[input]の必須フィールド値[basepackage]に値が設定されていません。");
            System.exit(END_ILLEGAL_ARGUMENT_EXCEPTION);
        }

        int retCode = batchProcess.execute(input);

        // 終了コードを戻します。
        // ※注意：System.exit()を呼び出している点に注意してください。
        System.exit(retCode);
    }

    /**
     * 具体的なバッチ処理内容を記述するためのメソッドです。
     *
     * このメソッドに実際の処理内容を記述します。
     *
     * @param input バッチ処理の入力パラメータ。
     * @return バッチ処理の終了コード。END_SUCCESS, END_ILLEGAL_ARGUMENT_EXCEPTION, END_IO_EXCEPTION, END_ERROR のいずれかの値を戻します。
     * @throws IOException 入出力例外が発生した場合。
     * @throws IllegalArgumentException 入力値に不正が見つかった場合。
     */
    public int process(final BlancoDbProcessInput input) throws IOException, IllegalArgumentException {
        // 入力パラメータをチェックします。
        validateInput(input);

        // この箇所でコンパイルエラーが発生する場合、BlancoDbProcessインタフェースを実装して blanco.db.taskパッケージに BlancoDbProcessImplクラスを作成することにより解決できる場合があります。
        final BlancoDbProcess process = new BlancoDbProcessImpl();

        // 処理の本体を実行します。
        final int retCode = process.execute(input);

        return retCode;
    }

    /**
     * クラスをインスタンス化してバッチを実行する際のエントリポイントです。
     *
     * このメソッドは下記の仕様を提供します。
     * <ul>
     * <li>メソッドの入力パラメータの内容チェック。
     * <li>IllegalArgumentException, RuntimeException, Errorなどの例外をcatchして戻り値へと変換。
     * </ul>
     *
     * @param input バッチ処理の入力パラメータ。
     * @return バッチ処理の終了コード。END_SUCCESS, END_ILLEGAL_ARGUMENT_EXCEPTION, END_IO_EXCEPTION, END_ERROR のいずれかの値を戻します。
     * @throws IllegalArgumentException 入力値に不正が見つかった場合。
     */
    public final int execute(final BlancoDbProcessInput input) throws IllegalArgumentException {
        try {
            // バッチ処理の本体を実行します。
            int retCode = process(input);

            return retCode;
        } catch (IllegalArgumentException ex) {
            System.out.println("BlancoDbBatchProcess: 入力例外が発生しました。バッチ処理を中断します。:" + ex.toString());
            // 入力異常終了。
            return END_ILLEGAL_ARGUMENT_EXCEPTION;
        } catch (IOException ex) {
            System.out.println("BlancoDbBatchProcess: 入出力例外が発生しました。バッチ処理を中断します。:" + ex.toString());
            // 入力異常終了。
            return END_IO_EXCEPTION;
        } catch (RuntimeException ex) {
            System.out.println("BlancoDbBatchProcess: ランタイム例外が発生しました。バッチ処理を中断します。:" + ex.toString());
            ex.printStackTrace();
            // 異常終了。
            return END_ERROR;
        } catch (Error er) {
            System.out.println("BlancoDbBatchProcess: ランタイムエラーが発生しました。バッチ処理を中断します。:" + er.toString());
            er.printStackTrace();
            // 異常終了。
            return END_ERROR;
        }
    }

    /**
     * このバッチ処理クラスの使い方の説明を標準出力に示すためのメソッドです。
     */
    public static final void usage() {
        System.out.println("BlancoDbBatchProcess: Usage:");
        System.out.println("  java blanco.db.task.BlancoDbBatchProcess -verbose=値1 -jdbcdriver=値2 -jdbcurl=値3 -jdbcuser=値4 -jdbcpassword=値5 -jdbcdriverfile=値6 -metadir=値7 -tmpdir=値8 -targetdir=値9 -basepackage=値10 -runtimepackage=値11 -schema=値12 -table=値13 -sql=値14 -failonerror=値15 -log=値16 -logmode=値17 -logsql=値18 -statementtimeout=値19 -executesql=値20 -encoding=値21 -convertStringToMsWindows31jUnicode=値22 -cache=値23");
        System.out.println("    -verbose");
        System.out.println("      説明[verboseモードで動作させるかどうか。]");
        System.out.println("      型[真偽]");
        System.out.println("      デフォルト値[false]");
        System.out.println("    -jdbcdriver");
        System.out.println("      説明[JDBCドライバのクラス名を指定します。]");
        System.out.println("      型[文字列]");
        System.out.println("      必須パラメータ");
        System.out.println("    -jdbcurl");
        System.out.println("      説明[JDBC接続先URLを指定します。]");
        System.out.println("      型[文字列]");
        System.out.println("      必須パラメータ");
        System.out.println("    -jdbcuser");
        System.out.println("      説明[JDBCデータベース接続を行う際のユーザ名を指定します。]");
        System.out.println("      型[文字列]");
        System.out.println("      必須パラメータ");
        System.out.println("    -jdbcpassword");
        System.out.println("      説明[JDBCデータベース接続を行う際のパスワードを指定します。]");
        System.out.println("      型[文字列]");
        System.out.println("      必須パラメータ");
        System.out.println("    -jdbcdriverfile");
        System.out.println("      説明[JDBCドライバの jar ファイル名を指定します。通常は利用しません。]");
        System.out.println("      型[文字列]");
        System.out.println("    -metadir");
        System.out.println("      説明[SQL定義メタファイルが格納されているディレクトリを指定します。]");
        System.out.println("      型[文字列]");
        System.out.println("      必須パラメータ");
        System.out.println("    -tmpdir");
        System.out.println("      説明[テンポラリフォルダを指定します。無指定の場合にはカレント直下のtmpフォルダを利用します。]");
        System.out.println("      型[文字列]");
        System.out.println("      デフォルト値[tmp]");
        System.out.println("    -targetdir");
        System.out.println("      説明[blancoDbがJavaソースコードを出力するディレクトリを指定します。]");
        System.out.println("      型[文字列]");
        System.out.println("      デフォルト値[blanco]");
        System.out.println("    -basepackage");
        System.out.println("      説明[blancoDbがJavaソースコードを生成する際の基準となるパッケージ名を指定します。]");
        System.out.println("      型[文字列]");
        System.out.println("      必須パラメータ");
        System.out.println("    -runtimepackage");
        System.out.println("      説明[ランタイムクラスを生成する生成先を指定します。無指定の場合には basepackageを基準に生成されます。]");
        System.out.println("      型[文字列]");
        System.out.println("    -schema");
        System.out.println("      説明[単一表情報を取得する際のスキーマ名。基本的に無指定です。ただしOracleの場合にのみ、ユーザ名を大文字化したものを指定します。Oracleの場合に これを指定しないと、システム表まで検索してしまい不具合が発生するためです。]");
        System.out.println("      型[文字列]");
        System.out.println("    -table");
        System.out.println("      説明[trueを設定すると単一表のためのアクセサ・コードを生成します。]");
        System.out.println("      型[文字列]");
        System.out.println("      デフォルト値[false]");
        System.out.println("    -sql");
        System.out.println("      説明[trueを設定するとSQL定義からコードを生成します。]");
        System.out.println("      型[文字列]");
        System.out.println("      デフォルト値[false]");
        System.out.println("    -failonerror");
        System.out.println("      説明[SQL 定義書からソースコード生成に失敗した際に処理を中断します。]");
        System.out.println("      型[文字列]");
        System.out.println("      デフォルト値[false]");
        System.out.println("    -log");
        System.out.println("      説明[trueを設定すると Jakarta Commons用のロギングコードを生成します。]");
        System.out.println("      型[文字列]");
        System.out.println("      デフォルト値[false]");
        System.out.println("    -logmode");
        System.out.println("      説明[ログモードの指定。debug, performance, sqlid のいずれかの値を指定。]");
        System.out.println("      型[文字列]");
        System.out.println("      デフォルト値[debug]");
        System.out.println("    -logsql");
        System.out.println("      説明[SQL をログで出力するかどうかのフラグ。「log」や「logmode」はトレースレベルのログを吐くが、「logsql」は、より可読性のあるログを出す。]");
        System.out.println("      型[文字列]");
        System.out.println("      デフォルト値[false]");
        System.out.println("    -statementtimeout");
        System.out.println("      説明[ステートメントのタイムアウト値。SQL文のタイムアウトさせたい値を設定します。setQueryTimeoutに反映されます。無指定の場合にはAPIデフォルト。]");
        System.out.println("      型[文字列]");
        System.out.println("    -executesql");
        System.out.println("      説明[ソースコード自動生成時にSQL定義のSQL文を実行するかどうかを設定するフラグ。デフォルトは iterator。iterator:検索型のみSQL文を実行して検証する。none:SQL文は実行しない。]");
        System.out.println("      型[文字列]");
        System.out.println("      デフォルト値[iterator]");
        System.out.println("    -encoding");
        System.out.println("      説明[自動生成するソースファイルの文字エンコーディングを指定します。]");
        System.out.println("      型[文字列]");
        System.out.println("    -convertStringToMsWindows31jUnicode");
        System.out.println("      説明[文字列について、Microsoft Windows 3.1日本語版のユニコードへと変換するかどうか。検索結果に反映されます。]");
        System.out.println("      型[文字列]");
        System.out.println("      デフォルト値[false]");
        System.out.println("    -cache");
        System.out.println("      説明[定義書メタファイルから中間XMLファイルへの変換をキャッシュで済ますかどうかのフラグ。]");
        System.out.println("      型[文字列]");
        System.out.println("      デフォルト値[false]");
        System.out.println("    -? , -help");
        System.out.println("      説明[使い方を表示します。]");
    }

    /**
     * このバッチ処理クラスの入力パラメータの妥当性チェックを実施するためのメソッドです。
     *
     * @param input バッチ処理の入力パラメータ。
     * @throws IllegalArgumentException 入力値に不正が見つかった場合。
     */
    public void validateInput(final BlancoDbProcessInput input) throws IllegalArgumentException {
        if (input == null) {
            throw new IllegalArgumentException("BlancoBatchProcessBatchProcess: 処理開始失敗。入力パラメータ[input]にnullが与えられました。");
        }
        if (input.getJdbcdriver() == null) {
            throw new IllegalArgumentException("BlancoDbBatchProcess: 処理開始失敗。入力パラメータ[input]の必須フィールド値[jdbcdriver]に値が設定されていません。");
        }
        if (input.getJdbcurl() == null) {
            throw new IllegalArgumentException("BlancoDbBatchProcess: 処理開始失敗。入力パラメータ[input]の必須フィールド値[jdbcurl]に値が設定されていません。");
        }
        if (input.getJdbcuser() == null) {
            throw new IllegalArgumentException("BlancoDbBatchProcess: 処理開始失敗。入力パラメータ[input]の必須フィールド値[jdbcuser]に値が設定されていません。");
        }
        if (input.getJdbcpassword() == null) {
            throw new IllegalArgumentException("BlancoDbBatchProcess: 処理開始失敗。入力パラメータ[input]の必須フィールド値[jdbcpassword]に値が設定されていません。");
        }
        if (input.getMetadir() == null) {
            throw new IllegalArgumentException("BlancoDbBatchProcess: 処理開始失敗。入力パラメータ[input]の必須フィールド値[metadir]に値が設定されていません。");
        }
        if (input.getBasepackage() == null) {
            throw new IllegalArgumentException("BlancoDbBatchProcess: 処理開始失敗。入力パラメータ[input]の必須フィールド値[basepackage]に値が設定されていません。");
        }
    }
}
