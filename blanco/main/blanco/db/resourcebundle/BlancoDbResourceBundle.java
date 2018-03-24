/*
 * このソースコードは blanco Frameworkにより自動生成されました。
 */
package blanco.db.resourcebundle;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * blancoDbが利用するリソースバンドルです。
 *
 * リソースバンドル定義: [BlancoDb]。<BR>
 * このクラスはリソースバンドル定義書から自動生成されたリソースバンドルクラスです。<BR>
 * 既知のロケール<BR>
 * <UL>
 * <LI>ja
 * </UL>
 */
public class BlancoDbResourceBundle {
    /**
     * リソースバンドルオブジェクト。
     *
     * 内部的に実際に入力を行うリソースバンドルを記憶します。
     */
    private ResourceBundle fResourceBundle;

    /**
     * BlancoDbResourceBundleクラスのコンストラクタ。
     *
     * 基底名[BlancoDb]、デフォルトのロケール、呼び出し側のクラスローダを使用して、リソースバンドルを取得します。
     */
    public BlancoDbResourceBundle() {
        try {
            fResourceBundle = ResourceBundle.getBundle("blanco/db/resourcebundle/BlancoDb");
        } catch (MissingResourceException ex) {
        }
    }

    /**
     * BlancoDbResourceBundleクラスのコンストラクタ。
     *
     * 基底名[BlancoDb]、指定されたロケール、呼び出し側のクラスローダを使用して、リソースバンドルを取得します。
     *
     * @param locale ロケールの指定
     */
    public BlancoDbResourceBundle(final Locale locale) {
        try {
            fResourceBundle = ResourceBundle.getBundle("blanco/db/resourcebundle/BlancoDb", locale);
        } catch (MissingResourceException ex) {
        }
    }

    /**
     * BlancoDbResourceBundleクラスのコンストラクタ。
     *
     * 基底名[BlancoDb]、指定されたロケール、指定されたクラスローダを使用して、リソースバンドルを取得します。
     *
     * @param locale ロケールの指定
     * @param loader クラスローダの指定
     */
    public BlancoDbResourceBundle(final Locale locale, final ClassLoader loader) {
        try {
            fResourceBundle = ResourceBundle.getBundle("blanco/db/resourcebundle/BlancoDb", locale, loader);
        } catch (MissingResourceException ex) {
        }
    }

    /**
     * 内部的に保持しているリソースバンドルオブジェクトを取得します。
     *
     * @return 内部的に保持しているリソースバンドルオブジェクト。
     */
    public ResourceBundle getResourceBundle() {
        return fResourceBundle;
    }

    /**
     * bundle[BlancoDb], key[METAFILE_DISPLAYNAME]
     *
     * [SQL定義書] (ja)<br>
     *
     * @return key[METAFILE_DISPLAYNAME]に対応する値。外部から読み込みができない場合には、定義書の値を戻します。必ずnull以外の値が戻ります。
     */
    public String getMetafileDisplayname() {
        // 初期値として定義書の値を利用します。
        String strFormat = "SQL定義書";
        try {
            if (fResourceBundle != null) {
                strFormat = fResourceBundle.getString("METAFILE_DISPLAYNAME");
            }
        } catch (MissingResourceException ex) {
        }
        // 置換文字列はひとつもありません。
        return strFormat;
    }

    /**
     * bundle[BlancoDb], key[EXPANDER.DISABLE_GET_STATEMENT]
     *
     * [false] (ja)<br>
     *
     * @return key[EXPANDER.DISABLE_GET_STATEMENT]に対応する値。外部から読み込みができない場合には、定義書の値を戻します。必ずnull以外の値が戻ります。
     */
    public String getExpanderDisableGetStatement() {
        // 初期値として定義書の値を利用します。
        String strFormat = "false";
        try {
            if (fResourceBundle != null) {
                strFormat = fResourceBundle.getString("EXPANDER.DISABLE_GET_STATEMENT");
            }
        } catch (MissingResourceException ex) {
        }
        // 置換文字列はひとつもありません。
        return strFormat;
    }

