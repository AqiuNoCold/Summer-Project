package vCampus.Dao;

import vCampus.Db.DbConnection;
import vCampus.Entity.User;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;

public class UserDao implements BaseDao<User> {
    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private ResultSet rs = null;

    @Override
    public boolean add(User user) {
        boolean isAdded = false;
        try {
            conn = DbConnection.getConnection();
            String sql = "INSERT INTO tblUser (id, pwd, age, gender, role, email, card, lost) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getId());
            pstmt.setString(2, user.getPwd());
            pstmt.setInt(3, user.getAge());
            pstmt.setBoolean(4, user.getGender());
            pstmt.setString(5, user.getRole());
            pstmt.setString(6, user.getEmail());
            pstmt.setString(7, user.getCard());
            // pstmt.setFloat(8, user.getRemain());
            // pstmt.setInt(9, user.getPassword());
            pstmt.setBoolean(8, user.getLost());
            // pstmt.setString(9, String.join(",", user.getCourses()));
            int rowsAffected = pstmt.executeUpdate();
            isAdded = rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return isAdded;
    }

    @Override
    public boolean update(User user) {
        boolean isUpdated = false;
        try {
            conn = DbConnection.getConnection();
            String sql = "UPDATE tblUser SET pwd = ?, age = ?, gender = ?, role = ?, email = ?, card = ?,  lost = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getPwd());
            pstmt.setInt(2, user.getAge());
            pstmt.setBoolean(3, user.getGender());
            pstmt.setString(4, user.getRole());
            pstmt.setString(5, user.getEmail());
            pstmt.setString(6, user.getCard());
            // pstmt.setFloat(7, user.getRemain());
            // pstmt.setInt(8, user.getPassword());
            pstmt.setBoolean(7, user.getLost());
            pstmt.setString(8, user.getId());
            // pstmt.setString(9, String.join(",", user.getCourses()));
            int rowsAffected = pstmt.executeUpdate();
            isUpdated = rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return isUpdated;
    }

    @Override
    public boolean delete(String id) {
        boolean isDeleted = false;
        try {
            conn = DbConnection.getConnection();
            String sql = "DELETE FROM tblUser WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            int rowsAffected = pstmt.executeUpdate();
            isDeleted = rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return isDeleted;
    }

    @Override
    public User find(String id) {
        User user = null;
        try {
            conn = DbConnection.getConnection();
            String sql = "SELECT * FROM tblUser WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                // String coursesStr = rs.getString("courses");
                // ArrayList<String> courses = new
                // ArrayList<>(Arrays.asList(coursesStr.split(",")));
                user = new User(
                        rs.getString("id"),
                        rs.getString("pwd"),
                        rs.getInt("age"),
                        rs.getBoolean("gender"),
                        rs.getString("role"),
                        rs.getString("email"),
                        rs.getString("card"),
                        // rs.getFloat("remain"),
                        // rs.getInt("password"),
                        rs.getBoolean("lost")
                // courses
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return user;
    }

    public boolean updateLost(boolean lost,String id) {
        boolean isUpdated = false;
        try {
            conn = DbConnection.getConnection();
            String sql = "UPDATE tblUser SET lost = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);

            pstmt.setBoolean(1, lost);
            pstmt.setString(2, id);
            // pstmt.setString(9, String.join(",", user.getCourses()));
            int rowsAffected = pstmt.executeUpdate();
            isUpdated = rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return isUpdated;
    }


    public static void main(String[] args) {
        UserDao dao = new UserDao();
        User user = new User(
                "123456789",         // ID
                "password123",        // 密码
                20,                   // 年龄
                true,                 // 性别（true表示男，false表示女）
                "ST",                 // 角色（ST表示学生）
                "user@example.com",   // 邮箱
                "987654321",          // 一卡通号
                false                 // 账户状态（false表示冻结，true表示正常）
        );
        dao.add(user);
    }
}
