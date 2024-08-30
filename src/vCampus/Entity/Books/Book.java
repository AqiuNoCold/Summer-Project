package vCampus.Entity.Books;

import java.math.BigDecimal;

public class Book {
    private String isbn; // ISBN
    private BigDecimal msrp; // 建议零售价
    private String image; // 图片
    private int pages; // 页数
    private String title; // 标题
    private String isbn13; // ISBN-13
    private String authors; // 作者
    private String binding; // 装订方式
    private String edition; // 版本
    private String related; // 相关书籍
    private String language; // 语言
    private String subjects; // 主题
    private String synopsis; // 简介
    private String publisher; // 出版社
    private String dimensions; // 尺寸
    private String titleLong; // 长标题
    private String datePublished; // 出版日期
    private int copyCount; // 复本数量
    private int reviewCount; // 书评数量
    private BigDecimal averageRating; // 平均评分
    private int favoriteCount; // 收藏数量
    private int borrowCount; // 借阅数量
    private boolean isActive; // 是否激活
    private boolean isDeleted; // 是否删除

    public Book(String isbn, BigDecimal msrp, String image, int pages, String title, String isbn13, String authors,
            String binding, String edition, String related, String language, String subjects, String synopsis,
            String publisher, String dimensions, String titleLong, String datePublished, int copyCount,
            int reviewCount, BigDecimal averageRating, int favoriteCount, int borrowCount, boolean isActive,
            boolean isDeleted) {
        this.isbn = isbn;
        this.msrp = msrp;
        this.image = image;
        this.pages = pages;
        this.title = title;
        this.isbn13 = isbn13;
        this.authors = authors;
        this.binding = binding;
        this.edition = edition;
        this.related = related;
        this.language = language;
        this.subjects = subjects;
        this.synopsis = synopsis;
        this.publisher = publisher;
        this.dimensions = dimensions;
        this.titleLong = titleLong;
        this.datePublished = datePublished;
        this.copyCount = copyCount;
        this.reviewCount = reviewCount;
        this.averageRating = averageRating;
        this.favoriteCount = favoriteCount;
        this.borrowCount = borrowCount;
        this.isActive = isActive;
        this.isDeleted = isDeleted;
    }

    // 返回组合主键
    public String getId() {
        return isbn + "," + isbn13;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public BigDecimal getMsrp() {
        return msrp;
    }

    public void setMsrp(BigDecimal msrp) {
        this.msrp = msrp;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getBinding() {
        return binding;
    }

    public void setBinding(String binding) {
        this.binding = binding;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getRelated() {
        return related;
    }

    public void setRelated(String related) {
        this.related = related;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSubjects() {
        return subjects;
    }

    public void setSubjects(String subjects) {
        this.subjects = subjects;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public String getTitleLong() {
        return titleLong;
    }

    public void setTitleLong(String titleLong) {
        this.titleLong = titleLong;
    }

    public String getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(String datePublished) {
        this.datePublished = datePublished;
    }

    public int getCopyCount() {
        return copyCount;
    }

    public void setCopyCount(int copyCount) {
        this.copyCount = copyCount;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public BigDecimal getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(BigDecimal averageRating) {
        this.averageRating = averageRating;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public int getBorrowCount() {
        return borrowCount;
    }

    public void setBorrowCount(int borrowCount) {
        this.borrowCount = borrowCount;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public String toString() {
        return String.format(
                "+-----------------+----------------------------------+\n" +
                        "| Field           | Value                            |\n" +
                        "+-----------------+----------------------------------+\n" +
                        "| ISBN            | %-32s |\n" +
                        "| MSRP            | %-32s |\n" +
                        "| Image           | %-32s |\n" +
                        "| Pages           | %-32s |\n" +
                        "| Title           | %-32s |\n" +
                        "| ISBN13          | %-32s |\n" +
                        "| Authors         | %-32s |\n" +
                        "| Binding         | %-32s |\n" +
                        "| Edition         | %-32s |\n" +
                        "| Related         | %-32s |\n" +
                        "| Language        | %-32s |\n" +
                        "| Subjects        | %-32s |\n" +
                        "| Synopsis        | %-32s |\n" +
                        "| Publisher       | %-32s |\n" +
                        "| Dimensions      | %-32s |\n" +
                        "| Title Long      | %-32s |\n" +
                        "| Date Published  | %-32s |\n" +
                        "| Copy Count      | %-32d |\n" +
                        "| Review Count    | %-32d |\n" +
                        "| Average Rating  | %-32s |\n" +
                        "| Favorite Count  | %-32d |\n" +
                        "| Borrow Count    | %-32d |\n" +
                        "| Is Active       | %-32b |\n" +
                        "| Is Deleted      | %-32b |\n" +
                        "+-----------------+----------------------------------+\n",
                isbn, msrp, image, pages, title, isbn13, authors, binding, edition, related, language, subjects,
                synopsis, publisher,
                dimensions, titleLong, datePublished, copyCount, reviewCount, averageRating, favoriteCount, borrowCount,
                isActive, isDeleted);
    }
}