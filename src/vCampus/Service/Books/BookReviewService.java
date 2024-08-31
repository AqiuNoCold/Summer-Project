package vCampus.Service.Books;

import java.time.LocalDateTime;
import java.math.BigDecimal;

import vCampus.Dao.Books.BookReviewDao;

public class BookReviewService {
    private Long id;
    private BookUserService user;
    private String userId;
    private BookService book;
    private String bookId;
    private Long shelfId;
    private String content;
    private BigDecimal rating;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Boolean isPublic;

    // 构造方法
    public BookReviewService(Long id,
            String userId,
            String bookId,
            Long shelfId,
            String content,
            BigDecimal rating,
            LocalDateTime createTime,
            LocalDateTime updateTime,
            Boolean isPublic) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.shelfId = shelfId;
        this.content = content;
        this.rating = rating;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.isPublic = isPublic;
    }

    // 只用id的构造方法
    public BookReviewService(Long id) {
        this.id = id;
        BookReviewDao dao = new BookReviewDao();
        BookReviewService review = dao.find(id.toString());
        if (review != null) {
            this.userId = review.getUserId();
            this.bookId = review.getBookId();
            this.shelfId = review.getShelfId();
            this.content = review.getContent();
            this.rating = review.getRating();
            this.createTime = review.getCreateTime();
            this.updateTime = review.getUpdateTime();
            this.isPublic = review.getIsPublic();
        }
    }

    // 只用id的构造方法
    public BookReviewService(String id) {
        this.id = Long.parseLong(id); // 类型转换
        BookReviewService review = new BookReviewDao().find(id);
        if (review != null) {
            this.userId = review.getUserId();
            this.bookId = review.getBookId();
            this.shelfId = review.getShelfId();
            this.content = review.getContent();
            this.rating = review.getRating();
            this.createTime = review.getCreateTime();
            this.updateTime = review.getUpdateTime();
            this.isPublic = review.getIsPublic();
        }
    }

    // 新建评论用的构造方法
    public BookReviewService(String userId, String bookId, Long shelfId, String content, BigDecimal rating,
            LocalDateTime createTime, Boolean isPublic) {
        this.userId = userId;
        this.bookId = bookId;
        this.shelfId = shelfId;
        this.content = content;
        this.rating = rating;
        this.createTime = createTime;
        this.updateTime = createTime;
        this.isPublic = isPublic;
        this.id = new BookReviewDao().save(this);
    }

    // Getter和Setter方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // 懒加载，避免在未初始化的情况下使用。
    public BookUserService getUser() {
        if (user == null && userId != null) {
            user = new BookUserService(userId);
        }
        return user;
    }

    public String getUserId() {
        if (userId == null && user != null) {
            userId = user.getId();
        }
        return userId;
    }

    public void setUser(BookUserService user) {
        this.user = user;
    }

    // 懒加载，避免在未初始化的情况下使用。
    public BookService getBook() {
        if (book == null && bookId != null) {
            book = new BookService(bookId);
        }
        return book;
    }

    public String getBookId() {
        if (bookId == null && book != null) {
            bookId = book.getId();
        }
        return bookId;
    }

    public void setBook(BookService book) {
        this.book = book;
    }

    public Long getShelfId() {
        return shelfId;
    }

    public void setShelfId(Long shelfId) {
        this.shelfId = shelfId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() { // 新增的getter方法
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) { // 新增的setter方法
        this.updateTime = updateTime;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }
}