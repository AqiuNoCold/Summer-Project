package vCampus.Dao.Books;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import vCampus.Dao.BaseDao;
import vCampus.Db.DbConnection;
import vCampus.Service.Books.BorrowRecordService;

public class BorrowRecordDao implements BaseDao<BorrowRecordService> {
    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private ResultSet rs = null;

    @Override
    public boolean add(BorrowRecordService borrowRecord) {
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
    public boolean update(BorrowRecordService borrowRecord) {
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
    public BorrowRecordService find(String id) {
        String sql = "SELECT * FROM tblBorrowRecord WHERE id = ? AND is_deleted = false";
        try {
            conn = DbConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, Long.parseLong(id));
            rs = pstmt.executeQuery();
            if (rs.next()) {
                BorrowRecordService borrowRecord = new BorrowRecordService(
                        rs.getLong("id"),
                        rs.getDate("borrow_date").toLocalDate(),
                        rs.getDate("return_date").toLocalDate(),
                        new BookDao().find(rs.getString("book_id")),
                        new BookUserDao().find(rs.getString("user_id")),
                        BorrowRecordService.BorrowStatus.valueOf(rs.getString("status")),
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

    public int getTotalRecords(Map<String, String> searchCriteria) {
        int totalRecords = 0;
        try {
            conn = DbConnection.getConnection();
            StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM tblBorrowRecord WHERE is_deleted = false");
            List<String> params = new ArrayList<>();

            for (Map.Entry<String, String> entry : searchCriteria.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                switch (key) {
                    case "user_id":
                    case "status":
                        sql.append(" AND ").append(key).append(" = ?");
                        params.add(value);
                        break;
                    default:
                        break;
                }
            }

            pstmt = conn.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                pstmt.setString(i + 1, params.get(i));
            }

            rs = pstmt.executeQuery();
            if (rs.next()) {
                totalRecords = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return totalRecords;
    }

    public List<BorrowRecordService> findAllByPage(Map<String, String> searchCriteria, List<String> sortCriteria,
            int page,
            int pageSize) {
        List<BorrowRecordService> borrowRecords = new ArrayList<>();
        try {
            conn = DbConnection.getConnection();
            StringBuilder sql = new StringBuilder("SELECT * FROM tblBorrowRecord WHERE is_deleted = false");
            List<String> params = new ArrayList<>();

            for (Map.Entry<String, String> entry : searchCriteria.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                switch (key) {
                    case "user_id":
                    case "status":
                        sql.append(" AND ").append(key).append(" = ?");
                        params.add(value);
                    default:
                        break;
                }
            }

            if (sortCriteria != null && !sortCriteria.isEmpty()) {
                sql.append(" ORDER BY ");
                for (int i = 0; i < sortCriteria.size(); i++) {
                    String criterion = sortCriteria.get(i);
                    sql.append(criterion);
                    if (i < sortCriteria.size() - 1) {
                        sql.append(", ");
                    }
                }
            }

            sql.append(" LIMIT ? OFFSET ?");
            pstmt = conn.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                pstmt.setString(i + 1, params.get(i));
            }
            pstmt.setInt(params.size() + 1, pageSize);
            pstmt.setInt(params.size() + 2, (page - 1) * pageSize);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                BorrowRecordService borrowRecord = new BorrowRecordService(
                        rs.getLong("id"),
                        rs.getDate("borrow_date").toLocalDate(),
                        rs.getDate("return_date").toLocalDate(),
                        new BookDao().find(rs.getString("book_id")),
                        new BookUserDao().find(rs.getString("user_id")),
                        BorrowRecordService.BorrowStatus.valueOf(rs.getString("status")),
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

    // 新的save方法，返回数据库自动分配的ID
    public Long save(BorrowRecordService borrowRecord) {
        Long generatedId = null;
        try {
            conn = DbConnection.getConnection();
            String sql = "INSERT INTO tblBorrowRecord (borrow_date, return_date, book_id, user_id, status, is_deleted) VALUES (?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setDate(1, java.sql.Date.valueOf(borrowRecord.getBorrowDate()));
            pstmt.setDate(2, java.sql.Date.valueOf(borrowRecord.getReturnDate()));
            pstmt.setString(3, borrowRecord.getBook().getId());
            pstmt.setString(4, borrowRecord.getBookUser().getId());
            pstmt.setString(5, borrowRecord.getStatus().name());
            pstmt.setBoolean(6, borrowRecord.getIsDeleted());
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