package vCampus.Dao.Books;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import vCampus.Dao.BaseDao;
import vCampus.Db.DbConnection;
import vCampus.Entity.Books.BookShelf;

public class BookShelfDao implements BaseDao<BookShelf> {
    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private ResultSet rs = null;

    @Override
    public boolean add(BookShelf bookShelf) {
        boolean isAdded = false;
        try {
            conn = DbConnection.getConnection();
            String sql = "INSERT INTO tblBookShelf (name, createTime, updateTime, userId, bookIds, reviewIds) VALUES (?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, bookShelf.getName());
            pstmt.setTimestamp(2, Timestamp.valueOf(bookShelf.getCreateTime()));
            pstmt.setTimestamp(3, Timestamp.valueOf(bookShelf.getUpdateTime()));
            pstmt.setLong(4, bookShelf.getUserId());
            pstmt.setString(5, String.join(",", bookShelf.getBookIds()));
            pstmt.setString(6, String.join(",", bookShelf.getReviewIds()));
            int rowsAffected = pstmt.executeUpdate();
            isAdded = rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return isAdded;
    }

    @Override
    public boolean update(BookShelf bookShelf) {
        boolean isUpdated = false;
        try {
            conn = DbConnection.getConnection();
            String sql = "UPDATE tblBookShelf SET name = ?, createTime = ?, updateTime = ?, userId = ?, bookIds = ?, reviewIds = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, bookShelf.getName());
            pstmt.setTimestamp(2, Timestamp.valueOf(bookShelf.getCreateTime()));
            pstmt.setTimestamp(3, Timestamp.valueOf(bookShelf.getUpdateTime()));
            pstmt.setLong(4, bookShelf.getUserId());
            pstmt.setString(5, String.join(",", bookShelf.getBookIds()));
            pstmt.setString(6, String.join(",", bookShelf.getReviewIds()));
            pstmt.setLong(7, bookShelf.getId());
            int rowsAffected = pstmt.executeUpdate();
            isUpdated = rowsAffected > 0;
        } catch (SQLException e) {
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
            String sql = "DELETE FROM tblBookShelf WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, Long.parseLong(id));
            int rowsAffected = pstmt.executeUpdate();
            isDeleted = rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return isDeleted;
    }

    @Override
    public BookShelf find(String id) {
        BookShelf bookShelf = null;
        try {
            conn = DbConnection.getConnection();
            String sql = "SELECT * FROM tblBookShelf WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, Long.parseLong(id));
            rs = pstmt.executeQuery();
            if (rs.next()) {
                bookShelf = new BookShelf(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getLong("userId"),
                        rs.getTimestamp("createTime").toLocalDateTime(),
                        rs.getTimestamp("updateTime").toLocalDateTime(),
                        List.of(rs.getString("bookIds").split(",")),
                        List.of(rs.getString("reviewIds").split(",")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return bookShelf;
    }

    public List<BookShelf> findAll() {
        List<BookShelf> bookShelves = new ArrayList<>();
        try {
            conn = DbConnection.getConnection();
            String sql = "SELECT * FROM tblBookShelf";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                BookShelf bookShelf = new BookShelf(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getLong("userId"),
                        rs.getTimestamp("createTime").toLocalDateTime(),
                        rs.getTimestamp("updateTime").toLocalDateTime(),
                        List.of(rs.getString("bookIds").split(",")),
                        List.of(rs.getString("reviewIds").split(",")));
                bookShelves.add(bookShelf);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return bookShelves;
    }

    public long save(BookShelf bookShelf) {
        long generatedId = -1;
        try {
            conn = DbConnection.getConnection();
            String sql = "INSERT INTO tblBookShelf (name, createTime, updateTime, userId, bookIds, reviewIds) VALUES (?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, bookShelf.getName());
            pstmt.setTimestamp(2, Timestamp.valueOf(bookShelf.getCreateTime()));
            pstmt.setTimestamp(3, Timestamp.valueOf(bookShelf.getUpdateTime()));
            pstmt.setLong(4, bookShelf.getUserId());
            pstmt.setString(5, String.join(",", bookShelf.getBookIds()));
            pstmt.setString(6, String.join(",", bookShelf.getReviewIds()));
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    generatedId = rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return generatedId;
    }
}