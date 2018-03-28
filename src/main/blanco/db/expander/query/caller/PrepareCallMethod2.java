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
import blanco.db.common.expander.BlancoDbAbstractMethod;
import blanco.db.common.stringgroup.BlancoDbLoggingModeStringGroup;
import blanco.db.common.util.BlancoDbQueryParserUtil;
import blanco.db.common.valueobject.BlancoDbSetting;
import blanco.db.common.valueobject.BlancoDbSqlInfoStructure;
import blanco.db.util.BlancoDbCgUtilJava;
import blanco.dbmetadata.BlancoDbMetaDataUtil;
import blanco.dbmetadata.valueobject.BlancoDbMetaDataColumnStructure;

/**
 * 個別のメソッドを展開するためのクラス。
 * 
 * @author tosiki iga
 */
public class PrepareCallMethod2 extends BlancoDbAbstractMethod {
    public PrepareCallMethod2(final BlancoDbSetting argDbSetting,
            final BlancoDbSqlInfoStructure argSqlInfo,
            final BlancoCgObjectFactory argCgFactory,
            final BlancoCgSourceFile argCgSourceFile,
            final BlancoCgClass argCgClass) {
        super(argDbSetting, argSqlInfo, argCgFactory, argCgSourceFile,
                argCgClass);
    }

    public void expand() {
        final BlancoCgMethod cgMethod = fCgFactory.createMethod("prepareCall",
                "与えられたSQL文をもちいてプリコンパイルを実施(動的SQL)します。");
        fCgClass.getMethodList().add(cgMethod);

        cgMethod
                .getParameterList()
                .add(
                        fCgFactory
                                .createParameter("query", "java.lang.String",
                                        "プリコンパイルを実施させたいSQL文。動的SQLの場合には、この引数には加工された後の実行可能なSQL文を与えます。"));

        BlancoDbCgUtilJava.addExceptionToMethodSqlException(fCgFactory,
                cgMethod);

        final List<String> listDesc = cgMethod.getLangDoc()
                .getDescriptionList();
        listDesc.add("動的に内容が変化するようなSQLを実行する必要がある場合にのみ、こちらのメソッドを利用します。<br>");
        listDesc
                .add("そうではない場合には、このメソッドの利用は避けて prepareCall()メソッド (引数なし)を呼び出してください。<br>");
        listDesc
                .add("なぜなら、このメソッドではSQL文そのものをパラメータとして与えることができて自由度が高い一方、SQLインジェクションと呼ばれるセキュリティホールが発生する可能性を引き起こしてしまうからです。<br>");
        listDesc.add("内部的にConnection.prepareCallを呼び出します。<br>");

        final List<String> listLine = cgMethod.getLineList();

        if (fDbSetting.getLogging()) {
            switch (fDbSetting.getLoggingMode()) {
            case BlancoDbLoggingModeStringGroup.DEBUG:
                listLine.add("if (fLog.isDebugEnabled()) {");
                listLine.add("fLog.debug(\"" + cgMethod.getName()
                        + ": query = \" + query);");
                listLine.add("}");
                break;
            case BlancoDbLoggingModeStringGroup.PERFORMANCE:
                listLine.add("fLog.info(\"" + cgMethod.getName()
                        + ": query = \" + query);");
                break;
            }
            listLine.add("");
        }

        listLine.add("close();");

        listLine.add("fStatement = fConnection.prepareCall(query);");

        final BlancoDbQueryParserUtil query = new BlancoDbQueryParserUtil(
                fSqlInfo.getQuery());

        for (int indexParameter = 0; indexParameter < fSqlInfo
                .getOutParameterList().size(); indexParameter++) {
            // 現状、とりあえず与えられた順序で登場と仮定が加わっています。
            final BlancoDbMetaDataColumnStructure columnStructure = (BlancoDbMetaDataColumnStructure) fSqlInfo
                    .getOutParameterList().get(indexParameter);

            final int[] listCol = query.getSqlParameters(columnStructure
                    .getName());
            if (listCol == null) {
                System.out.println("[" + fSqlInfo.getName() + "]の SQL出力パラメータ["
                        + columnStructure.getName() + "]が結びついていません.");
                continue;
            }
            for (int iteSame = 0; iteSame < listCol.length; iteSame++) {
                final int index = listCol[iteSame];

                String stmtLine = "fStatement.registerOutParameter("
                        + index
                        + ", java.sql.Types."
                        + BlancoDbMetaDataUtil
                                .convertJdbcDataTypeToString(columnStructure
                                        .getDataType());
                stmtLine += ");";
                listLine.add(stmtLine);
            }
        }
    }
}