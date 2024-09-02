package vCampus.User;

import vCampus.Entity.User;
import vCampus.Dao.UserDao;

import java.util.Objects;
import java.util.Scanner;

public class IUserServerSrv {

    // 以下inid为“输入的id”，inpwd为“输入的密码”

    // 登录功能
    public static User login(String inid, String inpwd) {
        User user = null;
        UserDao userDao = new UserDao();
        user = userDao.find(inid);
        if (user != null) {
            if (Objects.equals(inpwd, user.getPwd())) {
                //User.setCurrentUser(user);
                return user;
            } else {
                System.out.println("密码错误");
            }
        } else {
            System.out.println("用户不存在");
        }
        return null; // 登录失败，返回null
    }

    // 登出功能
    public static void logout(User user) {
       // User.setCurrentUser(null);
    }

    // 忘记密码功能
    public static User forgetPassword(String inid,String inemail) {
        User user = null;
        UserDao userDao = new UserDao();
        user = userDao.find(inid);
        if (user != null) {
            // 验证邮箱
            if (!inemail.equals(user.getEmail())) {
                System.out.println("验证邮箱不符，请输入注册时的验证邮箱。");
                return null;
            }

        } else {
            System.out.println("用户不存在");
        }
        return user;
    }

    //重置密码功能
    public static Boolean resetPassword(String inid,String newPwd) {
        User user = null;
        UserDao userDao = new UserDao();
        user = userDao.find(inid);
        if (user != null) {
            // 更新密码
            user.setPwd(newPwd);
            userDao.update(user);
            System.out.println("密码修改成功。");
        } else {
            System.out.println("用户不存在");
            return false;
        }
        return true;
    }
}

