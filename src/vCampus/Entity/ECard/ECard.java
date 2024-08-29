package vCampus.Entity.ECard;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import vCampus.Entity.User;

public class ECard extends User {
    protected Float remain;    //账户余额
    protected Integer password; //支付密码
//    protected Boolean lost;    //账户冻结情况
    private ArrayList<String> transactionHistory;

    public ECard(User user,ECardDTO testDTO) {
        super(user.getId(), user.getPwd(), user.getAge(), user.getGender(), user.getRole(), user.getEmail(), user.getCard(),user.getLost(),user.getCourses());
        remain = testDTO.getRemain();
        password = testDTO.getPassword();
    }

//    public ECard(String id, String pwd, Integer age, Boolean gender, String role, String email, String card,Float remain,Integer password,Boolean lost)
//    {
//        super(id, pwd, age, gender, role, email, card,lost,NULL);
//    }

    public ArrayList<String> getTransactionHistory() {
        return transactionHistory;
    }

    public void setTransactionHistory(ArrayList<String> transactionHistory) {
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

    public boolean isLost() {
        boolean result =!lost;
        this.lost=true;
        return result;
    }

    public boolean notLost() {
        boolean result =lost;
        lost=false;
        return result;
    }

    void deposit(float amount) {
        remain+=amount;
    }

    String addTransaction(float amount,String reason) {
        LocalDateTime now = LocalDateTime.now();
        // 自定义格式化
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedNow = now.format(formatter);

        return  formattedNow + ","+ amount +","+reason;
    }

    public String charge(float amount) {
        deposit(amount);
        return addTransaction(amount,"Charged");
    }

}
