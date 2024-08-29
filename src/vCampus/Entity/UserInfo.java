package vCampus.Entity;

public class UserInfo {
    private static UserInfo currentUser;

    private String role; // 角色
    private String card; // 一卡通号、账号
    private Boolean lost; // 账户冻结情况

    public UserInfo(String role, String card, Boolean lost) {
        this.role = role;
        this.card = card;
        this.lost = lost;
    }

    // 从 User 对象创建 Userinfo 对象的静态方法
    public static UserInfo fromUser(User user) {
        return new UserInfo(user.getRole(), user.getCard(), user.getLost());
    }

    // getters 和 setters
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    // 判断账户是否被冻结
    public boolean isLost() {
        return lost;
    }

    // 冻结账户
    public void freezeAccount() {
        lost = true;
    }

    // 解冻账户
    public void unfreezeAccount() {
        lost = false;
    }

    public static UserInfo getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(UserInfo user) {
        currentUser = user;
    }

    @Override
    public String toString() {
        return String.format(
                "+-----------------+----------------------------------+\n" +
                        "| Field           | Value                            |\n" +
                        "+-----------------+----------------------------------+\n" +
                        "| Role            | %-32s |\n" +
                        "| Card            | %-32s |\n" +
                        "| Lost            | %-32s |\n" +
                        "+-----------------+----------------------------------+\n",
                role, card, lost);
    }
}