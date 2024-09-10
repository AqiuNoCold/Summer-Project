package vCampus.Db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ExecuteSQLFile {
    public static void main(String[] args) {
        DbCreator.createTables();
        String sqlFilePath = "src/vCampus/Db/sql/All_tbl.sql";
        // 连接数据库
        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // 读取 SQL 文件
            StringBuilder sql = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(sqlFilePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    sql.append(line);
                    // 每一条 SQL 语句应该以分号结束
                    if (line.trim().endsWith(";")) {
                        stmt.execute(sql.toString());
                        sql.setLength(0); // 清空 StringBuilder 以执行下一条语句
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("SQL 文件已成功执行!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

