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

    public static ECard cardIni(User user) {
        TransactionDao tdao = new TransactionDao();
        if(tdao.find(user.getCard())==null)
            tdao.add(user.getCard());
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

    public static boolean pay(String card, float amount, String reason, Integer passwordEntered) {

        TransactionDao transactionDao = new TransactionDao();
        ECardDao cardDao = new ECardDao();
        ECardDTO cardInfo = cardDao.find(card);

        if (!Objects.equals(passwordEntered, cardInfo.getPassword())) {
            System.out.println("Wrong password!");
            return false;
        }
        cardInfo.setRemain(cardInfo.getRemain() - amount);
        cardDao.update(cardInfo);

        addTransaction(cardInfo.getCard(), amount, reason);
        System.out.println("Successfully payed!");
        return true;
    }

    public static void charge(String testcard, float amount) {
        ECardDao cardDao = new ECardDao();

        addTransaction(testcard, amount, "charge");

        float newRemain=cardDao.find(testcard).getRemain() + amount;

        cardDao.updateRemain(newRemain, testcard);
    }
}
