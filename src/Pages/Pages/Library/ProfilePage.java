package Pages.Pages.Library;

import javax.swing.*;
import java.awt.*;

public class ProfilePage extends JPanel {
    public ProfilePage() {
        setLayout(new BorderLayout());
        JLabel label = new JLabel("个人主页内容", SwingConstants.CENTER);
        add(label, BorderLayout.CENTER);
    }
}