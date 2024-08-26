package vCampus.Db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/*
 * @author Yihan Wan
 */
public final class DbConnection {
    /*
    * 连接数据库
     */
    public static Connection getConnection() {
        Connection conn = null;
        String url = "jdbc:mysql://localhost:3306/vCampus";
        String username = "Tester";
        String password = "12345678";
        try {
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("数据库成功连接。");
        }catch (SQLException e) {
            throw new RuntimeException("连接数据库失败", e);
        }
        return conn;
    }
    /*
    * 关闭数据库
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("数据库连接已关闭。");
            } catch (SQLException e) {
                throw new RuntimeException("关闭数据库连接失败", e);
            }
        }
    }

    public static void main(String[] args) {
        DbConnection dbConnection = new DbConnection();
        Connection conn = null;
        try {
            conn = getConnection();
            // 在这里执行数据库操作

        } finally {
            closeConnection(conn);
        }
    }
}
