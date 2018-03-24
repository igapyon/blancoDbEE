/*
 * このクラスは 'AbstractBlancoDbConstants' の具象クラスとして blanco Framework によって自動生成されました。
 */
package blanco.db;

import blanco.fw.BlancoGeneratedBy;

/**
 * blancoDb ????????N???X?B
 */
@BlancoGeneratedBy(name = "Blanco2g")
public class BlancoDbConstants extends AbstractBlancoDbConstants {
    /**
     * Version Number.
     *
     * [@BlancoConstantsVersion]
     */
    public static final String VERSION = "2.2.4-I201403121738";

    /**
     * Getter for version constants.
     *
     * [@BlancoConstantsVersion]
     *
     * @return Version string.
     */
    public static String getVersion() {
        return VERSION;
    }

    /**
     * ?v???_?N?g???B?p???\?????????B
     * [@BlancoGetterSetter]
     *
     * @return 取得したい値。
     */
    public static String getProductName() {
        return PRODUCT_NAME;
    }
}
