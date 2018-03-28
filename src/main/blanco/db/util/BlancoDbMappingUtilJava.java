/*
 * blancoDb Enterprise Edition Copyright (C) 2004-2005 Tosiki Iga
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 */
package blanco.db.util;

import java.sql.ResultSetMetaData;
import java.sql.Types;

import blanco.commons.util.BlancoNameUtil;
import blanco.dbmetadata.BlancoDbMetaDataUtil;
import blanco.dbmetadata.valueobject.BlancoDbMetaDataColumnStructure;

/**
 * blancoDbの型マッピングに関するメソッドを集めたクラス。
 * 
 * @author ToshikiIga
 */
public final class BlancoDbMappingUtilJava {
    /**
     * 列構造体からJava言語におけるフルクラス名を取得します。
     * 
     * この処理は、プログラミング言語ごとに異なるものです。 Typesを Java言語の何の型にマッピングするのか、という重要な処理にあたります。
     * 
     * @param columnStructure
     * @return
     */
    public static final String getFullClassName(
            final BlancoDbMetaDataColumnStructure columnStructure) {
        final boolean isNoNulls = (columnStructure.getNullable() == ResultSetMetaData.columnNoNulls);
        switch (columnStructure.getDataType()) {
        case Types.BIT:
        case Types.BOOLEAN:
            if (isNoNulls) {
                return "boolean";
            } else {
                return "java.lang.Boolean";
            }
        case Types.TINYINT:
            if (isNoNulls) {
                return "byte";
            } else {
                return "java.lang.Byte";
            }
        case Types.SMALLINT:
            if (isNoNulls) {
                return "short";
            } else {
                return "java.lang.Short";
            }
        case Types.INTEGER:
            if (isNoNulls) {
                return "int";
            } else {
                return "java.lang.Integer";
            }
        case Types.BIGINT:
            if (isNoNulls) {
                return "long";
            } else {
                return "java.lang.Long";
            }
        case Types.REAL:
            if (isNoNulls) {
                return "float";
            } else {
                return "java.lang.Float";
            }
        case Types.FLOAT:
        case Types.DOUBLE:
            if (isNoNulls) {
                return "double";
            } else {
                return "java.lang.Double";
            }
        case Types.NUMERIC:
        case Types.DECIMAL:
            return "java.math.BigDecimal";
        case Types.CHAR:
        case Types.VARCHAR:
        case Types.NCHAR:
        case Types.NVARCHAR:
            return "java.lang.String";
        case Types.DATE:
            // 仮で TIMESTAMPと同じ動きをさせています。
        case Types.TIME:
            // 仮で TIMESTAMPと同じ動きをさせています。
        case Types.TIMESTAMP:
            return "java.util.Date";
        case Types.BINARY:
        case Types.VARBINARY:
        case Types.LONGVARBINARY:
        case Types.BLOB:
            return "java.io.InputStream";
        case Types.LONGVARCHAR:
        case Types.LONGNVARCHAR:
        case Types.CLOB:
        case Types.NCLOB:
            return "java.io.Reader";
        case Types.JAVA_OBJECT:
        case Types.DISTINCT:
        case Types.STRUCT:
        case Types.ARRAY:
        case Types.NULL:
        case Types.OTHER:
        case Types.REF:
        case Types.DATALINK:
        case -101:
            // Oracle の SYSTIMESTAMP 型のための、workaround
            if("SYSTIMESTAMP".equals(columnStructure.getName())){
                return "java.util.Date";
            }
        default:
            throw new IllegalArgumentException("BlancoDbTableMeta2Xml: 列パラメータ["
                    + columnStructure.getName()
                    + "]("
                    + BlancoDbMetaDataUtil
                            .convertJdbcDataTypeToString(columnStructure
                                    .getDataType())
                    + ")のバインド: 処理できないSQL型("
                    + columnStructure.getDataType()
                    + "/"
                    + BlancoDbMetaDataUtil
                            .convertJdbcDataTypeToString(columnStructure
                                    .getDataType()) + ")が指定されました。");
        }
    }

    /**
     * 列構造体からJava言語におけるクラス名を取得します。
     * 
     * @param columnStructure
     * @return
     */
    public static final String getClassName(
            final BlancoDbMetaDataColumnStructure columnStructure) {
        return BlancoNameUtil.trimJavaPackage(BlancoDbMappingUtilJava
                .getFullClassName(columnStructure));
    }

