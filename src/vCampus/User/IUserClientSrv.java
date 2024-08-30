package vCampus.User;

import vCampus.Entity.User;
import vCampus.Entity.UserInfo;
import vCampus.Dao.UserDao;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class IUserClientSrv{

    // 以下inid为“输入的id”，inpwd为“输入的密码”

    // 登录功能
    public static User login(String inid,String inpwd) {
        User user = null;
        UserDao userDao = new UserDao();
        user = userDao.find(inid);
        if(user != null){
            if(Objects.equals(inpwd, user.getPwd())){
                UserInfo userInfo = UserInfo.fromUser(user);
                UserInfo.setCurrentUser(userInfo);
                User.setCurrentUser(user);
                return user;
            }else{
                System.out.println("密码错误");
            }
        }else{
            System.out.println("用户不存在");
        }
        return null; // 登录失败，返回null
    }

    // 登出功能
    public static void logout(User user) {
        User.setCurrentUser(null);
    }

    // 忘记密码功能
    public static User forgetPassword(String inid) {
        User user = null;
        UserDao userDao = new UserDao();
        user = userDao.find(inid);
        if(user != null){
            Scanner scanner = new Scanner(System.in);
            System.out.print("请输入注册时的验证邮箱: ");
            String inputEmail = scanner.nextLine();
            // 验证邮箱
            if (!inputEmail.equals(user.getEmail())) {
                System.out.println("验证邮箱不符，请输入注册时的验证邮箱。");
                return null;
            }

            // 输入新密码
            System.out.print("请输入新密码: ");
            String newPwd = scanner.nextLine();

            // 更新密码
            user.setPwd(newPwd);
            userDao.update(user);
            System.out.println("密码修改成功。");
        }else{
            System.out.println("用户不存在");
        }
        return user;
    }

    public static void main(String[] args) {

        UserDao userDao = new UserDao();
        // 构造 User1
        ArrayList<String> courses1 = new ArrayList<>();
        courses1.add("Java");
        courses1.add("Python");
        User User1 = new User("user1", "password123", 25, true, "ST", "user1@example.com", "123456789", false);
        userDao.add(User1);

        // 构造 User2
        ArrayList<String> courses2 = new ArrayList<>();
        courses2.add("C++");
        courses2.add("JavaScript");
        User User2 = new User("user2", "password456", 30, false, "TC", "user2@example.com", "987654321",  true);
        userDao.add(User2);

        Scanner scanner = new Scanner(System.in);
        IUserClientSrv userClientSrv = new IUserClientSrv();

        while (true) {
            System.out.println("欢迎使用用户管理系统");
            System.out.println("1. 登录");
            System.out.println("2. 登出");
            System.out.println("3. 找回密码");
            System.out.println("4. 退出");
            System.out.print("请选择操作: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // 清除换行符

            switch (choice) {
                case 1:
                    // 登录功能
                    System.out.print("请输入用户ID: ");
                    String loginId = scanner.nextLine();
                    System.out.print("请输入密码: ");
                    String loginPwd = scanner.nextLine();
                    User loggedInUser = userClientSrv.login(loginId, loginPwd);
                    if (loggedInUser != null) {
                        System.out.println("登录成功，欢迎 " + loggedInUser.getId());
                    }
                    break;

                case 2:
                    // 登出功能
                    User currentUser = User.getCurrentUser(); // 获取当前用户
                    if (currentUser != null) {
                        userClientSrv.logout(currentUser);
                        System.out.println("登出成功。");
                    } else {
                        System.out.println("当前没有用户登录。");
                    }
                    break;

                case 3:
                    // 找回密码功能
                    System.out.print("请输入用户ID: ");
                    String forgetId = scanner.nextLine();
                    userClientSrv.forgetPassword(forgetId);
                    break;

                case 4:
                    // 退出程序
                    System.out.println("感谢使用，再见！");
                    scanner.close();
                    return;

                default:
                    System.out.println("无效的选择，请重试。");
            }
        }
    }
}
