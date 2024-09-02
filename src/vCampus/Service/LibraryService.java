package vCampus.Service;

import vCampus.Dao.Books.BorrowRecordDao;
import vCampus.Entity.Books.BookUser;
import vCampus.Entity.Books.BorrowRecord;
import vCampus.Service.Books.BookService;
import vCampus.Service.Books.BookUserService;
import vCampus.Service.Books.BorrowRecordService;
import java.time.LocalDate;

public class LibraryService {
    private BorrowRecordDao borrowRecordDao;

    public LibraryService() {
        this.borrowRecordDao = new BorrowRecordDao();
    }

    // 登录
    public BookUser login(String id) {
        BookUserService bookUser = new BookUserService(id);
        return new BookUser(bookUser);
    }

    // 借书方法
    public BorrowRecord borrowBook(BookUser bookUser, String bookId) {
        // 获取当前用户
        BookUserService currentUser = new BookUserService(bookUser);

        // 查找要借阅的书籍
        BookService book = new BookService(bookId);

        // 检查图书的副本数量
        if (book.getCopyCount() <= 0) {
            throw new IllegalStateException("图书 " + bookId + " 的副本数量不足，无法借阅");
        }

        // 创建新的借阅记录
        BorrowRecordService borrowRecord = new BorrowRecordService(
                LocalDate.now(), // 借阅日期为当前日期
                book,
                currentUser);

        // 将借阅记录添加到数据库
        if (borrowRecordDao.add(borrowRecord)) {
            // 更新图书的借阅数量和副本数量
            book.borrowBook();
            borrowRecord.setBook(book);
            return new BorrowRecord(borrowRecord);
        } else {
            return null;
        }
    }

    // 还书方法
    public BorrowRecord returnBook(BorrowRecord borrowRecord) {
        BorrowRecordService borrowRecordService = new BorrowRecordService(borrowRecord);
        // 获取当前用户
        BookUserService currentUser = borrowRecordService.getBookUser();
        if (currentUser == null) {
            throw new IllegalStateException("当前没有登录的用户");
        }

        // 查找要归还的书籍
        BookService book = borrowRecordService.getBook();
        if (book == null) {
            throw new IllegalArgumentException("找不到借阅记录中的书籍");
        }

        // 检查是否逾期
        if (borrowRecordService.isOverdue()) {
            // 调用付款函数（目前用打印语句占位）
            System.out.println("借阅记录逾期，请支付罚款！");
            // 这里可以调用实际的付款函数，并检查付款是否成功
            boolean paymentSuccess = false; // 假设付款失败
            if (!paymentSuccess) {
                throw new IllegalStateException("逾期未付款，无法归还书籍");
            }
        }

        // 更新图书的副本数量
        book.returnBook();
        borrowRecordService.setBook(book);

        // 更新借阅记录的状态
        borrowRecordService.setStatus(BorrowRecordService.BorrowStatus.RETURNED);

        return new BorrowRecord(borrowRecordService);
    }
}