    /**
     * プリミティブでしかもNULLをサポートすべきものかどうかを判断します。
     * 
     * プリミティブ型ではnullが表現できない型がありますので、その型に該当するかどうか判定をおこないます。
     * 
     * @param columnStructure
     * @return
     */
    public static boolean getPrimitiveAndNullable(
            final BlancoDbMetaDataColumnStructure columnStructure) {
        final boolean isNoNulls = !(columnStructure.getNullable() == ResultSetMetaData.columnNullable);

        switch (columnStructure.getDataType()) {
        case Types.BIT:
        case Types.BOOLEAN:
            if (isNoNulls) {
                return false;
            } else {
                return true;
            }
        case Types.TINYINT:
            if (isNoNulls) {
                return false;
            } else {
                return true;
            }
        case Types.SMALLINT:
            if (isNoNulls) {
                return false;
            } else {
                return true;
            }
        case Types.INTEGER:
            if (isNoNulls) {
                return false;
            } else {
                return true;
            }
        case Types.BIGINT:
            if (isNoNulls) {
                return false;
            } else {
                return true;
            }
        case Types.REAL:
            if (isNoNulls) {
                return false;
            } else {
                return true;
            }
        case Types.FLOAT:
        case Types.DOUBLE:
            if (isNoNulls) {
                return false;
            } else {
                return true;
            }
        case Types.NUMERIC:
        case Types.DECIMAL:
            return false;
        case Types.CHAR:
        case Types.VARCHAR:
            return false;
        case Types.DATE:
            // TIMESTAMPと同じ動きをさせています。
        case Types.TIME:
            // TIMESTAMPと同じ動きをさせています。
        case Types.TIMESTAMP:
            // 特別な動き。DATE, TIME, TIMESTAMP については、プリミティブ型 + NULL許容の際と同じ挙動をさせています。
            return true;
        case Types.BINARY:
        case Types.VARBINARY:
        case Types.LONGVARBINARY:
        case Types.BLOB:
            return false;
        case Types.LONGVARCHAR:
        case Types.CLOB:
            return false;
        case Types.JAVA_OBJECT:
        case Types.DISTINCT:
        case Types.STRUCT:
        case Types.ARRAY:
        case Types.NULL:
        case Types.OTHER:
        case Types.REF:
        case Types.DATALINK:
        default:
            return false;
        }
    }

    /**
     * 列情報をもとに、PreparedStatementに対するセッターメソッド名を取得します。
     * 
     * @param columnStructure
     * @return
     */
    public static final String getSetterMethodNameForPreparedStatement(
            final BlancoDbMetaDataColumnStructure columnStructure) {
        return "set" + getGetterSetterBaseMethodName(columnStructure);
    }

    /**
     * 列情報をもとに、ResultSetに対するゲッターメソッド名を取得します。
     * 
     * @param columnStructure
     * @return
     */
    public static final String getGetterMethodNameForResultSet(
            final BlancoDbMetaDataColumnStructure columnStructure) {
        return "get" + getGetterSetterBaseMethodName(columnStructure);
    }

    /**
     * 列情報をもとに、ResultSetに対するupdateメソッド名を取得します。
     * 
     * @param columnStructure
     * @return
     */
    public static final String getUpdateMethodNameForResultSet(
            final BlancoDbMetaDataColumnStructure columnStructure) {
        return "update" + getGetterSetterBaseMethodName(columnStructure);
    }

    /**
     * ゲッターセッターメソッド名のベース名称を取得します。
     * 
     * @param columnStructure
     * @return
     */
    private static final String getGetterSetterBaseMethodName(
            final BlancoDbMetaDataColumnStructure columnStructure) {
        switch (columnStructure.getDataType()) {
        case Types.BIT:
        case Types.BOOLEAN:
            return "Boolean";
        case Types.TINYINT:
            return "Byte";
        case Types.SMALLINT:
            return "Short";
        case Types.INTEGER:
            return "Int";
        case Types.BIGINT:
            return "Long";
        case Types.REAL:
            return "Float";
        case Types.FLOAT:
        case Types.DOUBLE:
            return "Double";
        case Types.NUMERIC:
        case Types.DECIMAL:
            return "BigDecimal";
        case Types.CHAR:
        case Types.VARCHAR:
        case Types.NCHAR:
        case Types.NVARCHAR:
            return "String";
        case Types.DATE:
            // 仮で TIMESTAMPと同じ動きをさせています。
        case Types.TIME:
            // 仮で TIMESTAMPと同じ動きをさせています。
        case Types.TIMESTAMP:
            return "Timestamp";
        case Types.BINARY:
        case Types.VARBINARY:
        case Types.LONGVARBINARY:
        case Types.BLOB:
            return "BinaryStream";
        case Types.LONGVARCHAR:
        case Types.LONGNVARCHAR:
        case Types.CLOB:
        case Types.NCLOB:
            return "CharacterStream";
        case Types.JAVA_OBJECT:
        case Types.DISTINCT:
        case Types.STRUCT:
        case Types.ARRAY:
        case Types.NULL:
        case Types.OTHER:
        case Types.REF:
        case Types.DATALINK:
        case -101:
            // Oracle の SYSTIMESTAMP 型のための、workaround
            if("SYSTIMESTAMP".equals(columnStructure.getName())){
                return "Timestamp";
            }
        default:
            throw new IllegalArgumentException("ゲッターおよびセッターを取得する処理で、型["
                    + columnStructure.getDataType()
                    + "/"
                    + BlancoDbMetaDataUtil
                            .convertJdbcDataTypeToString(columnStructure
                                    .getDataType()) + "]に対応するメソッド名の解析に失敗しました。");
        }
    }