    /**
     * bundle[BlancoDb], key[TASK.ERR001]
     *
     * [SQL例外が発生しました。処理中断します。] (ja)<br>
     *
     * @return key[TASK.ERR001]に対応する値。外部から読み込みができない場合には、定義書の値を戻します。必ずnull以外の値が戻ります。
     */
    public String getTaskErr001() {
        // 初期値として定義書の値を利用します。
        String strFormat = "SQL例外が発生しました。処理中断します。";
        try {
            if (fResourceBundle != null) {
                strFormat = fResourceBundle.getString("TASK.ERR001");
            }
        } catch (MissingResourceException ex) {
        }
        // 置換文字列はひとつもありません。
        return strFormat;
    }

    /**
     * bundle[BlancoDb], key[TASK.ERR002]
     *
     * [クラスが見つかりません。クラスパスの設定などを確認してください。] (ja)<br>
     *
     * @return key[TASK.ERR002]に対応する値。外部から読み込みができない場合には、定義書の値を戻します。必ずnull以外の値が戻ります。
     */
    public String getTaskErr002() {
        // 初期値として定義書の値を利用します。
        String strFormat = "クラスが見つかりません。クラスパスの設定などを確認してください。";
        try {
            if (fResourceBundle != null) {
                strFormat = fResourceBundle.getString("TASK.ERR002");
            }
        } catch (MissingResourceException ex) {
        }
        // 置換文字列はひとつもありません。
        return strFormat;
    }

    /**
     * bundle[BlancoDb], key[TASK.ERR003]
     *
     * [想定されないSAX例外が発生しました。処理中断します。] (ja)<br>
     *
     * @return key[TASK.ERR003]に対応する値。外部から読み込みができない場合には、定義書の値を戻します。必ずnull以外の値が戻ります。
     */
    public String getTaskErr003() {
        // 初期値として定義書の値を利用します。
        String strFormat = "想定されないSAX例外が発生しました。処理中断します。";
        try {
            if (fResourceBundle != null) {
                strFormat = fResourceBundle.getString("TASK.ERR003");
            }
        } catch (MissingResourceException ex) {
        }
        // 置換文字列はひとつもありません。
        return strFormat;
    }

    /**
     * bundle[BlancoDb], key[TASK.ERR004]
     *
     * [想定されない入出力例外が発生しました。処理中断します。] (ja)<br>
     *
     * @return key[TASK.ERR004]に対応する値。外部から読み込みができない場合には、定義書の値を戻します。必ずnull以外の値が戻ります。
     */
    public String getTaskErr004() {
        // 初期値として定義書の値を利用します。
        String strFormat = "想定されない入出力例外が発生しました。処理中断します。";
        try {
            if (fResourceBundle != null) {
                strFormat = fResourceBundle.getString("TASK.ERR004");
            }
        } catch (MissingResourceException ex) {
        }
        // 置換文字列はひとつもありません。
        return strFormat;
    }

    /**
     * bundle[BlancoDb], key[TASK.ERR005]
     *
     * [想定されないXMLパーサ例外が発生しました。処理中断します。] (ja)<br>
     *
     * @return key[TASK.ERR005]に対応する値。外部から読み込みができない場合には、定義書の値を戻します。必ずnull以外の値が戻ります。
     */
    public String getTaskErr005() {
        // 初期値として定義書の値を利用します。
        String strFormat = "想定されないXMLパーサ例外が発生しました。処理中断します。";
        try {
            if (fResourceBundle != null) {
                strFormat = fResourceBundle.getString("TASK.ERR005");
            }
        } catch (MissingResourceException ex) {
        }
        // 置換文字列はひとつもありません。
        return strFormat;
    }

    /**
     * bundle[BlancoDb], key[TASK.ERR006]
     *
     * [想定されないXMLトランスフォーマー例外が発生しました。処理中断します。] (ja)<br>
     *
     * @return key[TASK.ERR006]に対応する値。外部から読み込みができない場合には、定義書の値を戻します。必ずnull以外の値が戻ります。
     */
    public String getTaskErr006() {
        // 初期値として定義書の値を利用します。
        String strFormat = "想定されないXMLトランスフォーマー例外が発生しました。処理中断します。";
        try {
            if (fResourceBundle != null) {
                strFormat = fResourceBundle.getString("TASK.ERR006");
            }
        } catch (MissingResourceException ex) {
        }
        // 置換文字列はひとつもありません。
        return strFormat;
    }
}
