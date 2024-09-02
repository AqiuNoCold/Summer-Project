package vCampus;

import Pages.Pages.LibraryPage;
import Pages.Pages.LoginPage;
import Pages.Pages.StorePage;
import vCampus.Entity.User;
import vCampus.User.IUserServerSrv;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

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
                String Model = (String) in.readObject();
                // 不断读取客户端发送的对象
                String function = (String) in.readObject();
                switch (Model){
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
                    User finduser = IUserServerSrv.forgetPassword(userId,email);
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
        if (function != null) {
            switch (function) {
                case "Course":
            }
        } else {
            System.out.println("Unknown function: " + function);
        }
    }

    private static void LibraryPage(String function, ObjectInputStream in, ObjectOutputStream out)
            throws IOException, ClassNotFoundException {
        if (function != null) {
            switch (function) {
                case "Course":
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
