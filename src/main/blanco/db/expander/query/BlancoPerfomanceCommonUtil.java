/*
 * blanco Framework
 * Copyright (C) 2004-2006 IGA Tosiki
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 */
package blanco.db.expander.query;

import blanco.cg.BlancoCgObjectFactory;
import blanco.cg.valueobject.BlancoCgClass;
import blanco.cg.valueobject.BlancoCgField;
import blanco.cg.valueobject.BlancoCgMethod;
import blanco.cg.valueobject.BlancoCgSourceFile;

public class BlancoPerfomanceCommonUtil {
    public static void addPerfomanceFieldMethod(
            final BlancoCgObjectFactory cgFactory,
            final BlancoCgSourceFile cgSourceFile, final BlancoCgClass cgClass) {
        {
            final BlancoCgField cgField = cgFactory.createField(
                    "fPerfomanceNumberFormat", "java.text.NumberFormat",
                    "パフォーマンス計測時にのみ利用する数値フォーマッタ。");
            cgClass.getFieldList().add(cgField);
            cgField.setFinal(true);
            cgField.setStatic(true);
            cgField.setDefault("NumberFormat.getInstance()");
        }

        {
            final BlancoCgMethod cgMethod = cgFactory.createMethod(
                    "getUsedMemory", "パフォーマンス計測時にのみ利用する消費メモリ取得メソッド。");
            cgClass.getMethodList().add(cgMethod);
            cgMethod.setStatic(true);
            cgMethod.getParameterList().add(
                    cgFactory.createParameter("runtime", "java.lang.Runtime",
                            "ランタイムのインスタンス。"));
            cgMethod.setReturn(cgFactory.createReturn("long", "メモリ消費量。"));
            cgMethod.getLineList().add(
                    "return runtime.totalMemory() - runtime.freeMemory();");
        }

        {
            final BlancoCgMethod cgMethod = cgFactory.createMethod(
                    "getMemorySizeString", "パフォーマンス計測時にのみ利用するメモリサイズ文字列取得メソッド。");
            cgClass.getMethodList().add(cgMethod);
            cgMethod.setStatic(true);
            cgMethod.getParameterList().add(
                    cgFactory.createParameter("memorySize", "long", "メモリサイズ。"));
            cgMethod.setReturn(cgFactory.createReturn("java.lang.String",
                    "メモリサイズの文字列表現。"));
            cgMethod.getLineList().add(
                    "final StringBuffer result = new StringBuffer();");
            cgMethod.getLineList().add(
                    "synchronized (fPerfomanceNumberFormat) {");
            cgMethod
                    .getLineList()
                    .add(
                            "result.append(fPerfomanceNumberFormat.format(memorySize / 1024));");
            cgMethod.getLineList().add("}");
            cgMethod.getLineList().add("result.append(\"(KB)\");");
            cgMethod.getLineList().add("return result.toString();");
        }

        {
            final BlancoCgMethod cgMethod = cgFactory.createMethod(
                    "getTimeString", "パフォーマンス計測時にのみ利用する消費ミリ秒文字列取得メソッド。");
            cgClass.getMethodList().add(cgMethod);
            cgMethod.setStatic(true);
            cgMethod.getParameterList().add(
                    cgFactory.createParameter("time", "long", "消費ミリ秒。"));
            cgMethod.setReturn(cgFactory.createReturn("java.lang.String",
                    "消費ミリ秒の文字列表現。"));
            cgMethod.getLineList().add(
                    "final StringBuffer result = new StringBuffer();");
            cgMethod.getLineList().add("result.append(time);");
            cgMethod.getLineList().add("result.append(\"(ms)\");");
            cgMethod.getLineList().add("return result.toString();");
        }
    }
}
