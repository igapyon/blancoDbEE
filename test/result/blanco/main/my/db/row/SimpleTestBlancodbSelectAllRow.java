/*
 * このソースコードは blanco Frameworkにより自動生成されました。
 */
package my.db.row;

/**
 * SQL定義書(blancoDb)から作成された行クラス。
 *
 * 'SimpleTestBlancodbSelectAllRow'行を表現します。
 * (1) 'COL_ID'列 型:int
 * (2) 'COL_TEXT'列 型:java.lang.String
 * (3) 'COL_NUMERIC'列 型:java.lang.Double
 */
public class SimpleTestBlancodbSelectAllRow {
    /**
     * フィールド[COL_ID]です。
     *
     * フィールド: [COL_ID]。
     */
    private int fColId;

    /**
     * フィールド[COL_TEXT]です。
     *
     * フィールド: [COL_TEXT]。
     */
    private String fColText;

    /**
     * フィールド[COL_NUMERIC]です。
     *
     * フィールド: [COL_NUMERIC]。
     */
    private Double fColNumeric;

    /**
     * フィールド [COL_ID] の値を設定します。
     *
     * フィールドの説明: [フィールド[COL_ID]です。]。
     *
     * @param argColId フィールド[COL_ID]に設定する値。
     */
    public void setColId(final int argColId) {
        fColId = argColId;
    }

    /**
     * フィールド [COL_ID] の値を取得します。
     *
     * フィールドの説明: [フィールド[COL_ID]です。]。
     *
     * @return フィールド[COL_ID]から取得した値。
     */
    public int getColId() {
        return fColId;
    }

    /**
     * フィールド [COL_TEXT] の値を設定します。
     *
     * フィールドの説明: [フィールド[COL_TEXT]です。]。
     *
     * @param argColText フィールド[COL_TEXT]に設定する値。
     */
    public void setColText(final String argColText) {
        fColText = argColText;
    }

    /**
     * フィールド [COL_TEXT] の値を取得します。
     *
     * フィールドの説明: [フィールド[COL_TEXT]です。]。
     *
     * @return フィールド[COL_TEXT]から取得した値。
     */
    public String getColText() {
        return fColText;
    }

    /**
     * フィールド [COL_NUMERIC] の値を設定します。
     *
     * フィールドの説明: [フィールド[COL_NUMERIC]です。]。
     *
     * @param argColNumeric フィールド[COL_NUMERIC]に設定する値。
     */
    public void setColNumeric(final Double argColNumeric) {
        fColNumeric = argColNumeric;
    }

    /**
     * フィールド [COL_NUMERIC] の値を取得します。
     *
     * フィールドの説明: [フィールド[COL_NUMERIC]です。]。
     *
     * @return フィールド[COL_NUMERIC]から取得した値。
     */
    public Double getColNumeric() {
        return fColNumeric;
    }

    /**
     * このバリューオブジェクトの文字列表現を取得します。
     *
     * <P>使用上の注意</P>
     * <UL>
     * <LI>オブジェクトのシャロー範囲のみ文字列化の処理対象となります。
     * <LI>オブジェクトが循環参照している場合には、このメソッドは使わないでください。
     * </UL>
     *
     * @return バリューオブジェクトの文字列表現。
     */
    @Override
    public String toString() {
        final StringBuffer buf = new StringBuffer();
        buf.append("my.db.row.SimpleTestBlancodbSelectAllRow[");
        buf.append("COL_ID=" + fColId);
        buf.append(",COL_TEXT=" + fColText);
        buf.append(",COL_NUMERIC=" + fColNumeric);
        buf.append("]");
        return buf.toString();
    }
}
