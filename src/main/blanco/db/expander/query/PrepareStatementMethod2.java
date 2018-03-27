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
import blanco.db.common.stringgroup.BlancoDbLoggingModeStringGroup;
import blanco.db.common.stringgroup.BlancoDbSqlInfoScrollStringGroup;
import blanco.db.common.stringgroup.BlancoDbSqlInfoTypeStringGroup;
import blanco.db.common.valueobject.BlancoDbSetting;
import blanco.db.common.valueobject.BlancoDbSqlInfoStructure;
import blanco.db.util.BlancoDbCgUtilJava;

/**
 * 個別のメソッドを展開するためのクラス。
 * 
 * @author tosiki iga
 */
public class PrepareStatementMethod2 extends BlancoDbAbstractMethod {
    public PrepareStatementMethod2(final BlancoDbSetting argDbSetting,
            final BlancoDbSqlInfoStructure argSqlInfo,
            final BlancoCgObjectFactory argCgFactory,
            final BlancoCgSourceFile argCgSourceFile,
            final BlancoCgClass argCgClass) {
        super(argDbSetting, argSqlInfo, argCgFactory, argCgSourceFile,
                argCgClass);
    }

    public void expand() {
        final BlancoCgMethod cgMethod = fCgFactory.createMethod(
                "prepareStatement", "与えられたSQL文をもちいてプリコンパイルを実施(動的SQL)します。");
        fCgClass.getMethodList().add(cgMethod);

        if (fSqlInfo.getDynamicSql() == false) {
            // 動的 SQL 利用フラグが OFF の場合、動的 SQL のためのこのメソッドは protected 化します。
            cgMethod.setAccess("protected");
        }

        cgMethod.getParameterList()
                .add(fCgFactory
                        .createParameter("query", "java.lang.String",
                                "プリコンパイルを実施させたいSQL文。動的SQLの場合には、この引数には加工された後の実行可能なSQL文を与えます。"));

        BlancoDbCgUtilJava.addExceptionToMethodSqlException(fCgFactory,
                cgMethod);

        final List<String> listDesc = cgMethod.getLangDoc()
                .getDescriptionList();

        listDesc.add("このメソッドは、動的に内容が変化するような SQL を実行する必要がある場合にのみ利用します。<br>");
        if (fSqlInfo.getDynamicSql() == false) {
            listDesc.add("動的 SQL を利用する必要がある場合には、SQL 定義書で「動的SQL」を「使用する」に変更してください。変更後は外部から利用可能になります。<br>");
        } else {
            listDesc.add("SQL 定義書で「動的SQL」が「使用する」に設定されています。<br>");
        }
        listDesc.add("内部的に JDBC ドライバの Connection.prepareStatement を呼び出します。<br>");

        if (fSqlInfo.getType() == BlancoDbSqlInfoTypeStringGroup.ITERATOR) {
            // 検索型の場合にのみ出力します。

            // TODO
            // BlancoDbSqlInfoScrollStringGroup.NOT_DEFINEDの場合には何も出力すべきではないのだが、1.6.4との互換性確保のため
            // スクロール方向をLangDocに出力しています。

            if (fSqlInfo.getScroll() == BlancoDbSqlInfoScrollStringGroup.TYPE_FORWARD_ONLY
                    && fSqlInfo.getUpdatable() == false) {
                // 順方向カーソルで且つ更新可能属性がOFFの場合には、何もLangDocに出力しません。
            } else {
                listDesc.add("スクロール属性: "
                        + new BlancoDbSqlInfoScrollStringGroup()
                                .convertToString(fSqlInfo.getScroll()));
                if (fSqlInfo.getUpdatable()) {
                    listDesc.add("更新可能属性: 有効");
                }
            }
        }

        final List<String> listLine = cgMethod.getLineList();

        if(fDbSetting.getLoggingsql()) {
        	// 標準出力に出力。 
			listLine.add("fLogSqlInParam = \"\";");

			if (fSqlInfo.getDynamicSql()) {
				listLine.add("fLogSqlDynamicSql = query;");
			}
		}
        
        if (fDbSetting.getLogging()) {
            switch (fDbSetting.getLoggingMode()) {
            case BlancoDbLoggingModeStringGroup.DEBUG:
                listLine.add("if (fLog.isDebugEnabled()) {");
                listLine.add("fLog.debug(\"" + cgMethod.getName()
                        + ": query = \" + query);");
                listLine.add("}");
                break;
            case BlancoDbLoggingModeStringGroup.PERFORMANCE:
                listLine.add("fLog.info(\"" + fSqlInfo.getName()
                        + "実行SQL\\n\" + query);");
                break;
            }
            listLine.add("");
        }

        listLine.add("close();");

        // TODO スクロール方向が無指定の場合 JDBC APIもスクロール方向無しで指定しようとしたが、その仕様だと
        // 1.6.4と動作が異なってしまいます。
        // TODO 1.6.4との互換性を優先し、スクロール方向指定無しの場合の条件を除去します。

        if (fSqlInfo.getType() == BlancoDbSqlInfoTypeStringGroup.INVOKER
                || fSqlInfo.getType() == BlancoDbSqlInfoTypeStringGroup.CALLER) {
            // 実行型・呼出型の場合には、単にprepareStatementを呼び出します。
            listLine.add("fStatement = fConnection.prepareStatement(query);");
        } else if (fSqlInfo.getScroll() == BlancoDbSqlInfoScrollStringGroup.TYPE_FORWARD_ONLY
                && fSqlInfo.getUpdatable() == false) {
            // 検索型のうち、パラメータのバリエーションが単純な場合には、単純にprepareStatementを呼び出します。
            listLine.add("fStatement = fConnection.prepareStatement(query);");
        } else {
            // バリエーションの内容に合わせて引数を生成します。
            // 検索型の BlancoDbSqlInfoScrollStringGroup.NOT_DEFINED についても
            // ここを通過する点に注意してください。これは 1.6.4との互換性のために必要です。

            String resultSetType = "ResultSet.TYPE_FORWARD_ONLY";
            String resultSetConcurrency = "ResultSet.CONCUR_READ_ONLY";
            if (fSqlInfo.getScroll() == BlancoDbSqlInfoScrollStringGroup.TYPE_SCROLL_INSENSITIVE) {
                resultSetType = "ResultSet.TYPE_SCROLL_INSENSITIVE";
            } else if (fSqlInfo.getScroll() == BlancoDbSqlInfoScrollStringGroup.TYPE_SCROLL_SENSITIVE) {
                resultSetType = "ResultSet.TYPE_SCROLL_SENSITIVE";
            }
            if (fSqlInfo.getUpdatable()) {
                resultSetConcurrency = "ResultSet.CONCUR_UPDATABLE";
            }
            listLine.add("fStatement = fConnection.prepareStatement(query, "
                    + resultSetType + ", " + resultSetConcurrency + ");");
        }

        if (fDbSetting.getStatementTimeout() >= 0) {
            listLine.add("// ステートメントタイムアウト値についてデフォルト値をセットします。");
            listLine.add("fStatement.setQueryTimeout("
                    + fDbSetting.getStatementTimeout() + ");");
        }
    }
}