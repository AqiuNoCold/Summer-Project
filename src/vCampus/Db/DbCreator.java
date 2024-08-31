package vCampus.Db;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DbCreator {
    private static final Logger logger = Logger.getLogger(DbCreator.class.getName());

    public static void createTables() {
        dropAllTables();

        createUserTable();
        createStuTable();
        createGradeTable();
        createBooksTable();
        createBookShelfTable();
        createBorrowRecordTable();
        createBookReviewTable();
        createBookUserTable();
        createShopStudentTable();
        createProductTable();
        createECardTable();
        createTransactionTable();
        createCourseTable();
    }

    private static void dropAllTables() {
        String[] tables = {
                "tblUser",
                "tblStu",
                "tblGrade",
                "tblBooks",
                "tblBookShelf",
                "tblBorrowRecord",
                "tblShopStudent",
                "tblProduct",
                "tblECard",
                "tblTransaction",
                "tblCourse",
                "tblBookReview",
                "tblBookUser"
        };

        try (Connection conn = DbConnection.getConnection();
                Statement stmt = conn.createStatement()) {

            for (String table : tables) {
                String dropTableSQL = "DROP TABLE IF EXISTS " + table;
                stmt.execute(dropTableSQL);
                logger.info("表 " + table + " 删除成功。");
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "删除表失败", e);
            throw new RuntimeException("删除表失败", e);
        }
    }

    private static void createUserTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS tblUser ("
                + "id VARCHAR(255) PRIMARY KEY, "
                + "pwd VARCHAR(16) CHECK (LENGTH(pwd) BETWEEN 6 AND 16), "
                + "age INT CHECK (age > 0), "
                + "gender BOOLEAN, "
                + "role ENUM('ST', 'TC', 'AD'), "
                + "email VARCHAR(255) CHECK (email REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$'), "
                + "card CHAR(9) CHECK (LENGTH(card) = 9), "
                + "lost BOOLEAN DEFAULT FALSE "
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
                + "stage VARCHAR(30), "
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
                + "msrp DECIMAL(10,2), "
                + "image TEXT, "
                + "pages INT, "
                + "title VARCHAR(255), "
                + "isbn13 CHAR(13), "
                + "authors TEXT, "
                + "binding VARCHAR(50), "
                + "edition VARCHAR(50), "
                + "related TEXT, "
                + "language VARCHAR(20), "
                + "subjects TEXT, "
                + "synopsis TEXT, "
                + "publisher VARCHAR(100), "
                + "dimensions VARCHAR(50), "
                + "title_long TEXT, "
                + "date_published VARCHAR(20), "
                + "copy_count INT, "
                + "review_count INT, "
                + "average_rating DECIMAL(2,1), "
                + "favorite_count INT, "
                + "borrow_count INT, "
                + "is_active BOOLEAN, "
                + "is_deleted BOOLEAN, "
                + "PRIMARY KEY (isbn, isbn13)"
                + ")";

        executeSQL(createTableSQL, "tblBooks");
    }

    private static void createBookShelfTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS tblBookShelf ("
                + "id BIGINT AUTO_INCREMENT PRIMARY KEY, "
                + "name VARCHAR(255) NOT NULL, "
                + "create_time TIMESTAMP NOT NULL, "
                + "update_time TIMESTAMP NOT NULL, "
                + "user_id CHAR(9) NOT NULL, "
                + "book_ids TEXT, "
                + "review_ids TEXT, "
                + "is_public BOOLEAN, "
                + "subscribe_count INT, "
                + "favorite_count INT "
                + ")";

        executeSQL(createTableSQL, "tblBookShelf");
    }

    private static void createBorrowRecordTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS tblBorrowRecord ("
                + "id BIGINT AUTO_INCREMENT PRIMARY KEY, "
                + "borrow_date DATE, "
                + "return_date DATE, "
                + "book_id CHAR(24), "
                + "user_id CHAR(9), "
                + "status VARCHAR(10), "
                + "is_deleted BOOLEAN "
                + ")";

        executeSQL(createTableSQL, "tblBorrowRecord");
    }

    private static void createBookReviewTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS tblBookReview ("
                + "id BIGINT AUTO_INCREMENT PRIMARY KEY, "
                + "user_id CHAR(9), "
                + "book_id CHAR(24), "
                + "shelf_id BIGINT, "
                + "content TEXT, "
                + "rating DECIMAL(3, 2), "
                + "create_time TIMESTAMP, "
                + "update_time TIMESTAMP, "
                + "is_public BOOLEAN "
                + ")";

        executeSQL(createTableSQL, "tblBookReview");
    }

    private static void createBookUserTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS tblBookUser ("
                + "id CHAR(9) PRIMARY KEY, "
                + "borrow_record_ids TEXT, "
                + "default_shelf_id BIGINT, "
                + "shelf_ids TEXT "
                + ")";

        executeSQL(createTableSQL, "tblBookUser");
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

    private static void createECardTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS tblECard ("
                + "remain FLOAT CHECK (remain >= 0), "
                + "password INT, "
                + "card CHAR(9) CHECK (LENGTH(card) = 9)"
                + ")";

        executeSQL(createTableSQL, "tblECard");
    }

    private static void createTransactionTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS tblTransaction ("
                + "transaction TEXT, "
                + "card CHAR(9) CHECK (LENGTH(card) = 9)"
                + ")";

        executeSQL(createTableSQL, "tblTransaction");
    }

    private static void createCourseTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS tblCourse ("
                + "course_ID VARCHAR(255) PRIMARY KEY, "
                + "course_name VARCHAR(255), "
                + "teacher_name VARCHAR(255), "
                + "time INT, "
                + "start_time INT, "
                + "end_time INT, "
                + "status ENUM('必修', '选修'), "
                + "now_num INT, "
                + "max_num INT, "
                + "classroom VARCHAR(225), "
                + "num INT"
                + ")";

        executeSQL(createTableSQL, "tblCourse");
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