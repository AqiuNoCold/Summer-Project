package Pages.Pages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel messageLabel;

    public LoginPage() {
        setTitle("登录页面");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 2));

        JLabel usernameLabel = new JLabel("用户名:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("密码:");
        passwordField = new JPasswordField();
        loginButton = new JButton("登录");
        messageLabel = new JLabel("");

        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(loginButton);
        add(messageLabel);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 假设用户名是"admin"，密码是"password"
                if (usernameField.getText().equals("1") && new String(passwordField.getPassword()).equals("1")) {
                    new NavigationPage().setVisible(true);
                    dispose(); // 关闭登录页面
                } else {
                    messageLabel.setText("用户名或密码错误");
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginPage().setVisible(true));
    }
}

