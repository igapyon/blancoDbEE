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

import java.sql.Types;
import java.util.List;

import blanco.cg.BlancoCgObjectFactory;
import blanco.cg.valueobject.BlancoCgClass;
import blanco.cg.valueobject.BlancoCgMethod;
import blanco.cg.valueobject.BlancoCgSourceFile;
import blanco.commons.util.BlancoNameAdjuster;
import blanco.commons.util.BlancoNameUtil;
import blanco.db.common.expander.BlancoDbAbstractMethod;
import blanco.db.common.stringgroup.BlancoDbLoggingModeStringGroup;
import blanco.db.common.util.BlancoDbUtil;
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
public class GetRowMethod extends BlancoDbAbstractMethod {
    public GetRowMethod(final BlancoDbSetting argDbSetting,
            final BlancoDbSqlInfoStructure argSqlInfo,
            final BlancoCgObjectFactory argCgFactory,
            final BlancoCgSourceFile argCgSourceFile,
            final BlancoCgClass argCgClass) {
        super(argDbSetting, argSqlInfo, argCgFactory, argCgSourceFile,
                argCgClass);
    }

    public void expand() {
        final BlancoCgMethod cgMethod = fCgFactory.createMethod("getRow",
                "現在の行のデータをオブジェクトとして取得します。");
        fCgClass.getMethodList().add(cgMethod);

        /*
         * シングル属性が有効である場合には protectedとします。
         */
        if (fSqlInfo.getSingle()) {
            cgMethod.setAccess("protected");
        }

        // 行オブジェクトの型名を取得します。
        final String rowObjectType = BlancoDbUtil.getBasePackage(fSqlInfo,
                fDbSetting)
                + ".row."
                + BlancoNameAdjuster.toClassName(fSqlInfo.getName())
                + "Row";

        cgMethod.setReturn(fCgFactory.createReturn(rowObjectType, "行オブジェクト。"));

        BlancoDbCgUtilJava.addExceptionToMethodSqlException(fCgFactory,
                cgMethod);

        final List<String> listDesc = cgMethod.getLangDoc()
                .getDescriptionList();

        if (fSqlInfo.getSingle()) {
            listDesc.add("シングル属性が有効なのでスコープをprotectedとします。<br>");
            listDesc.add("このメソッドの代わりに getSingleRowメソッドを利用してください。<br>");
        } else {
            listDesc.add("このメソッドを呼び出す前に、next()などのカーソルを操作するメソッドを呼び出す必要があります。");
        }

        final List<String> listLine = cgMethod.getLineList();

        if (fDbSetting.getLogging()) {
            switch (fDbSetting.getLoggingMode()) {
            case BlancoDbLoggingModeStringGroup.DEBUG:
                BlancoDbCgUtilJava.addBeginLogToMethod(cgMethod);
            }
        }

        listLine.add(BlancoNameUtil.trimJavaPackage(rowObjectType)
                + " result = new "
                + BlancoNameUtil.trimJavaPackage(rowObjectType) + "();");

        int indexCol = 1;
        for (int index = 0; index < fSqlInfo.getResultSetColumnList().size(); index++) {
            final BlancoDbMetaDataColumnStructure columnStructure = (BlancoDbMetaDataColumnStructure) fSqlInfo
                    .getResultSetColumnList().get(index);

            listLine
                    .add("result.set"
                            + BlancoNameAdjuster.toClassName(columnStructure
                                    .getName())
                            + "("
                    + (fDbSetting.getConvertStringToMsWindows31jUnicode()
                            && (columnStructure.getDataType() == Types.CHAR || columnStructure.getDataType() == Types.VARCHAR) ? "blanco.db.runtime.util.BlancoDbRuntimeStringUtil.convertToMsWindows31jUnicode("
                            : "")
                    + BlancoDbMappingUtilJava.mapPrimitiveIntoWrapperClass(columnStructure, "fResultSet."
                            + BlancoDbMappingUtilJava.getGetterMethodNameForResultSet(columnStructure) + "(" + indexCol
                            + ")")
                    + (fDbSetting.getConvertStringToMsWindows31jUnicode()
                            && (columnStructure.getDataType() == Types.CHAR || columnStructure.getDataType() == Types.VARCHAR) ? ")"
                            : "")                            + ");");

            if (BlancoDbMappingUtilJava
                    .getPrimitiveAndNullable(columnStructure)) {
                listLine.add("if (fResultSet.wasNull()) {");
                listLine.add("result.set"
                        + BlancoNameAdjuster.toClassName(columnStructure
                                .getName()) + "(null);");
                listLine.add("}");
            }
            indexCol++;
        }

        listLine.add("");

        listLine.add("return result;");
    }
}