    /**
     * 必要な場合のみ、プリミティブ型などに対してラッパークラスをラッピングします。
     * 
     * プリミティブ型をラッパークラスのオブジェクトへと置き換える処理をおこないます。<br>
     * 加えて、java.sql.Dateやjava.sql.Timestampなどから
     * java.util.Dateへの置き換えもここでおこなわれています。
     * 
     * @param String
     *            originalLine オリジナル行
     * @param String
     *            javaTypeName Java言語上の型
     * @return
     */
    public static final String mapPrimitiveIntoWrapperClass(
            final BlancoDbMetaDataColumnStructure columnStructure,
            final String originalLine) {
        String converter1 = "";
        String converter2 = "";

        switch (columnStructure.getDataType()) {
        case Types.BIT:
        case Types.BOOLEAN:
            if (columnStructure.getNullable() == ResultSetMetaData.columnNullable) {
                converter1 = "new Boolean(";
                converter2 = ")";
            }
            break;
        case Types.TINYINT:
            if (columnStructure.getNullable() == ResultSetMetaData.columnNullable) {
                converter1 = "new Byte(";
                converter2 = ")";
            }
            break;
        case Types.SMALLINT:
            if (columnStructure.getNullable() == ResultSetMetaData.columnNullable) {
                converter1 = "new Short(";
                converter2 = ")";
            }
            break;
        case Types.INTEGER:
            if (columnStructure.getNullable() == ResultSetMetaData.columnNullable) {
                converter1 = "new Integer(";
                converter2 = ")";
            }
            break;
        case Types.BIGINT:
            if (columnStructure.getNullable() == ResultSetMetaData.columnNullable) {
                converter1 = "new Long(";
                converter2 = ")";
            }
            break;
        case Types.REAL:
            if (columnStructure.getNullable() == ResultSetMetaData.columnNullable) {
                converter1 = "new Float(";
                converter2 = ")";
            }
            break;
        case Types.FLOAT:
        case Types.DOUBLE:
            if (columnStructure.getNullable() == ResultSetMetaData.columnNullable) {
                converter1 = "new Double(";
                converter2 = ")";
            }
            break;
        case Types.DATE:
        case Types.TIME:
        case Types.TIMESTAMP:
            // Dateの場合には ResultSetからはTimestampが渡ってきます。
            converter1 = "BlancoDbUtil.convertTimestampToDate(";
            converter2 = ")";
            break;
        }

        return converter1 + originalLine + converter2;
    }

    /**
     * ラッパークラスをプリミティブに変換します。<br>
     * ラッパークラスのオブジェクトをプリミティブ型へと置き換える処理をおこないます。<br>
     * 加えて、java.util.Dateから java.sql.Timestampへの置き換えもここでおこなわれています。
     * 
     * @param originalLine
     * @param javaTypeName
     * @return
     */
    public static final String mapWrapperClassIntoPrimitive(
            final BlancoDbMetaDataColumnStructure columnStructure,
            final String originalLine) {
        String converter1 = "";
        String converter2 = "";

        switch (columnStructure.getDataType()) {
        case Types.BIT:
        case Types.BOOLEAN:
            if (columnStructure.getNullable() == ResultSetMetaData.columnNullable) {
                converter1 = "";
                converter2 = ".booleanValue()";
            }
            break;
        case Types.TINYINT:
            if (columnStructure.getNullable() == ResultSetMetaData.columnNullable) {
                converter1 = "";
                converter2 = ".byteValue()";
            }
            break;
        case Types.SMALLINT:
            if (columnStructure.getNullable() == ResultSetMetaData.columnNullable) {
                converter1 = "";
                converter2 = ".shortValue()";
            }
            break;
        case Types.INTEGER:
            if (columnStructure.getNullable() == ResultSetMetaData.columnNullable) {
                converter1 = "";
                converter2 = ".intValue()";
            }
            break;
        case Types.BIGINT:
            if (columnStructure.getNullable() == ResultSetMetaData.columnNullable) {
                converter1 = "";
                converter2 = ".longValue()";
            }
            break;
        case Types.REAL:
            if (columnStructure.getNullable() == ResultSetMetaData.columnNullable) {
                converter1 = "";
                converter2 = ".floatValue()";
            }
            break;
        case Types.FLOAT:
        case Types.DOUBLE:
            if (columnStructure.getNullable() == ResultSetMetaData.columnNullable) {
                converter1 = "";
                converter2 = ".doubleValue()";
            }
            break;
        case Types.DATE:
        case Types.TIME:
        case Types.TIMESTAMP:
            converter1 = "new Timestamp(";
            converter2 = ".getTime())";
            break;
        }

        return converter1 + originalLine + converter2;
    }
}
