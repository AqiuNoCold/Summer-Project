package Pages.Pages;

import vCampus.Dao.ShopStudentDao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

import vCampus.Entity.Shop.*;

/*
public class StorePage extends JFrame {
    private JTextField searchField;
    private JButton searchButton;
    private JButton favoriteButton;
    private JButton belongsButton;
    private JButton myselfButton;
    private JPanel productsPanel;


    public StorePage() {
        setTitle("商店页面");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // 居中显示窗口

        setLayout(new BorderLayout());

        //搜索框和收藏夹
        JPanel topPanel = new JPanel();
        JPanel belowPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        searchField = new JTextField(20);
        searchButton = new JButton("搜索");
        favoriteButton = new JButton("收藏夹");
        belongsButton = new JButton ("我的商品");
        myselfButton = new JButton("我");
        JButton backButton = new JButton("返回");

        searchButton.addActionListener(new SearchButtonListener());
        favoriteButton.addActionListener(new FavoriteButtonListener());


        topPanel.add(new JLabel("搜索:"));
        topPanel.add(searchField);
        topPanel.add(searchButton);
        belowPanel.add(favoriteButton);
        belowPanel.add(belongsButton);
        belowPanel.add(myselfButton);

        add(topPanel, BorderLayout.NORTH);
        add(belowPanel,BorderLayout.SOUTH);



        //add(backButton, BorderLayout.SOUTH);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                navigateBack();
            }
        });
    }
    

    private class SearchButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

        String query = searchField.getText().toLowerCase();
        List<Product> filteredProducts = new ArrayList<>();
        for (Product product : products) {
            if (product.getName().toLowerCase().contains(query)) {
                filteredProducts.add(product);
            }
        }
        products = filteredProducts;
        updateProductsPanel();

        }
    }

    private class FavoriteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

        StringBuilder favoritesList = new StringBuilder("收藏夹:\n");
        for (Product product : favorites) {
            favoritesList.append(product.getName()).append(" - $").append(product.getPrice()).append("\n");
        }
        JOptionPane.showMessageDialog(StoreWindow.this, favoritesList.toString(), "收藏夹", JOptionPane.INFORMATION_MESSAGE);


        }
    }



    private void navigateBack() {
        new NavigationPage().setVisible(true);
        dispose(); // 关闭当前页面
    }
}
*/



public class StorePage extends JFrame {
    private ShopStudent student;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel homePanel, favoritesPanel, myPanel;
    private JFrame infoFrame;
    private JFrame productFrame;

