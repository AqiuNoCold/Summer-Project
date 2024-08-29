package vCampus.Entity.Books;

import java.util.Date;

public class BorrowRecord {
    private Long id; // 借阅记录ID
    private Date borrowDate; // 借阅日期
    private Date returnDate; // 归还日期
    private Book book; // 借阅的图书
    private BookUser bookUser; // 借阅的用户

    public enum BorrowStatus {
        BORROWING, RETURNED, LOST
    }

    private BorrowStatus status;

    // 构造方法
    public BorrowRecord(Long id, Date borrowDate, Date returnDate, Book book, BookUser bookUser, BorrowStatus status) {
        this.id = id;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.book = book;
        this.bookUser = bookUser;
        this.status = status;
    }

    // Getter 和 Setter 方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(Date borrowDate) {
        if (borrowDate == null) {
            this.borrowDate = new Date(); // 使用当前时间
        } else {
            this.borrowDate = borrowDate;
        }
    }

    public void setBorrowDate() {
        setBorrowDate(new Date());
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
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

    // 判断是否逾期
    public boolean isOverdue() {
        if (status == BorrowStatus.BORROWING && returnDate != null) {
            Date currentDate = new Date();
            return currentDate.after(returnDate);
        }
        return false;
    }

    // 计算逾期天数
    public int calculateOverdueDays() {
        if (isOverdue()) {
            Date currentDate = new Date();
            long diffInMillies = currentDate.getTime() - returnDate.getTime();
            return (int) (diffInMillies / (1000 * 60 * 60 * 24));
        }
        return 0;
    }

    // 计算罚款金额
    public double calculateFine() {
        int overdueDays = calculateOverdueDays();
        return overdueDays * DAILY_FINE_RATE;
    }

    // 处理书籍丢失或罚款达到建议零售价
    public boolean handleOverdueAndLost() {
        if (isOverdue()) {
            double fine = calculateFine();
            double suggestedRetailPrice;
            try {
                suggestedRetailPrice = Double.parseDouble(book.getMsrp());
            } catch (NumberFormatException e) {
                return false; // 无法解析书籍的建议零售价
            }

            if (fine >= suggestedRetailPrice) {
                status = BorrowStatus.LOST;
            }
        }
        return true;
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
                        "}",
                "ID", id,
                "Borrow Date", borrowDate,
                "Return Date", returnDate,
                "Book", book,
                "Book User", bookUser,
                "Status", status);
    }
}