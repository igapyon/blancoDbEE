/*
 * blancoDb
 * Copyright (C) 2004-2006 Yasuo Nakanishi
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 */
package blanco.db;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import blanco.cg.BlancoCgObjectFactory;
import blanco.cg.BlancoCgTransformer;
import blanco.cg.transformer.BlancoCgTransformerFactory;
import blanco.cg.valueobject.BlancoCgSourceFile;
import blanco.commons.util.BlancoNameAdjuster;
import blanco.commons.util.BlancoStringUtil;
import blanco.db.common.BlancoDbXml2SqlInfo;
import blanco.db.common.IBlancoDbProgress;
import blanco.db.common.stringgroup.BlancoDbDriverNameStringGroup;
import blanco.db.common.stringgroup.BlancoDbSqlInfoTypeStringGroup;
import blanco.db.common.util.BlancoDbUtil;
import blanco.db.common.valueobject.BlancoDbSetting;
import blanco.db.common.valueobject.BlancoDbSqlInfoStructure;
import blanco.db.expander.exception.DeadlockExceptionClass;
import blanco.db.expander.exception.IntegrityConstraintExceptionClass;
import blanco.db.expander.exception.LockTimeoutExceptionClass;
import blanco.db.expander.exception.NoRowFoundExceptionClass;
import blanco.db.expander.exception.NoRowModifiedExceptionClass;
import blanco.db.expander.exception.NotSingleRowExceptionClass;
import blanco.db.expander.exception.TimeoutExceptionClass;
import blanco.db.expander.exception.TooManyRowsFoundExceptionClass;
import blanco.db.expander.exception.TooManyRowsModifiedExceptionClass;
import blanco.db.expander.query.caller.QueryCallerClass;
import blanco.db.expander.query.invoker.QueryInvokerClass;
import blanco.db.expander.query.iterator.QueryIteratorClass;
import blanco.db.util.BlancoDbMappingUtilJava;
import blanco.db.util.BlancoDbUtilClassJava;
import blanco.dbmetadata.valueobject.BlancoDbMetaDataColumnStructure;
import blanco.valueobject.BlancoValueObjectXml2JavaClass;
import blanco.valueobject.valueobject.BlancoValueObjectClassStructure;
import blanco.valueobject.valueobject.BlancoValueObjectFieldStructure;

/**
 * 中間XMLファイルからソースコードを生成します。
 */
public abstract class BlancoDbXml2JavaClass implements IBlancoDbProgress {
    private BlancoDbSetting fDbSetting = null;

    /**
     * XMLファイルからソースコードを生成します。
     * 
     * @param connDef
     *            データベース接続情報。
     * @param blancoSqlDirectory
     *            SQL XMLファイルが格納されているディレクトリ。
     * @param rootPackage
     *            ルートとなる基準パッケージ。
     * @param runtimePackage
     *            blancoに設定するランタイムパッケージ。nullならデフォルトに出力。
     * @param statementTimeout
     *            ステートメントタイムアウト値。
     * @param blancoTargetSourceDirectory
     *            出力先ディレクトリ。
     * @throws SQLException
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws ClassNotFoundException
     * @throws TransformerException
     */
    public void process(final BlancoDbSetting argDbSetting,
            final File blancoSqlDirectory) throws SQLException, SAXException,
            IOException, ParserConfigurationException, ClassNotFoundException,
            TransformerException {
        System.out.println(BlancoDbConstants.PRODUCT_NAME + " ("
                + BlancoDbConstants.VERSION + ") ソースコード生成: 開始.");

        fDbSetting = argDbSetting;

        if (BlancoStringUtil.null2Blank(fDbSetting.getRuntimePackage()).trim()
                .length() == 0) {
            fDbSetting.setRuntimePackage(null);
        }

        Connection conn = null;
        try {
            conn = BlancoDbUtil.connect(fDbSetting);
            BlancoDbUtil.getDatabaseVersionInfo(conn, fDbSetting);

            if (blancoSqlDirectory != null) {
                // 指定がある場合にのみ SQL定義書ファイル格納ディレクトリを処理します。

                // ValueObject情報を格納するディレクトリを作成します。
                new File(blancoSqlDirectory.getAbsolutePath() + "/valueobject")
                        .mkdirs();

                final File[] fileSettingXml = blancoSqlDirectory.listFiles();
                for (int index = 0; index < fileSettingXml.length; index++) {
                    if (fileSettingXml[index].getName().endsWith(".xml") == false) {
                        // ファイルの拡張子が xml であるもののみ処理します。
                        continue;
                    }
                    if (progress(index + 1, fileSettingXml.length,
                            fileSettingXml[index].getName()) == false) {
                        break;
                    }

                    try {
                        // 生成はファイル毎に行います。
                        processEveryFile(conn, fileSettingXml[index], new File(
                                blancoSqlDirectory.getAbsolutePath()
                                        + "/valueobject"));
                    } catch (IllegalArgumentException ex) {
                        if (argDbSetting.getFailonerror()) {
                            // SQL 定義書の処理において例外が発生したので処理中断します。
                            throw ex;
                        } else {
                            // 標準エラー出力にエラー表示の上処理続行します。
                            System.err.println("SQL 定義書例外: " + ex.getMessage());
                        }
                    }
                }
            }

        } finally {
            BlancoDbUtil.close(conn);
            conn = null;
            System.out.println("ソースコード生成: 終了.");
        }
    }

