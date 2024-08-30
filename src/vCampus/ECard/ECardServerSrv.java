package vCampus.ECard;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.SimpleTimeZone;

import vCampus.Dao.ECardDao;
import vCampus.Dao.TransactionDao;
import vCampus.Entity.ECard.ECard;
import vCampus.Dao.UserDao;
import vCampus.Entity.ECard.ECard;
import vCampus.Entity.ECard.ECardDTO;
import vCampus.Entity.User;

public class ECardServerSrv {

    public static ECard cardIniServerSrv(User user)
    {
        return new ECard(user);
//        请求服务端进行初始化
    }

    public static boolean isLostServerSrv(ECard testcard) {
        boolean result =!testcard.getLost();
        if(result) {
            testcard.setLost(true);
            UserDao userDao = new UserDao();
            userDao.update(testcard);
        }
        return result;
    }

    public static boolean notLostServerSrv(ECard testcard) {
        boolean result =testcard.getLost();
        if(result) {
            testcard.setLost(false);
            UserDao userDao = new UserDao();
            userDao.update(testcard);
        }
        return result;
    }

    public static void addTransaction(String card,float amount,String reason) {
        TransactionDao transactionDao = new TransactionDao();
        String oldHistory=transactionDao.find(card);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedNow = now.format(formatter);
        String newHistory = formattedNow + ","+ amount +","+reason+";";
        transactionDao.update(oldHistory+newHistory,card);
    }


    public static float showStatusServerSrv(ECard testcard) {
        return testcard.getRemain();
    }

    public static String getTransactionHistoryServerSrv(ECard testcard) {
        TransactionDao transactionDao = new TransactionDao();
        return transactionDao.find(testcard.getCard());
    }

    public static boolean comparePasswordServerSrv(ECard testcard,Integer oldPassword) {
        return testcard.getPassword()==oldPassword;
    }

    public static boolean newPasswordServerSrv(ECard testcard,Integer newPassword) {
        testcard.setPassword(newPassword);
//        更新数据库tblUser
        ECardDao cardDao = new ECardDao();
        cardDao.updatePassword(newPassword,testcard.getCard());
        return true;
    }

    public static boolean payServerSrv(String card,float amount,String reason,Integer passwordEntered) {

        TransactionDao transactionDao = new TransactionDao();
        ECardDao cardDao = new ECardDao();
        ECardDTO cardInfo=cardDao.find(card);

        if(passwordEntered!=cardInfo.getPassword()) {
            return false;
        }
        cardInfo.setRemain(cardInfo.getRemain()-amount);
        cardDao.update(cardInfo);

        addTransaction(cardInfo.getCard(),amount,reason);
        return true;
    }
}
