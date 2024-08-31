package vCampus.Dao.Books;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
            String sql = "INSERT INTO tblBookShelf (name, create_time, update_time, user_id, book_ids, review_ids, is_public, subscribe_count, favorite_count) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, bookShelf.getName());
            pstmt.setTimestamp(2, Timestamp.valueOf(bookShelf.getCreateTime()));
            pstmt.setTimestamp(3, Timestamp.valueOf(bookShelf.getUpdateTime()));
            pstmt.setString(4, bookShelf.getUserId());
            pstmt.setString(5, String.join(",", bookShelf.getBookIds()));
            pstmt.setString(6, String.join(",", bookShelf.getReviewIds()));
            pstmt.setBoolean(7, bookShelf.getIsPublic());
            pstmt.setInt(8, bookShelf.getSubscribeCount());
            pstmt.setInt(9, bookShelf.getFavoriteCount());
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
            String sql = "UPDATE tblBookShelf SET name = ?, create_time = ?, update_time = ?, user_id = ?, book_ids = ?, review_ids = ?, is_public = ?, subscribe_count = ?, favorite_count = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, bookShelf.getName());
            pstmt.setTimestamp(2, Timestamp.valueOf(bookShelf.getCreateTime()));
            pstmt.setTimestamp(3, Timestamp.valueOf(bookShelf.getUpdateTime()));
            pstmt.setString(4, bookShelf.getUserId());
            pstmt.setString(5, String.join(",", bookShelf.getBookIds()));
            pstmt.setString(6, String.join(",", bookShelf.getReviewIds()));
            pstmt.setBoolean(7, bookShelf.getIsPublic());
            pstmt.setInt(8, bookShelf.getSubscribeCount());
            pstmt.setInt(9, bookShelf.getFavoriteCount());
            pstmt.setLong(10, bookShelf.getId());
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
            String sql = "UPDATE tblBookShelf SET is_deleted = true WHERE id = ?";
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
            String sql = "SELECT * FROM tblBookShelf WHERE id = ? AND is_deleted = false";
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, Long.parseLong(id));
            rs = pstmt.executeQuery();
            if (rs.next()) {
                bookShelf = new BookShelf(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("user_id"),
                        rs.getTimestamp("create_time").toLocalDateTime(),
                        rs.getTimestamp("update_time").toLocalDateTime(),
                        List.of(rs.getString("book_ids").split(",")),
                        List.of(rs.getString("review_ids").split(",")),
                        rs.getBoolean("is_public"),
                        rs.getInt("subscribe_count"),
                        rs.getInt("favorite_count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return bookShelf;
    }

    public long save(BookShelf bookShelf) {
        long generatedId = -1;
        try {
            conn = DbConnection.getConnection();
            String sql = "INSERT INTO tblBookShelf (name, create_time, update_time, user_id, book_ids, review_ids, is_public, subscribe_count, favorite_count) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, bookShelf.getName());
            pstmt.setTimestamp(2, Timestamp.valueOf(bookShelf.getCreateTime()));
            pstmt.setTimestamp(3, Timestamp.valueOf(bookShelf.getUpdateTime()));
            pstmt.setString(4, bookShelf.getUserId());
            pstmt.setString(5, String.join(",", bookShelf.getBookIds()));
            pstmt.setString(6, String.join(",", bookShelf.getReviewIds()));
            pstmt.setBoolean(7, bookShelf.getIsPublic());
            pstmt.setInt(8, bookShelf.getSubscribeCount());
            pstmt.setInt(9, bookShelf.getFavoriteCount());
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

    public List<BookShelf> findBookShelvesByPage(
            Map<String, String> searchCriteria,
            List<String> sortCriteria,
            int page,
            int pageSize) {
        List<BookShelf> bookShelves = new ArrayList<>();
        try {
            conn = DbConnection.getConnection();
            StringBuilder sql = new StringBuilder(
                    "SELECT * FROM tblBookShelf WHERE is_public = true AND is_deleted = false");
            List<String> params = new ArrayList<>();

            for (Map.Entry<String, String> entry : searchCriteria.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                switch (key) {
                    case "name":
                        sql.append(" AND ").append(key).append(" LIKE ?");
                        params.add("%" + value + "%");
                        break;
                    case "user_id":
                        sql.append(" AND ").append(key).append(" = ?");
                        params.add(value);
                        break;
                    default:
                        break;
                }
            }

            if (sortCriteria != null && !sortCriteria.isEmpty()) {
                sql.append(" ORDER BY ");
                for (int i = 0; i < sortCriteria.size(); i++) {
                    String criterion = sortCriteria.get(i);
                    switch (criterion) {
                        case "create_time":
                        case "update_time":
                            sql.append(criterion);
                            if (i < sortCriteria.size() - 1) {
                                sql.append(", ");
                            }
                            break;
                        default:
                            break;
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
                BookShelf bookShelf = new BookShelf(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("user_id"),
                        rs.getTimestamp("create_time").toLocalDateTime(),
                        rs.getTimestamp("update_time").toLocalDateTime(),
                        List.of(rs.getString("book_ids").split(",")),
                        List.of(rs.getString("review_ids").split(",")),
                        rs.getBoolean("is_public"),
                        rs.getInt("subscribe_count"),
                        rs.getInt("favorite_count"));
                bookShelves.add(bookShelf);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeConnection(conn);
        }
        return bookShelves;
    }
}