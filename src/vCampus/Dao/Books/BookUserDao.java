package vCampus.Dao.Books;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import vCampus.Dao.BaseDao;
import vCampus.Db.DbConnection;
import vCampus.Service.Books.BookShelfService;
import vCampus.Service.Books.BookUserService;

public class BookUserDao implements BaseDao<BookUserService> {
    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private ResultSet rs = null;

    @Override
    public boolean add(BookUserService bookUser) {
        boolean isAdded = false;
        try {
            conn = DbConnection.getConnection();
            String sql = "INSERT INTO tblBookUser (id, default_shelf_id, shelf_ids) VALUES (?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, bookUser.getId());
            pstmt.setLong(2, bookUser.getDefaultBookShelf().getId());
            pstmt.setString(3, String.join(",", bookUser.getShelfIds()));
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
    public boolean update(BookUserService bookUser) {
        boolean isUpdated = false;
        try {
            conn = DbConnection.getConnection();
            String sql = "UPDATE tblBookUser SET default_shelf_id = ?, shelf_ids = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, bookUser.getDefaultBookShelf().getId());
            pstmt.setString(2, String.join(",", bookUser.getShelfIds()));
            pstmt.setString(3, bookUser.getId());
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
    public BookUserService find(String id) {
        BookUserService bookUser = null;
        try {
            conn = DbConnection.getConnection();
            String sql = "SELECT * FROM tblBookUser WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                BookShelfService defaultBookShelf = new BookShelfService(rs.getLong("default_shelf_id"));
                List<String> shelfIds = Arrays.asList(rs.getString("shelf_ids").split(","));
                List<BookShelfService> bookShelves = new ArrayList<>();
                for (String shelfId : shelfIds) {
                    bookShelves.add(new BookShelfService(Long.parseLong(shelfId)));
                }
                bookUser = new BookUserService(id, defaultBookShelf, bookShelves);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return bookUser;
    }
}