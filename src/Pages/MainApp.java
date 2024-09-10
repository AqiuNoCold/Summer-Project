package Pages;

import Pages.Pages.Library.HomePage;
import Pages.Pages.LibraryPage;
import Pages.Pages.LoginPage;
import Pages.Pages.NavigationPage;
import vCampus.Entity.User;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MainApp {
    private static User currentUser;
    private static Socket socket;
    private static ObjectOutputStream out;
    private static ObjectInputStream in;
    private static LibraryPage libraryPage;
    private static boolean isLibraryPageLoaded = false;
    private static HomePage homePage; // 添加 HomePage 实例变量
    private static boolean isHomePageLoaded = false; // 添加布尔字段，默认为 false

    public static void main(String[] args) {
        // 初始化Socket或其他必要的设置
        initializeSocket();

        // 检查用户状态并显示相应的页面
        SwingUtilities.invokeLater(() -> {
            if (currentUser == null) {
                new LoginPage().setVisible(true);
            } else {
                new NavigationPage().setVisible(true);
                // 启动后台线程初始化 HomePage
                new Thread(() -> {
                    homePage = HomePage.init();
                    isHomePageLoaded = true;
                    // 启动后台线程初始化 LibraryPage
                    new Thread(() -> {
                        libraryPage = LibraryPage.init();
                        isLibraryPageLoaded = true;
                    }).start();
                }).start();
            }
        });

        Runtime.getRuntime().addShutdownHook(new Thread(MainApp::close_source));
    }

    public static void initializeSocket() {
        try {
            socket = new Socket("localhost", 5101);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            System.out.println("Connected to server");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void close_source() {
        try {
            out.writeObject("exit");
            out.flush();
            System.out.println("exit");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Getter and Setter for currentUser and socket
    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static Socket getSocket() {
        return socket;
    }

    public static void setSocket(Socket socket) {
        MainApp.socket = socket;
    }

    public static ObjectInputStream getIn() {
        return in;
    }

    public static void setIn(ObjectInputStream in) {
        MainApp.in = in;
    }

    public static ObjectOutputStream getOut() {
        return out;
    }

    public static void setOut(ObjectOutputStream out) {
        MainApp.out = out;
    }

    // 新增的 getHomePage 方法
    public static HomePage getHomePage() {
        if (!isHomePageLoaded) {
            throw new IllegalStateException("HomePage 尚未加载完成");
        }
        return homePage;
    }

    // 新增的 isHomePageLoaded 方法
    public static boolean isHomePageLoaded() {
        return isHomePageLoaded;
    }

    public static LibraryPage getLibraryPage() {
        if (!isLibraryPageLoaded) {
            throw new IllegalStateException("LibraryPage 尚未加载完成");
        }
        return libraryPage;
    }

    public static boolean isLibraryPageLoaded() {
        return isLibraryPageLoaded;
    }
}