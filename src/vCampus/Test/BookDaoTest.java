package vCampus.Test;

import java.util.Scanner;
import java.util.List;

import vCampus.Dao.Books.BookDao;
import vCampus.Dao.Criteria.BookSearchCriteria;
import vCampus.Dao.Criteria.BookSortCriteria;
import vCampus.Dao.Criteria.SortCriteria.SortOrder;
import vCampus.Service.Books.BookService;

public class BookDaoTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BookDao bookDao = new BookDao();

        while (true) {
            System.out.println("请选择要测试的方法：");
            System.out.println("1. getTotalBooks");
            System.out.println("2. findBooksByPage");
            System.out.println("0. 退出");
            int choice = scanner.nextInt();
            scanner.nextLine(); // 清除缓冲区

            if (choice == 0) {
                break;
            }

            BookSearchCriteria searchCriteria = new BookSearchCriteria();
            boolean firstCondition = true;
            while (true) {
                System.out.println("请选择搜索字段：");
                System.out.println("1. isbn");
                System.out.println("2. isbn13");
                System.out.println("3. title");
                System.out.println("4. authors");
                System.out.println("5. language");
                System.out.println("6. subjects");
                System.out.println("7. synopsis");
                System.out.println("8. publisher");
                System.out.println("0. 完成搜索条件构建");
                int fieldChoice = scanner.nextInt();
                scanner.nextLine(); // 清除缓冲区

                if (fieldChoice == 0) {
                    break;
                }

                String field = "";
                switch (fieldChoice) {
                    case 1:
                        field = "isbn";
                        break;
                    case 2:
                        field = "isbn13";
                        break;
                    case 3:
                        field = "title";
                        break;
                    case 4:
                        field = "authors";
                        break;
                    case 5:
                        field = "language";
                        break;
                    case 6:
                        field = "subjects";
                        break;
                    case 7:
                        field = "synopsis";
                        break;
                    case 8:
                        field = "publisher";
                        break;
                    default:
                        System.out.println("无效选择，请重新选择。");
                        continue;
                }
                System.out.println("请输入搜索内容：");
                String value = scanner.nextLine();

                String operator = "AND"; // 默认第一个条件前面是 AND
                if (!firstCondition) {
                    System.out.println("请选择逻辑运算符：");
                    System.out.println("1. AND");
                    System.out.println("2. OR");
                    int operatorChoice = scanner.nextInt();
                    scanner.nextLine(); // 清除缓冲区

                    if (operatorChoice == 1) {
                        operator = "AND";
                    } else if (operatorChoice == 2) {
                        operator = "OR";
                    } else {
                        System.out.println("无效选择，请重新选择。");
                        continue;
                    }
                }
                searchCriteria.addCriteria(field, value, operator);
                firstCondition = false;

                System.out.println("当前搜索条件：" + searchCriteria);

                System.out.println("是否添加更多搜索条件？(y/n)");
                String moreCriteria = scanner.nextLine();
                if (moreCriteria.equalsIgnoreCase("n")) {
                    break;
                }
            }

            System.out.println("最终搜索条件：" + searchCriteria);

            int totalBooks = bookDao.getTotalBooks(searchCriteria);
            System.out.println("符合条件的书籍总数：" + totalBooks);

            BookSortCriteria sortCriteria = new BookSortCriteria();
            while (true) {
                System.out.println("请选择排序字段：");
                System.out.println("1. copy_count");
                System.out.println("2. review_count");
                System.out.println("3. average_rating");
                System.out.println("4. favorite_count");
                System.out.println("5. borrow_count");
                System.out.println("0. 完成排序条件构建");
                int sortChoice = scanner.nextInt();
                scanner.nextLine(); // 清除缓冲区

                if (sortChoice == 0) {
                    break;
                }

                String sortField = "";
                switch (sortChoice) {
                    case 1:
                        sortField = "copy_count";
                        break;
                    case 2:
                        sortField = "review_count";
                        break;
                    case 3:
                        sortField = "average_rating";
                        break;
                    case 4:
                        sortField = "favorite_count";
                        break;
                    case 5:
                        sortField = "borrow_count";
                        break;
                    default:
                        System.out.println("无效选择，请重新选择。");
                        continue;
                }

                System.out.println("请选择排序顺序：");
                System.out.println("1. 正序 (ASC)");
                System.out.println("2. 倒序 (DESC)");
                int orderChoice = scanner.nextInt();
                scanner.nextLine(); // 清除缓冲区

                SortOrder sortOrder;
                if (orderChoice == 1) {
                    sortOrder = SortOrder.ASC;
                } else if (orderChoice == 2) {
                    sortOrder = SortOrder.DESC;
                } else {
                    System.out.println("无效选择，请重新选择。");
                    continue;
                }

                sortCriteria.addCriteria(sortField, sortOrder);

                System.out.println("是否添加更多排序条件？(y/n)");
                String moreSortCriteria = scanner.nextLine();
                if (moreSortCriteria.equalsIgnoreCase("n")) {
                    break;
                }
            }

            if (choice == 2) {
                System.out.println("请输入每页显示的记录数：");
                int pageSize = scanner.nextInt();
                scanner.nextLine(); // 清除缓冲区

                int currentPage = 1;
                while (true) {
                    List<String> bookIds = bookDao.findBooksByPage(searchCriteria, sortCriteria, currentPage, pageSize);
                    System.out.println("第 " + currentPage + " 页的书籍：");
                    for (String bookId : bookIds) {
                        BookService book = bookDao.find(bookId);
                        System.out.println(book);
                    }
                    System.out.println("请选择操作：");
                    System.out.println("1. 上一页");
                    System.out.println("2. 下一页");
                    System.out.println("0. 退出");
                    int pageChoice = scanner.nextInt();
                    scanner.nextLine(); // 清除缓冲区

                    if (pageChoice == 0) {
                        break;
                    } else if (pageChoice == 1) {
                        if (currentPage > 1) {
                            currentPage--;
                        } else {
                            System.out.println("已经是第一页！");
                        }
                    } else if (pageChoice == 2) {
                        if (currentPage * pageSize < totalBooks) {
                            currentPage++;
                        } else {
                            System.out.println("已经是最后一页！");
                        }
                    } else {
                        System.out.println("无效选择，请重新选择。");
                    }
                }
            }
        }

        scanner.close();
    }
}