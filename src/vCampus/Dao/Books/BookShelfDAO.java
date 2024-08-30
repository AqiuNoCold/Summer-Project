package vCampus.Dao.Books;

import vCampus.Dao.BaseDao;
import vCampus.Entity.Books.BookShelf;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookShelfDAO implements BaseDao<BookShelf> {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/vcampus";
    private static final String USER = "root";
    private static final String PASS = "password";

    // 添加书架
    @Override
    public boolean add(BookShelf bookShelf) {
        String sql = "INSERT INTO bookshelves (name, user_id, create_time, update_time) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, bookShelf.getName());
            pstmt.setLong(2, bookShelf.getUserId());
            pstmt.setTimestamp(3, Timestamp.valueOf(bookShelf.getCreateTime()));
            pstmt.setTimestamp(4, Timestamp.valueOf(bookShelf.getUpdateTime()));
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 更新书架
    @Override
    public boolean update(BookShelf bookShelf) {
        String sql = "UPDATE bookshelves SET name = ?, user_id = ?, update_time = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, bookShelf.getName());
            pstmt.setLong(2, bookShelf.getUserId());
            pstmt.setTimestamp(3, Timestamp.valueOf(bookShelf.getUpdateTime()));
            pstmt.setLong(4, bookShelf.getId());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 删除书架
    @Override
    public boolean delete(String id) {
        String sql = "DELETE FROM bookshelves WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, Long.parseLong(id));
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 查找书架
    @Override
    public BookShelf find(String id) {
        String sql = "SELECT * FROM bookshelves WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, Long.parseLong(id));
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new BookShelf(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getLong("user_id"),
                        rs.getTimestamp("create_time").toLocalDateTime(),
                        rs.getTimestamp("update_time").toLocalDateTime());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 查找所有书架
    public List<BookShelf> findAll() {
        String sql = "SELECT * FROM bookshelves";
        List<BookShelf> bookShelves = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                bookShelves.add(new BookShelf(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getLong("user_id"),
                        rs.getTimestamp("create_time").toLocalDateTime(),
                        rs.getTimestamp("update_time").toLocalDateTime()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookShelves;
    }

    // 搜索书架
    public List<BookShelf> search(String keyword) {
        String sql = "SELECT * FROM bookshelves WHERE name LIKE ?";
        List<BookShelf> bookShelves = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                bookShelves.add(new BookShelf(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getLong("user_id"),
                        rs.getTimestamp("create_time").toLocalDateTime(),
                        rs.getTimestamp("update_time").toLocalDateTime()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookShelves;
    }

    // 排序书架
    public List<BookShelf> sort(String sortBy) {
        String sql = "SELECT * FROM bookshelves ORDER BY " + sortBy;
        List<BookShelf> bookShelves = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                bookShelves.add(new BookShelf(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getLong("user_id"),
                        rs.getTimestamp("create_time").toLocalDateTime(),
                        rs.getTimestamp("update_time").toLocalDateTime()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookShelves;
    }

    // 分页获取书架
    public List<BookShelf> findByPage(int page, int pageSize) {
        String sql = "SELECT * FROM bookshelves LIMIT ?, ?";
        List<BookShelf> bookShelves = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, (page - 1) * pageSize);
            pstmt.setInt(2, pageSize);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                bookShelves.add(new BookShelf(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getLong("user_id"),
                        rs.getTimestamp("create_time").toLocalDateTime(),
                        rs.getTimestamp("update_time").toLocalDateTime()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookShelves;
    }
}