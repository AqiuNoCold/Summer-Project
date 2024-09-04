package Pages.Pages.Library;

import javax.swing.*;
import java.awt.*;

public class HomePage extends JPanel {
    public HomePage() {
        setLayout(new BorderLayout());
        JLabel label = new JLabel("首页内容", SwingConstants.CENTER);
        add(label, BorderLayout.CENTER);
    }
}
