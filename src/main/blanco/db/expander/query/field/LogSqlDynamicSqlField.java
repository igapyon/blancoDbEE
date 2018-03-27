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
 * QueryクラスのfLogフィールドです。
 * 
 * @author IGA Tosiki
 */
public class LogSqlDynamicSqlField extends BlancoDbAbstractField {
    /**
     * Queryクラスの 動的 SQL 用のフィールド
     * 
     * @param bindClassName
     *            ログオブジェクトとして結びつける先のクラス名。
     * @author IGA Tosiki
     */
    public LogSqlDynamicSqlField(final BlancoDbSetting argDbSetting,
            final BlancoDbSqlInfoStructure argSqlInfo,
            final BlancoCgObjectFactory argCgFactory,
            final BlancoCgSourceFile argCgSourceFile,
            final BlancoCgClass argCgClass) {
        super(argDbSetting, argSqlInfo, argCgFactory, argCgSourceFile,
                argCgClass);
    }

    public void expand() {
        final BlancoCgField cgField = fCgFactory.createField("fLogSqlDynamicSql",
                "java.lang.String",
                "このクラスが内部的に利用するロギングのためのSQL文字列。動的SQLの場合のみ生成。");
        fCgClass.getFieldList().add(cgField);

        cgField.getLangDoc().getDescriptionList().add(
                "動的SQLを蓄えます。");

        /*
         * ジェネレーションギャップデザインパターンが利用可能になる目的で、スコープはprotectedとします。
         */
        cgField.setAccess("protected");
        cgField.setDefault("\"\"");
    }
}
