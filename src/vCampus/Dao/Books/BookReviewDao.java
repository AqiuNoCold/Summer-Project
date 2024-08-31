package vCampus.Dao.Books;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import vCampus.Dao.BaseDao;
import vCampus.Db.DbConnection;
import vCampus.Service.Books.BookReviewService;

public class BookReviewDao implements BaseDao<BookReviewService> {
    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private ResultSet rs = null;

    @Override
    public boolean add(BookReviewService bookReview) {
        boolean isAdded = false;
        try {
            conn = DbConnection.getConnection();
            String sql = "INSERT INTO tblBookReviews (userId, bookId, shelfId, content, rating, createTime, updateTime, isPublic) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, bookReview.getUser().getId());
            pstmt.setString(2, bookReview.getBook().getId());
            pstmt.setLong(3, bookReview.getShelfId());
            pstmt.setString(4, bookReview.getContent());
            pstmt.setBigDecimal(5, bookReview.getRating());
            pstmt.setTimestamp(6, Timestamp.valueOf(bookReview.getCreateTime()));
            pstmt.setTimestamp(7, Timestamp.valueOf(bookReview.getUpdateTime()));
            pstmt.setBoolean(8, bookReview.getIsPublic());
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
    public boolean update(BookReviewService bookReview) {
        boolean isUpdated = false;
        try {
            conn = DbConnection.getConnection();
            String sql = "UPDATE tblBookReviews SET content = ?, rating = ?, updateTime = ?, isPublic = ?, shelfId = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, bookReview.getContent());
            pstmt.setBigDecimal(2, bookReview.getRating());
            pstmt.setTimestamp(3, Timestamp.valueOf(bookReview.getUpdateTime()));
            pstmt.setBoolean(4, bookReview.getIsPublic());
            pstmt.setLong(5, bookReview.getShelfId());
            pstmt.setLong(6, bookReview.getId());
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
    public BookReviewService find(String id) {
        BookReviewService bookReview = null;
        try {
            conn = DbConnection.getConnection();
            String sql = "SELECT * FROM tblBookReviews WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, Long.parseLong(id));
            rs = pstmt.executeQuery();
            if (rs.next()) {
                bookReview = new BookReviewService(
                        rs.getLong("id"),
                        rs.getString("userId"),
                        rs.getString("bookId"),
                        rs.getLong("shelfId"),
                        rs.getString("content"),
                        rs.getBigDecimal("rating"),
                        rs.getTimestamp("createTime").toLocalDateTime(),
                        rs.getTimestamp("updateTime").toLocalDateTime(),
                        rs.getBoolean("isPublic"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return bookReview;
    }

    // save方法
    public Long save(BookReviewService bookReview) {
        Long generatedId = null;
        try {
            conn = DbConnection.getConnection();
            String sql = "INSERT INTO tblBookReviews (userId, bookId, shelfId, content, rating, createTime, updateTime, isPublic) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, bookReview.getUserId());
            pstmt.setString(2, bookReview.getBookId());
            pstmt.setLong(3, bookReview.getShelfId());
            pstmt.setString(4, bookReview.getContent());
            pstmt.setBigDecimal(5, bookReview.getRating());
            pstmt.setTimestamp(6, Timestamp.valueOf(bookReview.getCreateTime()));
            pstmt.setTimestamp(7, Timestamp.valueOf(bookReview.getUpdateTime()));
            pstmt.setBoolean(8, bookReview.getIsPublic());
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