package vCampus.Entity.Books;

public class BookReview {
    private Long id;
    private BookUser user;
    private Book book;
    private String content;
    private Double rating;
    private LocalDateTime createTime;

    // 构造方法、getter和setter方法

    // ... 其他方法，比如：
    // updateContent: 更新书评内容
    // updateRating: 更新评分
}
