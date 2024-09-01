USE vCampus;

-- 为 userId 添加索引
CREATE INDEX idx_user_id ON tblBookReviews(userId);

-- 为 bookId 添加索引
CREATE INDEX idx_book_id ON tblBookReviews(bookId);

-- 为 shelfId 添加索引
CREATE INDEX idx_shelf_id ON tblBookReviews(shelfId);

-- 为 isPublic 添加索引
CREATE INDEX idx_is_public ON tblBookReviews(isPublic);

-- 为 rating 添加索引
CREATE INDEX idx_rating ON tblBookReviews(rating);

-- 为 createTime 添加索引
CREATE INDEX idx_create_time ON tblBookReviews(createTime);

-- 为 updateTime 添加索引
CREATE INDEX idx_update_time ON tblBookReviews(updateTime);