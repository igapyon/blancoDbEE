/*
 * blancoDb
 * Copyright (C) 2004-2006 Yasuo Nakanishi
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 */
package blanco.db.expander.query;

import java.util.List;

import blanco.cg.BlancoCgObjectFactory;
import blanco.cg.valueobject.BlancoCgClass;
import blanco.cg.valueobject.BlancoCgMethod;
import blanco.cg.valueobject.BlancoCgSourceFile;
import blanco.commons.util.BlancoJavaSourceUtil;
import blanco.db.common.expander.BlancoDbAbstractMethod;
import blanco.db.common.util.BlancoDbQueryParserUtil;
import blanco.db.common.valueobject.BlancoDbSetting;
import blanco.db.common.valueobject.BlancoDbSqlInfoStructure;

/**
 * 個別のメソッドを展開するためのクラス。
 * 
 * @author Yasuo Nakanishi
 */
public class GetQueryMethod extends BlancoDbAbstractMethod {
    public GetQueryMethod(final BlancoDbSetting argDbSetting,
            final BlancoDbSqlInfoStructure argSqlInfo,
            final BlancoCgObjectFactory argCgFactory,
            final BlancoCgSourceFile argCgSourceFile,
            final BlancoCgClass argCgClass) {
        super(argDbSetting, argSqlInfo, argCgFactory, argCgSourceFile,
                argCgClass);
    }

    public void expand() {
        final BlancoCgMethod cgMethod = fCgFactory.createMethod("getQuery",
                "SQL定義書で与えられたSQL文を取得します。");
        fCgClass.getMethodList().add(cgMethod);

        cgMethod.setReturn(fCgFactory.createReturn("java.lang.String",
                "JDBCドライバに与えて実行可能な状態のSQL文。"));

        cgMethod
                .getLangDoc()
                .getDescriptionList()
                .add(
                        "SQL入力パラメータとして #キーワードによる指定がある場合には、該当箇所を ? に置き換えた後の SQL文が取得できます。");

        final List<String> listLine = cgMethod.getLineList();

        // 2005.04.15 t.iga 改行は改行として出力するように変更。
        // 2005.10.12 t.iga blancoCommonsの変換ユーティリティを利用するように変更。
        final String escapedQuery = BlancoJavaSourceUtil
                .escapeStringAsJavaSource(fSqlInfo.getQuery());

        // クエリの #パラメータの?への変換
        final String actualSql = new BlancoDbQueryParserUtil(escapedQuery)
                .getNaturalSqlStringForJava();

        listLine.add("return \"" + actualSql + "\";");
    }
}