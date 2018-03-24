/*
 * blancoDb
 * Copyright (C) 2004-2006 Yasuo Nakanishi
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 */
package blanco.db.expander.query.iterator;

import java.util.List;

import blanco.cg.BlancoCgObjectFactory;
import blanco.cg.valueobject.BlancoCgClass;
import blanco.cg.valueobject.BlancoCgMethod;
import blanco.cg.valueobject.BlancoCgSourceFile;
import blanco.commons.util.BlancoNameAdjuster;
import blanco.db.common.expander.BlancoDbAbstractMethod;
import blanco.db.common.stringgroup.BlancoDbSqlInfoScrollStringGroup;
import blanco.db.common.valueobject.BlancoDbSetting;
import blanco.db.common.valueobject.BlancoDbSqlInfoStructure;
import blanco.db.util.BlancoDbCgUtilJava;

/**
 * 個別のメソッドを展開するためのクラス。
 * 
 * @author Tosiki Iga
 */
public class GetListMethod extends BlancoDbAbstractMethod {
    public GetListMethod(final BlancoDbSetting argDbSetting,
            final BlancoDbSqlInfoStructure argSqlInfo,
            final BlancoCgObjectFactory argCgFactory,
            final BlancoCgSourceFile argCgSourceFile,
            final BlancoCgClass argCgClass) {
        super(argDbSetting, argSqlInfo, argCgFactory, argCgSourceFile,
                argCgClass);
    }

    public void expand() {
        final BlancoCgMethod cgMethod = fCgFactory.createMethod("getList",
                "検索結果をリストの形式で取得します。");
        fCgClass.getMethodList().add(cgMethod);

        // 行オブジェクトの型名を取得します。
        final String rowObjectType = BlancoNameAdjuster.toClassName(fSqlInfo
                .getName()) + "Row";

        cgMethod.setReturn(fCgFactory.createReturn("java.util.List<"
                + rowObjectType + ">", fSqlInfo.getName()
                + "クラスのList。検索結果が0件の場合には空のリストが戻ります。"));

        BlancoDbCgUtilJava.addExceptionToMethodSqlException(fCgFactory,
                cgMethod);

        fCgSourceFile.getImportList().add("java.util.ArrayList");

        final List<String> listDesc = cgMethod.getLangDoc()
                .getDescriptionList();
        listDesc.add("リストには " + fSqlInfo.getName() + "クラスが格納されます。<br>");
        listDesc.add("検索結果の件数があらかじめわかっていて、且つ件数が少ない場合に利用することができます。<br>");
        listDesc.add("検索結果の件数が多い場合には、このメソッドは利用せず、代わりに next()メソッドを利用することをお勧めします。<br>");
        if (fSqlInfo.getScroll() == BlancoDbSqlInfoScrollStringGroup.TYPE_FORWARD_ONLY) {
            listDesc.add("このQueryIteratorは FORWARD_ONLY(順方向カーソル)です。大量のデータを扱うことがわかっている場合には、このgetListメソッドの利用は極力避けるか、あるいは スクロールカーソルとしてソースコードを再生成してください。");
        } else {
            cgMethod.getParameterList().add(
                    fCgFactory.createParameter("absoluteStartPoint", "int",
                            "読み出しを開始する行。最初の行から読み出したい場合には 1 を指定します。"));
        }

        cgMethod.getParameterList().add(
                fCgFactory.createParameter("size", "int", "読み出しを行う行数。"));

        final List<String> listLine = cgMethod.getLineList();

        listLine.add("List<" + rowObjectType + "> result = new ArrayList<"
                + rowObjectType + ">(8192);");
        if (fSqlInfo.getScroll() != BlancoDbSqlInfoScrollStringGroup.TYPE_FORWARD_ONLY) {
            listLine.add("if (absolute(absoluteStartPoint) == false) {");
            listLine.add("return result;");
            listLine.add("}");
        }
        listLine.add("for (int count = 1; count <= size; count++) {");
        if (fSqlInfo.getScroll() == BlancoDbSqlInfoScrollStringGroup.TYPE_FORWARD_ONLY) {
            listLine.add("if (next() == false) {");
            listLine.add("break;");
            listLine.add("}");
        } else {
            listLine.add("if (count != 1) {");
            listLine.add("if (next() == false) {");
            listLine.add("break;");
            listLine.add("}");
            listLine.add("}");
        }
        listLine.add("result.add(getRow());");
        listLine.add("}");
        listLine.add("return result;");
    }
}