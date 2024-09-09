package vCampus.ECard;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import vCampus.Dao.ECardDao;
import vCampus.Dao.TransactionDao;
import vCampus.Entity.ECard.ECard;
import vCampus.Dao.UserDao;
import vCampus.Entity.ECard.ECardDTO;
import vCampus.Entity.User;

public class ECardServerSrv {

    public static ECard cardIni(String card) {
        TransactionDao tdao = new TransactionDao();
        if(tdao.find(card)==null)
            tdao.add(card);
        UserDao userDao = new UserDao();
        User user=userDao.find(card);
        return new ECard(user);
    }

    public static void LostSettings(String id,boolean isLost) {
        UserDao udao = new UserDao();
        udao.updateLost(!isLost,id);
    }

    public static void addTransaction(String card, float amount, String reason) {
        TransactionDao transactionDao = new TransactionDao();
        String oldHistory = transactionDao.find(card);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedNow = now.format(formatter);
        String newHistory = formattedNow + ", " + amount + ", " + reason + ";";
        transactionDao.update(newHistory+oldHistory,card);
    }

    public static ECardDTO showStatus(String testcard) {
        ECardDao cardDao = new ECardDao();
        return cardDao.find(testcard);
    }

    public static String getTransactionHistory(String testcard) {
        TransactionDao transactionDao = new TransactionDao();
        return transactionDao.find(testcard);
    }

    public static boolean comparePassword(String testcard, Integer enteredPassword) {
        ECardDao cardDao = new ECardDao();
        return (Objects.equals(enteredPassword, cardDao.find(testcard).getPassword()));
    }

    public static void newPassword(String testcard, Integer newPassword) {
        ECardDao cardDao = new ECardDao();
        cardDao.updatePassword(newPassword, testcard);
    }

    public static int pay(String id,String card, float amount, String reason) {

        ECardDao cardDao = new ECardDao();
        UserDao userDao = new UserDao();
        float currentBalance = cardDao.find(card).getRemain();
        if(userDao.find(id).getLost())
            return 2;
        else if(currentBalance<amount)
            return 1;
        cardDao.updateRemain(currentBalance - amount,card);
        addTransaction(card, -amount, reason);
        return 0;
    }

    public static void charge(String testcard, float amount) {
        ECardDao cardDao = new ECardDao();

        addTransaction(testcard, amount, "charge");

        float newRemain=cardDao.find(testcard).getRemain() + amount;

        cardDao.updateRemain(newRemain, testcard);
    }
}
