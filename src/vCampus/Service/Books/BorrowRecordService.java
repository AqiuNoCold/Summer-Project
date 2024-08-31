package vCampus.Service.Books;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import vCampus.Dao.Books.BorrowRecordDao;

public class BorrowRecordService {
    private Long id; // 借阅记录ID
    private LocalDate borrowDate; // 借阅日期
    private LocalDate returnDate; // 归还日期
    private BookService book; // 借阅的图书
    private BookUserService bookUser; // 借阅的用户
    private boolean isDeleted; // 是否删除

    public static enum BorrowStatus {
        BORROWING, RETURNED, LOST
    }

    private static final BigDecimal DAILY_FINE_RATE = new BigDecimal("0.50"); // 示例罚款率

    private BorrowStatus status;

    // 构造方法
    public BorrowRecordService(Long id,
            LocalDate borrowDate,
            LocalDate returnDate, BookService book, BookUserService bookUser, BorrowStatus status, boolean isDeleted) {
        this.id = id;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.book = book;
        this.bookUser = bookUser;
        this.status = status;
        this.isDeleted = isDeleted;
    }

    // 新的构造方法，只接受初始借书时间
    public BorrowRecordService(LocalDate borrowDate, BookService book, BookUserService bookUser) {
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

    public BookService getBook() {
        return book;
    }

    public void setBook(BookService book) {
        this.book = book;
    }

    public BookUserService getBookUser() {
        return bookUser;
    }

    public void setBookUser(BookUserService bookUser) {
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

    // 新增的isOverdue方法
    public boolean isOverdue() {
        if (status == BorrowStatus.BORROWING && returnDate != null) {
            return returnDate.isBefore(LocalDate.now());
        }
        return false;
    }

    // 计算逾期天数
    public int getOverdueDays() {
        if (isOverdue()) {
            LocalDate currentDate = LocalDate.now();
            long diffInDays = ChronoUnit.DAYS.between(returnDate, currentDate);
            return (int) diffInDays;
        }
        return 0;
    }

    // 计算罚款金额
    public BigDecimal getFine(BorrowRecordService record) {
        int overdueDays = getOverdueDays();
        BigDecimal suggestedRetailPrice = record.getBook().getMsrp();
        BigDecimal fine = DAILY_FINE_RATE.multiply(new BigDecimal(overdueDays));
        if (fine.compareTo(suggestedRetailPrice) >= 0) {
            record.setStatus(BorrowRecordService.BorrowStatus.LOST);
            new BorrowRecordDao().update(record);
            fine = suggestedRetailPrice;
        }
        return fine;
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