    public StorePage() {
        setTitle("商店界面");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        infoFrame = new JFrame("个人信息");
        productFrame = new JFrame("商品详情");
        productFrame.setResizable(false);

        // Create main panel with CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Create panels for each view
        homePanel = createHomePanel();
        favoritesPanel = createFavoritesPanel();
        myPanel = createMyPanel();

        // Add panels to mainPanel
        mainPanel.add(homePanel, "首页");
        mainPanel.add(favoritesPanel, "收藏夹");
        mainPanel.add(myPanel, "我的");

        // Create bottom navigation panel
        JPanel bottomPanel = new JPanel();

        JButton returnButton = new JButton("");
        ImageIcon returnIcon = new ImageIcon("src/imgs/shop/return.png");
        returnButton.setIcon(returnIcon);
        returnButton.setOpaque(false);
        returnButton.setContentAreaFilled(false);
        returnButton.setBorderPainted(false);

        JButton homeButton = new JButton("");
        ImageIcon homeIcon = new ImageIcon("src/imgs/shop/home.png");
        homeButton.setIcon(homeIcon);
        homeButton.setOpaque(false);
        homeButton.setContentAreaFilled(false);
        homeButton.setBorderPainted(false);

        JButton favoritesButton = new JButton("");
        ImageIcon favoritesIcon = new ImageIcon("src/imgs/shop/favourite.png");
        favoritesButton.setIcon(favoritesIcon);
        favoritesButton.setOpaque(false);
        favoritesButton.setContentAreaFilled(false);
        favoritesButton.setBorderPainted(false);

        JButton myButton = new JButton("");
        ImageIcon myIcon = new ImageIcon("src/imgs/shop/my.png");
        myButton.setIcon(myIcon);
        myButton.setOpaque(false);
        myButton.setContentAreaFilled(false);
        myButton.setBorderPainted(false);
        //个人信息
        JButton infoButton = new JButton("");
        ImageIcon infoIcon = new ImageIcon("src/imgs/shop/info.png");
        infoButton.setIcon(infoIcon);
        infoButton.setOpaque(false);
        infoButton.setContentAreaFilled(false);
        infoButton.setBorderPainted(false);

        returnButton.addActionListener(e-> navigateBack());
        homeButton.addActionListener(e -> cardLayout.show(mainPanel, "首页"));
        favoritesButton.addActionListener(e -> cardLayout.show(mainPanel, "收藏夹"));
        myButton.addActionListener(e -> cardLayout.show(mainPanel, "我的"));
        infoButton.addActionListener(e -> createInfoFrame());

        bottomPanel.add(returnButton);
        bottomPanel.add(homeButton);
        bottomPanel.add(favoritesButton);
        bottomPanel.add(myButton);
        bottomPanel.add(infoButton);

        getContentPane().add(mainPanel, BorderLayout.CENTER);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private JPanel createHomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel searchPanel = new JPanel();
        //搜索相关
        JTextField searchField = new JTextField(30);
        String defaultText = "搜索";
        searchField.setText("搜索");
        JButton searchButton = new JButton();
        ImageIcon searchIcon = new ImageIcon("src/imgs/shop/search.png");
        searchButton.setIcon(searchIcon);
        searchButton.setOpaque(false);
        searchButton.setContentAreaFilled(false);
        searchButton.setBorderPainted(false);

        boolean[] isDefaultText = {true};
        searchField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (isDefaultText[0]) {
                    searchField.setText("");
                    isDefaultText[0] = false;
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText(defaultText);
                    isDefaultText[0] = true;
                }
            }
        });

        JPanel priceButton = new JPanel();
        JButton priceDown = new JButton("价格从低到高排序");
        JButton priceUp = new JButton("价格从高到低排序");
        ImageIcon downIcon = resizePicture("src/imgs/shop/down.png",15,15);
        ImageIcon upIcon = resizePicture("src/imgs/shop/up.png",15,15);
        priceUp.setIcon(upIcon);
        priceDown.setIcon(downIcon);
        priceUp.setOpaque(false);
        priceUp.setContentAreaFilled(false);
        priceUp.setBorderPainted(false);
        priceDown.setOpaque(false);
        priceDown.setContentAreaFilled(false);
        priceDown.setBorderPainted(false);

        priceButton.add(priceUp);
        priceButton.add(priceDown);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(priceButton);
        panel.add(searchPanel, BorderLayout.NORTH);


        //商品展示相关
        JPanel productPanel = new JPanel(new GridLayout(0, 4));
        for (int i = 1; i <= 9; i++) { // Example products
            // Create an ImageIcon from an image file
            ImageIcon imageIcon = resizePicture("src/vCampus/Shop/img/2.jpg",175,175);
            JPanel product = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.BOTH;

            // 添加第一个组件，占据 1:2 的比例
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 1.0;
            gbc.weighty = 0.5;
            JLabel imageLabel = new JLabel(imageIcon);
            imageLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Change cursor to hand icon
            int finalI = i;
            imageLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    creatProductFrame(finalI, imageIcon, "商品 " + finalI, finalI * 10);
                }
            });

            product.add(imageLabel,gbc);
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.weightx = 1.0;
            gbc.weighty = 0.3;
            product.add(new JLabel("商品 " + i),gbc);
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.weightx = 1.0;
            gbc.weighty = 0.2;
            product.add(new JLabel("价格: ￥" + (i * 10)),gbc);
            productPanel.add(product);
        }

        JScrollPane scrollPane = new JScrollPane(productPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createFavoritesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel searchPanel = new JPanel();
        //收藏夹相关
        JTextField searchField = new JTextField(30);
        searchField.setText("搜索收藏夹内商品");
        String defaultText = "搜索收藏夹内商品";
        JButton searchButton = new JButton();
        ImageIcon searchIcon = new ImageIcon("src/imgs/shop/search.png");
        searchButton.setIcon(searchIcon);
        searchButton.setOpaque(false);
        searchButton.setContentAreaFilled(false);
        searchButton.setBorderPainted(false);

        boolean[] isDefaultText = {true};
        searchField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (isDefaultText[0]) {
                    searchField.setText("");
                    isDefaultText[0] = false;
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText(defaultText);
                    isDefaultText[0] = true;
                }
            }
        });

        JPanel priceButton = new JPanel();
        JButton priceDown = new JButton("价格从低到高排序");
        JButton priceUp = new JButton("价格从高到低排序");
        ImageIcon downIcon = resizePicture("src/imgs/shop/down.png",15,15);
        ImageIcon upIcon = resizePicture("src/imgs/shop/up.png",15,15);
        priceUp.setIcon(upIcon);
        priceDown.setIcon(downIcon);
        priceUp.setOpaque(false);
        priceUp.setContentAreaFilled(false);
        priceUp.setBorderPainted(false);
        priceDown.setOpaque(false);
        priceDown.setContentAreaFilled(false);
        priceDown.setBorderPainted(false);

        priceButton.add(priceUp);
        priceButton.add(priceDown);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(priceButton);
        panel.add(searchPanel, BorderLayout.NORTH);


        //商品展示相关
        JPanel productPanel = new JPanel(new GridLayout(0, 4));
        for (int i = 1; i <= 8; i++) { // Example products
            // Create an ImageIcon from an image file
            ImageIcon imageIcon = resizePicture("src/vCampus/Shop/img/2.jpg",175,175);
            JPanel product = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.BOTH;

            // 添加第一个组件，占据 1:2 的比例
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 1.0;
            gbc.weighty = 0.5;
            product.add(new JLabel(imageIcon),gbc);
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.weightx = 1.0;
            gbc.weighty = 0.3;
            product.add(new JLabel("商品 " + i),gbc);
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.weightx = 1.0;
            gbc.weighty = 0.2;
            product.add(new JLabel("价格: ￥" + (i * 10)),gbc);
            productPanel.add(product);
        }

        JScrollPane scrollPane = new JScrollPane(productPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createMyPanel() {

        JPanel panel = new JPanel(new BorderLayout());
        JPanel searchPanel = new JPanel();
        //所属商品相关
        JTextField searchField = new JTextField(30);
        searchField.setText("搜索所属的商品");
        String defaultText = "搜索所属的商品";
        JButton searchButton = new JButton();
        ImageIcon searchIcon = new ImageIcon("src/imgs/shop/search.png");
        searchButton.setIcon(searchIcon);
        searchButton.setOpaque(false);
        searchButton.setContentAreaFilled(false);
        searchButton.setBorderPainted(false);

        boolean[] isDefaultText = {true};
        searchField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (isDefaultText[0]) {
                    searchField.setText("");
                    isDefaultText[0] = false;
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText(defaultText);
                    isDefaultText[0] = true;
                }
            }
        });

        JPanel priceButton = new JPanel();
        JButton priceDown = new JButton("价格从低到高排序");
        JButton priceUp = new JButton("价格从高到低排序");
        ImageIcon downIcon = resizePicture("src/imgs/shop/down.png",15,15);
        ImageIcon upIcon = resizePicture("src/imgs/shop/up.png",15,15);
        priceUp.setIcon(upIcon);
        priceDown.setIcon(downIcon);
        priceUp.setOpaque(false);
        priceUp.setContentAreaFilled(false);
        priceUp.setBorderPainted(false);
        priceDown.setOpaque(false);
        priceDown.setContentAreaFilled(false);
        priceDown.setBorderPainted(false);

        priceButton.add(priceUp);
        priceButton.add(priceDown);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(priceButton);
        panel.add(searchPanel, BorderLayout.NORTH);


        //商品展示相关
        JPanel productPanel = new JPanel(new GridLayout(0, 4));
        for (int i = 1; i <= 9; i++) { // Example products
            // Create an ImageIcon from an image file
            ImageIcon imageIcon = resizePicture("src/vCampus/Shop/img/2.jpg",175,175);
            JPanel product = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.BOTH;

            // 添加第一个组件，占据 1:2 的比例
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 1.0;
            gbc.weighty = 0.5;
            product.add(new JLabel(imageIcon),gbc);
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.weightx = 1.0;
            gbc.weighty = 0.3;
            product.add(new JLabel("商品 " + i),gbc);
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.weightx = 1.0;
            gbc.weighty = 0.2;
            product.add(new JLabel("价格: ￥" + (i * 10)),gbc);
            productPanel.add(product);
        }

        JScrollPane scrollPane = new JScrollPane(productPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;

        /*
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel balanceLabel = new JLabel("余额: ￥100");
        JButton billButton = new JButton("账单");

        panel.add(balanceLabel);
        panel.add(billButton);

        // Add code to handle billButton action here

        return panel;
         */
    }

    public ImageIcon resizePicture(String imagePath,int targetWidth,int targetHeight){
        ImageIcon originalIcon = new ImageIcon(imagePath);
        Image originalImage = originalIcon.getImage();

        // Resize the image
        Image resizedImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        // Create a new ImageIcon from the resized image
        ImageIcon resizedIcon = new ImageIcon(resizedImage);
        return resizedIcon;
    }

    private void createInfoFrame(){
        infoFrame.setSize(400,550);
        infoFrame.setLocationRelativeTo(null);
        infoFrame.setVisible(true);

        JPanel panel = new JPanel();


        JLabel balanceLabel = new JLabel("余额: ¥1000.00");
        balanceLabel.setHorizontalAlignment(SwingConstants.LEFT);
        balanceLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 设置边距
        infoFrame.add(balanceLabel, BorderLayout.NORTH);

        // 创建不可修改的文本框用于显示信息
        JTextArea infoTextArea = new JTextArea("这里显示一些信息...");
        infoTextArea.setEditable(false); // 设置文本框为不可修改
        infoTextArea.setLineWrap(true); // 自动换行
        infoTextArea.setWrapStyleWord(true); // 保持单词完整
        infoTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 设置边距
        infoTextArea.setPreferredSize(new Dimension(250, 50)); // 设置首选大小
        infoTextArea.setBackground(Color.WHITE); // 设置背景颜色

        JScrollPane scrollPane = new JScrollPane(infoTextArea);
        //infoTextArea.setFont(new Font("Arial", Font.PLAIN, 12)); // 设置字体
        infoFrame.add(scrollPane, BorderLayout.CENTER);
/*
        //余额
        JLabel balanceLabel = new JLabel();
        balanceLabel.setText("余额："+100);

        JPanel balancePanel = new JPanel();
        balancePanel.add(balanceLabel);

        //账单
        JPanel billPanel = new JPanel();

        JLabel billLabel = new JLabel("账单");
        ImageIcon billIcon = new ImageIcon("src/imgs/shop/bill.png");
        billLabel.setIcon(billIcon);

        JTextField billText = new JTextField(30);

        billPanel.add(balanceLabel);
        billPanel.add(billLabel);

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(balancePanel);
        panel.add(billPanel);
        infoFrame.add(panel);
*/

        // Add code to handle billButton action here

    }

    private void creatProductFrame(int id, ImageIcon imageIcon, String name, double price){
        productFrame.setSize(600, 400);
        productFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        productFrame.setLocationRelativeTo(null);
        productFrame.setLayout(new BorderLayout());

        imageIcon = resizePicture("src/vCampus/Shop/img/2.jpg",300,400);
        // Create a panel for product details
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Add product details to the panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.weighty = 0.5;
        detailsPanel.add(new JLabel(imageIcon), gbc);

        JPanel rightUp = new JPanel(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        rightUp.add(new JLabel("名称: " + name),gbc);

        // Add Owner label
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        rightUp.add(new JLabel("所属: " + "987654321"), gbc);

        //Time
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        Date time = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String datetime = dateFormat.format(time);
        rightUp.add(new JLabel("时间: " + datetime), gbc);

        //Add numbers
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        rightUp.add(new JLabel("数量: " + 1), gbc);

        //Add Price
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        rightUp.add(new JLabel("价格: ￥" + price), gbc);

        //discount
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        rightUp.add(new JLabel("折扣: " + "暂无"), gbc);

        //购买按钮
        JButton buyButton = new JButton("购买");

        //buyButton.setBackground(new Color(228, 88, 88));
        // 确保按钮背景色可见
        //buyButton.setOpaque(true);
        //buyButton.setBorderPainted(false); // 可选，去掉按钮的边框

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        rightUp.add(buyButton, gbc);

        //收藏按钮
        JButton favorButton = new JButton("");
        //需要ifelse
        ImageIcon favorIcon = resizePicture("src/imgs/shop/star0.png",30,30);
        favorButton.setIcon(favorIcon);
        favorButton.setOpaque(false);
        favorButton.setContentAreaFilled(false);
        favorButton.setBorderPainted(false);
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        rightUp.add(favorButton, gbc);


        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.2;
        detailsPanel.add(rightUp,gbc);

        // Add the details panel to the frame
        productFrame.add(detailsPanel, BorderLayout.CENTER);

        // Display the frame
        productFrame.setVisible(true);
    }

    private void navigateBack() {
        new NavigationPage().setVisible(true);
        dispose(); // 关闭当前页面
    }

}
