package vCampus.ECard;

import java.util.ArrayList;
import vCampus.Entity.User;
import vCampus.Entity.ECard;


public class ECardServerSrv {
    public static boolean isLostServerSrv(ECard testcard) {
        return testcard.isLost();
    }

    public static boolean notLostServerSrv(ECard testcard) {

        return testcard.notLost();
    }

    public static boolean chargeServerSrv(ECard testcard, float amount) {
        String newHistory = testcard.charge(amount);
//        用newHistory更新数据库tblTransactionHistory
        return true;
    }

    public static float showStatusServerSrv(ECard testcard) {
        return testcard.getRemain();
    }

    public static ArrayList<String> getTransactionHistoryServerSrv(ECard testcard) {
//        连接数据库获取流水后转化成ArrayList格式，传递给客户端
        ArrayList<String> currentHistory=new ArrayList<String>();
        currentHistory.add("yyyy-MM-dd HH:mm:ss,-500,Charged");
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
