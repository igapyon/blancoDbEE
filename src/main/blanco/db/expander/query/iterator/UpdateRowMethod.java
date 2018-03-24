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
 * �ʂ̃��\�b�h��W�J���邽�߂̃N���X�B
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
     * �R�����g <br>
     * 
     * PostgreSQL�ɂ����� FOR UPDATE�J�[�\����updateRow���Ăяo�����ۂ� ����ᔽ�̍ۂɂ́ASQLState[23505],
     * ErrorCode [0] ���������܂��B <br>
     * java.sql.SQLException: ERROR: duplicate key violates unique constraint
     * "ract007_ketsugo_model_pkey" at
     * org.postgresql.core.v3.QueryExecutorImpl.receiveErrorResponse
     * (QueryExecutorImpl.java:1471)
     */
    public void expand() {
        final BlancoCgMethod cgMethod = fCgFactory.createMethod("updateRow",
                "�X�V��̌��݂̍s�̒l���������ăf�[�^�x�[�X���X�V���܂��B");
        fCgClass.getMethodList().add(cgMethod);

        BlancoDbCgUtilJava.addExceptionToMethodIntegrityConstraintException(
                fCgFactory, cgMethod, fDbSetting);
        BlancoDbCgUtilJava.addExceptionToMethodDeadlockTimeoutException(
                fCgFactory, cgMethod, fDbSetting);
        BlancoDbCgUtilJava.addExceptionToMethodSqlException(fCgFactory,
                cgMethod);

        cgMethod.getLangDoc().getDescriptionList().add(
                "�X�V�\�������L���ƂȂ��Ă���̂Ő�������܂��B<br>");

        final List<String> listLine = cgMethod.getLineList();

        if (fDbSetting.getLogging()) {
            switch (fDbSetting.getLoggingMode()) {
            case BlancoDbLoggingModeStringGroup.DEBUG:
                BlancoDbCgUtilJava.addBeginLogToMethod(cgMethod);
            }
        }

        // �ׂ�����ƓW�J���܂��B
        listLine.add("try{");
        listLine.add("fResultSet.updateRow();");
        listLine.add("} catch (SQLException ex) {");
        listLine
                .add("if (ex.getSQLState() != null && ex.getSQLState().startsWith(\"23\")) {");
        listLine
                .add("final IntegrityConstraintException exBlanco = new IntegrityConstraintException(\"����ᔽ�ɂ��ύX�Ɏ��s���܂����B:\" + ex.toString(), ex.getSQLState(), ex.getErrorCode());");
        listLine.add("exBlanco.initCause(ex);");
        listLine.add("throw exBlanco;");
        listLine.add("}");
        listLine.add("throw ex;");
        listLine.add("}");
    }
}