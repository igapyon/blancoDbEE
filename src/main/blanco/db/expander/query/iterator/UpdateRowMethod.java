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
 * @author Tosiki Iga
 */
public class UpdateRowMethod extends BlancoDbAbstractMethod {
    public UpdateRowMethod(final BlancoDbSetting argDbSetting,
            final BlancoDbSqlInfoStructure argSqlInfo,
            final BlancoCgObjectFactory argCgFactory,
            final BlancoCgSourceFile argCgSourceFile,
            final BlancoCgClass argCgClass) {
        super(argDbSetting, argSqlInfo, argCgFactory, argCgSourceFile,
                argCgClass);
    }

    /**
     * コメント <br>
     * 
     * PostgreSQLにおいて FOR UPDATEカーソルでupdateRowを呼び出した際に 制約違反の際には、SQLState[23505],
     * ErrorCode [0] が発生します。 <br>
     * java.sql.SQLException: ERROR: duplicate key violates unique constraint
     * "ract007_ketsugo_model_pkey" at
     * org.postgresql.core.v3.QueryExecutorImpl.receiveErrorResponse
     * (QueryExecutorImpl.java:1471)
     */
    public void expand() {
        final BlancoCgMethod cgMethod = fCgFactory.createMethod("updateRow",
                "更新後の現在の行の値をもちいてデータベースを更新します。");
        fCgClass.getMethodList().add(cgMethod);

        BlancoDbCgUtilJava.addExceptionToMethodIntegrityConstraintException(
                fCgFactory, cgMethod, fDbSetting);
        BlancoDbCgUtilJava.addExceptionToMethodDeadlockTimeoutException(
                fCgFactory, cgMethod, fDbSetting);
        BlancoDbCgUtilJava.addExceptionToMethodSqlException(fCgFactory,
                cgMethod);

        cgMethod.getLangDoc().getDescriptionList().add(
                "更新可能属性が有効となっているので生成されます。<br>");

        final List<String> listLine = cgMethod.getLineList();

        if (fDbSetting.getLogging()) {
            switch (fDbSetting.getLoggingMode()) {
            case BlancoDbLoggingModeStringGroup.DEBUG:
                BlancoDbCgUtilJava.addBeginLogToMethod(cgMethod);
            }
        }

        // べったりと展開します。
        listLine.add("try{");
        listLine.add("fResultSet.updateRow();");
        listLine.add("} catch (SQLException ex) {");
        listLine
                .add("if (ex.getSQLState() != null && ex.getSQLState().startsWith(\"23\")) {");
        listLine
                .add("final IntegrityConstraintException exBlanco = new IntegrityConstraintException(\"制約違反により変更に失敗しました。:\" + ex.toString(), ex.getSQLState(), ex.getErrorCode());");
        listLine.add("exBlanco.initCause(ex);");
        listLine.add("throw exBlanco;");
        listLine.add("}");
        listLine.add("throw ex;");
        listLine.add("}");
    }
}