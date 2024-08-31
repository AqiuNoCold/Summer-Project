package vCampus.Entity.Books;

import java.time.LocalDate;

import vCampus.Dao.Books.BorrowRecordDao;

public class BorrowRecord {
    private Long id; // 借阅记录ID
    private LocalDate borrowDate; // 借阅日期
    private LocalDate returnDate; // 归还日期
    private Book book; // 借阅的图书
    private BookUser bookUser; // 借阅的用户
    private boolean isDeleted; // 是否删除

    public enum BorrowStatus {
        BORROWING, RETURNED, LOST
    }

    private BorrowStatus status;

    // 新增的isOverdue方法
    public boolean isOverdue() {
        if (status == BorrowStatus.BORROWING && returnDate != null) {
            return returnDate.isBefore(LocalDate.now());
        }
        return false;
    }

    // 构造方法
    public BorrowRecord(Long id,
            LocalDate borrowDate,
            LocalDate returnDate, Book book, BookUser bookUser, BorrowStatus status, boolean isDeleted) {
        this.id = id;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.book = book;
        this.bookUser = bookUser;
        this.status = status;
        this.isDeleted = isDeleted;
    }

    // 新的构造方法，只接受初始借书时间
    public BorrowRecord(LocalDate borrowDate, Book book, BookUser bookUser) {
        this.borrowDate = borrowDate;
        this.returnDate = borrowDate.plusMonths(1); // 默认还书时间为1个月后
        this.book = book;
        this.bookUser = bookUser;
        this.status = BorrowStatus.BORROWING;
        this.isDeleted = false;
        this.id = new BorrowRecordDao().save(this);
    }

    // Getter 和 Setter 方法
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

    public BorrowStatus getStatus() {
        return status;
    }

    public void setStatus(BorrowStatus status) {
        this.status = status;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return String.format(
                "BorrowRecord {\n" +
                        "  %-15s: %s\n" +
                        "  %-15s: %s\n" +
                        "  %-15s: %s\n" +
                        "  %-15s: %s\n" +
                        "  %-15s: %s\n" +
                        "  %-15s: %s\n" +
                        "  %-15s: %s\n" +
                        "}",
                "ID", id,
                "Borrow Date", borrowDate,
                "Return Date", returnDate,
                "Book", book,
                "Book User", bookUser,
                "Status", status,
                "Is Deleted", isDeleted);
    }
}