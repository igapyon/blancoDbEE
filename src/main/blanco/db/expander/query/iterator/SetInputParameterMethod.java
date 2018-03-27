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
import java.util.Iterator;
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
import blanco.db.util.BlancoDbMappingUtilJava;
import blanco.dbmetadata.BlancoDbMetaDataUtil;
import blanco.dbmetadata.valueobject.BlancoDbMetaDataColumnStructure;

/**
 * 個別のメソッドを展開するためのクラス。
 * 
 * @author Tosiki Iga
 */
public class SetInputParameterMethod extends BlancoDbAbstractMethod {
    private boolean fIsCallableStatement = false;

    public SetInputParameterMethod(final BlancoDbSetting argDbSetting,
            final BlancoDbSqlInfoStructure argSqlInfo,
            final BlancoCgObjectFactory argCgFactory,
            final BlancoCgSourceFile argCgSourceFile,
            final BlancoCgClass argCgClass, final boolean isCallableStatement) {
        super(argDbSetting, argSqlInfo, argCgFactory, argCgSourceFile,
                argCgClass);
        fIsCallableStatement = isCallableStatement;
    }

    public void expand() {
        final BlancoCgMethod cgMethod = fCgFactory.createMethod(
                "setInputParameter", "SQL文に与えるSQL入力パラメータをセットします。");
        fCgClass.getMethodList().add(cgMethod);

        BlancoDbCgUtilJava.addExceptionToMethodSqlException(fCgFactory,
                cgMethod);

        for (int index = 0; index < fSqlInfo.getInParameterList().size(); index++) {
            BlancoDbMetaDataColumnStructure columnStructure = (BlancoDbMetaDataColumnStructure) fSqlInfo
                    .getInParameterList().get(index);

            cgMethod.getParameterList().add(
                    fCgFactory.createParameter(columnStructure.getName(),
                            BlancoDbMappingUtilJava
                                    .getFullClassName(columnStructure), "'"
                                    + columnStructure.getName() + "'列の値"));

            switch (columnStructure.getDataType()) {
            case Types.BINARY:
            case Types.VARBINARY:
            case Types.LONGVARBINARY:
            case Types.BLOB:
            case Types.LONGVARCHAR:
            case Types.CLOB:
                cgMethod.getParameterList().add(
                        fCgFactory.createParameter(columnStructure.getName()
                                + "StreamLength", "int", null));
                break;
            }
        }

        if (fIsCallableStatement == false) {
            cgMethod.getLangDoc().getDescriptionList().add(
                    "内部的には PreparedStatementにSQL入力パラメータをセットします。");
        } else {
            cgMethod.getLangDoc().getDescriptionList().add(
                    "内部的には CallableStatementにSQL入力パラメータをセットします。");
        }

        final List<String> listLine = cgMethod.getLineList();

        if (fDbSetting.getLogging()) {
            switch (fDbSetting.getLoggingMode()) {
            case BlancoDbLoggingModeStringGroup.DEBUG:
                listLine.add("if (fLog.isDebugEnabled()) {");
                String strLine = "fLog.debug(\"" + cgMethod.getName() + ": ";
                boolean isFirst = true;
                for (int index = 0; index < fSqlInfo.getInParameterList()
                        .size(); index++) {
                    if (isFirst) {
                        isFirst = false;
                    } else {
                        strLine += " + \", ";
                    }

                    final BlancoDbMetaDataColumnStructure parameter = (BlancoDbMetaDataColumnStructure) fSqlInfo
                            .getInParameterList().get(index);

                    strLine += parameter.getName() + " = \" + "
                            + parameter.getName();
                }
                strLine += ");";

                listLine.add(strLine);
                listLine.add("}");

                listLine.add("");
                break;
            }
        }

        // statementが未確保であるばあい、強制的にprepareStatementを呼び出します。
        listLine.add("if (fStatement == null) {");
        if (fIsCallableStatement == false) {
            listLine.add("prepareStatement();");
        } else {
            listLine.add("prepareCall();");
        }
        listLine.add("}");

		if (fDbSetting.getLoggingsql()) {
			// ポイント: prepareStatement(); や prepareCall(); のあとで記憶しないのと消え去ってしまいます。そのためここに配置しています。 
			String strLine = "fLogSqlInParam = \"";
			boolean isFirst = true;
			for (int index = 0; index < fSqlInfo.getInParameterList().size(); index++) {
				if (isFirst) {
					isFirst = false;
				} else {
					strLine += ",";
				}

				final BlancoDbMetaDataColumnStructure parameter = (BlancoDbMetaDataColumnStructure) fSqlInfo
						.getInParameterList().get(index);

				strLine += parameter.getName() + "=[\" + "
						+ parameter.getName() + " + \"]";
			}
			strLine += "\";";

			listLine.add(strLine);
		}

        final BlancoDbQueryParserUtil query = new BlancoDbQueryParserUtil(
                fSqlInfo.getQuery());

        final Iterator<BlancoDbMetaDataColumnStructure> ite = fSqlInfo
                .getInParameterList().iterator();
        while (ite.hasNext()) {
            // SQL文からパラメータを発見しています。
            final BlancoDbMetaDataColumnStructure columnStructure = ite.next();

            final int[] listCol = query.getSqlParameters(columnStructure
                    .getName());
            if (listCol == null) {
                throw new IllegalArgumentException("SQL定義ID["
                        + fSqlInfo.getName() + "]の SQL入力パラメータ["
                        + columnStructure.getName() + "]が結びついていせん.");
            }
            for (int iteSame = 0; iteSame < listCol.length; iteSame++) {
                final int index = listCol[iteSame];
                if (BlancoDbMappingUtilJava
                        .getPrimitiveAndNullable(columnStructure)) {
                    listLine.add("if (" + columnStructure.getName()
                            + " == null) {");

                    // 以前、ここに過去のバージョン (1.6.4) のバグをエミュレートするためのコードがありました。
                    // 現在は、この過去バグエミュレート機能は破棄されています。
                    final int jdbcDataType = columnStructure.getDataType();

                    listLine.add("fStatement.setNull("
                            + index
                            + ", "
                            + "java.sql.Types."
                            + BlancoDbMetaDataUtil
                                    .convertJdbcDataTypeToString(jdbcDataType)
                            + ");");
                    listLine.add("} else {");
                }

                final String type = BlancoDbMappingUtilJava
                        .getSetterMethodNameForPreparedStatement(columnStructure);
                switch (columnStructure.getDataType()) {
                case Types.DATE:
                case Types.TIME:
                case Types.TIMESTAMP:
                    fCgSourceFile.getImportList().add("java.sql.Timestamp");
                    break;
                }

                String stmtLine = "fStatement."
                        + type
                        + "("
                        + index
                        + ", "
                        + BlancoDbMappingUtilJava.mapWrapperClassIntoPrimitive(
                                columnStructure, columnStructure.getName());

                switch (columnStructure.getDataType()) {
                case Types.BINARY:
                case Types.VARBINARY:
                case Types.LONGVARBINARY:
                case Types.BLOB:
                case Types.LONGVARCHAR:
                case Types.CLOB:
                    stmtLine += ", " + columnStructure.getName()
                            + "StreamLength";
                    break;
                }

                stmtLine += ")";
                listLine.add(stmtLine + ";");

                if (BlancoDbMappingUtilJava
                        .getPrimitiveAndNullable(columnStructure)) {
                    listLine.add("}");
                }

                switch (fDbSetting.getLoggingMode()) {
                case BlancoDbLoggingModeStringGroup.PERFORMANCE:
                    // SQL入力パラメータ値を番号つきでログ出力。
                    // 番号は 1折人とします。
                    String strLineInfo = "fLog.info(\"" + fSqlInfo.getName()
                            + ": SQL入力パラメータ " + (index) + ": [\" + "
                            + columnStructure.getName() + " + \"]\");";
                    listLine.add(strLineInfo);
                    break;
                }
            }
        }
    }
}