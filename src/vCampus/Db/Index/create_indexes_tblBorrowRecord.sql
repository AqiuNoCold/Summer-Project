DELIMITER //

CREATE PROCEDURE create_indexes_tblBorrowRecord_if_not_exists()
BEGIN
    -- 为 user_id 添加索引
    IF NOT EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_SCHEMA = 'vCampus' AND TABLE_NAME = 'tblBorrowRecord' AND INDEX_NAME = 'idx_user_id') THEN
        CREATE INDEX idx_user_id ON tblBorrowRecord(user_id);
    END IF;

    -- 为 book_id 添加索引
    IF NOT EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_SCHEMA = 'vCampus' AND TABLE_NAME = 'tblBorrowRecord' AND INDEX_NAME = 'idx_book_id') THEN
        CREATE INDEX idx_book_id ON tblBorrowRecord(book_id);
    END IF;

    -- 为 borrow_date 添加索引
    IF NOT EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_SCHEMA = 'vCampus' AND TABLE_NAME = 'tblBorrowRecord' AND INDEX_NAME = 'idx_borrow_date') THEN
        CREATE INDEX idx_borrow_date ON tblBorrowRecord(borrow_date);
    END IF;

    -- 为 return_date 添加索引
    IF NOT EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_SCHEMA = 'vCampus' AND TABLE_NAME = 'tblBorrowRecord' AND INDEX_NAME = 'idx_return_date') THEN
        CREATE INDEX idx_return_date ON tblBorrowRecord(return_date);
    END IF;

    -- 为 status 添加索引
    IF NOT EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_SCHEMA = 'vCampus' AND TABLE_NAME = 'tblBorrowRecord' AND INDEX_NAME = 'idx_status') THEN
        CREATE INDEX idx_status ON tblBorrowRecord(status);
    END IF;

    -- 为 is_deleted 添加索引
    IF NOT EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_SCHEMA = 'vCampus' AND TABLE_NAME = 'tblBorrowRecord' AND INDEX_NAME = 'idx_is_deleted') THEN
        CREATE INDEX idx_is_deleted ON tblBorrowRecord(is_deleted);
    END IF;
END //

DELIMITER ;

-- 调用存储过程
CALL create_indexes_tblBorrowRecord_if_not_exists();