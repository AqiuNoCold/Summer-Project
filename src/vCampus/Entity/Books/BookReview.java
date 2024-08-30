package vCampus.Entity.Books;

import java.time.LocalDateTime;

import vCampus.Dao.Books.BookReviewDao;

import java.math.BigDecimal;

public class BookReview {
    private Long id;
    private BookUser user;
    private String userId;
    private Book book;
    private String bookId;
    private String content;
    private BigDecimal rating;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // 构造方法
    public BookReview(Long id,
            String userId,
            String bookId,
            String content,
            BigDecimal rating,
            LocalDateTime createTime,
            LocalDateTime updateTime) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.content = content;
        this.rating = rating;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    // 新的只用id的构造方法
    public BookReview(Long id) {
        this.id = id;
        BookReviewDao dao = new BookReviewDao();
        BookReview review = dao.find(id.toString());
        if (review != null) {
            this.userId = review.getUserId();
            this.bookId = review.getBookId();
            this.content = review.getContent();
            this.rating = review.getRating();
            this.createTime = review.getCreateTime();
            this.updateTime = review.getUpdateTime();
        }
    }

    // Getter和Setter方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // 懒加载，避免在未初始化的情况下使用。
    public BookUser getUser() {
        if (user == null && userId != null) {
            user = new BookUser(userId);
        }
        return user;
    }

    public String getUserId() {
        if (userId == null && user != null) {
            userId = user.getId();
        }
        return userId;
    }

    public void setUser(BookUser user) {
        this.user = user;
    }

    // 懒加载，避免在未初始化的情况下使用。
    public Book getBook() {
        if (book == null && bookId != null) {
            book = new Book(bookId);
        }
        return book;
    }

    public String getBookId() {
        if (bookId == null && book != null) {
            bookId = book.getId();
        }
        return bookId;
    }

    public void setBook(Book book) {
        this.book = book;
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
}