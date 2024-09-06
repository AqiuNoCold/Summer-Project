package vCampus.Db;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class insertTestData {
    private static final Logger logger = Logger.getLogger(DbCreator.class.getName());
    private static void insertTestData() {

        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // 插入数据到tblUser表
            String insertUserSQL = "INSERT INTO tblUser (id, pwd, age, gender, role, email, card, lost) VALUES "
                    + "('user001', 'password1', 25, TRUE, 'ST', 'user001@example.com', '123456789', FALSE),"
                    + "('user002', 'password2', 30, FALSE, 'TC', 'user002@example.com', '987654321', TRUE)";
            stmt.execute(insertUserSQL);
            logger.info("插入数据到 tblUser 表成功。");

            // 插入数据到tblStu表
            String insertStuSQL = "INSERT INTO tblStu (id, card_id, name, gender, birth, college, grade, major, email, stage, honor, punish, stu_code) VALUES "
                    + "('stu001', '123456789', 'Alice', 'F', '2000-01-15', 'College A', 'Grade 1', 'Computer Science', 'alice@example.com', 'Freshman', 'Dean', NULL, 'SC123456789'),"
                    + "('stu002', '987654321', 'Bob', 'M', '1999-12-25', 'College B', 'Grade 2', 'Mathematics', 'bob@example.com', 'Sophomore', NULL, 'Late Assignment', 'SC987654321')";
            stmt.execute(insertStuSQL);
            logger.info("插入数据到 tblStu 表成功。");

            // 插入数据到tblGrade表
            String insertGradeSQL = "INSERT INTO tblGrade (id, card_id, course_name, course_id, usual, mid, final, total, point, is_first, term) VALUES "
                    + "('grade001', '123456789', 'Data Science', 'CS101', 85.0, 90.0, 92.0, 89.0, 9.0, FALSE, '2024-1'),"
                    + "('grade002', '987654321', 'Algorithms', 'CS102', 75.0, 80.0, 78.0, 77.0, 8.0, TRUE, '2024-2')";
            stmt.execute(insertGradeSQL);
            logger.info("插入数据到 tblGrade 表成功。");

            // 插入数据到tblShopStudent表
            //String insertShopStudentSQL = "INSERT INTO tblShopStudent (id, pwd, age, gender, role, email, card, remain, password, lost, favorites, belongs, bill) VALUES "
            //        + "('shopuser001', 'shoppwd1', 28, TRUE, 'AD', 'shopuser001@example.com', '123456789', 100.0, 1234, FALSE, '1,2,3', 'Store A', 'Bill A'),"
            //        + "('shopuser002', 'shoppwd2', 35, FALSE, 'ST', 'shopuser002@example.com', '987654321', 50.0, 5678, TRUE, '4,5,6', 'Store B', 'Bill B')";
            //stmt.execute(insertShopStudentSQL);
            //logger.info("插入数据到 tblShopStudent 表成功。");

            // 插入数据到tblProduct表
            String insertProductSQL = "INSERT INTO tblProduct (id, name, price, numbers, owner, discount, time, image) VALUES "
                    + "('prod001', 'Product A', 15.99, 100, '001', 0.1, '2024-09-01', NULL),"
                    + "('prod002', 'Product B', 25.49, 50, '002', 0.2, '2024-09-02', NULL)";
            stmt.execute(insertProductSQL);
            logger.info("插入数据到 tblProduct 表成功。");

            // 插入数据到tblECard表
            String insertECardSQL = "INSERT INTO tblECard (remain, password, card) VALUES "
                    + "(200.0, 1234, '123456789'),"
                    + "(150.0, 5678, '987654321')";
            stmt.execute(insertECardSQL);
            logger.info("插入数据到 tblECard 表成功。");

            // 插入数据到tblTransaction表
            String insertTransactionSQL = "INSERT INTO tblTransaction (transaction, card) VALUES "
                    + "('Transaction 1 details', '123456789'),"
                    + "('Transaction 2 details', '987654321')";
            stmt.execute(insertTransactionSQL);
            logger.info("插入数据到 tblTransaction 表成功。");

            // 插入数据到tblCourse表
            String insertCourseSQL = "INSERT INTO tblCourse (course_ID, course_name, teacher_name, time, start_time, end_time, status, now_num, max_num, classroom, num) VALUES "
                    + "('CS101', 'Introduction to Data Science', 'Dr. Smith', 3, 9, 12, '必修', 30, 40, 'Room 101', 1),"
                    + "('CS102', 'Advanced Algorithms', 'Dr. Johnson', 3, 14, 17, '选修', 25, 30, 'Room 102', 2)";
            stmt.execute(insertCourseSQL);
            logger.info("插入数据到 tblCourse 表成功。");

            // 插入数据到tblTeacher表
            String insertTeacherSQL = "INSERT INTO tblTeacher (id, course) VALUES "
                    + "('teacher001', 'CS101'),"
                    + "('teacher002', 'CS102')";
            stmt.execute(insertTeacherSQL);
            logger.info("插入数据到 tblTeacher 表成功。");

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "插入测试数据时出错：" + e);
        }
    }

    public static void main(String[] args) {
        insertTestData();
    }
}
