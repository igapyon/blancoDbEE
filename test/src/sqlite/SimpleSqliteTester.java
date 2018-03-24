package sqlite;

import java.sql.Connection;
import java.sql.DriverManager;

import my.db.query.SampleSQLite001Iterator;
import my.db.row.SampleSQLite001Row;

public class SimpleSqliteTester {
    public static void main(String[] args) throws Exception {
        Class.forName("org.sqlite.JDBC");
        final Connection conn = DriverManager.getConnection("jdbc:sqlite:.\\test\\data\\sqlite\\sqlite.db");
        conn.setAutoCommit(false);

        final SampleSQLite001Iterator ite = new SampleSQLite001Iterator(conn);
        ite.setInputParameter(1);

        final SampleSQLite001Row row = ite.getSingleRow();
        System.out.println("Search result: [" + row.getColText() + "]");

        ite.close();

        conn.close();
    }
}
