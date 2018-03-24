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
import blanco.db.common.valueobject.BlancoDbSetting;
import blanco.db.common.valueobject.BlancoDbSqlInfoStructure;

/**
 * �ʂ̃��\�b�h��W�J���邽�߂̃N���X�B
 * 
 * @author Yasuo Nakanishi
 */
public class QueryConstructor extends BlancoDbAbstractMethod {
    public QueryConstructor(final BlancoDbSetting argDbSetting,
            final BlancoDbSqlInfoStructure argSqlInfo,
            final BlancoCgObjectFactory argCgFactory,
            final BlancoCgSourceFile argCgSourceFile,
            final BlancoCgClass argCgClass) {
        super(argDbSetting, argSqlInfo, argCgFactory, argCgSourceFile,
                argCgClass);
    }

    public void expand() {
        {
            final BlancoCgMethod cgMethod = fCgFactory.createMethod(
                    fCgClass.getName(), fCgClass.getName() + "�N���X�̃R���X�g���N�^�B");
            fCgClass.getMethodList().add(cgMethod);

            cgMethod.getParameterList().add(
                    fCgFactory.createParameter("conn",
                            "java.sql.Connection", "�f�[�^�x�[�X�ڑ�"));

            cgMethod.getLangDoc().getDescriptionList()
                    .add("�f�[�^�x�[�X�R�l�N�V�����I�u�W�F�N�g�������Ƃ��ăN�G���N���X���쐬���܂��B<br>");
            cgMethod.getLangDoc().getDescriptionList()
                    .add("���̃N���X�̗��p��́A�K�� close()���\�b�h���Ăяo���K�v������܂��B<br>");

            cgMethod.setConstructor(true);

            final List<String> listLine = cgMethod.getLineList();
            listLine.add("fConnection = conn;");
        }

        {
            final BlancoCgMethod cgMethod = fCgFactory.createMethod(
                    fCgClass.getName(), fCgClass.getName() + "�N���X�̃R���X�g���N�^�B");
            fCgClass.getMethodList().add(cgMethod);

            cgMethod.getLangDoc().getDescriptionList()
                    .add("�f�[�^�x�[�X�R�l�N�V�����I�u�W�F�N�g��^�����ɃN�G���N���X���쐬���܂��B<br>");
            cgMethod.getAnnotationList().add("Deprecated");

            cgMethod.setConstructor(true);
        }
        {
            final BlancoCgMethod cgMethod = fCgFactory.createMethod(
                    "setConnection", fCgClass.getName() + "�N���X�Ƀf�[�^�x�[�X�ڑ���ݒ�B");
            fCgClass.getMethodList().add(cgMethod);

            cgMethod.getParameterList().add(
                    fCgFactory.createParameter("conn",
                            "java.sql.Connection", "�f�[�^�x�[�X�ڑ�"));

            cgMethod.getAnnotationList().add("Deprecated");

            final List<String> listLine = cgMethod.getLineList();
            listLine.add("fConnection = conn;");
        }
    }
}