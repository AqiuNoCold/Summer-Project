package vCampus.Entity.ECard;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

import vCampus.Entity.User;

public class ECard extends User {
    protected Float remain;    //账户余额
    protected Integer password; //支付密码
//    protected Boolean lost;    //账户冻结情况
    private LinkedList<String> transactionHistory;

    public ECard(User user,ECardDTO testDTO) {
        super(user.getId(), user.getPwd(), user.getAge(), user.getGender(), user.getRole(), user.getEmail(), user.getCard(),user.getLost(),user.getCourses());
        remain = testDTO.getRemain();
        password = testDTO.getPassword();
    }

//    public ECard(String id, String pwd, Integer age, Boolean gender, String role, String email, String card,Float remain,Integer password,Boolean lost)
//    {
//        super(id, pwd, age, gender, role, email, card,lost,NULL);
//    }

    public LinkedList<String> getTransactionHistory() {
        return transactionHistory;
    }

    public void setTransactionHistory(LinkedList<String> transactionHistory) {
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
        remain+=amount;
    }


}
