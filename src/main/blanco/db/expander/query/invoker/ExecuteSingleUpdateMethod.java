/*
 * blancoDb
 * Copyright (C) 2004-2006 Yasuo Nakanishi
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 */
package blanco.db.expander.query.invoker;

import java.util.List;

import blanco.cg.BlancoCgObjectFactory;
import blanco.cg.valueobject.BlancoCgClass;
import blanco.cg.valueobject.BlancoCgMethod;
import blanco.cg.valueobject.BlancoCgSourceFile;
import blanco.db.common.expander.BlancoDbAbstractMethod;
import blanco.db.common.stringgroup.BlancoDbLoggingModeStringGroup;
import blanco.db.common.util.BlancoDbUtil;
import blanco.db.common.valueobject.BlancoDbSetting;
import blanco.db.common.valueobject.BlancoDbSqlInfoStructure;
import blanco.db.expander.exception.NoRowModifiedExceptionClass;
import blanco.db.expander.exception.TooManyRowsModifiedExceptionClass;
import blanco.db.util.BlancoDbCgUtilJava;

/**
 * 個別のメソッドを展開するためのクラス。
 * 
 * @author Tosiki Iga
 */
public class ExecuteSingleUpdateMethod extends BlancoDbAbstractMethod {
    public ExecuteSingleUpdateMethod(final BlancoDbSetting argDbSetting,
            final BlancoDbSqlInfoStructure argSqlInfo,
            final BlancoCgObjectFactory argCgFactory,
            final BlancoCgSourceFile argCgSourceFile,
            final BlancoCgClass argCgClass) {
        super(argDbSetting, argSqlInfo, argCgFactory, argCgSourceFile,
                argCgClass);
    }

    public void expand() {
        final BlancoCgMethod cgMethod = fCgFactory.createMethod(
                "executeSingleUpdate", "SQL文を実行します。");
        fCgClass.getMethodList().add(cgMethod);

        cgMethod.getThrowList().add(
                fCgFactory.createException(BlancoDbUtil
                        .getRuntimePackage(fDbSetting)
                        + ".exception.NoRowModifiedException",
                        "データベースの処理の結果、1行もデータが変更されなかった場合。"));

        cgMethod.getThrowList().add(
                fCgFactory.createException(BlancoDbUtil
                        .getRuntimePackage(fDbSetting)
                        + ".exception.TooManyRowsModifiedException",
                        "データベースの処理の結果、1行を超えるデータが変更されてしまった場合。"));

        BlancoDbCgUtilJava.addExceptionToMethodIntegrityConstraintException(
                fCgFactory, cgMethod, fDbSetting);
        BlancoDbCgUtilJava.addExceptionToMethodDeadlockTimeoutException(
                fCgFactory, cgMethod, fDbSetting);
        BlancoDbCgUtilJava.addExceptionToMethodSqlException(fCgFactory,
                cgMethod);

        cgMethod.getLangDoc().getDescriptionList().add(
                "SQL文の実行結果が1行であることを確認します。実行結果が1行以外である場合には例外を発生させます。<br>");
        cgMethod.getLangDoc().getDescriptionList().add(
                "シングル属性が有効となっているので生成されます。<br>");

        final List<String> listLine = cgMethod.getLineList();

        if (fDbSetting.getLogging()) {
            switch (fDbSetting.getLoggingMode()) {
            case BlancoDbLoggingModeStringGroup.DEBUG:
                BlancoDbCgUtilJava.addBeginLogToMethod(cgMethod);
            }
        }

        listLine.add("int result = 0;");
        listLine.add("result = executeUpdate();");
        listLine.add("");

        fCgSourceFile.getImportList().add(
                BlancoDbUtil.getRuntimePackage(fDbSetting) + ".exception."
                        + NoRowModifiedExceptionClass.CLASS_NAME);
        fCgSourceFile.getImportList().add(
                BlancoDbUtil.getRuntimePackage(fDbSetting) + ".exception."
                        + TooManyRowsModifiedExceptionClass.CLASS_NAME);

        listLine.add("if (result == 0) {");
        listLine.add("throw new " + NoRowModifiedExceptionClass.CLASS_NAME
                + "(\"データベースの処理の結果、1行もデータが変更されませんでした。\");");
        listLine.add("} else if (result > 1) {");
        listLine
                .add("String message = \"データベースの処理の結果、1行を超えるデータが変更されました。変更件数:\" + result;");
        listLine.add("throw new "
                + TooManyRowsModifiedExceptionClass.CLASS_NAME + "(message);");
        listLine.add("}");
    }
}