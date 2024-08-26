package ECard;

import User.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ECard extends User {
    public ArrayList<String> transactionHistory;


    public ECard(User user) {
        super(user.getId(), user.getPwd(), user.getAge(), user.getGender(), user.getRole(), user.getEmail(), user.getCard(), user.getRemain(), user.getPassword(), user.getLost());
    }

    public ArrayList<String> getTransactionHistory() {
        return transactionHistory;
    }

    public void setTransactionHistory(ArrayList<String> transactionHistory) {
        this.transactionHistory = transactionHistory;
    }

    public boolean isLost() {
        if (!getLost()) {
            setLost(true);
            return true;
        } else {
            return false;
        }
    }

    public boolean notLost() {
        if (getLost()) {
            setLost(false);
            return true;
        } else {
            return false;
        }
    }

    void deposit(float amount) {
        setRemain(getRemain() + amount);
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

    public void changePassword(Integer newPassword) {
        setPassword(newPassword);
    }

}
