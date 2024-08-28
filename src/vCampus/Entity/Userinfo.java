package vCampus.Entity;

public class Userinfo {
    private static Userinfo currentUser;

    private String role; // 角色
    private String card; // 一卡通号、账号
    private Boolean lost; // 账户冻结情况

    public Userinfo(String role, String card, Boolean lost) {
        this.role = role;
        this.card = card;
        this.lost = lost;
    }

    // 从 User 对象创建 Userinfo 对象的静态方法
    public static Userinfo fromUser(User user) {
        return new Userinfo(user.getRole(), user.getCard(), user.getLost());
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

    public Boolean getLost() {
        return lost;
    }

    public void setLost(Boolean lost) {
        this.lost = lost;
    }

    public static Userinfo getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(Userinfo user) {
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