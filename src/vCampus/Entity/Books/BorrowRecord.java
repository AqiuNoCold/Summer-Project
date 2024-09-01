package vCampus.Entity.Books;

import java.math.BigDecimal;
import java.time.LocalDate;

import vCampus.Service.Books.BorrowRecordService;
import vCampus.Service.Books.BorrowRecordService.BorrowStatus;

public class BorrowRecord {
    private Long id; // 借阅记录ID
    private LocalDate borrowDate; // 借阅日期
    private LocalDate returnDate; // 归还日期
    private Book book; // 借阅的图书
    private BookUser bookUser; // 借阅的用户
    private boolean isDeleted; // 是否删除
    private BorrowStatus status; // 借阅状态
    private Boolean isOverdue; // 是否逾期
    private int overdueDays; // 逾期天数
    private BigDecimal fine; // 罚款金额

    // 拷贝构造函数
    public BorrowRecord(BorrowRecord other) {
        this.id = other.id;
        this.borrowDate = other.borrowDate;
        this.returnDate = other.returnDate;
        this.book = other.book;
        this.bookUser = other.bookUser;
        this.isDeleted = other.isDeleted;
        this.status = other.status;
        this.isOverdue = other.isOverdue;
        this.overdueDays = other.overdueDays;
        this.fine = other.fine;
    }

    // 通过BorrowRecordService对象的get方法建立的构造函数
    public BorrowRecord(BorrowRecordService service) {
        this.id = service.getId();
        this.borrowDate = service.getBorrowDate();
        this.returnDate = service.getReturnDate();
        this.book = new Book(service.getBook());
        this.bookUser = new BookUser(service.getBookUser());
        this.isDeleted = service.getIsDeleted();
        this.status = service.getStatus();
        this.isOverdue = service.isOverdue();
        this.overdueDays = service.getOverdueDays();
        this.fine = service.getFine(service);
    }

    // Getter和Setter方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public BookUser getBookUser() {
        return bookUser;
    }

    public void setBookUser(BookUser bookUser) {
        this.bookUser = bookUser;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public BorrowStatus getStatus() {
        return status;
    }

    public void setStatus(BorrowStatus status) {
        this.status = status;
    }

    public Boolean getIsOverdue() {
        return isOverdue;
    }

    public void setIsOverdue(Boolean isOverdue) {
        this.isOverdue = isOverdue;
    }

    public int getOverdueDays() {
        return overdueDays;
    }

    public void setOverdueDays(int overdueDays) {
        this.overdueDays = overdueDays;
    }

    public BigDecimal getFine() {
        return fine;
    }

    public void setFine(BigDecimal fine) {
        this.fine = fine;
    }

    // 重写toString方法
    @Override
    public String toString() {
        return String.format(
                "+----------------+-------------------------+\n" +
                        "| %-14s | %-23s |\n" +
                        "+----------------+-------------------------+\n" +
                        "| %-14s | %-23s |\n" +
                        "| %-14s | %-23s |\n" +
                        "| %-14s | %-23s |\n" +
                        "| %-14s | %-23s |\n" +
                        "| %-14s | %-23s |\n" +
                        "| %-14s | %-23s |\n" +
                        "| %-14s | %-23s |\n" +
                        "| %-14s | %-23s |\n" +
                        "| %-14s | %-23s |\n" +
                        "| %-14s | %-23s |\n" +
                        "+----------------+-------------------------+\n",
                "Field", "Value",
                "ID", id,
                "Borrow Date", borrowDate,
                "Return Date", returnDate,
                "Book", book,
                "Book User", bookUser,
                "Is Deleted", isDeleted,
                "Status", status,
                "Is Overdue", isOverdue,
                "Overdue Days", overdueDays,
                "Fine", fine);
    }
}