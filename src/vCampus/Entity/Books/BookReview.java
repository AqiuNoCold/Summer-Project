package vCampus.Entity.Books;

import java.time.LocalDateTime;
import java.math.BigDecimal;

public class BookReview {
    private Long id;
    private BookUser user;
    private Book book;
    private String content;
    private BigDecimal rating;
    private LocalDateTime createTime;
    private LocalDateTime updateTime; // 新增字段

    // 构造方法
    public BookReview(Long id, BookUser user, Book book, String content, BigDecimal rating, LocalDateTime createTime,
            LocalDateTime updateTime) {
        this.id = id;
        this.user = user;
        this.book = book;
        this.content = content;
        this.rating = rating;
        this.createTime = createTime;
        this.updateTime = updateTime; // 初始化新增字段
    }

    // Getter和Setter方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BookUser getUser() {
        return user;
    }

    public void setUser(BookUser user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
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

    // 更新书评内容
    public void updateContent(String newContent) {
        this.content = newContent;
    }

    // 更新评分
    public void updateRating(BigDecimal newRating) {
        this.rating = newRating;
    }
}