    /**
     * 個別のXMLファイルを処理します。
     * 
     * @param dbInfoCollector
     * @param rootPackage
     * @param fileSqlForm
     * @param outputDirectory
     * @throws IOException
     * @throws SAXException
     * @throws TransformerException
     * @throws SQLException
     * @throws ParserConfigurationException
     */
    private void processEveryFile(final Connection conn,
            final File fileSqlForm, final File outputDirectory)
            throws IOException, SAXException, TransformerException,
            SQLException, ParserConfigurationException {

        System.out.println("ファイル[" + fileSqlForm.getAbsolutePath() + "]を処理します");

        final BlancoDbXml2SqlInfo collector = new BlancoDbXml2SqlInfo();
        final List<BlancoDbSqlInfoStructure> definition = collector.process(
                conn, fDbSetting, fileSqlForm);

        final String packageNameException = BlancoDbUtil
                .getRuntimePackage(fDbSetting) + ".exception";

        // 従来と互換性を持たせるため、/mainサブフォルダに出力します。
        final File fileBlancoMain = new File(fDbSetting.getTargetDir()
                + "/main");

        final BlancoCgObjectFactory cgFactory = BlancoCgObjectFactory
                .getInstance();

        final BlancoCgTransformer transformer = BlancoCgTransformerFactory
                .getJavaSourceTransformer();

        // exception系
        transformer.transform(adjust(new DeadlockExceptionClass(cgFactory,
                packageNameException).expand()), fileBlancoMain);
        transformer.transform(adjust(new IntegrityConstraintExceptionClass(
                cgFactory, packageNameException).expand()), fileBlancoMain);
        transformer.transform(adjust(new NoRowFoundExceptionClass(cgFactory,
                packageNameException).expand()), fileBlancoMain);
        transformer.transform(adjust(new NoRowModifiedExceptionClass(cgFactory,
                packageNameException).expand()), fileBlancoMain);
        transformer.transform(adjust(new NotSingleRowExceptionClass(cgFactory,
                packageNameException).expand()), fileBlancoMain);
        transformer.transform(adjust(new TimeoutExceptionClass(cgFactory,
                packageNameException).expand()), fileBlancoMain);
        transformer.transform(adjust(new TooManyRowsFoundExceptionClass(
                cgFactory, packageNameException).expand()), fileBlancoMain);
        transformer.transform(adjust(new TooManyRowsModifiedExceptionClass(
                cgFactory, packageNameException).expand()), fileBlancoMain);

        switch (fDbSetting.getDriverName()) {
        case BlancoDbDriverNameStringGroup.SQLSERVER_2000:
        case BlancoDbDriverNameStringGroup.SQLSERVER_2005:
            // SQL Server 2000/2005の場合にのみ、LockTimeoutExceptionクラスを生成します。
            transformer.transform(adjust(new LockTimeoutExceptionClass(
                    cgFactory, packageNameException).expand()), fileBlancoMain);
            break;
        default:
            break;
        }

        // util系
        transformer.transform(adjust(new BlancoDbUtilClassJava(cgFactory,
                BlancoDbUtil.getRuntimePackage(fDbSetting) + ".util",
                fDbSetting).expand()), fileBlancoMain);

        // iterator, invoker, caller
        for (int index = 0; index < definition.size(); index++) {
            final BlancoDbSqlInfoStructure sqlInfo = definition.get(index);
            switch (sqlInfo.getType()) {
            case BlancoDbSqlInfoTypeStringGroup.ITERATOR:
                createRowObjectClass(
                        BlancoDbUtil.getBasePackage(sqlInfo, fDbSetting),
                        sqlInfo, outputDirectory, fDbSetting);

                transformer.transform(adjust(new QueryIteratorClass(fDbSetting,
                        sqlInfo, cgFactory).expand()), fileBlancoMain);
                break;
            case BlancoDbSqlInfoTypeStringGroup.INVOKER:
                transformer.transform(adjust(new QueryInvokerClass(fDbSetting,
                        sqlInfo, cgFactory).expand()), fileBlancoMain);
                break;
            case BlancoDbSqlInfoTypeStringGroup.CALLER:
                transformer.transform(adjust(new QueryCallerClass(fDbSetting,
                        sqlInfo, cgFactory).expand()), fileBlancoMain);
                break;
            default:
                throw new IllegalArgumentException(
                        "想定外のエラー。不明なクエリオブジェクトが与えられました。" + sqlInfo.toString());
            }
        }
    }

