package Pages.Pages.support;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.util.Enumeration;


/**
 * @author JCSTARS
 * @version 6.0
 * 为所有UI界面提供格式支持 (字体/图片,icon路径/颜色)
 */

import java.awt.Color;
import java.awt.Font;
import java.util.Enumeration;

import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

public class MyType {
    // path
    public static String figurePath = "src/imgs/course";
    public static String iconPath = figurePath;

    // font
    public static Font beautFont = new Font("微软雅黑", Font.PLAIN, 13);
    public static Font h1Font = new Font("微软雅黑", Font.BOLD, 24);
    public static Font h0Font = new Font("微软雅黑", Font.BOLD, 30);
    public static Font clearFont = new Font("微软雅黑", Font.PLAIN, 23);

    // color
    public static Color beautColor = new Color(230, 245, 245);
    public static Color defaultColor = new Color(246, 246, 246);
    public static Color seuGreenColor = new Color(30, 140, 0);
    public static Color focusColor = new Color(110, 110, 245);
    public static Color taroColor = new Color(230, 230, 250);
    public static Color redColor = new Color(180, 0, 0);
    public static Color lightGreenColor = new Color(211, 244, 202);
    public static Color blueColor = new Color(0, 162, 232);
    public static Color purpleColor = new Color(63, 72, 204);

    public static void initGlobalFont() {
        FontUIResource fontUIResource = new FontUIResource(beautFont);
        for (Enumeration keys = UIManager.getDefaults().keys(); keys.hasMoreElements();) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, fontUIResource);
            }
        }
    }
}
