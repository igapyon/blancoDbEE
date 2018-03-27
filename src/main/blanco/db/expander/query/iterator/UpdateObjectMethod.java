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
import blanco.db.common.expander.BlancoDbAbstractMethod;
import blanco.db.common.stringgroup.BlancoDbLoggingModeStringGroup;
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
public class UpdateObjectMethod extends BlancoDbAbstractMethod {
    private BlancoDbMetaDataColumnStructure fColumnStructure = null;

    public UpdateObjectMethod(final BlancoDbSetting argDbSetting,
            final BlancoDbSqlInfoStructure argSqlInfo,
            final BlancoCgObjectFactory argCgFactory,
            final BlancoCgSourceFile argCgSourceFile,
            final BlancoCgClass argCgClass,
            final BlancoDbMetaDataColumnStructure columnStructure) {
        super(argDbSetting, argSqlInfo, argCgFactory, argCgSourceFile,
                argCgClass);
        fColumnStructure = columnStructure;
    }

    public void expand() {
        final BlancoCgMethod cgMethod = fCgFactory.createMethod("update"
                + BlancoNameAdjuster.toClassName(fColumnStructure.getName()),
                "現在カーソルがある行の'" + fColumnStructure.getName() + "'列を更新します。");
        fCgClass.getMethodList().add(cgMethod);

        BlancoDbCgUtilJava.addExceptionToMethodSqlException(fCgFactory,
                cgMethod);

        cgMethod.getParameterList().add(
                fCgFactory.createParameter("arg"
                        + BlancoNameAdjuster.toClassName(fColumnStructure
                                .getName()), BlancoDbMappingUtilJava
                        .getFullClassName(fColumnStructure), fColumnStructure
                        .getName()
                        + "列にセットする値"));

        switch (fColumnStructure.getDataType()) {
        case Types.BINARY:
        case Types.VARBINARY:
        case Types.LONGVARBINARY:
        case Types.BLOB:
        case Types.LONGVARCHAR:
        case Types.CLOB:
            cgMethod.getParameterList().add(
                    fCgFactory.createParameter("sourceLength", "int", null));
            break;
        }

        cgMethod.getLangDoc().getDescriptionList().add(
                "実際の更新はupdateRowメソッドの呼び出し時におこなわれます。<br>");
        cgMethod.getLangDoc().getDescriptionList().add(
                "更新可能属性が有効となっているので生成されます。<br>");

        final List<String> listLine = cgMethod.getLineList();

        if (fDbSetting.getLogging()) {
            switch (fDbSetting.getLoggingMode()) {
            case BlancoDbLoggingModeStringGroup.DEBUG:
                listLine.add("if (fLog.isDebugEnabled()) {");
                listLine.add("fLog.debug(\""
                        + cgMethod.getName()
                        + ": arg"
                        + BlancoNameAdjuster.toClassName(fColumnStructure
                                .getName())
                        + " = \" + arg"
                        + BlancoNameAdjuster.toClassName(fColumnStructure
                                .getName()) + ");");
                listLine.add("}");
                listLine.add("");
            }
        }

        if (BlancoDbMappingUtilJava.getPrimitiveAndNullable(fColumnStructure)) {
            listLine.add("if (arg"
                    + BlancoNameAdjuster
                            .toClassName(fColumnStructure.getName())
                    + " == null) {");
            listLine.add("fResultSet.updateNull(\""
                    + fColumnStructure.getName() + "\");");
            listLine.add("} else {");
        }

        String optionParam = "";

        switch (fColumnStructure.getDataType()) {
        case Types.BINARY:
        case Types.VARBINARY:
        case Types.LONGVARBINARY:
        case Types.BLOB:
        case Types.LONGVARCHAR:
        case Types.CLOB:
            optionParam = ", sourceLength";
            break;
        }

        listLine.add("fResultSet."
                + BlancoDbMappingUtilJava
                        .getUpdateMethodNameForResultSet(fColumnStructure)
                + "(\""
                + fColumnStructure.getName()
                + "\", "
                + BlancoDbMappingUtilJava
                        .mapWrapperClassIntoPrimitive(fColumnStructure,
                                "arg"
                                        + BlancoNameAdjuster
                                                .toClassName(fColumnStructure
                                                        .getName()))
                + optionParam + ");");

        if (BlancoDbMappingUtilJava.getClassName(fColumnStructure).equals(
                "Date")) {
            // BlancoDbMappingUtil.mapWrapperClassIntoPrimitiveメソッドでTimestamp型が必要です。
            fCgSourceFile.getImportList().add("java.sql.Timestamp");
        }

        if (BlancoDbMappingUtilJava.getPrimitiveAndNullable(fColumnStructure)) {
            listLine.add("}");
        }
    }
}