    /**
     * 行オブジェクトを作成します。
     * 
     * @param className
     * @param packageName
     * @param sqlInfo
     * @param outputDirectory
     * @throws SAXException
     * @throws IOException
     * @throws TransformerException
     */
    public static void createRowObjectClass(final String rootPackage,
            final BlancoDbSqlInfoStructure sqlInfo, final File outputDirectory, final BlancoDbSetting dbSetting)
            throws SAXException, IOException, TransformerException {
        final String packageName = rootPackage + ".row";
        final String className = BlancoNameAdjuster.toClassName(sqlInfo
                .getName()) + "Row";

        final List<String[]> listFieldTypes = new ArrayList<String[]>();
        for (int index = 0; index < sqlInfo.getResultSetColumnList().size(); index++) {
            final BlancoDbMetaDataColumnStructure columnStructure = sqlInfo
                    .getResultSetColumnList().get(index);

            try {
                listFieldTypes.add(new String[] {
                        columnStructure.getName(),
                        BlancoDbMappingUtilJava
                                .getFullClassName(columnStructure) });
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException("SQL定義[" + sqlInfo.getName()
                        + "] 項目名[" + columnStructure.getName()
                        + "] データソース依存の型名[" + columnStructure.getTypeName()
                        + "] が処理できません。:" + ex.toString(), ex);
            }
        }

        final BlancoValueObjectClassStructure voClass = new BlancoValueObjectClassStructure();

        voClass.setName(className);
        voClass.setPackage(packageName);
        voClass.setDescription("SQL定義書(blancoDb)から作成された行クラス。");
        voClass.getDescriptionList().add("'" + className + "'行を表現します。");
        for (int index = 0; index < listFieldTypes.size(); index++) {
            final String[] columnTypes = listFieldTypes.get(index);
            final String columnName = columnTypes[0];
            final String columnType = columnTypes[1];

            voClass.getDescriptionList().add(
                    "(" + String.valueOf(index + 1) + ") '" + columnName
                            + "'列 型:" + columnType);
        }

        for (int index = 0; index < listFieldTypes.size(); index++) {
            final String[] columnTypes = listFieldTypes.get(index);
            final String columnName = columnTypes[0];
            final String columnType = columnTypes[1];

            final BlancoValueObjectFieldStructure voField = new BlancoValueObjectFieldStructure();
            voField.setName(columnName);
            voField.setType(columnType);
            voField.setDescription("フィールド[" + columnName + "]です。");
            voClass.getFieldList().add(voField);
        }

        final BlancoValueObjectXml2JavaClass xml2javaclass = new BlancoValueObjectXml2JavaClass();
        xml2javaclass.setEncoding(dbSetting.getEncoding());
        if (dbSetting.getTargetDir() == null) {
            throw new IllegalArgumentException(
                    "BlancoDbGenerator: blanco出力先フォルダが未設定(null)です。");
        }
        xml2javaclass.structure2Source(voClass,
                new File(dbSetting.getTargetDir()));
    }

    /**
     * ソース・オブジェクトの内容を調整。
     * 
     * <UL>
     * <LI>ソースコードのエンコーディングを設定。
     * </UL>
     * 
     * @param arg
     * @return
     */
    private BlancoCgSourceFile adjust(final BlancoCgSourceFile arg) {
        if (BlancoStringUtil.null2Blank(fDbSetting.getEncoding()).length() > 0) {
            arg.setEncoding(fDbSetting.getEncoding());
        }
        return arg;
    }
}
