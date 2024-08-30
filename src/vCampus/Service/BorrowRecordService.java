package vCampus.Service;

import vCampus.Entity.Books.BorrowRecord;
import vCampus.Entity.Books.Book;

import java.math.BigDecimal;
import java.util.Date;

public class BorrowRecordService {
    private static final BigDecimal DAILY_FINE_RATE = new BigDecimal("0.50"); // 示例罚款率

    // 判断是否逾期
    public boolean isOverdue(BorrowRecord record) {
        if (record.getStatus() == BorrowRecord.BorrowStatus.BORROWING && record.getReturnDate() != null) {
            Date currentDate = new Date();
            return currentDate.after(record.getReturnDate());
        }
        return false;
    }

    // 计算逾期天数
    public int calculateOverdueDays(BorrowRecord record) {
        if (isOverdue(record)) {
            Date currentDate = new Date();
            long diffInMillies = currentDate.getTime() - record.getReturnDate().getTime();
            return (int) (diffInMillies / (1000 * 60 * 60 * 24));
        }
        return 0;
    }

    // 计算罚款金额
    public BigDecimal calculateFine(BorrowRecord record) {
        int overdueDays = calculateOverdueDays(record);
        return DAILY_FINE_RATE.multiply(new BigDecimal(overdueDays));
    }

    // 处理罚款达到建议零售价
    public boolean handleOverdueAndLost(BorrowRecord record) {
        if (isOverdue(record)) {
            BigDecimal fine = calculateFine(record);
            BigDecimal suggestedRetailPrice = record.getBook().getMsrp();

            if (fine.compareTo(suggestedRetailPrice) >= 0) {
                record.setStatus(BorrowRecord.BorrowStatus.LOST);
                return false;
            }
        }
        return true;
    }
}