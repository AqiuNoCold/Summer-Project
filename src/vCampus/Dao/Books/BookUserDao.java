package vCampus.Dao.Books;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;

import vCampus.Dao.BaseDao;
import vCampus.Db.DbConnection;
import vCampus.Entity.Books.BookShelf;
import vCampus.Entity.Books.BookUser;

public class BookUserDao implements BaseDao<BookUser> {
    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private ResultSet rs = null;

    @Override
    public boolean add(BookUser bookUser) {
        boolean isAdded = false;
        try {
            conn = DbConnection.getConnection();
            String sql = "INSERT INTO tblBookUser (id, borrow_record_ids, default_shelf_id, shelf_ids) VALUES (?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, bookUser.getId());
            pstmt.setString(2, String.join(",", bookUser.getBorrowRecordIds()));
            pstmt.setLong(3, bookUser.getDefaultBookShelf().getId());
            pstmt.setString(4, String.join(",", bookUser.getShelfIds()));
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
    public boolean update(BookUser bookUser) {
        boolean isUpdated = false;
        try {
            conn = DbConnection.getConnection();
            String sql = "UPDATE tblBookUser SET borrow_record_ids = ?, default_shelf_id = ?, shelf_ids = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, String.join(",", bookUser.getBorrowRecordIds()));
            pstmt.setLong(2, bookUser.getDefaultBookShelf().getId());
            pstmt.setString(3, String.join(",", bookUser.getShelfIds()));
            pstmt.setString(4, bookUser.getId());
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
            String sql = "DELETE FROM tblBookUser WHERE id = ?";
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
    public BookUser find(String id) {
        BookUser bookUser = null;
        try {
            conn = DbConnection.getConnection();
            String sql = "SELECT * FROM tblBookUser WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                List<String> borrowRecordIds = Arrays.asList(rs.getString("borrow_record_ids").split(","));
                BookShelf defaultBookShelf = new BookShelf(rs.getLong("default_shelf_id"));
                List<String> shelfIds = Arrays.asList(rs.getString("shelf_ids").split(","));

                bookUser = new BookUser(id, borrowRecordIds, defaultBookShelf, shelfIds);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return bookUser;
    }
}