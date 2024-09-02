package vCampus.Entity.ECard;

import vCampus.Dao.ECardDao;
// import vCampus.Dao.TransactionDao;
import vCampus.Entity.User;

public class ECard extends User {
    protected Float remain; // 账户余额
    protected Integer password; // 支付密码
    // protected Boolean lost; //账户冻结情况
    private String transactionHistory;

    public ECard(User user) {
        super(user.getId(), user.getPwd(), user.getAge(), user.getGender(), user.getRole(), user.getEmail(),
                user.getCard(), user.getLost());
        ECardDao dao = new ECardDao();
        ECardDTO dto = dao.find(user.getId());
        remain = dto.getRemain();
        password = dto.getPassword();
    }

    public String getTransactionHistory() {
        return transactionHistory;
    }

    public void setTransactionHistory(String transactionHistory) {
        this.transactionHistory = transactionHistory;
    }

    public float getRemain() {
        return remain;
    }

    public void setRemain(Float remain) {
        this.remain = remain; // 可以根据需要添加更多验证
    }

    // password的getter和setter
    public Integer getPassword() {
        return password;
    }

    public void setPassword(Integer password) {
        this.password = password; // 可以根据需要添加更多验证
    }

    void deposit(float amount) {
        remain += amount;
    }

    @Override
    public String toString() {
        return "ECard{" +
                "id='" + id + '\'' +
                ", pwd='" + pwd + '\'' +
                ", age=" + age +
                ", gender=" + (gender ? "男" : "女") +
                ", role='" + role + '\'' +
                ", email='" + email + '\'' +
                ", card='" + card + '\'' +
                ", lost=" + (lost ? "正常" : "冻结") +
                ", remain=" + remain +
                ", password='" + password + '\'' +
                '}';
    }
}
