package vCampus;

import vCampus.Dao.Criteria.*;
import vCampus.Entity.ECard.ECard;
import vCampus.Entity.Grade;
import vCampus.Entity.Student;
import vCampus.Entity.Shop.Product;
import vCampus.Entity.Shop.ShopStudent;
import vCampus.Entity.User;
import vCampus.Entity.Books.*;
import vCampus.Service.*;
import vCampus.StuMS.StuMSServerSrv;
import vCampus.User.IUserServerSrv;
import vCampus.ECard.ECardServerSrv;
//import vCampus.Shop.ShopServerSrv;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static vCampus.ECard.ECardServerSrv.*;

public class MainServer {
    private static final int PORT = 5101;
    private static final int MAX_CONNECTIONS = 10;
    private static ConcurrentHashMap<Socket, Thread> socketMap = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, User> userMap = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started. Waiting for client...");

            while (true) {
                Socket clientSocket = serverSocket.accept();

                if (socketMap.size() >= MAX_CONNECTIONS) {
                    System.out.println("Max connections reached, rejecting new connection.");
                    clientSocket.close();
                    continue;
                }

                Thread clientThread = new Thread(() -> {
                    try {
                        handleClient(clientSocket);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                socketMap.put(clientSocket, clientThread);
                clientThread.start();

                System.out.println("Client connected!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) throws IOException {
        String userid = null;
        try (ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {
            while (true) {
                String model = (String) in.readObject();
                if (model.equals("exit")) {
                    try {
                        userid = (String) in.readObject();
                    } catch (IOException e) {
                        System.out.println("客户端未进行登录操作");
                    }
                    in.close();
                    out.flush();
                    out.close();
                    break;
                }
                String function = (String) in.readObject();
                switch (model) {
                    case "1":
                        userid = (String) in.readObject();
                        LoginPage(function, in, out, userid);
                        break;
                    case "2":
                        CoursePage(function, in, out);
                        break;
                    case "3":
                        EcardPage(function, in, out);
                        break;
                    case "4":
                        LibraryPage(function, in, out);
                        break;
                    case "5":
                        StorePage(function, in, out);
                        break;
                    case "6":
                        StudentRecordPage(function, in, out);
                        break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            socketMap.remove(clientSocket);
            if (userid != null) {
                userMap.remove(userid);
            }
            clientSocket.close();
            System.out.println("Connection closed with " + clientSocket.getRemoteSocketAddress());
        }
    }

    private static void LibraryPage(String function, ObjectInputStream in, ObjectOutputStream out)
            throws IOException, ClassNotFoundException {
        LibraryService libraryService = new LibraryService();
        switch (function) {
            case "login":
                String userId = (String) in.readObject();
                BookUser bookUser = libraryService.login(userId);
                out.writeObject(bookUser);
                break;
            case "borrowBook":
                BookUser borrower = (BookUser) in.readObject();
                String bookId = (String) in.readObject();
                BorrowRecord borrowRecord = libraryService.borrowBook(borrower, bookId);
                out.writeObject(borrowRecord);
                break;
            case "returnBook":
                BorrowRecord returnRecord = (BorrowRecord) in.readObject();
                BorrowRecord returnedRecord = libraryService.returnBook(returnRecord);
                out.writeObject(returnedRecord);
                break;
            case "searchBooks":
                BookSearchCriteria searchCriteria = (BookSearchCriteria) in.readObject();
                BookSortCriteria sortCriteria = (BookSortCriteria) in.readObject();
                int page = (int) in.readObject();
                int pageSize = (int) in.readObject();
                SearchResult<Book> searchResult = libraryService.searchBooks(searchCriteria, sortCriteria, page,
                        pageSize);
                out.writeObject(searchResult);
                break;
            case "getTotalBooks":
                BookSearchCriteria totalSearchCriteria = (BookSearchCriteria) in.readObject();
                int totalBooks = libraryService.getTotalBooks(totalSearchCriteria);
                out.writeObject(totalBooks);
                break;
            case "createBookShelf":
                BookUser shelfUser = (BookUser) in.readObject();
                String shelfName = (String) in.readObject();
                BookUser updatedUser = libraryService.createBookShelf(shelfUser, shelfName);
                out.writeObject(updatedUser);
                break;
            case "setCurrentBookShelf":
                BookUser currentUser = (BookUser) in.readObject();
                BookShelf currentShelf = (BookShelf) in.readObject();
                BookUser updatedCurrentUser = libraryService.setCurrentBookShelf(currentUser, currentShelf);
                out.writeObject(updatedCurrentUser);
                break;
            case "addBookToShelfById":
                BookUser addUser = (BookUser) in.readObject();
                Long addShelfId = (Long) in.readObject();
                String addBookId = (String) in.readObject();
                BookUser updatedAddUser = libraryService.addBookToShelfById(addUser, addShelfId, addBookId);
                out.writeObject(updatedAddUser);
                break;
            case "addBookToShelfByObject":
                BookUser addObjectUser = (BookUser) in.readObject();
                Long addObjectShelfId = (Long) in.readObject();
                Book addObjectBook = (Book) in.readObject();
                BookUser updatedAddObjectUser = libraryService.addBookToShelfByObject(addObjectUser, addObjectShelfId,
                        addObjectBook);
                out.writeObject(updatedAddObjectUser);
                break;
            case "removeBookFromShelfById":
                BookUser removeUser = (BookUser) in.readObject();
                Long removeShelfId = (Long) in.readObject();
                String removeBookId = (String) in.readObject();
                BookUser updatedRemoveUser = libraryService.removeBookFromShelfById(removeUser, removeShelfId,
                        removeBookId);
                out.writeObject(updatedRemoveUser);
                break;
            case "getRandomBooks":
                int count = (int) in.readObject();
//                List<Book> randomBooks = libraryService.getRandomBooks(count);
//                out.writeObject(randomBooks);
                break;
            default:
                System.out.println("Unknown function: " + function);
                break;
        }
        out.flush();
    }

    private static void LoginPage(String function, ObjectInputStream in, ObjectOutputStream out, String userId)
            throws IOException, ClassNotFoundException {
        if (function != null) {
            switch (function) {
                case "Login":
                    String password = (String) in.readObject();

                    if (userMap.containsKey(userId)) {
                        // 如果用户已经在线，返回相应的消息
                        out.writeObject("用户已经登录");
                        out.flush();
                        return;  // 终止登录处理
                    }
                    User user = IUserServerSrv.login(userId, password);
                    if (user != null) {
                        userMap.put(userId, user);  // 将用户ID和User对象存入userMap
                        out.writeObject(user);  // 返回用户对象
                    } else {
                        out.writeObject("Invalid login credentials.");  // 登录失败信息
                    }
                    out.flush();
                    break;

                case "Forget":
                    String email = (String) in.readObject();
                    User finduser = IUserServerSrv.forgetPassword(userId, email);
                    out.writeObject(finduser);
                    out.flush();
                    break;

                case "Reset":
                    String newPassword = (String) in.readObject();
                    boolean success = IUserServerSrv.resetPassword(userId, newPassword);
                    out.writeObject(success);
                    out.flush();
                    break;
            }
        } else {
            System.out.println("Unknown function: " + function);
        }
    }

    private static void CoursePage(String function, ObjectInputStream in, ObjectOutputStream out)
            throws IOException, ClassNotFoundException {
        if (function != null) {
            switch (function) {
                case "Course":
            }
        } else {
            System.out.println("Unknown function: " + function);
        }
    }

    private static void EcardPage(String function, ObjectInputStream in, ObjectOutputStream out)
            throws IOException, ClassNotFoundException {
        String eCard;
        if (function != null) {
            switch (function) {
                case "cardIni":
                    String iniId = (String) in.readObject();
                    out.writeObject(ECardServerSrv.cardIni(iniId));
                    out.flush();
                    break;
                case "Charge":
                    eCard = (String) in.readObject();
                    System.out.println(eCard);
                    float amount = (float) in.readObject();
                    charge(eCard, amount);
                    break;
                case "History":
                    String card = (String) in.readObject();
                    String response = getTransactionHistory(card);
                    out.writeObject(response);
                    out.flush();
                    break;
                case "comparePassword":
                    eCard = (String) in.readObject();
                    Integer enteredPassword = (Integer) in.readObject();
                    out.writeObject(comparePassword(eCard, enteredPassword));
                    out.flush();
                    break;
                case "newPassword":
                    eCard = (String) in.readObject();
                    Integer newEnPassword = (Integer) in.readObject();
                    newPassword(eCard, newEnPassword);
                    break;
                case "LostSettings":
                    String id = (String) in.readObject();
                    boolean isLost = (boolean) in.readObject();
                    LostSettings(id, isLost);
                    break;
                case "Status":
                    eCard = (String) in.readObject();
                    out.writeObject(showStatus(eCard));
                    out.flush();
                    break;
                case "Pay":
                    String payid = (String) in.readObject();
                    eCard = (String) in.readObject();

                    float payamount = (float) in.readObject();
                    String reason = (String) in.readObject();
                    out.writeObject(pay(payid, eCard, payamount, reason));
                    out.flush();
            }
        } else {
            System.out.println("Unknown function: " + function);
        }
    }

    private static void StorePage(String function, ObjectInputStream in, ObjectOutputStream out)
            throws IOException, ClassNotFoundException {
        ShopStudent student;
        if (function != null) {
            switch (function) {
                case "initialShopStudent":
//                    User user = (User) in.readObject();
//                    student = ShopServerSrv.initialShopStudent(user);
//                    ShopServerSrv.initialShop(8,student);
//                    out.writeObject(student);
//                    break;
//                case "refreshShop":
//                    student = (ShopStudent) in.readObject();
//                    ShopServerSrv.initialShop(8,student);
//                    out.writeObject(student);
//                    break;
//                case "searchProduct":
//                    student = (ShopStudent) in.readObject();
//                    String searchName = (String) in.readObject();
//                    boolean success = ShopServerSrv.searchProduct(student,searchName);
//                    out.writeObject(student);
//                    out.writeObject(success);
//                    break;
//                case "changeFavorites":
//                    String productId = (String) in.readObject();
//                    student = (ShopStudent) in.readObject();
//                    boolean is = (boolean) in.readObject();
//                    ShopServerSrv.changeFavorites(productId,student,is);
//                    out.writeObject(student);
//                    break;
//                case "purchaseProduct":
//                    productId = (String) in.readObject();
//                    int buyNums = (int) in.readObject();
//                    int password = (int) in.readObject();
//                    student = (ShopStudent) in.readObject();
//                    int situation = ShopServerSrv.purchaseProduct(productId,buyNums,password,student);
//                    out.writeObject(student);
//                    out.writeObject(situation);
//                    break;
//                case "getRecordCount":
//                    String tablename = (String) in.readObject();
//                    String Id = ShopServerSrv.getRecordCount(tablename);
//                    out.writeObject(Id);
//                    break;
//                case "addNew":
//                    Product newProduct = (Product) in.readObject();
//                    student = (ShopStudent) in.readObject();
//                    success = ShopServerSrv.addNew(student,newProduct);
//                    out.writeObject(success);
//                    out.writeObject(newProduct);
//                    out.writeObject(student);
//                    break;
//                case "updateProduct":
//                    Product updateProduct = (Product) in.readObject();
//                    student = (ShopStudent) in.readObject();
//                    success = ShopServerSrv.updateProduct(student,updateProduct);
//                    out.writeObject(updateProduct);
//                    out.writeObject(student);
//                    out.writeObject(success);
//                    break;
//                case "deleteProduct":
//                    Product deletProduct = (Product) in.readObject();
//                    student = (ShopStudent) in.readObject();
//                    success = ShopServerSrv.deleteProduct(student,deletProduct);
//                    out.writeObject(student);
//                    out.writeObject(success);
                    break;
            }
        } else {
            System.out.println("Unknown function: " + function);
        }
    }

    private static void StudentRecordPage(String function, ObjectInputStream in, ObjectOutputStream out)
            throws IOException, ClassNotFoundException {
        if (function != null) {
            switch (function) {
                case "studentFindInfo":
                    String studentId = (String) in.readObject();
                    Student student1 = StuMSServerSrv.studentFindInfo(studentId);
                    out.writeObject(student1);
                    out.flush();
                    break;
                case "studentFindGrade":
                    String studentGradeId = (String) in.readObject();
                    out.writeObject(StuMSServerSrv.studentFindGrade(studentGradeId));
                    out.flush();
                    break;
                case "teacherFindAllInfo":
                    out.writeObject(StuMSServerSrv.teacherFindAllInfo());
                    out.flush();
                    break;
                case "teacherAddInfo":
                    Student student = (Student) in.readObject();
                    out.writeObject(StuMSServerSrv.teacherAddInfo(student));
                    out.flush();
                    break;
                case "teacherDeleteInfo":
                    String studentId2 = (String) in.readObject();
                    out.writeObject(StuMSServerSrv.teacherDeleteInfo(studentId2));
                    out.flush();
                    break;
                case "teaccherModifyInfo":
                    Student student2 = (Student) in.readObject();
                    out.writeObject(StuMSServerSrv.teacherModifyInfo(student2));
                    out.flush();
                    break;
                case "teacherFindAllGrade":
                    out.writeObject(StuMSServerSrv.teacherFindAllGrade());
                    out.flush();
                    break;
                case "teacherModifyGrade":
                    Grade grade = (Grade) in.readObject();
                    out.writeObject(StuMSServerSrv.teacherModifyGrade(grade));
                    out.flush();
                    break;
                case "teacherDeleteGrade":
                    String studentId3 = (String) in.readObject();
                    String courseId = (String) in.readObject();
                    boolean isFirst = (boolean) in.readObject();
                    out.writeObject(StuMSServerSrv.teacherDeleteGrade(studentId3, courseId, isFirst));
                    out.flush();
                    break;
            }
        } else {
            System.out.println("Unknown function: " + function);
        }
    }
}