USE vCampus;

-- 为 isDeleted 添加索引
CREATE INDEX idx_is_deleted ON tblBooks(is_deleted);

-- 为 language 添加索引
CREATE INDEX idx_language ON tblBooks(language);

-- 为 title 和 title_long 添加全文索引
CREATE FULLTEXT INDEX idx_fulltext_title ON tblBooks(title, title_long);

-- 为 authors 添加全文索引
CREATE FULLTEXT INDEX idx_fulltext_authors ON tblBooks(authors);

-- 为 subjects 添加全文索引
CREATE FULLTEXT INDEX idx_fulltext_subjects ON tblBooks(subjects);

-- 为 synopsis 添加全文索引
CREATE FULLTEXT INDEX idx_fulltext_synopsis ON tblBooks(synopsis);

-- 为 publisher 添加全文索引
CREATE FULLTEXT INDEX idx_fulltext_publisher ON tblBooks(publisher);

-- 为 copy_count 添加索引
CREATE INDEX idx_copy_count ON tblBooks(copy_count);

-- 为 review_count 添加索引
CREATE INDEX idx_review_count ON tblBooks(review_count);

-- 为 average_rating 添加索引
CREATE INDEX idx_average_rating ON tblBooks(average_rating);

-- 为 favorite_count 添加索引
CREATE INDEX idx_favorite_count ON tblBooks(favorite_count);

-- 为 borrow_count 添加索引
CREATE INDEX idx_borrow_count ON tblBooks(borrow_count);