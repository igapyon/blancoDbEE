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
import blanco.db.common.expander.BlancoDbAbstractMethod;
import blanco.db.common.stringgroup.BlancoDbLoggingModeStringGroup;
import blanco.db.common.valueobject.BlancoDbSetting;
import blanco.db.common.valueobject.BlancoDbSqlInfoStructure;
import blanco.db.util.BlancoDbCgUtilJava;

/**
 * 個別のメソッドを展開するためのクラス。
 * 
 * カーソル属性がtrueの場合に、このメソッドは作成されます。
 * 
 * @author Tosiki Iga
 */
public class RelativeMethod extends BlancoDbAbstractMethod {
    public RelativeMethod(final BlancoDbSetting argDbSetting,
            final BlancoDbSqlInfoStructure argSqlInfo,
            final BlancoCgObjectFactory argCgFactory,
            final BlancoCgSourceFile argCgSourceFile,
            final BlancoCgClass argCgClass) {
        super(argDbSetting, argSqlInfo, argCgFactory, argCgSourceFile,
                argCgClass);
    }

    public void expand() {
        final BlancoCgMethod cgMethod = fCgFactory.createMethod("relative",
                null);
        fCgClass.getMethodList().add(cgMethod);

        /*
         * シングル属性が有効である場合には protectedとします。
         */
        if (fSqlInfo.getSingle()) {
            cgMethod.setAccess("protected");
        }

        cgMethod.setReturn(fCgFactory.createReturn("boolean",
                "新しい現在の行が有効な場合はtrue、それ以上の行がない場合はfalse。"));

        BlancoDbCgUtilJava.addExceptionToMethodDeadlockTimeoutException(
                fCgFactory, cgMethod, fDbSetting);
        BlancoDbCgUtilJava.addExceptionToMethodSqlException(fCgFactory,
                cgMethod);

        cgMethod
                .getParameterList()
                .add(
                        fCgFactory
                                .createParameter("rows", "int",
                                        "現在の行から移動する相対行数を指定します。正の数ではカーソルを順方向に移動し、負の数ではカーソルを逆方向に移動します。"));

        cgMethod.getLangDoc().getDescriptionList().add(
                "カーソルを結果セットの相対行数だけ移動します。");
        cgMethod.getLangDoc().getDescriptionList().add("");
        if (fSqlInfo.getSingle()) {
            cgMethod.getLangDoc().getDescriptionList().add(
                    "シングル属性が有効なのでスコープをprotectedとします。<br>");
        }

        cgMethod.getLangDoc().getDescriptionList().add(
                "relative(1)はnext()を呼び出すのと同じです。<br>");
        cgMethod.getLangDoc().getDescriptionList().add(
                "relative(-1)はprevious()を呼び出すのと同じです。<br>");

        final List<String> listLine = cgMethod.getLineList();

        if (fDbSetting.getLogging()) {
            switch (fDbSetting.getLoggingMode()) {
            case BlancoDbLoggingModeStringGroup.DEBUG:
                listLine.add("if (fLog.isDebugEnabled()) {");
                listLine.add("fLog.debug(\"" + cgMethod.getName()
                        + ": rows = \" + rows);");
                listLine.add("}");
                listLine.add("");
            }
        }

        // resultSetが未確保であるばあい、強制的にexecuteQueryを呼び出します。
        listLine.add("if (fResultSet == null) {");
        listLine.add("executeQuery();");
        listLine.add("}");

        listLine.add("");
        listLine.add("try {");
        listLine.add("return fResultSet.relative(rows);");
        listLine.add("} catch (SQLException ex) {");
        listLine.add("throw BlancoDbUtil.convertToBlancoException(ex);");
        listLine.add("}");
    }
}