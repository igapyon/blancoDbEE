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
import blanco.commons.util.BlancoJavaSourceUtil;
import blanco.commons.util.BlancoStringUtil;
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
public class ExecuteUpdateMethod extends BlancoDbAbstractMethod {
    public ExecuteUpdateMethod(final BlancoDbSetting argDbSetting,
            final BlancoDbSqlInfoStructure argSqlInfo,
            final BlancoCgObjectFactory argCgFactory,
            final BlancoCgSourceFile argCgSourceFile,
            final BlancoCgClass argCgClass) {
        super(argDbSetting, argSqlInfo, argCgFactory, argCgSourceFile,
                argCgClass);
    }

    public void expand() {
        final BlancoCgMethod cgMethod = fCgFactory.createMethod(
                "executeUpdate", "SQL文を実行します。");
        fCgClass.getMethodList().add(cgMethod);

        cgMethod.setReturn(fCgFactory.createReturn("int", "処理された行数"));

        /*
         * シングル属性が有効である場合には protectedとします。
         */
        if (fSqlInfo.getSingle()) {
            cgMethod.setAccess("protected");
        } else {
            // publicのままです。
        }

        BlancoDbCgUtilJava.addExceptionToMethodIntegrityConstraintException(
                fCgFactory, cgMethod, fDbSetting);
        BlancoDbCgUtilJava.addExceptionToMethodDeadlockTimeoutException(
                fCgFactory, cgMethod, fDbSetting);
        BlancoDbCgUtilJava.addExceptionToMethodSqlException(fCgFactory,
                cgMethod);

        if (fSqlInfo.getSingle()) {
            cgMethod.getLangDoc().getDescriptionList().add(
                    "シングル属性が有効なのでスコープをprotectedとします。<br>");
            cgMethod.getLangDoc().getDescriptionList().add(
                    "このメソッドの代わりに executeSingleUpdateメソッドを利用してください。<br>");
        }

        final List<String> listLine = cgMethod.getLineList();

        if (fDbSetting.getLogging()) {
            switch (fDbSetting.getLoggingMode()) {
            case BlancoDbLoggingModeStringGroup.DEBUG:
                BlancoDbCgUtilJava.addBeginLogToMethod(cgMethod);
            }
        }

        // statementが未確保であるばあい、強制的にprepareStatementを呼び出します。
        listLine.add("if (fStatement == null) {");
        listLine
                .add("// PreparedStatementが未取得の状態なので、PreparedStatement.executeUpdate()実行に先立ちprepareStatement()メソッドを呼び出して取得します。");
        listLine.add("prepareStatement();");
        listLine.add("}");

        listLine.add("");

        if(fDbSetting.getLoggingsql()) {
        	// 標準出力に出力。 
			listLine.add("System.out.println(\"SQL: ["
					+ fSqlInfo.getName()
					+ "](Invoker) "
					+ BlancoJavaSourceUtil
							.escapeStringAsJavaSource(BlancoStringUtil
									.null2Blank(fSqlInfo.getDescription()))
					+ ": \" + fLogSqlInParam + \": ["
					+ (fSqlInfo.getDynamicSql() == false ? BlancoJavaSourceUtil.escapeStringAsJavaSource(fSqlInfo
							.getQuery().replace('\n', ' ')) : "\" + (\"\" + fLogSqlDynamicSql).replace('\\n', ' ') + \"") + "]\");");
		}

        if (fDbSetting.getLogging()) {
            switch (fDbSetting.getLoggingMode()) {
            case BlancoDbLoggingModeStringGroup.PERFORMANCE:
            case BlancoDbLoggingModeStringGroup.SQLID:
                listLine.add("final Runtime runtime = Runtime.getRuntime();");
                listLine
                        .add("final long usedMemoryStart = BlancoDbUtil.getUsedMemory(runtime);");
                listLine
                        .add("final long startTime = System.currentTimeMillis();");
                listLine.add("fLog.info(\"" + fSqlInfo.getName() + "開始\");");
                break;
            }
            listLine.add("");
        }

        // 例外処理を含めて展開します。
        listLine.add("try {");
        listLine.add("return fStatement.executeUpdate();");
        listLine.add("} catch (SQLException ex) {");
        listLine.add("throw BlancoDbUtil.convertToBlancoException(ex);");

        if (fDbSetting.getLogging()) {
            switch (fDbSetting.getLoggingMode()) {
            case BlancoDbLoggingModeStringGroup.PERFORMANCE:
            case BlancoDbLoggingModeStringGroup.SQLID:
                listLine.add("} finally {");
                listLine
                        .add("final long endTime = System.currentTimeMillis();");
                listLine
                        .add("final long usedMemoryEnd = BlancoDbUtil.getUsedMemory(runtime);");
                listLine
                        .add("fLog.info(\""
                                + fSqlInfo.getName()
                                + "終了 所要時間：\" + BlancoDbUtil.getTimeString(endTime - startTime) + \" 終了時使用メモリ：\" + BlancoDbUtil.getMemorySizeString(usedMemoryEnd) + \" 使用メモリ差分：\" + BlancoDbUtil.getMemorySizeString(usedMemoryEnd - usedMemoryStart));");
                break;
            }
        }

        listLine.add("}");
    }
}