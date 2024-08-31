package vCampus.Dao.Books;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vCampus.Dao.BaseDao;
import vCampus.Db.DbConnection;
import vCampus.Entity.Books.BorrowRecord;

public class BorrowRecordDao implements BaseDao<BorrowRecord> {
    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private ResultSet rs = null;

    @Override
    public boolean add(BorrowRecord borrowRecord) {
        boolean isAdded = false;
        try {
            conn = DbConnection.getConnection();
            String sql = "INSERT INTO tblBorrowRecord (borrow_date, return_date, book_id, user_id, status, is_deleted) VALUES (?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setDate(1, java.sql.Date.valueOf(borrowRecord.getBorrowDate()));
            pstmt.setDate(2, java.sql.Date.valueOf(borrowRecord.getReturnDate()));
            pstmt.setString(3, borrowRecord.getBook().getId());
            pstmt.setString(4, borrowRecord.getBookUser().getId());
            pstmt.setString(5, borrowRecord.getStatus().name());
            pstmt.setBoolean(6, borrowRecord.getIsDeleted());
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
    public boolean update(BorrowRecord borrowRecord) {
        boolean isUpdated = false;
        try {
            conn = DbConnection.getConnection();
            String sql = "UPDATE tblBorrowRecord SET borrow_date = ?, return_date = ?, book_id = ?, user_id = ?, status = ?, is_deleted = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setDate(1, java.sql.Date.valueOf(borrowRecord.getBorrowDate()));
            pstmt.setDate(2, java.sql.Date.valueOf(borrowRecord.getReturnDate()));
            pstmt.setString(3, borrowRecord.getBook().getId());
            pstmt.setString(4, borrowRecord.getBookUser().getId());
            pstmt.setString(5, borrowRecord.getStatus().name());
            pstmt.setBoolean(6, borrowRecord.getIsDeleted());
            pstmt.setLong(7, borrowRecord.getId());
            int rows = pstmt.executeUpdate();
            isUpdated = rows > 0;
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
            String sql = "UPDATE tblBorrowRecord SET is_deleted = true WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, Long.parseLong(id));
            int rows = pstmt.executeUpdate();
            isDeleted = rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return isDeleted;
    }

    @Override
    public BorrowRecord find(String id) {
        String sql = "SELECT * FROM tblBorrowRecord WHERE id = ? AND is_deleted = false";
        try {
            conn = DbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, Long.parseLong(id));
            rs = pstmt.executeQuery();
            if (rs.next()) {
                BorrowRecord borrowRecord = new BorrowRecord(
                        rs.getLong("id"),
                        rs.getDate("borrow_date").toLocalDate(),
                        rs.getDate("return_date").toLocalDate(),
                        new BookDao().find(rs.getString("book_id")),
                        new BookUserDao().find(rs.getString("user_id")),
                        BorrowRecord.BorrowStatus.valueOf(rs.getString("status")),
                        rs.getBoolean("is_deleted"));
                return borrowRecord;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return null;
    }

    public int getTotalRecords() {
        String sql = "SELECT COUNT(*) FROM tblBorrowRecord WHERE is_deleted = false";
        try {
            conn = DbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return 0;
    }

    public List<BorrowRecord> findAll(int page, int pageSize) {
        List<BorrowRecord> borrowRecords = new ArrayList<>();
        String sql = "SELECT * FROM tblBorrowRecord WHERE is_deleted = false LIMIT ? OFFSET ?";
        try {
            conn = DbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, pageSize);
            pstmt.setInt(2, (page - 1) * pageSize);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                BorrowRecord borrowRecord = new BorrowRecord(
                        rs.getLong("id"),
                        rs.getDate("borrow_date").toLocalDate(),
                        rs.getDate("return_date").toLocalDate(),
                        new BookDao().find(rs.getString("book_id")),
                        new BookUserDao().find(rs.getString("user_id")),
                        BorrowRecord.BorrowStatus.valueOf(rs.getString("status")),
                        rs.getBoolean("is_deleted"));
                borrowRecords.add(borrowRecord);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return borrowRecords;
    }
}