package vCampus.ECard;
import vCampus.Entity.ECard.ECard;

import java.util.ArrayList;

import vCampus.Dao.UserDao;
import vCampus.Entity.ECard.ECard;

public class ECardServerSrv {
    public static boolean isLostServerSrv(ECard testcard) {
        boolean result = testcard.isLost();
        UserDao userDao = new UserDao();
        userDao.update(testcard);
        return result;
    }

    public static boolean notLostServerSrv(ECard testcard) {
        boolean result = testcard.notLost();
        UserDao userDao = new UserDao();
        userDao.update(testcard);
        return result;
    }

    public static boolean chargeServerSrv(ECard testcard, float amount) {
        String newHistoryShort = testcard.charge(amount);
        ArrayList<String> oldHistoryLong=testcard.getTransactionHistory();
        oldHistoryLong.remove(0);
        oldHistoryLong.add(newHistoryShort);
//        用newHistory更新数据库tblTransactionHistory
        return true;
    }

    public static float showStatusServerSrv(ECard testcard) {
        return testcard.getRemain();
    }

    public static ArrayList<String> getTransactionHistoryServerSrv(ECard testcard) {
//        连接数据库获取流水后转化成ArrayList格式，传递给客户端
        ArrayList<String> currentHistory=new ArrayList<String>();
        currentHistory.add("yyyy-MM-dd HH:mm:ss,+500,Charged");
        return currentHistory;
    }

    public static boolean comparePasswordServerSrv(ECard testcard,Integer oldPassword) {
        if(testcard.getPassword()==oldPassword)
            return true;
        else return false;
    }

    public static boolean newPasswordServerSrv(ECard testcard,Integer newPassword) {
        testcard.setPassword(newPassword);
//        更新数据库tblUser
        return true;
    }
}
