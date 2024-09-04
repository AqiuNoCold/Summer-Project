package vCampus;

import vCampus.Dao.Criteria.*;
import vCampus.Entity.ECard.ECard;
import vCampus.Entity.User;
import vCampus.Entity.Books.*;
import vCampus.Service.*;
import vCampus.User.IUserServerSrv;
import vCampus.ECard.ECardServerSrv;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import static vCampus.ECard.ECardServerSrv.charge;

public class MainServer {
    private static final int PORT = 5101;
    private static final int MAX_CONNECTIONS = 10;
    private static ConcurrentHashMap<Socket, Thread> socketMap = new ConcurrentHashMap<>();

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

                Thread clientThread = new Thread(() -> handleClient(clientSocket));
                socketMap.put(clientSocket, clientThread);
                clientThread.start();

                System.out.println("Client connected!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {
            while (true) {
                String model = (String) in.readObject();
                String function = (String) in.readObject();
                switch (model) {
                    case "1":
                        LoginPage(function, in, out);
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
            default:
                System.out.println("Unknown function: " + function);
                break;
        }
        out.flush();
    }

    private static void LoginPage(String function, ObjectInputStream in, ObjectOutputStream out)
            throws IOException, ClassNotFoundException {
        if (function != null) {
            switch (function) {
                case "Login":
                    String username = (String) in.readObject();
                    String password = (String) in.readObject();
                    System.out.println("Logged in: " + username + " " + password);
                    User user = IUserServerSrv.login(username, password);
                    out.writeObject(user);
                    out.flush();
                    break;
                case "Forget":
                    String userId = (String) in.readObject();
                    String email = (String) in.readObject();
                    User finduser = IUserServerSrv.forgetPassword(userId, email);
                    out.writeObject(finduser);
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
        ECard eCard;
        if (function != null) {
            switch (function) {
                case "cardIni":
                    User user = (User) in.readObject();
                    eCard = ECardServerSrv.cardIni(user);
                    out.writeObject(eCard);
                    break;
                case "Charge":
                    eCard = (ECard) in.readObject();
                    float amount = (float) in.readObject();
                    out.writeObject(ECardServerSrv.charge(eCard, amount));
            }
        } else {
            System.out.println("Unknown function: " + function);
        }
    }

    private static void StorePage(String function, ObjectInputStream in, ObjectOutputStream out)
            throws IOException, ClassNotFoundException {
        if (function != null) {
            switch (function) {
                case "Course":
            }
        } else {
            System.out.println("Unknown function: " + function);
        }
    }

    private static void StudentRecordPage(String function, ObjectInputStream in, ObjectOutputStream out)
            throws IOException, ClassNotFoundException {
        if (function != null) {
            switch (function) {
                case "Course":
            }
        } else {
            System.out.println("Unknown function: " + function);
        }
    }
}