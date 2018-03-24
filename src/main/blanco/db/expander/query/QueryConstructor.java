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
                    fCgClass.getName(), fCgClass.getName() + "クラスのコンストラクタ。");
            fCgClass.getMethodList().add(cgMethod);

            cgMethod.getParameterList().add(
                    fCgFactory.createParameter("conn",
                            "java.sql.Connection", "データベース接続"));

            cgMethod.getLangDoc().getDescriptionList()
                    .add("データベースコネクションオブジェクトを引数としてクエリクラスを作成します。<br>");
            cgMethod.getLangDoc().getDescriptionList()
                    .add("このクラスの利用後は、必ず close()メソッドを呼び出す必要があります。<br>");

            cgMethod.setConstructor(true);

            final List<String> listLine = cgMethod.getLineList();
            listLine.add("fConnection = conn;");
        }

        {
            final BlancoCgMethod cgMethod = fCgFactory.createMethod(
                    fCgClass.getName(), fCgClass.getName() + "クラスのコンストラクタ。");
            fCgClass.getMethodList().add(cgMethod);

            cgMethod.getLangDoc().getDescriptionList()
                    .add("データベースコネクションオブジェクトを与えずにクエリクラスを作成します。<br>");
            cgMethod.getAnnotationList().add("Deprecated");

            cgMethod.setConstructor(true);
        }
        {
            final BlancoCgMethod cgMethod = fCgFactory.createMethod(
                    "setConnection", fCgClass.getName() + "クラスにデータベース接続を設定。");
            fCgClass.getMethodList().add(cgMethod);

            cgMethod.getParameterList().add(
                    fCgFactory.createParameter("conn",
                            "java.sql.Connection", "データベース接続"));

            cgMethod.getAnnotationList().add("Deprecated");

            final List<String> listLine = cgMethod.getLineList();
            listLine.add("fConnection = conn;");
        }
    }
}