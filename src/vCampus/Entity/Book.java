package vCampus.Entity;

public class Book {
    private String isbn;          // ISBN
    private String msrp;          // 建议零售价
    private String image;         // 图片
    private String pages;         // 页数
    private String title;         // 标题
    private String isbn13;        // ISBN-13
    private String authors;       // 作者
    private String edition;       // 版本
    private String language;      // 语言
    private String subjects;      // 主题
    private String synopsis;      // 简介
    private String publisher;     // 出版社
    private String titleLong;     // 长标题
    private String datePublished; // 出版日期

    public Book(String isbn, String msrp, String image, String pages, String title, String isbn13, String authors, String edition, String language, String subjects, String synopsis, String publisher, String titleLong, String datePublished) {
        this.isbn = isbn;
        this.msrp = msrp;
        this.image = image;
        this.pages = pages;
        this.title = title;
        this.isbn13 = isbn13;
        this.authors = authors;
        this.edition = edition;
        this.language = language;
        this.subjects = subjects;
        this.synopsis = synopsis;
        this.publisher = publisher;
        this.titleLong = titleLong;
        this.datePublished = datePublished;
    }

    // Getter and Setter methods
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getMsrp() {
        return msrp;
    }

    public void setMsrp(String msrp) {
        this.msrp = msrp;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
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

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
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
}