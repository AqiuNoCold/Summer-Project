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
        tdao.add(user.getCard());
        return new ECard(user);
        // 请求服务端进行初始化
    }

    public static boolean LostSettings(ECard testcard) {
        boolean result = !testcard.getLost();
        if (result) {
            testcard.setLost(true);
            UserDao userDao = new UserDao();
            userDao.update(testcard);
        }
        return result;
    }


    public static void addTransaction(String card, float amount, String reason) {
        TransactionDao transactionDao = new TransactionDao();
        String oldHistory = transactionDao.find(card);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedNow = now.format(formatter);
        String newHistory = formattedNow + "," + amount + "," + reason + ";";
         transactionDao.update(oldHistory+newHistory,card);
    }

    public static float showStatus(ECard testcard) {
        ECardDao cardDao = new ECardDao();
        return cardDao.find(testcard.getCard()).getRemain();
    }

    public static String getTransactionHistory(ECard testcard) {
        TransactionDao transactionDao = new TransactionDao();
        String transaction = transactionDao.find(testcard.getCard());
        // 连接数据库获取流水，传递给客户端
        return transaction;
    }

    public static boolean comparePassword(ECard testcard, Integer passwordEntered) {
        return Objects.equals(testcard.getPassword(), passwordEntered);
    }

    public static boolean newPassword(ECard testcard, Integer newPassword) {
        testcard.setPassword(newPassword);
        // 更新数据库tblUser
        ECardDao cardDao = new ECardDao();
        cardDao.updatePassword(newPassword, testcard.getCard());
        return true;
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
}
