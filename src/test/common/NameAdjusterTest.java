package common;

import junit.framework.TestCase;
import blanco.commons.util.BlancoNameAdjuster;

/**
 * @author iga
 * 
 * TODO この生成された型コメントのテンプレートを変更するには次へジャンプ: ウィンドウ - 設定 - Java - コード・スタイル -
 * コード・テンプレート
 */
public class NameAdjusterTest extends TestCase {

    public void testGetMethodName() throws Exception {
        assertEquals("getMyName", "get"
                + BlancoNameAdjuster.toClassName("my_name"));
        assertEquals("getMyName", "get"
                + BlancoNameAdjuster.toClassName("MY_NAME"));
        assertEquals("getMyNameIsFoo", "get"
                + BlancoNameAdjuster.toClassName("my__name__is__foo"));

        assertEquals("getMyName", "get"
                + BlancoNameAdjuster.toClassName("MyName"));
        assertEquals("getMyName", "get"
                + BlancoNameAdjuster.toClassName("My_Name"));
        assertEquals("getMyName", "get"
                + BlancoNameAdjuster.toClassName("MY NAME"));
        assertEquals("getMyName", "get"
                + BlancoNameAdjuster.toClassName("my name"));

        assertEquals("getABc", "get" + BlancoNameAdjuster.toClassName("a_bc"));

        assertEquals("getA", "get" + BlancoNameAdjuster.toClassName("_a"));

        assertEquals("getA", "get" + BlancoNameAdjuster.toClassName("a_"));
        assertEquals("getAB", "get" + BlancoNameAdjuster.toClassName("a__b_"));

        assertEquals("getAB", "get" + BlancoNameAdjuster.toClassName("a_b"));

        assertEquals("getAB", "get" + BlancoNameAdjuster.toClassName("a__b"));
    }

    public void testToClassName() throws Exception {
        assertEquals("AB", BlancoNameAdjuster.toClassName("a_b"));
        assertEquals("A", BlancoNameAdjuster.toClassName("a"));
        assertEquals("A", BlancoNameAdjuster.toClassName("A"));
        assertEquals("A", BlancoNameAdjuster.toClassName("_A"));

        assertEquals("Abc", BlancoNameAdjuster.toClassName("abc"));
        assertEquals("ClassName", BlancoNameAdjuster.toClassName("class_name"));
        assertEquals("ClassName", BlancoNameAdjuster.toClassName("className"));
        assertEquals("ClassName", BlancoNameAdjuster.toClassName("ClassName"));

        assertEquals("MyName", BlancoNameAdjuster.toClassName("my_name"));
        assertEquals("MyName", BlancoNameAdjuster.toClassName("MY_NAME"));
        assertEquals("MyName", BlancoNameAdjuster.toClassName("MyName"));
        assertEquals("MyName", BlancoNameAdjuster.toClassName("My_Name"));

        // こっちの方は適切に処理される。
        assertEquals("AB", BlancoNameAdjuster.toClassName("a__b"));
        assertEquals("AbCd", BlancoNameAdjuster.toClassName("ab__cd"));
    }

    public void testToFieldName() throws Exception {
        assertEquals("aB", BlancoNameAdjuster.toParameterName("a_b"));
        assertEquals("aB", BlancoNameAdjuster.toParameterName("a__b"));
        assertEquals("myName", BlancoNameAdjuster.toParameterName("my_name"));
        assertEquals("myName", BlancoNameAdjuster.toParameterName("MY_NAME"));
        assertEquals("myName", BlancoNameAdjuster.toParameterName("my name"));
        assertEquals("myName", BlancoNameAdjuster.toParameterName("MY NAME"));
        assertEquals("myName", BlancoNameAdjuster.toParameterName("MyName"));
    }

    public void testToTitleCase() throws Exception {
        assertEquals("A_b", BlancoNameAdjuster.toUpperCaseTitle("a_b"));
        assertEquals("A__b", BlancoNameAdjuster.toUpperCaseTitle("a__b"));
        assertEquals("A__b", BlancoNameAdjuster.toUpperCaseTitle("a__b"));
        assertEquals("MyName", BlancoNameAdjuster.toUpperCaseTitle("MyName"));
        assertEquals("_myName", BlancoNameAdjuster.toUpperCaseTitle("_myName"));
        assertEquals("MyName", BlancoNameAdjuster.toUpperCaseTitle("myName"));
    }

    public void testToValueName() throws Exception {
        assertEquals("aB", BlancoNameAdjuster.toParameterName("a_b"));
        assertEquals("myName", BlancoNameAdjuster.toParameterName("my_name"));
        assertEquals("myName", BlancoNameAdjuster.toParameterName("MY_NAME"));
        assertEquals("myName", BlancoNameAdjuster.toParameterName("my name"));
        assertEquals("myName", BlancoNameAdjuster.toParameterName("MY NAME"));
        assertEquals("myName", BlancoNameAdjuster.toParameterName("my__name"));
        assertEquals("myName", BlancoNameAdjuster.toParameterName("MY__NAME"));
        assertEquals("myName", BlancoNameAdjuster.toParameterName("MY_ NAME"));
        assertEquals("myNameIs", BlancoNameAdjuster
                .toParameterName("my__name__is"));
    }
}
