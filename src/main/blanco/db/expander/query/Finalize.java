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
 * 個別のメソッドを展開するためのクラス。
 * 
 * @author Yasuo Nakanishi
 */
public class Finalize extends BlancoDbAbstractMethod {
    public Finalize(final BlancoDbSetting argDbSetting,
            final BlancoDbSqlInfoStructure argSqlInfo,
            final BlancoCgObjectFactory argCgFactory,
            final BlancoCgSourceFile argCgSourceFile,
            final BlancoCgClass argCgClass) {
        super(argDbSetting, argSqlInfo, argCgFactory, argCgSourceFile,
                argCgClass);
    }

    public void expand() {
        final BlancoCgMethod cgMethod = fCgFactory.createMethod("finalize",
                "finalizeメソッド。");
        fCgClass.getMethodList().add(cgMethod);

        cgMethod.setAccess("protected");

        cgMethod.getThrowList().add(
                fCgFactory.createException("java.lang.Throwable",
                        "finalize処理の中で発生した例外。"));

        cgMethod
                .getLangDoc()
                .getDescriptionList()
                .add(
                        "このクラスが内部的に生成したオブジェクトのなかで、close()呼び出し忘れバグが存在するかどうかチェックします。<br>");

        final List<String> listLine = cgMethod.getLineList();

        listLine.add("super.finalize();");
        listLine.add("if (fStatement != null) {");
        listLine.add("final String message = \"" + fCgClass.getName()
                + " : close()メソッドによるリソースの開放が行われていません。\";");
        listLine.add("System.out.println(message);");

        if (fDbSetting.getLogging()) {
            listLine.add("");
            listLine.add("fLog.error(\"" + cgMethod.getName()
                    + ": \" + message);");
        }

        listLine.add("}");
    }
}