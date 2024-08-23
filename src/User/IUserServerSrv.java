package User;

public class IUserServerSrv {
    // 以下inid为“输入的id”，inpwd为“输入的密码”

    // 登录功能
    public static User login(String inid,String inpwd) {
        // 这里应该有验证逻辑,比如查询数据库等
        // 如果登录成功,返回用户对象
        // 否则返回null
        return user;
    }

    // 注册功能
    public static User register(String inid,String inpwd) {
        // 这里应该有注册逻辑,比如插入数据库等
        // 如果注册成功,返回用户对象
        // 否则返回null
        return user;
    }

    // 登出功能
    public static void logout(User user) {
        // 这里应该有登出逻辑,比如清除session等
        // 无返回值
    }

    //待定……

}
