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
import blanco.db.common.expander.BlancoDbAbstractMethod;
import blanco.db.common.valueobject.BlancoDbSetting;
import blanco.db.common.valueobject.BlancoDbSqlInfoStructure;

/**
 * 個別のメソッドを展開するためのクラス。
 * 
 * @author tosiki iga
 */
public class GetStatementMethod extends BlancoDbAbstractMethod {
    protected boolean fIsCallableStatement = false;

    public GetStatementMethod(final BlancoDbSetting argDbSetting,
            final BlancoDbSqlInfoStructure argSqlInfo,
            final BlancoCgObjectFactory argCgFactory,
            final BlancoCgSourceFile argCgSourceFile,
            final BlancoCgClass argCgClass, final boolean isCallableStatement) {
        super(argDbSetting, argSqlInfo, argCgFactory, argCgSourceFile,
                argCgClass);
        fIsCallableStatement = isCallableStatement;
    }

    public void expand() {
        final BlancoCgMethod cgMethod = fCgFactory.createMethod("getStatement",
                null);
        fCgClass.getMethodList().add(cgMethod);

        String resultType = "java.sql.PreparedStatement";
        if (fIsCallableStatement == false) {
        } else {
            resultType = "java.sql.CallableStatement";
        }

        cgMethod.setReturn(fCgFactory.createReturn(resultType, "内部的に利用されている "
                + resultType + "オブジェクト"));

        cgMethod.getLangDoc().getDescriptionList().add(
                "ステートメント (" + resultType + ") を取得します。");
        cgMethod.getLangDoc().getDescriptionList().add(
                "@deprecated 基本的にStatementは外部から直接利用する必要はありません。");

        final List<String> listLine = cgMethod.getLineList();

        listLine.add("return fStatement;");
    }
}