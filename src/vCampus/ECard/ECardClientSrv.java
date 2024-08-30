package vCampus.ECard;

import java.util.ArrayList;
import java.util.Scanner;

public class ECardClientSrv {
    public static void cardIniClientSrv() {
        // 请求服务端进行初始化
    }

    public static void isLostClientSrv() {
        // 请求服务端isLostSSrv并根据返回值输出提示语（关于是否挂失成功）,先假设存放返回值的变量名为result

        boolean ServerResult = true;
        if (ServerResult)
            System.out.println("Reported the loss successfully!");
        else
            System.out.println("Failed to report the loss!");
    }

    public static void notLostClientSrv() {
        // 请求服务端isLostSSrv并根据返回值输出提示语（关于是否挂失成功）,先假设存放返回值的变量名为result
        boolean ServerResult = true;
        if (ServerResult)
            System.out.println("Cancelled the loss successfully!");
        else
            System.out.println("Failed to cancel the loss!");
    }

    public static void chargeClientSrv() {
        // 输入充值金额，先假设为amount
        float amount = 100.0F;
        // 传递给服务端后获取返回值，先假设为ServerResult
        boolean ServerResult = true;
        if (ServerResult)
            System.out.println("Successfully charged!");
    }

    public static void showStatusClientSrv() {
        // 请求服务端showStatusSSrv后展示服务端返回的数据,假设为currentBalance
        float currentBalance = 200f;
        System.out.println("Current Balance: " + currentBalance);
    }

    public static void getTransactionHistoryClientSrv() {
        // 请求服务端getTransactionHistorySSrv后展示返回值.,假设为currentHistory
        ArrayList<String> currentHistory = new ArrayList<String>();
        currentHistory.add("1");
        System.out.println(currentHistory);
    }

    public static void comparePasswordClientSrv() {
        // Scanner scanner = new Scanner(System.in);
        // String input = "";
        // while (true) {
        // System.out.print("Please enter the old password: ");
        // input = scanner.nextLine();
        // if (input.matches("\\d{6}")) {
        // break;
        // } else {
        // System.out.println("Invalid input，please make sure you enter 6 digits");
        // }
        // }
        // int inputPassword =Integer.parseInt(input);
        // scanner.close();
        // 输入原支付密码提交给服务端，获取返回值，假设为ServerResult
        boolean ServerResult = false;
        if (!ServerResult)
            System.out.println("Doesn't match the old password");
    }

    public static void newPasswordClientSrv() {
        // Scanner scanner = new Scanner(System.in);
        // String input = "";
        // while (true) {
        // System.out.print("Please enter the new password: ");
        // input = scanner.nextLine();
        // if (input.matches("\\d{6}")) {
        // break;
        // } else {
        // System.out.println("Invalid input，please make sure you enter 6 digits");
        // }
        // }
        // int newPassword =Integer.parseInt(input);
        // scanner.close();
        // 将新密码提交给服务端，获取返回值表示修改成功
        boolean ServerResult = true;
        if (ServerResult)
            System.out.println("Password changed successfully!");
    }

    public static boolean payClientSrv() {
        comparePasswordClientSrv();
        return true;
    }
}
