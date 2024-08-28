package vCampus.Db;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DbCreator {
    private static final Logger logger = Logger.getLogger(DbCreator.class.getName());

    public static void createTables() {
        createUserTable();
        createStuTable();
        createGradeTable();
        createBooksTable();
        createShopStudentTable();
        createProductTable();
        createTransactionTable();
    }

    private static void createUserTable() {
        String createTableSQL =  "CREATE TABLE IF NOT EXISTS tblUser ("
                + "id VARCHAR(255) PRIMARY KEY, "
                + "pwd VARCHAR(16) CHECK (LENGTH(pwd) BETWEEN 6 AND 16), "
                + "age INT CHECK (age > 0), "
                + "gender BOOLEAN, "
                + "role ENUM('ST', 'TC', 'AD'), "
                + "email VARCHAR(255) CHECK (email REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$'), "
                + "card CHAR(9) CHECK (LENGTH(card) = 9), "
                + "remain FLOAT CHECK (remain >= 0), "
                + "password INT, "
                + "lost BOOLEAN DEFAULT FALSE, "
                + "courses TEXT"
                + ")";

        executeSQL(createTableSQL, "tblUser");
    }

    private static void createStuTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS tblStu ("
                + "id VARCHAR(255), "
                + "card_id VARCHAR(9) PRIMARY KEY, "
                + "name VARCHAR(10), "
                + "gender VARCHAR(1), "
                + "birth DATE, "
                + "college VARCHAR(255), "
                + "grade VARCHAR(255), "
                + "major VARCHAR(20), "
                + "email VARCHAR(255) CHECK (email LIKE '%@%.com'), "
                + "stage VARCHAR(3), "
                + "honor TEXT, "
                + "punish TEXT, "
                + "stu_code VARCHAR(19)"
                + ")";

        executeSQL(createTableSQL, "tblStu");
    }

    private static void createGradeTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS tblGrade ("
                + "id VARCHAR(255), "
                + "card_id VARCHAR(9), "
                + "course_name VARCHAR(25), "
                + "course_id VARCHAR(8), "
                + "usual DOUBLE DEFAULT 0 CHECK (usual BETWEEN 0 AND 100), "
                + "mid DOUBLE DEFAULT 0 CHECK (mid BETWEEN 0 AND 100), "
                + "final DOUBLE DEFAULT 0 CHECK (final BETWEEN 0 AND 100), "
                + "total DOUBLE DEFAULT 0 CHECK (total BETWEEN 0 AND 100), "
                + "point DOUBLE DEFAULT 0 CHECK (point BETWEEN 0 AND 10), "
                + "is_first BOOLEAN DEFAULT FALSE, "
                + "term VARCHAR(14)"
                + ")";

        executeSQL(createTableSQL, "tblGrade");
    }

    private static void createBooksTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS tblBooks ("
                + "isbn CHAR(10), "
                + "msrp VARCHAR(20), "
                + "image TEXT, "
                + "pages INT, "
                + "title TEXT, "
                + "isbn13 CHAR(13) PRIMARY KEY, "  // 将 isbn13 作为主键
                + "authors TEXT, "
                + "edition INT, "
                + "language VARCHAR(50), "
                + "subjects TEXT, "
                + "synopsis TEXT, "
                + "publisher TEXT, "
                + "title_long TEXT, "
                + "date_published DATE, "
                + "UNIQUE (isbn)"  // isbn 作为唯一键
                + ")";

        executeSQL(createTableSQL, "tblBooks");
    }


    private static void createShopStudentTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS tblShopStudent ("
                + "id VARCHAR(255) PRIMARY KEY, "
                + "pwd VARCHAR(16) CHECK (LENGTH(pwd) BETWEEN 6 AND 16), "
                + "age INT CHECK (age > 0), "
                + "gender BOOLEAN, "
                + "role ENUM('ST', 'TC', 'AD'), "
                + "email VARCHAR(255) CHECK (email REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z]{2,6}$'), "
                + "card CHAR(9) CHECK (LENGTH(card) = 9), "
                + "remain FLOAT CHECK (remain >= 0), "
                + "password INT, "
                + "lost BOOLEAN DEFAULT FALSE, "
                + "favorites TEXT, "
                + "belongs TEXT, "
                + "bill TEXT"
                + ")";

        executeSQL(createTableSQL, "tblShopStudent");
    }

    private static void createProductTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS tblProduct ("
                + "id VARCHAR(8) PRIMARY KEY, "
                + "name VARCHAR(64), "
                + "price FLOAT CHECK (price >= 0), "
                + "numbers INT CHECK (numbers >= 0), "
                + "owner VARCHAR(9), "
                + "discount FLOAT CHECK (discount BETWEEN 0 AND 1), "
                + "time DATE"
                + ")";

        executeSQL(createTableSQL, "tblProduct");
    }

    private static void createTransactionTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS tblTransaction ("
                + "transaction VARCHAR(255), "
                + "card CHAR(9) CHECK (LENGTH(card) = 9)"
                + ")";

        executeSQL(createTableSQL, "tblTransaction");
    }

    private static void executeSQL(String sql, String tableName) {
        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            logger.info("表 " + tableName + " 创建成功。");

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "创建表 " + tableName + " 失败", e);
            throw new RuntimeException("创建表 " + tableName + " 失败", e);
        }
    }

    public static void main(String[] args) {
        createTables();
    }
}
