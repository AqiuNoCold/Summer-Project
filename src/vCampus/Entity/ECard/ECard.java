package vCampus.Entity.ECard;

import vCampus.Dao.ECardDao;
import vCampus.Dao.TransactionDao;
import vCampus.Entity.User;

public class ECard extends User {
    protected Float remain;    //账户余额
    protected Integer password; //支付密码
//    protected Boolean lost;    //账户冻结情况
    private String transactionHistory;

    public ECard(User user) {
        super(user.getId(), user.getPwd(), user.getAge(), user.getGender(), user.getRole(), user.getEmail(), user.getCard(),user.getLost(),user.getCourses());
        ECardDao dao = new ECardDao();
        ECardDTO cardInfo=dao.find(user.getCard());
        if(cardInfo==null){
            remain = 0f;
            password = Integer.parseInt(card.substring(card.length() - 6));
            cardInfo.setCard(user.getCard());
            cardInfo.setRemain(remain);
            cardInfo.setPassword(password);
            dao.add(cardInfo);
            TransactionDao daoTransaction = new TransactionDao();
            daoTransaction.add(user.getCard());
        }
        else {
            remain = cardInfo.getRemain();
            password = cardInfo.getPassword();
        }
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
        remain+=amount;
    }


}
