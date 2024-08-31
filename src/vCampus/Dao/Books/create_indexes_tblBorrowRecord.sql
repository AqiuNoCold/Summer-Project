USE vCampus;

-- 为 user_id 添加索引
CREATE INDEX idx_user_id ON tblBorrowRecord(user_id);

-- 为 book_id 添加索引
CREATE INDEX idx_book_id ON tblBorrowRecord(book_id);

-- 为 borrow_date 添加索引
CREATE INDEX idx_borrow_date ON tblBorrowRecord(borrow_date);

-- 为 status 添加索引
CREATE INDEX idx_status ON tblBorrowRecord(status);

-- 为 is_deleted 添加索引
CREATE INDEX idx_is_deleted ON tblBorrowRecord(is_deleted);