/*
 * blancoDb
 * Copyright (C) 2004-2006 Yasuo Nakanishi
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 */
package blanco.db.expander.query.caller;

import java.util.List;

import blanco.cg.BlancoCgObjectFactory;
import blanco.cg.valueobject.BlancoCgClass;
import blanco.cg.valueobject.BlancoCgMethod;
import blanco.cg.valueobject.BlancoCgSourceFile;
import blanco.commons.util.BlancoJavaSourceUtil;
import blanco.commons.util.BlancoNameAdjuster;
import blanco.db.common.expander.BlancoDbAbstractMethod;
import blanco.db.common.stringgroup.BlancoDbLoggingModeStringGroup;
import blanco.db.common.util.BlancoDbQueryParserUtil;
import blanco.db.common.valueobject.BlancoDbSetting;
import blanco.db.common.valueobject.BlancoDbSqlInfoStructure;
import blanco.db.util.BlancoDbCgUtilJava;
import blanco.db.util.BlancoDbMappingUtilJava;
import blanco.dbmetadata.valueobject.BlancoDbMetaDataColumnStructure;

/**
 * 個別のメソッドを展開するためのクラス。
 * 
 * @author Tosiki Iga
 */
public class GetCallerOutputParameterMethod extends BlancoDbAbstractMethod {
    private BlancoDbMetaDataColumnStructure fColumnStructure = null;

    public GetCallerOutputParameterMethod(final BlancoDbSetting argDbSetting,
            final BlancoDbSqlInfoStructure argSqlInfo,
            final BlancoCgObjectFactory argCgFactory,
            final BlancoCgSourceFile argCgSourceFile,
            final BlancoCgClass argCgClass,
            final BlancoDbMetaDataColumnStructure columnStructure) {
        super(argDbSetting, argSqlInfo, argCgFactory, argCgSourceFile,
                argCgClass);
        this.fColumnStructure = columnStructure;
    }

    public void expand() {
        final BlancoCgMethod cgMethod = fCgFactory.createMethod("get"
                + BlancoNameAdjuster.toClassName(fColumnStructure.getName()),
                null);
        fCgClass.getMethodList().add(cgMethod);

        cgMethod.getLangDoc().getDescriptionList().add(
                "ストアドプロシージャの実行結果の出力パラメータを取得します。");
        cgMethod.getLangDoc().getDescriptionList().add("SQL出力パラメータをゲットします。");
        cgMethod.getLangDoc().getDescriptionList().add("");
        cgMethod.getLangDoc().getDescriptionList().add(
                "内部的には CallableStatementから出力パラメータをゲットします。");

        BlancoDbCgUtilJava.addExceptionToMethodSqlException(fCgFactory,
                cgMethod);

        cgMethod.setReturn(fCgFactory.createReturn(BlancoDbMappingUtilJava
                .getFullClassName(fColumnStructure), "ストアドプロシージャの["
                + BlancoJavaSourceUtil.escapeStringAsJavaDoc(fColumnStructure
                        .getName()) + "]出力"));

        final List<String> listLine = cgMethod.getLineList();

        if (fDbSetting.getLogging()) {
            switch (fDbSetting.getLoggingMode()) {
            case BlancoDbLoggingModeStringGroup.DEBUG:
                BlancoDbCgUtilJava.addBeginLogToMethod(cgMethod);
            }
        }

        final BlancoDbQueryParserUtil query = new BlancoDbQueryParserUtil(
                fSqlInfo.getQuery());

        final int[] listCol = query
                .getSqlParameters(fColumnStructure.getName());
        if (listCol == null) {
            throw new IllegalArgumentException("SQL定義ID[" + fSqlInfo.getName()
                    + "]の SQL出力パラメータ[" + fColumnStructure.getName()
                    + "]が結びついていません.");
        }
        for (int iteSame = 0; iteSame < listCol.length; iteSame++) {
            final int index = listCol[iteSame];
            final String type = BlancoDbMappingUtilJava
                    .getGetterMethodNameForResultSet(fColumnStructure);
            if (BlancoDbMappingUtilJava
                    .getPrimitiveAndNullable(fColumnStructure)) {
                listLine.add(BlancoDbMappingUtilJava
                        .getClassName(fColumnStructure)
                        + " wrk = "
                        + BlancoDbMappingUtilJava.mapPrimitiveIntoWrapperClass(
                                fColumnStructure, "fStatement." + type + "("
                                        + index + ")") + ";");
                listLine.add("if (fStatement.wasNull()) {");
                listLine.add("return null;");
                listLine.add("}");
                listLine.add("return wrk;");
            } else {
                listLine.add("return fStatement." + type + "(" + index + ");");
            }
        }
    }
}