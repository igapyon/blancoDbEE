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
public class LogField extends BlancoDbAbstractField {
    /**
     * QueryクラスのfLogフィールドのコンストラクタです。
     * 
     * @param bindClassName
     *            ログオブジェクトとして結びつける先のクラス名。
     * @author IGA Tosiki
     */
    public LogField(final BlancoDbSetting argDbSetting,
            final BlancoDbSqlInfoStructure argSqlInfo,
            final BlancoCgObjectFactory argCgFactory,
            final BlancoCgSourceFile argCgSourceFile,
            final BlancoCgClass argCgClass) {
        super(argDbSetting, argSqlInfo, argCgFactory, argCgSourceFile,
                argCgClass);
    }

    public void expand() {
        final BlancoCgField cgField = fCgFactory.createField("fLog",
                "org.apache.commons.logging.Log",
                "このクラスが内部的に利用するロギングのためのオブジェクトオブジェクト。");
        fCgClass.getFieldList().add(cgField);

        cgField.getLangDoc().getDescriptionList().add(
                "このオブジェクトを経由して、このクラスのロギングが実行されます。");
        cgField.setDefault("LogFactory.getLog(" + fCgClass.getName()
                + ".class)");

        cgField.setStatic(true);
        cgField.setFinal(true);

        /*
         * ジェネレーションギャップデザインパターンが利用可能になる目的で、スコープはprotectedとします。
         */
        cgField.setAccess("protected");
    }
}
