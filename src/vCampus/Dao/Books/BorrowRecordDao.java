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
            String sql = "INSERT INTO tblBorrowRecord (borrowDate, returnDate, bookId, bookUserId, status) VALUES (?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setDate(1, new java.sql.Date(borrowRecord.getBorrowDate().getTime()));
            pstmt.setDate(2, new java.sql.Date(borrowRecord.getReturnDate().getTime()));
            pstmt.setString(3, String.valueOf(borrowRecord.getBook().getId()));
            pstmt.setString(4, String.valueOf(borrowRecord.getBookUser().getId()));
            pstmt.setString(5, borrowRecord.getStatus().name());
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
            String sql = "UPDATE tblBorrowRecord SET borrowDate = ?, returnDate = ?, bookId = ?, bookUserId = ?, status = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setDate(1, new java.sql.Date(borrowRecord.getBorrowDate().getTime()));
            pstmt.setDate(2, new java.sql.Date(borrowRecord.getReturnDate().getTime()));
            pstmt.setString(3, String.valueOf(borrowRecord.getBook().getId()));
            pstmt.setString(4, String.valueOf(borrowRecord.getBookUser().getId()));
            pstmt.setString(5, borrowRecord.getStatus().name());
            pstmt.setLong(6, borrowRecord.getId());
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
            String sql = "DELETE FROM tblBorrowRecord WHERE id = ?";
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
        String sql = "SELECT * FROM tblBorrowRecord WHERE id = ?";
        try {
            conn = DbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, Long.parseLong(id));
            rs = pstmt.executeQuery();
            if (rs.next()) {
                BorrowRecord borrowRecord = new BorrowRecord(
                        rs.getLong("id"),
                        rs.getDate("borrowDate"),
                        rs.getDate("returnDate"),
                        new BookDao().find(String.valueOf(rs.getLong("bookId"))),
                        new BookUserDao().find(String.valueOf(rs.getLong("bookUserId"))),
                        BorrowRecord.BorrowStatus.valueOf(rs.getString("status")));
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
        String sql = "SELECT COUNT(*) FROM tblBorrowRecord";
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
        String sql = "SELECT * FROM tblBorrowRecord LIMIT ? OFFSET ?";
        try {
            conn = DbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, pageSize);
            pstmt.setInt(2, (page - 1) * pageSize);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                BorrowRecord borrowRecord = new BorrowRecord(
                        rs.getLong("id"),
                        rs.getDate("borrowDate"),
                        rs.getDate("returnDate"),
                        new BookDao().find(String.valueOf(rs.getLong("bookId"))),
                        new BookUserDao().find(String.valueOf(rs.getLong("bookUserId"))),
                        BorrowRecord.BorrowStatus.valueOf(rs.getString("status")));
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