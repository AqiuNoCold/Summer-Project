# 服务类说明

为了方便前后端数据的传输，服务类可以与Dao沟通，但实体类不行。

## Book

1. 属性差异比较

    | 属性            | Book       | BookService |
    | --------------- | ---------- | ----------- |
    | `isbn`          | String     | String      |
    | `msrp`          | BigDecimal | BigDecimal  |
    | `image`         | String     | String      |
    | `pages`         | int        | int         |
    | `title`         | String     | String      |
    | `isbn13`        | String     | String      |
    | `authors`       | String     | String      |
    | `binding`       | String     | String      |
    | `edition`       | String     | String      |
    | `related`       | String     | String      |
    | `language`      | String     | String      |
    | `subjects`      | String     | String      |
    | `synopsis`      | String     | String      |
    | `publisher`     | String     | String      |
    | `dimensions`    | String     | String      |
    | `titleLong`     | String     | String      |
    | `datePublished` | String     | String      |
    | `copyCount`     | int        | int         |
    | `reviewCount`   | int        | int         |
    | `averageRating` | BigDecimal | BigDecimal  |
    | `favoriteCount` | int        | int         |
    | `borrowCount`   | int        | int         |
    | `isActive`      | boolean    | boolean     |
    | `isDeleted`     | boolean    | boolean     |

2. 服务类中的特殊方法

    | 方法名         | 返回类型 | 描述       |
    | -------------- | -------- | ---------- |
    | `borrowBook()` | boolean  | 借阅图书。 |
    | `returnBook()` | boolean  | 归还图书。 |

## BookUser

1. 属性差异比较

    | 属性               | BookUser         | BookUserService         |
    | ------------------ | ---------------- | ----------------------- |
    | `defaultBookShelf` | BookShelf        | BookShelfService        |
    | `currentBookShelf` | BookShelf        | BookShelfService        |
    | `bookShelves`      | List\<BookShelf> | List\<BookShelfService> |
    | `firstLogin`       | Boolean          | Boolean                 |

2. 服务类中的特殊方法

    | 方法名                                   | 返回类型                | 描述               |
    | ---------------------------------------- | ----------------------- | ------------------ |
    | `getDefaultBookShelf()`                  | BookShelfService        | 获取默认书架。     |
    | `setDefaultBookShelf(BookShelfService)`  | void                    | 设置默认书架。     |
    | `getCurrentBookShelf()`                  | BookShelfService        | 获取当前书架。     |
    | `setCurrentBookShelf(BookShelfService)`  | void                    | 设置当前书架。     |
    | `getBookShelves()`                       | List\<BookShelfService> | 获取所有书架。     |
    | `setBookShelves(List<BookShelfService>)` | void                    | 设置所有书架。     |
    | `addBookShelf(BookShelfService)`         | void                    | 添加书架。         |
    | `removeBookShelf(BookShelfService)`      | boolean                 | 移除书架。         |
    | `getShelfIds()`                          | List\<String>           | 获取书架ID列表。   |
    | `isFirstLogin()`                         | Boolean                 | 获取首次登录标志。 |

## BorrowRecord

1. 属性差异比较

    | 属性          | BorrowRecord | BorrowRecordService |
    | ------------- | ------------ | ------------------- |
    | `id`          | Long         | Long                |
    | `borrowDate`  | LocalDate    | LocalDate           |
    | `returnDate`  | LocalDate    | LocalDate           |
    | `book`        | Book         | BookService         |
    | `bookUser`    | BookUser     | BookUserService     |
    | `isDeleted`   | boolean      | boolean             |
    | `status`      | BorrowStatus | BorrowStatus        |
    | `isOverdue`   | Boolean      | -                   |
    | `overdueDays` | int          | -                   |
    | `fine`        | BigDecimal   | -                   |

2. 服务类中的特殊方法

    | 方法名                                | 返回类型   | 描述                   |
    | ------------------------------------- | ---------- | ---------------------- |
    | `isOverdue()`                         | boolean    | 判断借阅记录是否逾期。 |
    | `getOverdueDays()`                    | int        | 计算逾期天数。         |
    | `getFine(BorrowRecordService record)` | BigDecimal | 计算罚款金额。         |

## BookShelf

1. 属性差异比较

    | 属性             | BookShelf         | BookShelfService         |
    | ---------------- | ----------------- | ------------------------ |
    | `id`             | Long              | Long                     |
    | `name`           | String            | String                   |
    | `createTime`     | LocalDateTime     | LocalDateTime            |
    | `updateTime`     | LocalDateTime     | LocalDateTime            |
    | `userId`         | String            | String                   |
    | `books`          | List\<Book>       | List\<BookService>       |
    | `reviews`        | List\<BookReview> | List\<BookReviewService> |
    | `isPublic`       | Boolean           | Boolean                  |
    | `subscribeCount` | Integer           | Integer                  |
    | `favoriteCount`  | Integer           | Integer                  |
    | `bookIds`        | -                 | List\<String>            |
    | `reviewIds`      | -                 | List\<String>            |

2. 服务类中的特殊方法

    | 方法名                         | 返回类型 | 描述       |
    | ------------------------------ | -------- | ---------- |
    | `addBook(BookService)`         | boolean  | 添加图书。 |
    | `removeBook(BookService)`      | boolean  | 移除图书。 |
    | `addReview(BookReviewService)` | void     | 添加书评。 |

## BookReview

1. 属性差异比较

    | 属性         | BookReview    | BookReviewService |
    | ------------ | ------------- | ----------------- |
    | `id`         | Long          | Long              |
    | `user`       | BookUser      | BookUserService   |
    | `book`       | Book          | BookService       |
    | `shelfId`    | Long          | Long              |
    | `content`    | String        | String            |
    | `rating`     | BigDecimal    | BigDecimal        |
    | `createTime` | LocalDateTime | LocalDateTime     |
    | `updateTime` | LocalDateTime | LocalDateTime     |
    | `isPublic`   | Boolean       | Boolean           |
    | `userId`     | -             | String            |
    | `bookId`     | -             | String            |

2. 服务类中的特殊方法

    | 方法名        | 返回类型        | 描述         |
    | ------------- | --------------- | ------------ |
    | `getUser()`   | BookUserService | 获取用户。   |
    | `getBook()`   | BookService     | 获取图书。   |
    | `getUserId()` | String          | 获取用户ID。 |
    | `getBookId()` | String          | 获取图书ID。 |
