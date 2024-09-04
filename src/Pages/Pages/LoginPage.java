package Pages.Pages;

import Pages.MainApp;
import vCampus.Entity.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class LoginPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton forgotPasswordButton;
    private JLabel messageLabel;
    private JLabel titleLabel;

    public LoginPage() {
        ObjectInputStream in = MainApp.getIn();
        ObjectOutputStream out = MainApp.getOut();

        setTitle("登录页面");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 居中显示窗口

        // 创建一个自定义的 JPanel
        LoginPanel panel = new LoginPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 210); // 添加间距

        // 用户认证标题
        titleLabel = new JLabel("用户认证");
        titleLabel.setFont(new Font("黑体", Font.BOLD, 32));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(titleLabel, gbc);

        // 用户名输入框
        gbc.gridy = 1;
        usernameField = createPlaceholderTextField("用户名");
        panel.add(usernameField, gbc);

        // 密码输入框
        gbc.gridy = 2;
        passwordField = createPasswordPlaceholderTextField("密码");
        panel.add(passwordField, gbc);

        // 登录按钮
        gbc.gridy = 3;
        loginButton = new JButton(new ImageIcon("src/imgs/Login.png"));
        loginButton.setContentAreaFilled(false);
        loginButton.setBorderPainted(false);
        loginButton.setFocusPainted(false);
        panel.add(loginButton, gbc);

        // 找回密码按钮
        gbc.gridy = 4;
        forgotPasswordButton = new JButton("找回密码");
        forgotPasswordButton.setContentAreaFilled(false);
        forgotPasswordButton.setBorderPainted(false);
        forgotPasswordButton.setFocusPainted(false);
        panel.add(forgotPasswordButton, gbc);

        // 消息标签
        gbc.gridy = 5;
        messageLabel = new JLabel("");
        panel.add(messageLabel, gbc);

        // 将面板添加到 JFrame
        add(panel);

        // 登录按钮事件
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    out.writeObject("1");
                    out.writeObject("Login");
                    out.writeObject(usernameField.getText());
                    out.writeObject(new String(passwordField.getPassword()));
                    out.flush();

                    User response = (User) in.readObject();
                    MainApp.setCurrentUser(response);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println(ex.getMessage());
                }

                if (MainApp.getCurrentUser() != null) {
                    new NavigationPage().setVisible(true);
                    dispose(); // 关闭登录页面
                } else {
                    messageLabel.setText("用户名或密码错误");
                }
            }
        });

        // 找回密码按钮事件
        forgotPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 创建并显示 ForgotPasswordPage
                ForgotPasswordPage forgotPasswordPage = new ForgotPasswordPage();
                forgotPasswordPage.setVisible(true);
                dispose(); // 关闭登录页面
            }
        });
    }

    private JTextField createPlaceholderTextField(String placeholder) {
        JTextField textField = new JTextField(15);
        textField.setText(placeholder);
        textField.setForeground(Color.GRAY);

        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setForeground(Color.GRAY);
                }
            }
        });
        return textField;
    }

    private JPasswordField createPasswordPlaceholderTextField(String placeholder) {
        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setEchoChar((char) 0); // 显示占位符文本
        passwordField.setText(placeholder);
        passwordField.setForeground(Color.GRAY);

        passwordField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (new String(passwordField.getPassword()).equals(placeholder)) {
                    passwordField.setText("");
                    passwordField.setForeground(Color.BLACK);
                    passwordField.setEchoChar('●'); // 设置密码显示字符
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (passwordField.getPassword().length == 0) {
                    passwordField.setEchoChar((char) 0); // 不显示密码字符
                    passwordField.setText(placeholder);
                    passwordField.setForeground(Color.GRAY);
                }
            }
        });
        return passwordField;
    }

    // 自定义 JPanel 类
    private class LoginPanel extends JPanel {
        private Image backgroundImage;

        public LoginPanel() {
            // 加载背景图片
            backgroundImage = Toolkit.getDefaultToolkit().getImage("src/imgs/background.png");
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // 绘制背景图片
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginPage().setVisible(true);
        });
    }
}