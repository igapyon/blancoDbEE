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
import blanco.commons.util.BlancoNameUtil;
import blanco.db.common.expander.BlancoDbAbstractMethod;
import blanco.db.common.stringgroup.BlancoDbLoggingModeStringGroup;
import blanco.db.common.util.BlancoDbUtil;
import blanco.db.common.valueobject.BlancoDbSetting;
import blanco.db.common.valueobject.BlancoDbSqlInfoStructure;
import blanco.db.expander.exception.NoRowFoundExceptionClass;
import blanco.db.expander.exception.TooManyRowsFoundExceptionClass;
import blanco.db.util.BlancoDbCgUtilJava;

/**
 * 個別のメソッドを展開するためのクラス。
 * 
 * シングル属性がtrueの場合にのみ、このクラスは利用されます
 * 
 * @author Tosiki Iga
 */
public class GetSingleRowMethod extends BlancoDbAbstractMethod {
    public GetSingleRowMethod(final BlancoDbSetting argDbSetting,
            final BlancoDbSqlInfoStructure argSqlInfo,
            final BlancoCgObjectFactory argCgFactory,
            final BlancoCgSourceFile argCgSourceFile,
            final BlancoCgClass argCgClass) {
        super(argDbSetting, argSqlInfo, argCgFactory, argCgSourceFile,
                argCgClass);
    }

    public void expand() {
        final BlancoCgMethod cgMethod = fCgFactory.createMethod("getSingleRow",
                "現在の行のデータをオブジェクトとして取得します。");
        fCgClass.getMethodList().add(cgMethod);

        // 行オブジェクトの型名を取得します。
        final String rowObjectType = BlancoDbUtil.getBasePackage(fSqlInfo,
                fDbSetting)
                + ".row."
                + BlancoNameAdjuster.toClassName(fSqlInfo.getName())
                + "Row";

        cgMethod.setReturn(fCgFactory.createReturn(rowObjectType, "行オブジェクト。"));

        cgMethod.getThrowList().add(
                fCgFactory.createException(BlancoDbUtil
                        .getRuntimePackage(fDbSetting)
                        + ".exception.NoRowFoundException",
                        "データベースの処理の結果、1行もデータが検索されなかった場合。"));
        cgMethod.getThrowList().add(
                fCgFactory.createException(BlancoDbUtil
                        .getRuntimePackage(fDbSetting)
                        + ".exception.TooManyRowsFoundException",
                        "データベースの処理の結果、1行を超えるデータが検索されてしまった場合。"));

        BlancoDbCgUtilJava.addExceptionToMethodSqlException(fCgFactory,
                cgMethod);

        final List<String> listDesc = cgMethod.getLangDoc()
                .getDescriptionList();

        listDesc.add("SQL文の実行結果が1行であることを確認します。実行結果が1行以外である場合には例外を発生させます。<br>");
        listDesc.add("シングル属性が有効となっているので生成されます。<br>");

        final List<String> listLine = cgMethod.getLineList();

        if (fDbSetting.getLogging()) {
            switch (fDbSetting.getLoggingMode()) {
            case BlancoDbLoggingModeStringGroup.DEBUG:
                BlancoDbCgUtilJava.addBeginLogToMethod(cgMethod);
            }
        }

        fCgSourceFile.getImportList().add(
                BlancoDbUtil.getRuntimePackage(fDbSetting) + ".exception."
                        + NoRowFoundExceptionClass.CLASS_NAME);

        listLine.add("if (next() == false) {");
        listLine
                .add("throw new NoRowFoundException(\"データベースの処理の結果、1行もデータが検索されませんでした。\");");
        listLine.add("}");
        listLine.add("");

        listLine.add(BlancoNameUtil.trimJavaPackage(rowObjectType)
                + " result = getRow();");
        listLine.add("");

        fCgSourceFile.getImportList().add(
                BlancoDbUtil.getRuntimePackage(fDbSetting) + ".exception."
                        + TooManyRowsFoundExceptionClass.CLASS_NAME);

        // 1行を超えて変更があったかどうかをチェック。
        listLine.add("if (next()) {");
        listLine
                .add("throw new TooManyRowsFoundException(\"データベースの処理の結果、1行を超えるデータが検索されました。\");");
        listLine.add("}");
        listLine.add("");

        listLine.add("return result;");
    }
}