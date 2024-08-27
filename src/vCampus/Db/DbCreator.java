package vCampus.Db;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DbCreator {
    private static final Logger logger = Logger.getLogger(DbCreator.class.getName());

    public static void UserCreator() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS tblUser ("
                + "id VARCHAR(255) PRIMARY KEY, "
                + "pwd VARCHAR(16) NOT NULL CHECK (LENGTH(pwd) BETWEEN 6 AND 16), "
                + "age INT CHECK (age > 0), "
                + "gender BOOLEAN, "
                + "role ENUM('ST', 'TC', 'AD') NOT NULL, "
                + "email VARCHAR(255) CHECK (email REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z]{2,6}$'), "
                + "card CHAR(9) CHECK (LENGTH(card) = 9), "
                + "remain FLOAT DEFAULT 0 CHECK (remain >= 0), "
                + "password INT , "
                + "lost BOOLEAN DEFAULT FALSE"
                + ")";


        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(createTableSQL);
            logger.info("表 tblUser 创建成功。");

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "创建表失败", e);
            throw new RuntimeException("创建表失败", e);
        }
    }

    public static void BooksCreator() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS tblBooks ("
                + "isbn VARCHAR(20) PRIMARY KEY, "
                + "msrp VARCHAR(20), "
                + "image TEXT, "
                + "pages INT, "
                + "title TEXT, "
                + "isbn13 VARCHAR(20), "
                + "authors TEXT, "
                + "binding VARCHAR(50), "
                + "edition INT, "
                + "language VARCHAR(50), "
                + "subjects TEXT, "
                + "synopsis TEXT, "
                + "publisher TEXT, "
                + "dimensions TEXT, "
                + "title_long TEXT, "
                + "date_published DATE"
                + ")";

        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(createTableSQL);
            logger.info("表 tblBooks 创建成功。");

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "创建表失败", e);
            throw new RuntimeException("创建表失败", e);
        }
    }

    public static void ECardCreator() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS tblECard ("
                + "id VARCHAR(255) PRIMARY KEY, "
                + "card CHAR(9) NOT NULL, "
                + "lost BOOLEAN DEFAULT FALSE, "
                + "password INT NOT NULL CHECK (LENGTH(CAST(password AS CHAR(6))) = 6), "
                + "remain FLOAT NOT NULL CHECK (remain >= 0)"
                + ")";

        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(createTableSQL);
            logger.info("表 tblECard 创建成功。");

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "创建表失败", e);
            throw new RuntimeException("创建表失败", e);
        }
    }

    public static void main(String[] args) {
        UserCreator();
        BooksCreator();
        ECardCreator();
    }
}
