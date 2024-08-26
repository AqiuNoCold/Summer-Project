package vCampus.Entity;

public class BookList {
    private static final int DEFAULT_SIZE = 10; // 定义书架默认能放多少本书
    private Book[] books = new Book[DEFAULT_SIZE]; // 书架数组
    private int usedSize; // 记录当前书架中有几本书

    // Getter and setter methods for the attributes (省略了详细代码)
    // ...

    // 其他操作，如新增图书、删除图书、显示图书等，可以在这里实现
    // ...

}
