package Pages.Pages;



import Pages.MainApp;
import org.w3c.dom.Text;
import vCampus.Dao.ShopStudentDao;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import vCampus.Entity.Shop.*;
import vCampus.Entity.User;
import Pages.Utils.*;

import static Pages.Utils.ImageUtils.compressImage;
import static Pages.Utils.ImageUtils.saveCompressedImage;

public class StorePage extends JFrame {
    ObjectInputStream in = MainApp.getIn();
    ObjectOutputStream out = MainApp.getOut();

    private ShopStudent student;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel homePanel, favoritesPanel, myPanel;
    private JDialog addFrame;
    private JDialog updateFrame;
    private JFrame infoFrame;
    private JFrame productFrame;

    public StorePage(ShopStudent shopStudent) {
        //student的初始化
        student = shopStudent;
        List<Product> shopList = student.getProducts();

        //商店界面的初始化
        setTitle("商店界面");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        addFrame = new JDialog((Frame) null, "添加新的商品", true);
        updateFrame = new JDialog((Frame) null, "更新商品", true);
        infoFrame = new JFrame("个人信息");
        productFrame = new JFrame("商品详情");
        productFrame.setResizable(false);

        // Create main panel with CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        //mainPanel.setBorder(BorderFactory.createLineBorder(new Color(144, 238, 144), 5)); // 浅绿色边框，宽度为5像素
        // Create panels for each view
        homePanel = createHomePanel(shopList);
        favoritesPanel = createFavoritesPanel(student.getFavorites());
        myPanel = createMyPanel(student.getBelongs());

        // Add panels to mainPanel
        mainPanel.add(homePanel, "首页");
        mainPanel.add(favoritesPanel, "收藏夹");
        mainPanel.add(myPanel, "我的");

        // Create bottom navigation panel
        JPanel bottomPanel = new JPanel();

        JButton addButton = new JButton("");
        ImageIcon addIcon = getImage("/imgs/shop/add.png");
        addButton.setIcon(addIcon);
        addButton.setOpaque(false);
        addButton.setContentAreaFilled(false);
        addButton.setBorderPainted(false);

        JButton homeButton = new JButton("");
        ImageIcon homeIcon = getImage("/imgs/shop/home.png");
        homeButton.setIcon(homeIcon);
        homeButton.setOpaque(false);
        homeButton.setContentAreaFilled(false);
        homeButton.setBorderPainted(false);

        JButton favoritesButton = new JButton("");
        ImageIcon favoritesIcon = getImage("/imgs/shop/favourite.png");
        favoritesButton.setIcon(favoritesIcon);
        favoritesButton.setOpaque(false);
        favoritesButton.setContentAreaFilled(false);
        favoritesButton.setBorderPainted(false);

        JButton myButton = new JButton("");
        ImageIcon myIcon = getImage("/imgs/shop/my.png");
        myButton.setIcon(myIcon);
        myButton.setOpaque(false);
        myButton.setContentAreaFilled(false);
        myButton.setBorderPainted(false);
        //个人信息
        JButton infoButton = new JButton("");
        ImageIcon infoIcon = getImage("/imgs/shop/info.png");
        infoButton.setIcon(infoIcon);
        infoButton.setOpaque(false);
        infoButton.setContentAreaFilled(false);
        infoButton.setBorderPainted(false);


        homeButton.addActionListener(e -> cardLayout.show(mainPanel, "首页"));
        favoritesButton.addActionListener(e -> cardLayout.show(mainPanel, "收藏夹"));
        myButton.addActionListener(e -> cardLayout.show(mainPanel, "我的"));
        addButton.addActionListener(e-> createAddFrame());
        infoButton.addActionListener(e -> createInfoFrame(true));

        bottomPanel.add(homeButton);
        bottomPanel.add(favoritesButton);
        bottomPanel.add(myButton);
        bottomPanel.add(addButton);
        bottomPanel.add(infoButton);

        getContentPane().add(mainPanel, BorderLayout.CENTER);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                navigateBack();
            }
        });
    }

    private JPanel createHomePanel(List<Product> productList) {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel searchPanel = new JPanel();
        //搜索相关
        JTextField searchField = new JTextField(20);
        String defaultText = "搜索";
        searchField.setText("搜索");
        JButton searchButton = new JButton();
        ImageIcon searchIcon = getImage("/imgs/shop/search.png");
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
        searchField.addActionListener(e->searchProducts(searchField));
        searchButton.addActionListener(e->searchProducts(searchField));
        JPanel priceButton = new JPanel();
        JButton priceDown = new JButton("价格从低到高排序");
        JButton priceUp = new JButton("价格从高到低排序");
        JButton refresh = new JButton("");
        ImageIcon downIcon = resizePicture("/imgs/shop/down.png",15,15);
        ImageIcon upIcon = resizePicture("/imgs/shop/up.png",15,15);
        ImageIcon refreshIcon = resizePicture("/imgs/shop/refresh.png",20,20);
        priceUp.setIcon(upIcon);
        priceDown.setIcon(downIcon);
        refresh.setIcon(refreshIcon);
        priceUp.setOpaque(false);
        priceUp.setContentAreaFilled(false);
        priceUp.setBorderPainted(false);
        priceDown.setOpaque(false);
        priceDown.setContentAreaFilled(false);
        priceDown.setBorderPainted(false);
        refresh.setOpaque(false);
        refresh.setContentAreaFilled(false);
        refresh.setBorderPainted(false);

        priceButton.add(priceUp);
        priceButton.add(priceDown);
        priceButton.add(refresh);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(priceButton);

        panel.add(searchPanel, BorderLayout.NORTH);

        //价格排序功能
        priceDown.addActionListener(e->displayProductsSortedByPrice(true,1,productList) );
        priceUp.addActionListener(e->displayProductsSortedByPrice(false,1,productList) );
        //更新功能
        refresh.addActionListener(e-> refreshShop());

        //商品展示相关
        JScrollPane scrollPane = createShowProducts(productList);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createFavoritesPanel(List<Product> productList) {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel searchPanel = new JPanel();
        //收藏夹相关
        JTextField searchField = new JTextField(20);
        searchField.setText("搜索收藏夹内商品");
        String defaultText = "搜索收藏夹内商品";
        JButton searchButton = new JButton();
        ImageIcon searchIcon = getImage("/imgs/shop/search.png");
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
        searchField.addActionListener(e->searchListProducts(searchField,1));
        searchButton.addActionListener(e->searchListProducts(searchField,1));

        JPanel priceButton = new JPanel();
        JButton priceDown = new JButton("价格从低到高排序");
        JButton priceUp = new JButton("价格从高到低排序");
        ImageIcon downIcon = resizePicture("/imgs/shop/down.png",15,15);
        ImageIcon upIcon = resizePicture("/imgs/shop/up.png",15,15);
        priceUp.setIcon(upIcon);
        priceDown.setIcon(downIcon);
        priceUp.setOpaque(false);
        priceUp.setContentAreaFilled(false);
        priceUp.setBorderPainted(false);
        priceDown.setOpaque(false);
        priceDown.setContentAreaFilled(false);
        priceDown.setBorderPainted(false);

        //更新
        JButton refresh = new JButton("");
        ImageIcon refreshIcon = resizePicture("/imgs/shop/refresh.png",20,20);
        refresh.setIcon(refreshIcon);
        refresh.setOpaque(false);
        refresh.setContentAreaFilled(false);
        refresh.setBorderPainted(false);


        priceButton.add(priceUp);
        priceButton.add(priceDown);
        priceButton.add(refresh);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(priceButton);
        panel.add(searchPanel, BorderLayout.NORTH);

        //价格排序
        priceDown.addActionListener(e->displayProductsSortedByPrice(true,1,productList) );
        priceUp.addActionListener(e->displayProductsSortedByPrice(false,1,productList) );
        //更新功能
        refresh.addActionListener(e-> refreshList(1));
        //商品展示相关
        JScrollPane scrollPane = createShowProducts(productList);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createMyPanel(List<Product> productList) {

        JPanel panel = new JPanel(new BorderLayout());
        JPanel searchPanel = new JPanel();
        //所属商品相关
        JTextField searchField = new JTextField(20);
        searchField.setText("搜索所属的商品");
        String defaultText = "搜索所属的商品";
        JButton searchButton = new JButton();
        ImageIcon searchIcon = getImage("/imgs/shop/search.png");
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
        ImageIcon downIcon = resizePicture("/imgs/shop/down.png",15,15);
        ImageIcon upIcon = resizePicture("/imgs/shop/up.png",15,15);
        priceUp.setIcon(upIcon);
        priceDown.setIcon(downIcon);
        priceUp.setOpaque(false);
        priceUp.setContentAreaFilled(false);
        priceUp.setBorderPainted(false);
        priceDown.setOpaque(false);
        priceDown.setContentAreaFilled(false);
        priceDown.setBorderPainted(false);

        //刷新
        JButton refresh = new JButton("");
        ImageIcon refreshIcon = resizePicture("/imgs/shop/refresh.png",20,20);
        refresh.setIcon(refreshIcon);
        refresh.setOpaque(false);
        refresh.setContentAreaFilled(false);
        refresh.setBorderPainted(false);

        priceButton.add(priceUp);
        priceButton.add(priceDown);
        priceButton.add(refresh);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(priceButton);
        panel.add(searchPanel, BorderLayout.NORTH);

        //价格排序
        priceDown.addActionListener(e->displayProductsSortedByPrice(true,2,productList) );
        priceUp.addActionListener(e->displayProductsSortedByPrice(false,2,productList) );
        //更新功能
        refresh.addActionListener(e-> refreshList(2));
        //商品展示
        JScrollPane scrollPane = createShowProducts(student.getBelongs());
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private void createInfoFrame(boolean show){
        infoFrame.getContentPane().removeAll();
        infoFrame.setSize(400,550);
        infoFrame.setLocationRelativeTo(null);

        infoFrame.setVisible(show);

        JPanel panel = new JPanel();

        JLabel balanceLabel = new JLabel("余额: ¥"+student.getRemain());
        balanceLabel.setHorizontalAlignment(SwingConstants.LEFT);
        balanceLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 设置边距
        infoFrame.add(balanceLabel, BorderLayout.NORTH);

        // 创建不可修改的文本框用于显示信息
        String Bill = "账单： \n";
        List<String> bills = student.getBill();
        Collections.reverse(bills);
        for(String his : bills){
            Bill = Bill + his+"\n"+"\n";
        }
        JTextArea infoTextArea = new JTextArea(Bill);
        infoTextArea.setEditable(false); // 设置文本框为不可修改
        infoTextArea.setLineWrap(true); // 自动换行
        infoTextArea.setWrapStyleWord(true); // 保持单词完整
        infoTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 设置边距
        //infoTextArea.setPreferredSize(new Dimension(250, 200)); // 设置首选大小
        infoTextArea.setBackground(Color.WHITE); // 设置背景颜色

        JScrollPane scrollPane = new JScrollPane(infoTextArea);
        //infoTextArea.setFont(new Font("Arial", Font.PLAIN, 12)); // 设置字体
        infoFrame.add(scrollPane, BorderLayout.CENTER);
        infoFrame.revalidate();
        infoFrame.repaint();
    }

    public void createAddFrame() {
        addFrame.getContentPane().removeAll();
        addFrame.setSize(300, 300);
        addFrame.setLocationRelativeTo(null);
        addFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addFrame.setLayout(new BorderLayout());
        addFrame.setResizable(false);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(0, 2, 10, 10));

        // Product ID - auto-generated
        JLabel idLabel = new JLabel("Product ID:");
        JTextField idField = new JTextField();
        idField.setEditable(false);
        idField.setText(getRecordCount("tblproduct"));
        formPanel.add(idLabel);
        formPanel.add(idField);

        // Product Name
        formPanel.add(new JLabel("新的商品名字:"));
        JTextField nameField = new JTextField();
        nameField.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
                if ((getLength() + str.length()) <= 20) {
                    super.insertString(offset, str, attr);
                }
            }
        });

        formPanel.add(nameField);

        // Product Quantity
        formPanel.add(new JLabel("新的商品数量:"));
        JTextField quantityField = new JTextField();
        quantityField.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
                if (str.matches("\\d*") && (getLength() + str.length() <= 4)) {
                    super.insertString(offset, str, attr);
                }
            }
        });

        formPanel.add(quantityField);

        // Product Price
        formPanel.add(new JLabel("新的商品单价:"));
        JTextField priceField = new JTextField();
        priceField.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
                if (str.matches("\\d*(\\.\\d*)?") && (getLength() + str.length() <= 7)) { // 7 is for up to 9999.99
                    super.insertString(offset, str, attr);
                }
            }
        });

        formPanel.add(priceField);

        // Product Discount
        formPanel.add(new JLabel("新的商品折扣(0-1):"));
        JTextField discountField = new JTextField();
        discountField.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
                if (str.matches("^[0-1]?(\\.[0-9]{0,2})?$")) {
                    super.insertString(offset, str, attr);
                }
            }
        });
        formPanel.add(discountField);

        // Image Path
        formPanel.add(new JLabel("新的商品图片路径:"));
        JTextField imagePathField = new JTextField();
        JButton browseButton = new JButton("浏览...");
        browseButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "png");
            fileChooser.setFileFilter(filter); // Set the file filter
            int result = fileChooser.showOpenDialog(addFrame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                imagePathField.setText(selectedFile.getAbsolutePath());
            }
        });
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.add(imagePathField, BorderLayout.CENTER);
        imagePanel.add(browseButton, BorderLayout.EAST);
        //formPanel.add(new JLabel("Image Path:"));
        formPanel.add(imagePanel);

        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("确定");
        JButton cancelButton = new JButton("取消");

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String id = idField.getText();
                    String name = nameField.getText();
                    int quantity = Integer.parseInt(quantityField.getText());
                    float price = Float.parseFloat(priceField.getText());
                    float discount = Float.parseFloat(discountField.getText());
                    String imagePath = imagePathField.getText();
                    // Define target dimensions for the image
                    int targetWidth = 175;
                    int targetHeight = 175;

                    // Load the image
                    BufferedImage originalImage = ImageIO.read(new File(imagePath));

                    // Create a new file for saving the compressed image
                    //URL imageUrl = getClass().getResource(imagePath);
                    //outputFile = new File("src/vCampus/Shop/img/" + id + ".png");
                    File outputFile = new File("src/vCampus/Shop/img/" + id + ".png");

                    // Construct the path to the out folder
                    String projectRoot = System.getProperty("user.dir");
                    File outputFiletoOut = new File(projectRoot + "/out/production/Summer Project/vCampus/Shop/img/" + id + ".png");
                    // Save the resized image as PNG
                    saveCompressedImage(originalImage, outputFile, targetWidth, targetHeight);
                    saveCompressedImage(originalImage, outputFiletoOut, targetWidth, targetHeight);

                    // Load the compressed image into a byte array
                    byte[] compressedImage = Files.readAllBytes(outputFiletoOut.toPath());

                    // Compress image
                    //byte[] compressedImage = compressImage(new File(imagePath), 48 * 1024,200,200); // 63 KB
                    // Create new product
                    Product newProduct = new Product(id, name, price, quantity, student.getCard());
                    newProduct.setDiscount(discount);
                    newProduct.setImage2(compressedImage); // Use compressed image


                    // Add product to student
                    try {
                        //数据库信息更新
                        out.writeObject("5");
                        out.writeObject("addNew");
                        out.writeObject(newProduct);
                        out.writeObject(student);
                        out.flush();
                        boolean success = (boolean) in.readObject();
                        newProduct = (Product) in.readObject();
                        student = (ShopStudent) in.readObject();
                        if (success) {
                            //本地信息更新
                            myPanel.removeAll();
                            JPanel newMyPanel = createMyPanel(student.getBelongs());
                            myPanel.add(newMyPanel, BorderLayout.CENTER);
                            myPanel.revalidate();
                            myPanel.repaint();
                            JOptionPane.showMessageDialog(addFrame, "新商品添加成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(addFrame, "添加失败！", "异常", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                    addFrame.dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(addFrame, "请填写有效值！", "错误", JOptionPane.ERROR_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(addFrame, "图片压缩错误！", "错误", JOptionPane.ERROR_MESSAGE);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        cancelButton.addActionListener(e -> addFrame.dispose());

        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);

        addFrame.add(formPanel, BorderLayout.CENTER);
        addFrame.add(buttonPanel, BorderLayout.SOUTH);

        infoFrame.revalidate();
        infoFrame.repaint();
        addFrame.setVisible(true);
    }

    private JScrollPane createShowProducts(List<Product> productList){
        JPanel productPanel = new JPanel(new GridLayout(0, 4));
        for (Product randomProduct : productList) {
            ImageIcon imageIcon = resizePicture("/vCampus/Shop/img/"+randomProduct.getId()+".png",175,175);
            JPanel product = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.BOTH;

            // 添加第一个组件，占据 1:2 的比例
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 1.0;
            gbc.weighty = 0.25;
            JLabel imageLabel = new JLabel(imageIcon);
            imageLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Change cursor to hand icon
            imageLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    createProductFrame(randomProduct);
                }
            });
            product.add(imageLabel,gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.weightx = 1.0;
            gbc.weighty = 0.15;
            product.add(new JLabel("商品 " + randomProduct.getName()),gbc);
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.weightx = 1.0;
            gbc.weighty = 0.1;
            product.add(new JLabel("价格: ￥" + randomProduct.getPrice()),gbc);
            productPanel.add(product);

            if(productList.size()<=4){
                gbc.gridx = 0;
                gbc.gridy = 3;
                gbc.weightx = 1.0;
                gbc.weighty = 0.5;
                product.add(new JLabel(""),gbc);
                gbc.gridx = 0;
                gbc.gridy = 4;
                gbc.weightx = 1.0;
                gbc.weighty = 0.5;
                product.add(new JLabel(""),gbc);
                gbc.gridx = 0;
                gbc.gridy = 5;
                gbc.weightx = 1.0;
                gbc.weighty = 0.5;
                product.add(new JLabel(""),gbc);
            }
        }

        JScrollPane scrollPane = new JScrollPane(productPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        return scrollPane;
    }

    private void createProductFrame(Product product){
        clearFrameContent(productFrame);
        productFrame.setSize(600, 400);
        productFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        productFrame.setLocationRelativeTo(null);
        productFrame.setLayout(new BorderLayout());

        ImageIcon imageIcon = new ImageIcon();
        imageIcon = resizePicture("/vCampus/Shop/img/"+product.getId()+".png",300,400);
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
        JLabel newImageLabel = new JLabel(resizePicture("/vCampus/Shop/img/" + product.getId() + ".png", 300, 400));
        detailsPanel.add(newImageLabel, gbc);

        JPanel rightUp = new JPanel(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        rightUp.add(new JLabel("名称: " + product.getName()),gbc);

        // Add Owner label
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        rightUp.add(new JLabel("所属: " + product.getOwner()), gbc);

        //Time
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        Date time = product.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String datetime = dateFormat.format(time);
        rightUp.add(new JLabel("时间: " + datetime), gbc);

        //Add numbers
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        rightUp.add(new JLabel("数量: " + product.getNumbers()), gbc);

        //Add Price
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        rightUp.add(new JLabel("价格: ￥" + product.getPrice()), gbc);

        //discount
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        if(product.getDiscount() == 1)
            rightUp.add(new JLabel("折扣: " + "暂无"), gbc);
        else
            rightUp.add(new JLabel("折扣: " + product.getDiscount()*100+"%"), gbc);

        //购买/更新按钮
        JButton buyButton = new JButton();
        if(Objects.equals(student.getCard(), product.getOwner())) {
            buyButton.setText("更新");
            buyButton.addActionListener(e->createUpdateFrame(product));
        }else{
            buyButton.setText("购买");
            buyButton.addActionListener(e->purchaseProduct(product));
        }

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        rightUp.add(buyButton, gbc);

        //收藏按钮
        JButton favorButton = new JButton("");
        //是否收藏
        ImageIcon favorIcon = new ImageIcon();
        boolean is = isFavorites(product.getId());
        if(is)
            favorIcon = resizePicture("/imgs/shop/star1.png",30,30);
        else
            favorIcon = resizePicture("/imgs/shop/star0.png",30,30);
        favorButton.setIcon(favorIcon);
        favorButton.setOpaque(false);
        favorButton.setContentAreaFilled(false);
        favorButton.setBorderPainted(false);
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        rightUp.add(favorButton, gbc);
        favorButton.addActionListener(e->changeFavorites(product,is));

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.2;
        detailsPanel.add(rightUp,gbc);

        // Add the details panel to the frame
        productFrame.add(detailsPanel, BorderLayout.CENTER);

        productFrame.revalidate();
        productFrame.repaint();
        // Display the frame
        productFrame.setVisible(true);
    }

    public void createUpdateFrame(Product product) {
        updateFrame.getContentPane().removeAll();
        updateFrame.setSize(300, 300);
        updateFrame.setLocationRelativeTo(null);
        updateFrame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        updateFrame.setLayout(new BorderLayout());
        updateFrame.setResizable(false);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(0, 2, 10, 10));

        // Product ID
        JLabel idLabel = new JLabel("Product ID:");
        JTextField idField = new JTextField(product.getId());
        idField.setEditable(false);
        formPanel.add(idLabel);
        formPanel.add(idField);

        // Product Name
        formPanel.add(new JLabel("新的商品名字:"));
        String text = product.getName();
        JTextField nameField = new JTextField();
        nameField.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
                if ((getLength() + str.length()) <= 20) {
                    super.insertString(offset, str, attr);
                }
            }
        });
        formPanel.add(nameField);

        // Product Quantity
        formPanel.add(new JLabel("新的商品数量:"));
        JTextField quantityField = new JTextField(String.valueOf(product.getNumbers()));
        quantityField.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
                if (str.matches("\\d*") && (getLength() + str.length() <= 4)) {
                    super.insertString(offset, str, attr);
                }
            }
        });
        formPanel.add(quantityField);

        // Product Price
        formPanel.add(new JLabel("新的商品单价:"));
        JTextField priceField = new JTextField(String.valueOf(product.getPrice()));
        priceField.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
                if (str.matches("\\d*(\\.\\d*)?") && (getLength() + str.length() <= 7)) {
                    super.insertString(offset, str, attr);
                }
            }
        });
        formPanel.add(priceField);

        // Product Discount
        formPanel.add(new JLabel("新的商品折扣(0-1):"));
        JTextField discountField = new JTextField(String.valueOf(product.getDiscount()));
        discountField.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
                if (str.matches("^[0-1]?(\\.[0-9]{0,2})?$")) {
                    super.insertString(offset, str, attr);
                }
            }
        });
        formPanel.add(discountField);

        // Image Path
        formPanel.add(new JLabel("新的商品图片路径:"));
        JTextField imagePathField = new JTextField();
        imagePathField.setEditable(false);
        JButton browseButton = new JButton("浏览...");
        browseButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "png");
            fileChooser.setFileFilter(filter);
            int result = fileChooser.showOpenDialog(updateFrame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                imagePathField.setText(selectedFile.getAbsolutePath());
            }
        });
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.add(imagePathField, BorderLayout.CENTER);
        imagePanel.add(browseButton, BorderLayout.EAST);
        formPanel.add(imagePanel);

        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton originButton = new JButton("原始");
        JButton updateButton = new JButton("确定");
        JButton cancelButton = new JButton("取消");
        JButton deleteButton = new JButton("删除");

        originButton.addActionListener(e -> {
            nameField.setText(product.getName());
            quantityField.setText(String.valueOf(product.getNumbers()));
            priceField.setText(String.valueOf(product.getPrice()));
            discountField.setText(String.valueOf(product.getDiscount()));
        });

        updateButton.addActionListener(e -> {
            try {
                String id = idField.getText();
                String name = nameField.getText();
                if(Objects.equals(name, "")) {
                    name = product.getName();
                }
                String Squantity = quantityField.getText();
                int quantity = Integer.parseInt(Squantity);
                if(Objects.equals(Squantity, "")) {
                    quantity = product.getNumbers();
                }
                String Sprice = priceField.getText();
                float price = Float.parseFloat(Sprice);
                if(Objects.equals(Sprice, "")) {
                    price = product.getPrice();
                }
                String Sdiscount = discountField.getText();
                float discount = Float.parseFloat(Sdiscount);
                if(Objects.equals(Sdiscount, "")) {
                    discount = product.getDiscount();
                }
                String imagePath = imagePathField.getText();
                // Optional: handle image processing

                Product updatedProduct = new Product(id, name, price, quantity, product.getOwner());
                updatedProduct.setDiscount(discount);
                // Optionally update image here if needed
                if(!Objects.equals(imagePath, "")){
                    int targetWidth = 175;
                    int targetHeight = 175;

                    // Load the image
                    BufferedImage originalImage = ImageIO.read(new File(imagePath));
                    File outputFile = new File("src/vCampus/Shop/img/" + id + ".png");

                    String projectRoot = System.getProperty("user.dir");
                    File outputFiletoOut = new File(projectRoot + "/out/production/Summer Project/vCampus/Shop/img/" + id + ".png");
                    // Save the resized image as PNG
                    saveCompressedImage(originalImage, outputFile, targetWidth, targetHeight);
                    saveCompressedImage(originalImage, outputFiletoOut, targetWidth, targetHeight);

                    byte[] compressedImage = Files.readAllBytes(outputFiletoOut.toPath());
                    updatedProduct.setImage2(compressedImage); // Use compressed image
                }else{
                    updatedProduct.setImage2(product.getImage());
                }

                // Send updated product to server/database
                // Handle the update logic
                out.writeObject("5");
                out.writeObject("updateProduct");
                out.writeObject(updatedProduct);
                out.writeObject(student);
                out.flush();
                updatedProduct = (Product) in.readObject();
                student = (ShopStudent) in.readObject();
                boolean success = (boolean) in.readObject();
                if (success) {
                    //本地信息更新
                    homePanel.removeAll();
                    JPanel newHomePanel = createHomePanel(student.getProducts());
                    homePanel.add(newHomePanel, BorderLayout.CENTER);
                    homePanel.revalidate();
                    homePanel.repaint();

                    myPanel.removeAll();
                    JPanel newMyPanel = createMyPanel(student.getBelongs());
                    myPanel.add(newMyPanel, BorderLayout.CENTER);
                    myPanel.revalidate();
                    myPanel.repaint();

                    favoritesPanel.removeAll();
                    JPanel newfavoritesPanel = createFavoritesPanel(student.getFavorites());
                    favoritesPanel.add(newfavoritesPanel, BorderLayout.CENTER);
                    favoritesPanel.revalidate();
                    favoritesPanel.repaint();

                    JOptionPane.showMessageDialog(updateFrame, "商品更新成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                    updateFrame.dispose();
                    createProductFrame(updatedProduct);
                } else {
                    JOptionPane.showMessageDialog(addFrame, "添加失败！", "异常", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(updateFrame, "请填写有效值！", "错误", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(updateFrame, "更新错误！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> updateFrame.dispose());

        deleteButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(updateFrame, "确定要删除这个商品吗?", "删除确认", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try{
                    out.writeObject("5");
                    out.writeObject("deleteProduct");
                    out.writeObject(product);
                    out.writeObject(student);
                    out.flush();
                    student = (ShopStudent) in.readObject();
                    boolean success = (boolean) in.readObject();
                    if (success) {
                        //本地信息更新
                        homePanel.removeAll();
                        JPanel newHomePanel = createHomePanel(student.getProducts());
                        homePanel.add(newHomePanel, BorderLayout.CENTER);
                        homePanel.revalidate();
                        homePanel.repaint();

                        myPanel.removeAll();
                        JPanel newMyPanel = createMyPanel(student.getBelongs());
                        myPanel.add(newMyPanel, BorderLayout.CENTER);
                        myPanel.revalidate();
                        myPanel.repaint();

                        favoritesPanel.removeAll();
                        JPanel newfavoritesPanel = createFavoritesPanel(student.getFavorites());
                        favoritesPanel.add(newfavoritesPanel, BorderLayout.CENTER);
                        favoritesPanel.revalidate();
                        favoritesPanel.repaint();

                        JOptionPane.showMessageDialog(updateFrame, "商品删除成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                        updateFrame.dispose();
                        productFrame.dispose();
                    } else {
                        JOptionPane.showMessageDialog(addFrame, "删除失败！", "异常", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(updateFrame, "更新错误！", "错误", JOptionPane.ERROR_MESSAGE);
                }

                // Handle delete logic
                JOptionPane.showMessageDialog(updateFrame, "商品已删除！", "删除", JOptionPane.INFORMATION_MESSAGE);
                updateFrame.dispose();
            }
        });

        buttonPanel.add(originButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(deleteButton);

        updateFrame.add(formPanel, BorderLayout.CENTER);
        updateFrame.add(buttonPanel, BorderLayout.SOUTH);
        updateFrame.revalidate();
        updateFrame.repaint();
        updateFrame.setVisible(true);
    }

    private void clearFrameContent(JFrame frame) {
        // 获取内容面板并清空
        frame.getContentPane().removeAll();
        // 刷新JFrame以显示更改
        frame.revalidate();
        frame.repaint();
    }

    public void refreshShop() {
        try {
            out.writeObject("5");
            out.writeObject("refreshShop");
            out.writeObject(student);
            out.flush();
            student=(ShopStudent) in.readObject();
            homePanel.removeAll();
            // Recreate the homePanel with updated data
            JPanel newHomePanel = createHomePanel(student.getProducts());
            homePanel.add(newHomePanel, BorderLayout.CENTER);
            // Revalidate and repaint to reflect changes
            homePanel.revalidate();
            homePanel.repaint();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void refreshList(int situation) {
        switch (situation){
            case 1:
                favoritesPanel.removeAll();
                JPanel newfavoritesPanel = createFavoritesPanel(student.getFavorites());
                favoritesPanel.add(newfavoritesPanel, BorderLayout.CENTER);
                favoritesPanel.revalidate();
                favoritesPanel.repaint();
                break;
            case 2:
                myPanel.removeAll();
                JPanel newMyPanel = createMyPanel(student.getBelongs());
                myPanel.add(newMyPanel, BorderLayout.CENTER);
                myPanel.revalidate();
                myPanel.repaint();
                break;
        }
    }

    //true为从低到高，false为从高到低
    public void displayProductsSortedByPrice(boolean ascending,int which,List<Product> productList) {
        productList.sort(Comparator.comparingDouble(Product::getPrice));
        if (!ascending) {
            Collections.reverse(productList);
        }
        switch(which){
            case 1:
                homePanel.removeAll();
                JPanel newHomePanel = createHomePanel(productList);
                homePanel.add(newHomePanel, BorderLayout.CENTER);
                homePanel.revalidate();
                homePanel.repaint();
                break;
            case 2:
                createFavoritesPanel(productList);
                break;
            case 3:
                createMyPanel(productList);
        }
    }

    public void searchProducts(JTextField searchField) {
        try {
            out.writeObject("5");
            out.writeObject("searchProduct");
            out.writeObject(student);
            out.writeObject(searchField.getText());
            out.flush();
            student=(ShopStudent) in.readObject();
            boolean success = (boolean) in.readObject();
            if(success) {
                homePanel.removeAll();
                // Recreate the homePanel with updated data
                JPanel newHomePanel = createHomePanel(student.getProducts());
                homePanel.add(newHomePanel, BorderLayout.CENTER);
                // Revalidate and repaint to reflect changes
                homePanel.revalidate();
                homePanel.repaint();
            }else{
                failToSearch();
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void searchListProducts(JTextField searchField,int situation){
        // 获取搜索关键字
        String searchText = searchField.getText().trim().toLowerCase();

        // 创建一个新列表用于存放搜索结果
        List<Product> filteredList = new ArrayList<>();

        // 遍历产品列表并进行模糊搜索
        switch(situation) {
            case 1:
                for (Product product : student.getFavorites()) {
                    if (product.getName().toLowerCase().contains(searchText)) {
                        filteredList.add(product);
                    }
                }
                if (!filteredList.isEmpty()) {
                    favoritesPanel.removeAll();
                    JPanel newfavoritesPanel = createFavoritesPanel(filteredList);
                    favoritesPanel.add(newfavoritesPanel, BorderLayout.CENTER);
                    favoritesPanel.revalidate();
                    favoritesPanel.repaint();
                } else {
                    failToSearch();
                }
                break;
            case 2:
                for (Product product : student.getBelongs()) {
                    if (product.getName().toLowerCase().contains(searchText)) {
                        filteredList.add(product);
                    }
                }
                if (!filteredList.isEmpty()) {
                    myPanel.removeAll();
                    JPanel newMyPanel = createMyPanel(student.getBelongs());
                    myPanel.add(newMyPanel, BorderLayout.CENTER);
                    myPanel.revalidate();
                    myPanel.repaint();
                } else {
                    failToSearch();
                }
                break;

        }

    }

    public boolean isFavorites(String id){
        for(Product favorProduct:student.getFavorites()){
           if(Objects.equals(id, favorProduct.getId())){
               return true;
           }
        }
        return false;
    }

    public void changeFavorites(Product product,boolean is){
        try {
            out.writeObject("5");
            out.writeObject("changeFavorites");
            out.writeObject(product.getId());
            out.writeObject(student);
            out.writeObject(is);
            out.flush();
            student=(ShopStudent) in.readObject();
            createProductFrame(product);
            favoritesPanel.removeAll();
            // Recreate the homePanel with updated data
            JPanel newFavoritesPanel = createHomePanel(student.getFavorites());
            favoritesPanel.add(newFavoritesPanel, BorderLayout.CENTER);
            // Revalidate and repaint to reflect changes
            favoritesPanel.revalidate();
            favoritesPanel.repaint();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void purchaseProduct(Product product){
        // Create a new dialog for purchase
        JDialog purchaseDialog = new JDialog(productFrame, "购买", true);
        purchaseDialog.setSize(300, 300);
        purchaseDialog.setLocationRelativeTo(productFrame);
        purchaseDialog.setLayout(new GridBagLayout());

        GridBagConstraints dialogGbc = new GridBagConstraints();
        dialogGbc.fill = GridBagConstraints.BOTH;
        dialogGbc.insets = new Insets(10, 10, 10, 10);

        // Price label
        dialogGbc.gridx = 0;
        dialogGbc.gridy = 0;
        dialogGbc.weightx = 1.0;
        dialogGbc.weighty = 0.1;
        purchaseDialog.add(new JLabel("单价: ￥" + product.getPrice()), dialogGbc);

        // Discount label
        dialogGbc.gridx = 0;
        dialogGbc.gridy = 1;
        dialogGbc.weighty = 0.1;
        String discountText = (product.getDiscount() == 1) ? "暂无" : (product.getDiscount() * 100 + "%");
        purchaseDialog.add(new JLabel("折扣: " + discountText), dialogGbc);

        // Quantity input
        dialogGbc.gridx = 0;
        dialogGbc.gridy = 2;
        dialogGbc.weighty = 0.2;
        JTextField quantityField = new JTextField(5);
        purchaseDialog.add(new JLabel("数量: "), dialogGbc);
        dialogGbc.gridx = 1;
        purchaseDialog.add(quantityField, dialogGbc);

        // Total price label
        dialogGbc.gridx = 0;
        dialogGbc.gridy = 3;
        dialogGbc.weighty = 0.2;
        JTextField totalPriceField = new JTextField(5);
        totalPriceField.setEditable(false);
        purchaseDialog.add(new JLabel("总价: "), dialogGbc);
        dialogGbc.gridx = 1;
        purchaseDialog.add(totalPriceField, dialogGbc);

        // Password input
        dialogGbc.gridx = 0;
        dialogGbc.gridy = 4;
        dialogGbc.weighty = 0.2;
        JPasswordField passwordField = new JPasswordField(5);
        ((AbstractDocument) passwordField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if ((fb.getDocument().getLength() + string.length()) <= 6) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if ((fb.getDocument().getLength() - length + text.length()) <= 6) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
        purchaseDialog.add(new JLabel("支付密码: "), dialogGbc);
        dialogGbc.gridx = 1;
        purchaseDialog.add(passwordField, dialogGbc);

        // Confirm button
        dialogGbc.gridx = 0;
        dialogGbc.gridy = 5;
        dialogGbc.weighty = 0.3;
        JButton confirmButton = new JButton("确定");
        purchaseDialog.add(confirmButton, dialogGbc);



        // Add ActionListener for quantityField to update totalPriceField
        quantityField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                try {
                    int quantity = Integer.parseInt(quantityField.getText());
                    // Ensure quantity is within the limit
                    if (quantity > product.getNumbers()) {
                        quantityField.setText(String.valueOf(product.getNumbers()));
                        quantity = product.getNumbers();
                    }
                    float discount = product.getDiscount();
                    float price = product.getPrice();
                    float total = price * discount * quantity;
                    totalPriceField.setText("￥" + total);
                } catch (NumberFormatException ex) {
                    totalPriceField.setText("￥0.00");
                }
            }
        });

        // Add ActionListener for confirmButton
        confirmButton.addActionListener(ev -> {
            // 获取数量和密码
            String quantityText = quantityField.getText().trim();
            char[] passwordChars = passwordField.getPassword();
            String passwordText = new String(passwordChars).trim();

            // 验证数量和密码
            if (quantityText.isEmpty()) {
                JOptionPane.showMessageDialog(purchaseDialog,
                        "请输入数量！",
                        "错误",
                        JOptionPane.ERROR_MESSAGE);
                return; // 结束方法
            }

            if (passwordText.isEmpty()) {
                JOptionPane.showMessageDialog(purchaseDialog,
                        "请输入密码！",
                        "错误",
                        JOptionPane.ERROR_MESSAGE);
                return; // 结束方法
            }

            try {
                out.writeObject("5");
                out.writeObject("purchaseProduct");
                out.writeObject(product.getId());
                out.writeObject(Integer.parseInt(quantityField.getText()));
                String password = new String(passwordField.getPassword());
                out.writeObject(Integer.parseInt(password));
                out.writeObject(student);
                out.flush();
                student=(ShopStudent) in.readObject();
                int situation = (int) in.readObject();
                switch(situation){
                    case 0: // Purchase success
                        JOptionPane.showMessageDialog(purchaseDialog,
                                "购买成功！",
                                "成功",
                                JOptionPane.INFORMATION_MESSAGE);
                        purchaseDialog.dispose();
                        createInfoFrame(false);
                        break;
                    case 1: // Insufficient balance
                        JOptionPane.showMessageDialog(purchaseDialog,
                                "余额不足！",
                                "失败",
                                JOptionPane.ERROR_MESSAGE);
                        break;
                    case 2: // Password error
                        JOptionPane.showMessageDialog(purchaseDialog,
                                "密码错误！",
                                "失败",
                                JOptionPane.ERROR_MESSAGE);
                        break;
                    case 3: // Password error
                        JOptionPane.showMessageDialog(purchaseDialog,
                                "一卡通已冻结！",
                                "失败",
                                JOptionPane.ERROR_MESSAGE);
                        break;
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }


            //String password = new String(passwordField.getPassword());
            // Add logic to handle purchase with password
            //System.out.println("Purchase confirmed. Password: " + password);
            //purchaseDialog.dispose();
        });

        // Display the purchase dialog
        purchaseDialog.setVisible(true);
        purchaseDialog.setResizable(false);
    }

    public void failToSearch(){
        JFrame errorFrame = new JFrame("你要找什么？");
        errorFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        errorFrame.setSize(325, 475);
        errorFrame.setLocationRelativeTo(null);
        JLabel errorLabel = new JLabel("");
        ImageIcon failIcon = resizePicture("/imgs/shop/searchfail.png",320,450);
        errorLabel.setIcon(failIcon);
        errorFrame.add(errorLabel);

        // Play the WAV file
        String projectRoot = System.getProperty("user.dir");
        playWav(projectRoot+"/imgs/shop/searchfail.wav");

        errorFrame.setVisible(true);
        errorFrame.setResizable(false);
    }

    private void playWav(String filePath) {
        try {
            File audioFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            Clip audioClip = AudioSystem.getClip();
            audioClip.open(audioStream);
            audioClip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    // Load image from classpath
//        URL imageUrl = getClass().getResource(imagePath);
//
//        if (imageUrl == null) {
//            System.out.println("Image not found: " + imagePath);
//            return null;
//        }
//        BufferedImage originalImage = null;
//        try {
//            // Read the image from the file
//            originalImage = ImageIO.read(new File(imagePath));
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null; // Return null or handle error as needed
//        }

//        if (originalImage == null) {
//            return null; // Handle the case where the image couldn't be read
//        }
    //ImageIcon originalIcon = getImage(imagePath);
    //Image originalImage = originalIcon.getImage();
    //
//URL imageUrl = getClass().getResource(imagePath);
//
//        if (imageUrl == null) {
//        System.out.println("找不到图片: " + imagePath);
//        return null;
//    }
//

    public ImageIcon resizePicture(String imagePath,int targetWidth,int targetHeight){

        ImageIcon originalIcon = getImage(imagePath);
        Image originalImage = originalIcon.getImage();


        // Resize the image
        Image resizedImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        // Create a new ImageIcon from the resized image
        ImageIcon resizedIcon = new ImageIcon(resizedImage);
        return resizedIcon;
    }

    public ImageIcon getImage(String imagePath){
        Image img = Toolkit.getDefaultToolkit().getImage(getClass().getResource(imagePath));
        return new ImageIcon(img);
    }

    public String getRecordCount(String tablename){
        try {
            out.writeObject("5");
            out.writeObject("getRecordCount");
            out.writeObject(tablename);
            out.flush();
            return (String) in.readObject();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void navigateBack() {
        new NavigationPage().setVisible(true);
        dispose(); // 关闭当前页面
    }
}
