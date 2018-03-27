/*
 * blancoDb
 * Copyright (C) 2004-2006 Yasuo Nakanishi
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 */
package blanco.db.expander.query.field;

import blanco.cg.BlancoCgObjectFactory;
import blanco.cg.valueobject.BlancoCgClass;
import blanco.cg.valueobject.BlancoCgField;
import blanco.cg.valueobject.BlancoCgSourceFile;
import blanco.db.common.expander.BlancoDbAbstractField;
import blanco.db.common.valueobject.BlancoDbSetting;
import blanco.db.common.valueobject.BlancoDbSqlInfoStructure;

/**
 * QueryクラスのfStatementフィールドです。
 * 
 * @author IGA Tosiki
 */
public class StatementField extends BlancoDbAbstractField {
    private boolean fIsCallableStatement = false;

    /**
     * QueryクラスのfStatementフィールドのコンストラクタです。
     * 
     * @param className
     *            ステートメントの実際のクラス名。java.sql.PreparedStatementクラスの場合と
     *            java.sql.CallableStatementクラスの場合があります。
     * @author IGA Tosiki
     */
    public StatementField(final BlancoDbSetting argDbSetting,
            final BlancoDbSqlInfoStructure argSqlInfo,
            final BlancoCgObjectFactory argCgFactory,
            final BlancoCgSourceFile argCgSourceFile,
            final BlancoCgClass argCgClass, final boolean argIsCallableStatement) {
        super(argDbSetting, argSqlInfo, argCgFactory, argCgSourceFile,
                argCgClass);
        fIsCallableStatement = argIsCallableStatement;
    }

    public void expand() {
        String statementClassName = "java.sql.PreparedStatement";
        if (fIsCallableStatement) {
            statementClassName = "java.sql.CallableStatement";
        }

        final BlancoCgField cgField = fCgFactory.createField("fStatement",
                statementClassName, "このクラスが内部的に利用するステートメントオブジェクト。");
        fCgClass.getFieldList().add(cgField);

        cgField.getLangDoc().getDescriptionList().add(
                "このオブジェクトはデータベース接続オブジェクトから生成されて内部的に利用されます。<br>");
        cgField.getLangDoc().getDescriptionList().add(
                "closeメソッドの呼び出し時に、このオブジェクトのcloseを実行します。");

        /*
         * ジェネレーションギャップデザインパターンが利用可能になる目的で、スコープはprotectedとします。
         */
        cgField.setAccess("protected");
    }
}
