package vCampus.Service;

import vCampus.Entity.Books.BorrowRecord;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BorrowRecordService {
    private static final BigDecimal DAILY_FINE_RATE = new BigDecimal("0.50"); // 示例罚款率

    // 判断是否逾期
    public boolean isOverdue(BorrowRecord record) {
        if (record.getStatus() == BorrowRecord.BorrowStatus.BORROWING && record.getReturnDate() != null) {
            LocalDate currentDate = LocalDate.now();
            return currentDate.isAfter(record.getReturnDate());
        }
        return false;
    }

    // 计算逾期天数
    public int calculateOverdueDays(BorrowRecord record) {
        if (isOverdue(record)) {
            LocalDate currentDate = LocalDate.now();
            long diffInDays = ChronoUnit.DAYS.between(record.getReturnDate(), currentDate);
            return (int) diffInDays;
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