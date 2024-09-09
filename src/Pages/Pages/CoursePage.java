package Pages.Pages;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CoursePage extends JFrame {
    public CoursePage() {

        //setTitle("选课页面");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // 居中显示窗口

        setLayout(new BorderLayout());

        JLabel label = new JLabel("选课页面内容", SwingConstants.CENTER);
        JButton backButton = new JButton("返回");

        add(label, BorderLayout.CENTER);
        add(backButton, BorderLayout.SOUTH);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                navigateBack();
            }
        });
        render();
    }

    public void render() {
        this.removeAll();
        this.repaint();
        this.setBackground(Color.WHITE);
        int real_count = 0;

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
