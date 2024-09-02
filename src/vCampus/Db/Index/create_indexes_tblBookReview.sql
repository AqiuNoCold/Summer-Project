DELIMITER //

CREATE PROCEDURE create_indexes_tblBookReview_if_not_exists()
BEGIN
    -- 为 userId 添加索引
    IF NOT EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_SCHEMA = 'vCampus' AND TABLE_NAME = 'tblBookReview' AND INDEX_NAME = 'idx_user_id') THEN
        CREATE INDEX idx_user_id ON tblBookReview(user_id);
    END IF;

    -- 为 bookId 添加索引
    IF NOT EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_SCHEMA = 'vCampus' AND TABLE_NAME = 'tblBookReview' AND INDEX_NAME = 'idx_book_id') THEN
        CREATE INDEX idx_book_id ON tblBookReview(book_id);
    END IF;

    -- 为 shelfId 添加索引
    IF NOT EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_SCHEMA = 'vCampus' AND TABLE_NAME = 'tblBookReview' AND INDEX_NAME = 'idx_shelf_id') THEN
        CREATE INDEX idx_shelf_id ON tblBookReview(shelf_id);
    END IF;

    -- 为 isPublic 添加索引
    IF NOT EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_SCHEMA = 'vCampus' AND TABLE_NAME = 'tblBookReview' AND INDEX_NAME = 'idx_is_public') THEN
        CREATE INDEX idx_is_public ON tblBookReview(is_public);
    END IF;

    -- 为 rating 添加索引
    IF NOT EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_SCHEMA = 'vCampus' AND TABLE_NAME = 'tblBookReview' AND INDEX_NAME = 'idx_rating') THEN
        CREATE INDEX idx_rating ON tblBookReview(rating);
    END IF;

    -- 为 createTime 添加索引
    IF NOT EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_SCHEMA = 'vCampus' AND TABLE_NAME = 'tblBookReview' AND INDEX_NAME = 'idx_create_time') THEN
        CREATE INDEX idx_create_time ON tblBookReview(create_time);
    END IF;

    -- 为 updateTime 添加索引
    IF NOT EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_SCHEMA = 'vCampus' AND TABLE_NAME = 'tblBookReview' AND INDEX_NAME = 'idx_update_time') THEN
        CREATE INDEX idx_update_time ON tblBookReview(update_time);
    END IF;
END //

DELIMITER ;

-- 调用存储过程
CALL create_indexes_tblBookReview_if_not_exists();