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
 * @author Yasuo Nakanishi
 */
public class NextMethod extends BlancoDbAbstractMethod {
    public NextMethod(final BlancoDbSetting argDbSetting,
            final BlancoDbSqlInfoStructure argSqlInfo,
            final BlancoCgObjectFactory argCgFactory,
            final BlancoCgSourceFile argCgSourceFile,
            final BlancoCgClass argCgClass) {
        super(argDbSetting, argSqlInfo, argCgFactory, argCgSourceFile,
                argCgClass);
    }

    public void expand() {
        final BlancoCgMethod cgMethod = fCgFactory.createMethod("next", null);
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

        cgMethod.getLangDoc().getDescriptionList()
                .add("カーソルを現在の位置から1行次へ移動します。");
        cgMethod.getLangDoc().getDescriptionList().add("");
        if (fSqlInfo.getSingle()) {
            cgMethod.getLangDoc().getDescriptionList().add(
                    "シングル属性が有効なのでスコープをprotectedとします。<br>");
        }

        final List<String> listLine = cgMethod.getLineList();

        if (fDbSetting.getLogging()) {
            switch (fDbSetting.getLoggingMode()) {
            case BlancoDbLoggingModeStringGroup.DEBUG:
                BlancoDbCgUtilJava.addBeginLogToMethod(cgMethod);
            }
        }

        // resultSetが未確保であるばあい、強制的にexecuteQueryを呼び出します。
        listLine.add("if (fResultSet == null) {");
        listLine.add("executeQuery();");
        listLine.add("}");

        listLine.add("");
        listLine.add("try {");
        listLine.add("return fResultSet.next();");
        listLine.add("} catch (SQLException ex) {");
        listLine.add("throw BlancoDbUtil.convertToBlancoException(ex);");
        listLine.add("}");
    }
}