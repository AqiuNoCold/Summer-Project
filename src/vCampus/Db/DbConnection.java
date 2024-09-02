package vCampus.Db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * @author Yihan Wan
 */

public final class DbConnection {
    private static final Logger logger = Logger.getLogger(DbConnection.class.getName());
    private static final String URL = "jdbc:mysql://localhost:3306/vCampus?serverTimezone=UTC";
    private static final String USERNAME = "Tester";
    private static final String PASSWORD = "12345678";

    /*
     * 连接数据库
     */
    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            logger.info("数据库成功连接。");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "连接数据库失败", e);
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
//                logger.info("数据库连接已关闭。");
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "关闭数据库连接失败", e);
                throw new RuntimeException("关闭数据库连接失败", e);
            }
        }
    }

    public static void main(String[] args) {
        Connection conn = null;
        try {
            conn = getConnection();
            // 在这里执行数据库操作
        } finally {
            closeConnection(conn);
        }
    }
}
