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
public class ExecuteQueryMethod extends BlancoDbAbstractMethod {
    public ExecuteQueryMethod(final BlancoDbSetting argDbSetting,
            final BlancoDbSqlInfoStructure argSqlInfo,
            final BlancoCgObjectFactory argCgFactory,
            final BlancoCgSourceFile argCgSourceFile,
            final BlancoCgClass argCgClass) {
        super(argDbSetting, argSqlInfo, argCgFactory, argCgSourceFile,
                argCgClass);
    }

    public void expand() {
        final BlancoCgMethod cgMethod = fCgFactory.createMethod("executeQuery",
                null);
        fCgClass.getMethodList().add(cgMethod);

        BlancoDbCgUtilJava.addExceptionToMethodDeadlockTimeoutException(
                fCgFactory, cgMethod, fDbSetting);
        BlancoDbCgUtilJava.addExceptionToMethodSqlException(fCgFactory,
                cgMethod);

        cgMethod.getLangDoc().getDescriptionList().add("検索型クエリを実行します。<br>");

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
                .add("// PreparedStatementが未取得の状態なので、PreparedStatement.executeQuery()実行に先立ちprepareStatement()メソッドを呼び出して取得します。");
        listLine.add("prepareStatement();");
        listLine.add("}");

        // resultSetがあいた状態であれば、先にcloseを行います。
        listLine.add("if (fResultSet != null) {");
        listLine.add("// 前回の結果セット(ResultSet)が残っているので、これを一旦開放します。");
        listLine.add("fResultSet.close();");
        listLine.add("fResultSet = null;");
        listLine.add("}");

        listLine.add("");

        if(fDbSetting.getLoggingsql()) {
        	// 標準出力に出力します。 
			listLine.add("System.out.println(\"SQL: ["
					+ fSqlInfo.getName()
					+ "](Iterator) "
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
				listLine.add("final long usedMemoryStart = BlancoDbUtil.getUsedMemory(runtime);");
				listLine.add("final long startTime = System.currentTimeMillis();");
				listLine.add("fLog.info(\"" + fSqlInfo.getName() + "開始\");");
				listLine.add("");
				break;
			}
		}

		listLine.add("try {");
		listLine.add("fResultSet = fStatement.executeQuery();");
		listLine.add("} catch (SQLException ex) {");
		listLine.add("throw BlancoDbUtil.convertToBlancoException(ex);");

		if (fDbSetting.getLogging()) {
			switch (fDbSetting.getLoggingMode()) {
			case BlancoDbLoggingModeStringGroup.PERFORMANCE:
			case BlancoDbLoggingModeStringGroup.SQLID:
				listLine.add("} finally {");
				listLine.add("final long endTime = System.currentTimeMillis();");
				listLine.add("final long usedMemoryEnd = BlancoDbUtil.getUsedMemory(runtime);");
				listLine.add("fLog.info(\""
						+ fSqlInfo.getName()
						+ "終了 所要時間：\" + BlancoDbUtil.getTimeString(endTime - startTime) + \" 終了時使用メモリ：\" + BlancoDbUtil.getMemorySizeString(usedMemoryEnd) + \" 使用メモリ差分：\" + BlancoDbUtil.getMemorySizeString(usedMemoryEnd - usedMemoryStart));");
				break;
			}
		}

		listLine.add("}");
	}
}