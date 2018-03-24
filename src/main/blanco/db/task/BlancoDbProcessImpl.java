package blanco.db.task;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import blanco.commons.util.BlancoStringUtil;
import blanco.db.BlancoDbConstants;
import blanco.db.BlancoDbXml2JavaClass;
import blanco.db.common.BlancoDbMeta2Xml;
import blanco.db.common.BlancoDbTableMeta2Xml;
import blanco.db.common.stringgroup.BlancoDbExecuteSqlStringGroup;
import blanco.db.common.stringgroup.BlancoDbLoggingModeStringGroup;
import blanco.db.common.valueobject.BlancoDbSetting;
import blanco.db.resourcebundle.BlancoDbResourceBundle;
import blanco.db.task.valueobject.BlancoDbProcessInput;

public class BlancoDbProcessImpl implements BlancoDbProcess {
    /**
     * リソースバンドルアクセサオブジェクトを記憶します。
     */
    private final BlancoDbResourceBundle fBundle = new BlancoDbResourceBundle();

    public int execute(final BlancoDbProcessInput input) throws IOException,
            IllegalArgumentException {
        System.out.println("- " + BlancoDbConstants.PRODUCT_NAME + " ("
                + BlancoDbConstants.VERSION + ")");

        try {
            System.out.println("db: begin.");
            final long startMills = System.currentTimeMillis();

            final File blancoTmpDbTableDirectory = new File(input.getTmpdir()
                    + "/db/table");
            final File blancoTmpDbSqlDirectory = new File(input.getTmpdir()
                    + "/db/sql");
            blancoTmpDbTableDirectory.mkdirs();
            blancoTmpDbSqlDirectory.mkdirs();

            final BlancoDbSetting dbSetting = new BlancoDbSetting();
            dbSetting.setTargetDir(input.getTargetdir());
            dbSetting.setBasePackage(input.getBasepackage());
            dbSetting.setRuntimePackage(input.getRuntimepackage());

            dbSetting.setJdbcdriver(input.getJdbcdriver());
            dbSetting.setJdbcurl(input.getJdbcurl());
            dbSetting.setJdbcuser(input.getJdbcuser());
            dbSetting.setJdbcpassword(input.getJdbcpassword());
            if (BlancoStringUtil.null2Blank(input.getJdbcdriverfile()).length() > 0) {
                dbSetting.setJdbcdriverfile(input.getJdbcdriverfile());
            }
            dbSetting.setEncoding(input.getEncoding());

            if ("true".equals(input.getConvertStringToMsWindows31jUnicode())) {
                dbSetting.setConvertStringToMsWindows31jUnicode(true);
            }
            
            // 処理中に SQL 定義書の例外が発生した場合に処理中断するかどうか。 
            dbSetting.setFailonerror("true".equals(input.getFailonerror()));

            if (input.getLog().equals("true")) {
                dbSetting.setLogging(true);
                dbSetting.setLoggingMode(new BlancoDbLoggingModeStringGroup()
                        .convertToInt(input.getLogmode()));
                if (dbSetting.getLoggingMode() == BlancoDbLoggingModeStringGroup.NOT_DEFINED) {
                    throw new IllegalArgumentException("ロギングモードとして指定された値["
                            + input.getLogmode() + "]はサポートされません。処理中断します。");
                }
            }
			if (input.getLogsql().equals("true")) {
				// 可読性の高いロギングを有効化します。
				dbSetting.setLoggingsql(true);
			}
            if (BlancoStringUtil.null2Blank(input.getStatementtimeout())
                    .length() > 0) {
                try {
                    dbSetting.setStatementTimeout(Integer.parseInt(input
                            .getStatementtimeout()));
                } catch (NumberFormatException ex) {
                    throw new IllegalArgumentException(
                            "ステートメントタイムアウト値として指定された値["
                                    + input.getStatementtimeout()
                                    + "]は数値として解析できませんでした。処理中断します。:"
                                    + ex.toString());
                }
            }
            dbSetting.setExecuteSql(new BlancoDbExecuteSqlStringGroup()
                    .convertToInt(input.getExecutesql()));
            if (dbSetting.getExecuteSql() == BlancoDbExecuteSqlStringGroup.NOT_DEFINED) {
                throw new IllegalArgumentException("executesqlとして不正な値("
                        + input.getExecutesql() + ")が与えられました。");
            }

            if (input.getSchema() != null) {
                // スキーマ名を指定。
                dbSetting.setSchema(input.getSchema());
            }

            if (input.getTable() == null || input.getTable().equals("true")) {
                // 単一表アクセスを自動生成
                final BlancoDbTableMeta2Xml tableMeta2Xml = new BlancoDbTableMeta2Xml() {
                    public boolean progress(int progressCurrent,
                            int progressTotal, String progressItem) {
                        // 常にtrueを返します。
                        return true;
                    }
                };
                tableMeta2Xml.process(dbSetting, blancoTmpDbTableDirectory);

                // XMLファイルを元にR/Oマッピングを自動生成
                final BlancoDbXml2JavaClass generator = new BlancoDbXml2JavaClass() {
                    public boolean progress(int progressCurrent,
                            int progressTotal, String progressItem) {
                        // 常にtrueを返します。
                        return true;
                    }
                };

                generator.process(dbSetting, blancoTmpDbTableDirectory);
            }

            if (input.getSql() == null || input.getSql().equals("true")) {
                final File fileMetadir = new File(input.getMetadir());
                if (fileMetadir.exists() == false) {
                    throw new IllegalArgumentException("メタディレクトリ["
                            + input.getMetadir() + "]が存在しません。");
                }

                final BlancoDbMeta2Xml meta2Xml = new BlancoDbMeta2Xml();
                meta2Xml.setCacheMeta2Xml(input.getCache().equals("true"));
                meta2Xml.processDirectory(fileMetadir, blancoTmpDbSqlDirectory
                        .getAbsolutePath());

                // XMLファイルを元にR/Oマッピングを自動生成
                final BlancoDbXml2JavaClass generator = new BlancoDbXml2JavaClass() {
                    public boolean progress(int progressCurrent,
                            int progressTotal, String progressItem) {
                        // 常にtrueを返します。
                        return true;
                    }
                };
                generator.process(dbSetting, blancoTmpDbSqlDirectory);
            }

            final long endMills = System.currentTimeMillis() - startMills;
            System.out.println("db: end: " + (endMills / 1000) + " sec.");
        } catch (SQLException e) {
            throw new IllegalArgumentException(fBundle.getTaskErr001()
                    + e.toString());
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(fBundle.getTaskErr002()
                    + e.toString());
        } catch (SAXException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(fBundle.getTaskErr003()
                    + e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(fBundle.getTaskErr004()
                    + e.toString());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(fBundle.getTaskErr005()
                    + e.toString());
        } catch (TransformerException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(fBundle.getTaskErr006()
                    + e.toString());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("入力値エラー:" + e.toString());
        }
        return BlancoDbBatchProcess.END_SUCCESS;
    }

    /**
     * {@inheritDoc}
     */
    public boolean progress(final String argProgressMessage) {
        System.out.println(argProgressMessage);
        return false;
    }
}
