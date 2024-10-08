package Pages.Utils;

import java.awt.Color;

import javax.swing.*;

public class IconUtils {

    public static ImageIcon loadSVGImage(String imagePath, int windowWidth, int windowHeight) {
        double scaleFactor = Math.min(windowWidth / 1920.0, windowHeight / 1080.0);
        int iconSize = (int) (64 * scaleFactor); // 根据缩放比例调整图标大小
        ImageIcon icon = SVGImageLoader.loadSVGImage(imagePath, iconSize, iconSize);

        if (icon == null || icon.getIconWidth() == -1) { // 判断图片是否加载成功
            System.err.println("Error: Could not load image at " + imagePath);
        }

        return icon;
    }

    public static void updateButtonIcon(JButton button, String imagePath, int windowWidth, int windowHeight) {
        double scaleFactor = Math.min(windowWidth / 1920.0, windowHeight / 1080.0);
        int iconSize = (int) (64 * scaleFactor);

        button.setIcon(SVGImageLoader.loadSVGImage(imagePath, iconSize, iconSize));
        setButtonBorder(button, scaleFactor);
    }

    public static void setButtonBorder(JButton button, double scaleFactor) {
        button.setBorder(BorderFactory.createEmptyBorder((int) (10 * scaleFactor), (int) (10 * scaleFactor),
                (int) (10 * scaleFactor), (int) (10 * scaleFactor))); // 根据缩放比例调整按钮内边距
    }

    public static JButton createImageButton(String imagePath, int windowWidth, int windowHeight) {
        ImageIcon icon = loadSVGImage(imagePath, windowWidth, windowHeight);
        JButton button = new JButton(icon);
        button.setBackground(new Color(255, 255, 255)); // 设置背景颜色
        button.setFocusPainted(false);
        double scaleFactor = Math.min(windowWidth / 1920.0, windowHeight / 1080.0);
        setButtonBorder(button, scaleFactor); // 设置按钮边框
        return button;
    }
}