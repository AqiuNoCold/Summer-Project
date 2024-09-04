package Pages.Pages.Library;

import javax.swing.*;
import java.awt.*;

public class ExplorePage extends JPanel {
    public ExplorePage() {
        setLayout(new BorderLayout());
        JLabel label = new JLabel("探索页面内容", SwingConstants.CENTER);
        add(label, BorderLayout.CENTER);
    }
}