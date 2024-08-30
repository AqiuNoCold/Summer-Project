# 数据库字段说明

本模块共包含5个实体类，分别对应5个数据表。5个数据表的关系如下：

| 类名            | 描述                                                           | 与其他类的关系                           |
| --------------- | -------------------------------------------------------------- | ---------------------------------------- |
| tblBooks        | 表示一本书，除基本信息外，还包含副本数量、评分信息与收藏数量。 | BookReview和BorrowRecord都唯一包含BookId |
| tblBookShelf    | 表示书架/收藏夹，包含多个图书和书评。不要求为每本书添加书评。  | 与BookReview和Book一对多关系。           |
| tblBookReview   | 表示一条书评，包含评论内容、用户、图书、书架、时间等信息。     | 可以从属于BookShelf，也可以独立存在。    |
| tblBookUser     | 表示一个用户，包含一卡通号，书架/收藏夹和借阅记录。            | 与BorrowRecord和BookShelf一对多关系。    |
| tblBorrowRecord | 表示一条借阅记录，包含借阅时间、归还时间、用户、图书等信息。   | 与Book、BookUser多对一关系。             |

## tblBooks

| 字段名         | 数据类型      | 说明                 |
| -------------- | ------------- | -------------------- |
| isbn           | CHAR(13)      | 书籍ISBN，主键       |
| msrp           | DECIMAL(10,2) | 建议零售价           |
| image          | TEXT          | 图片路径             |
| pages          | INT           | 页数                 |
| title          | VARCHAR(255)  | 书名                 |
| isbn13         | CHAR(13)      | ISBN-13，主键        |
| authors        | TEXT          | 作者列表（逗号分隔） |
| binding        | VARCHAR(50)   | 装帧                 |
| edition        | VARCHAR(50)   | 版本                 |
| related        | TEXT          | 相关书籍（逗号分隔） |
| language       | VARCHAR(20)   | 语言                 |
| subjects       | TEXT          | 主题（逗号分隔）     |
| synopsis       | TEXT          | 简介                 |
| publisher      | VARCHAR(100)  | 出版社               |
| dimensions     | VARCHAR(50)   | 尺寸                 |
| title_long     | TEXT          | 长标题               |
| date_published | VARCHAR(20)   | 出版日期             |
| copy_count     | INT           | 副本数量             |
| review_count   | INT           | 评论数量             |
| average_rating | DECIMAL(2,1)  | 平均评分（0-5分）    |
| favorite_count | INT           | 收藏数               |
| borrow_count   | INT           | 累计借阅数           |
| isActive       | Boolen        | 当前是否可借         |
| isDeleted      | Boolen        | 是否已被删除         |

其中，被标记为删除的图书，副本数量为零，不可借阅，正常情况下无法被搜索，但是如果已经位于书单中，可以查看其基本信息。

## tblBookShelf

| 字段名      | 数据类型     | 说明                   |
| ----------- | ------------ | ---------------------- |
| id          | BIGINT       | 书架ID，主键，自增     |
| name        | VARCHAR(255) | 书架名称               |
| create_time | TIMESTAMP    | 创建时间               |
| update_time | TIMESTAMP    | 更新时间               |
| user_id     | BIGINT       | 用户ID，外键           |
| book_ids    | TEXT         | 图书ID列表（逗号分隔） |
| review_ids  | TEXT         | 书评ID列表（逗号分隔） |

删除时，若书评数不为零，询问用户是否一并删除所有书评

## tblBookReview

| 字段名      | 数据类型      | 说明                   |
| ----------- | ------------- | ---------------------- |
| id          | BIGINT        | 主键，自增             |
| user_id     | BIGINT        | 用户ID，外键           |
| book_id     | BIGINT        | 外键，关联到 Book 表   |
| content     | TEXT          | 评论内容               |
| rating      | DECIMAL(3, 2) | 评分，范围 0.00 - 5.00 |
| create_time | TIMESTAMP     | 创建时间               |
| update_time | TIMESTAMP     | 更新时间               |

## tblBookUser

| 字段名            | 数据类型 | 说明                             |
| ----------------- | -------- | -------------------------------- |
| id                | CHAR(9)  | 用户ID，主键，不自增             |
| borrow_record_ids | TEXT     | 关联借阅记录ID的列表（JSON格式） |
| default_shelf_id  | BIGINT   | 默认书架ID                       |
| shelf_ids         | TEXT     | 所有书架ID的列表（JSON格式）     |

## tblBorrowRecord

| 字段名      | 数据类型    | 说明                                  |
| ----------- | ----------- | ------------------------------------- |
| id          | BIGINT      | 借阅记录ID，主键，自增                |
| borrow_date | DATE        | 借阅日期                              |
| return_date | DATE        | 归还日期                              |
| book_id     | BIGINT      | 外键，关联到 Book 表                  |
| user_id     | BIGINT      | 用户ID，外键                          |
| status      | VARCHAR(10) | 借阅状态（BORROWING, RETURNED, LOST） |
| is_deleted  | BOOLEAN     | 是否删除                              |
