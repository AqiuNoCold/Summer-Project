package Pages.Pages;
import javax.swing.*;
import Pages.MainApp;
import Pages.Pages.support.TextIcon;
import Pages.Pages.support.CCButton;
import Pages.Pages.support.BackgroundTabbedFrame;
import vCampus.Entity.User;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.*;

public class CoursePage extends JFrame {
    static int X = 1200, Y = 650, X1 = 300, X2 = 900;
    ImageIcon background = new ImageIcon(getClass().getResource("/imgs/course/bg2_resized.png"));
    private BackgroundTabbedFrame jf = new BackgroundTabbedFrame(background, X, Y);
    private JPanel grid = new JPanel(null);
    private JPanel sum = new JPanel(null);

    public CoursePage() {

        TextIcon jwcIcon = new TextIcon("选课",180,80,180,80);

        User per = MainApp.getCurrentUser();
        //add(label, BorderLayout.CENTER);

        CCButton jwcButton = new CCButton(" ", jwcIcon, jf, per);
        jf.setTitle("学生选课系统");
        jf.setIconImage(background.getImage());
        sum.setSize(X, Y);
        grid.setBounds(X1, 0, X2, Y);
        grid.setOpaque(false);
        grid.add(jwcButton);
        sum.add(grid);

        String logoPath = "src\\imgs\\course\\head.png";  // 替换为你想要的图片路径
        ImageIcon perIcon = new ImageIcon(logoPath);

        // Resize 图片到 42x61
        Image img = perIcon.getImage();
        Image resizedImg = img.getScaledInstance(84, 122, java.awt.Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImg);

        // 使用 resizedIcon 设置 JLabel
        JLabel logoLabel = new JLabel(resizedIcon);
        // 设置图片位置
        logoLabel.setBounds(550, 150, 84, 122);
        grid.add(logoLabel);

        String ns = "<html>"+"欢迎您<br>"+per.getId()+"</html>";
        JLabel nameL = new JLabel(ns);
        nameL.setFont(new Font("微软雅黑", Font.BOLD, 20));
        nameL.setBounds(650, 150, 180,100);
        grid.add(nameL);

        String infos = "选课指南";
        JLabel infoL = new JLabel(infos);
        infoL.setFont(new Font("微软雅黑", Font.BOLD, 20));
        infoL.setBounds(0, 50, 180,80);
        grid.add(infoL);

        String sps = "<html>首页->选课<br>学生课表->课程界面<br>单击课程框-><br>在弹出的对话框中单击：是</html>";
        JLabel spl = new JLabel(sps);
        spl.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        spl.setBounds(40, 80, 220,180);
        grid.add(spl);

        String asks = "咨询方式";
        JLabel askl = new JLabel(asks);
        askl.setFont(new Font("微软雅黑", Font.BOLD, 20));
        askl.setBounds(0, 250, 180,40);
        grid.add(askl);

        String ways = "<html>四牌楼教务处：025-83794380<br>" +
                "九龙湖教务服务大厅：025-52090218<br>" +
                "<br>丁家桥教务处：025-83272295</html>";
        JLabel wayl = new JLabel(ways);
        wayl.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        wayl.setBounds(40, 260, 300,200);
        grid.add(wayl);

        jf.addMainPanel(sum);
        //int basex = 70, basey = 330, spacex = 155, widthx = 100, heighty = 100;
        //jwcButton.setBounds(basex + spacex, basey, widthx, heighty);
/*
        add(backButton, BorderLayout.SOUTH);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                navigateBack();
            }
        });*/
        jf.display();
    }

    private void navigateBack() {
        new NavigationPage().setVisible(true);
        dispose(); // 关闭当前页面
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CoursePage().setVisible(true);
            }
        });
    }
}

