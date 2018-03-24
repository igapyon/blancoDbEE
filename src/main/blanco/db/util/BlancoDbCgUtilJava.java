/*
 * blancoDb
 * Copyright (C) 2004-2006 Yasuo Nakanishi
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 */
package blanco.db.util;

import java.util.List;

import blanco.cg.BlancoCgObjectFactory;
import blanco.cg.valueobject.BlancoCgMethod;
import blanco.db.common.util.BlancoDbUtil;
import blanco.db.common.valueobject.BlancoDbSetting;

/**
 * blancoDbにおいて blancoCgに関するユーティリティを集めたクラス。
 * 
 * 特に良くある組み合わせについて、この場所で一括して処理します。
 * 
 * @author ToshikiIga
 */
public class BlancoDbCgUtilJava {
    /**
     * メソッドに SQL例外のスローを追加します。
     * 
     * @param cgFactory
     * @param cgMethod
     */
    public static void addExceptionToMethodSqlException(
            final BlancoCgObjectFactory cgFactory, final BlancoCgMethod cgMethod) {
        cgMethod.getThrowList().add(
                cgFactory.createException("java.sql.SQLException",
                        "SQL例外が発生した場合。"));
    }

    /**
     * メソッドに デッドロックとタイムアウトのスローを追加します。
     * 
     * @param cgFactory
     * @param cgMethod
     * @param storage
     */
    public static void addExceptionToMethodDeadlockTimeoutException(
            final BlancoCgObjectFactory cgFactory,
            final BlancoCgMethod cgMethod, final BlancoDbSetting storage) {
        cgMethod.getThrowList().add(
                cgFactory.createException(BlancoDbUtil
                        .getRuntimePackage(storage)
                        + ".exception.DeadlockException",
                        "データベースデッドロックが発生した場合。"));
        cgMethod.getThrowList().add(
                cgFactory
                        .createException(BlancoDbUtil
                                .getRuntimePackage(storage)
                                + ".exception.TimeoutException",
                                "データベースタイムアウトが発生した場合。"));
    }

    /**
     * メソッドに IntegrityConstraintExceptionのスローを追加します。
     * 
     * @param cgFactory
     * @param cgMethod
     * @param storage
     */
    public static void addExceptionToMethodIntegrityConstraintException(
            final BlancoCgObjectFactory cgFactory,
            final BlancoCgMethod cgMethod, final BlancoDbSetting storage) {
        cgMethod.getThrowList().add(
                cgFactory.createException(BlancoDbUtil
                        .getRuntimePackage(storage)
                        + ".exception.IntegrityConstraintException",
                        "データベース制約違反が発生した場合。"));
    }

    /**
     * メソッドにメソッド開始の典型的なロギングを追加します。
     * 
     * 典型的ではないログについては、このメソッドは利用せずに個別に実装してください。
     * 
     * @param cgMethod
     */
    public static void addBeginLogToMethod(final BlancoCgMethod cgMethod) {
        final List<String> listLine = cgMethod.getLineList();

        listLine.add("if (fLog.isDebugEnabled()) {");
        listLine.add("fLog.debug(\"" + cgMethod.getName() + "\");");
        listLine.add("}");
        listLine.add("");
    }
}
