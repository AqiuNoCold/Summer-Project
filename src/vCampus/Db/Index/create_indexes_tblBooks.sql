DELIMITER //

CREATE PROCEDURE create_indexes_if_not_exists()
BEGIN
    -- 为 is_deleted 添加索引
    IF NOT EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_SCHEMA = 'vCampus' AND TABLE_NAME = 'tblBooks' AND INDEX_NAME = 'idx_is_deleted') THEN
        CREATE INDEX idx_is_deleted ON tblBooks(is_deleted);
    END IF;

    -- 为 language 添加索引
    IF NOT EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_SCHEMA = 'vCampus' AND TABLE_NAME = 'tblBooks' AND INDEX_NAME = 'idx_language') THEN
        CREATE INDEX idx_language ON tblBooks(language);
    END IF;

    -- 为 title 和 title_long 添加全文索引
    IF NOT EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_SCHEMA = 'vCampus' AND TABLE_NAME = 'tblBooks' AND INDEX_NAME = 'idx_fulltext_title') THEN
        CREATE FULLTEXT INDEX idx_fulltext_title ON tblBooks(title, title_long);
    END IF;

    -- 为 authors 添加全文索引
    IF NOT EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_SCHEMA = 'vCampus' AND TABLE_NAME = 'tblBooks' AND INDEX_NAME = 'idx_fulltext_authors') THEN
        CREATE FULLTEXT INDEX idx_fulltext_authors ON tblBooks(authors);
    END IF;

    -- 为 subjects 添加全文索引
    IF NOT EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_SCHEMA = 'vCampus' AND TABLE_NAME = 'tblBooks' AND INDEX_NAME = 'idx_fulltext_subjects') THEN
        CREATE FULLTEXT INDEX idx_fulltext_subjects ON tblBooks(subjects);
    END IF;
END //

DELIMITER ;

-- 调用存储过程
CALL create_indexes_if_not_exists();