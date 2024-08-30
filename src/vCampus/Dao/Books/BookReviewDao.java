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
import vCampus.Entity.Books.BookReview;

public class BookReviewDao implements BaseDao<BookReview> {
    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private ResultSet rs = null;

    @Override
    public boolean add(BookReview bookReview) {
        boolean isAdded = false;
        try {
            conn = DbConnection.getConnection();
            String sql = "INSERT INTO tblBookReviews (userId, bookId, content, rating, createTime, updateTime) VALUES (?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, bookReview.getUser().getCard());
            pstmt.setString(2, bookReview.getBook().getId());
            pstmt.setString(3, bookReview.getContent());
            pstmt.setBigDecimal(4, bookReview.getRating());
            pstmt.setTimestamp(5, Timestamp.valueOf(bookReview.getCreateTime()));
            pstmt.setTimestamp(6, Timestamp.valueOf(bookReview.getUpdateTime()));
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
    public boolean update(BookReview bookReview) {
        boolean isUpdated = false;
        try {
            conn = DbConnection.getConnection();
            String sql = "UPDATE tblBookReviews SET content = ?, rating = ?, updateTime = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, bookReview.getContent());
            pstmt.setBigDecimal(2, bookReview.getRating());
            pstmt.setTimestamp(3, Timestamp.valueOf(bookReview.getUpdateTime()));
            pstmt.setLong(4, bookReview.getId());
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
            String sql = "DELETE FROM tblBookReviews WHERE id = ?";
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
    public BookReview find(String id) {
        BookReview bookReview = null;
        try {
            conn = DbConnection.getConnection();
            String sql = "SELECT * FROM tblBookReviews WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, Long.parseLong(id));
            rs = pstmt.executeQuery();
            if (rs.next()) {
                bookReview = new BookReview(
                        rs.getLong("id"),
                        new BookUserDao().find(rs.getString("userId")),
                        new BookDao().find(rs.getString("bookId")),
                        rs.getString("content"),
                        rs.getBigDecimal("rating"),
                        rs.getTimestamp("createTime").toLocalDateTime(),
                        rs.getTimestamp("updateTime").toLocalDateTime());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return bookReview;
    }

    public List<BookReview> findAll() {
        List<BookReview> bookReviews = new ArrayList<>();
        try {
            conn = DbConnection.getConnection();
            String sql = "SELECT * FROM tblBookReviews";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                BookReview bookReview = new BookReview(
                        rs.getLong("id"),
                        new BookUserDao().find(rs.getString("userId")),
                        new BookDao().find(rs.getString("bookId")),
                        rs.getString("content"),
                        rs.getBigDecimal("rating"),
                        rs.getTimestamp("createTime").toLocalDateTime(),
                        rs.getTimestamp("updateTime").toLocalDateTime());
                bookReviews.add(bookReview);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return bookReviews;
    }
}