package vCampus.Entity.Books;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.math.BigDecimal;

import vCampus.Service.Books.BookReviewService;

public class BookReview implements Serializable {
    private Long id;
    private BookUser user;
    private Book book;
    private Long shelfId;
    private String content;
    private BigDecimal rating;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Boolean isPublic;

    // 拷贝构造函数
    public BookReview(BookReview other) {
        this.id = other.id;
        this.user = other.user;
        this.book = other.book;
        this.shelfId = other.shelfId;
        this.content = other.content;
        this.rating = other.rating;
        this.createTime = other.createTime;
        this.updateTime = other.updateTime;
        this.isPublic = other.isPublic;
    }

    // 通过BookReviewService对象的get方法建立的构造函数
    public BookReview(BookReviewService service) {
        this.id = service.getId();
        this.user = new BookUser(service.getUser());
        this.book = new Book(service.getBook());
        this.shelfId = service.getShelfId();
        this.content = service.getContent();
        this.rating = service.getRating();
        this.createTime = service.getCreateTime();
        this.updateTime = service.getUpdateTime();
        this.isPublic = service.getIsPublic();
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

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }
}