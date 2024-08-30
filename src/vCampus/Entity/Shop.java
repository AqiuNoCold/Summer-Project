package vCampus.Entity;

import vCampus.Dao.*;
import vCampus.Entity.ECard.ECard;
import vCampus.Entity.ECard.ECardDTO;

import java.text.SimpleDateFormat;
import java.util.*;

// 商店类
public class Shop {
    private Map<String, Product> products = new HashMap<>();
    private ShopStudent student;

    public Shop(ShopStudent student) {
        this.student = student;
        initialShop(8);
        //初始化展示
        this.viewProducts();
    }

    public float countPrice(Product product,int nums){
        //计算总价格,1为单价
        return product.getPrice()*product.getDiscount()*nums;
    }

    public boolean comparePrice(Product product1,Product product2){
        //左比较右，大者为true
        if((countPrice(product1,1))>=(countPrice(product2,1)))
            return true;
        else
            return false;
    }

    public boolean compareTime(Product product1,Product product2){
        //左比较右，新的为true
        if(product1.getTime().getTime() >=product2.getTime().getTime())
            return true;
        else
            return false;
    }

    public void addProduct(Product product) {
        //给商店添加已有商品
        String id = product.getId();
        products.put(id, product);
    }

    public void initialShop(int nums){
        ProductDao productDao = new ProductDao();
        int totalNums = productDao.getRecordCount("tblproduct");

        //添加随机商品与收藏夹商品与所属商品
        Set<String> shopSet = new HashSet<String>();
        while((shopSet.size()) < nums && (shopSet.size() < totalNums))
        {
            Random rand = new Random();
            int random = rand.nextInt(totalNums);
            String id = random+"";
            shopSet.add(id);
        }

        for(Product product : student.getFavorites()){
            shopSet.add(product.getId());
        }

        for(Product product : student.getBelongs()){
            shopSet.add(product.getId());
        }

        for (String Element : shopSet) {
            Product randomProduct = productDao.find(Element);
            products.put(Element,randomProduct);
        }

    }

    public void addNew(){
        int size = products.size();
        ProductDao productDao = new ProductDao();

        Scanner scanner = new Scanner(System.in);
        String newId = String.valueOf(productDao.getRecordCount("tblproduct")+1);
        System.out.print("请输入新的商品名称: ");
        String newName = scanner.nextLine();
        System.out.print("请输入新的商品数量: ");
        int  newNums = scanner.nextInt();
        System.out.print("请输入新的商品价格: ");
        float newPrice = scanner.nextFloat();
        float newDiscount;
        while(true) {
            System.out.print("请输入新的商品折扣(0~1): ");
            newDiscount = scanner.nextFloat();
            if((newDiscount>=0) && (newDiscount<=1))
                break;
        }
        scanner.nextLine();
        String owner = student.card;
        Product newProduct = new Product(newId,newName,newPrice,newNums,owner);
        newProduct.setDiscount(newDiscount);
        student.getBelongs().add(newProduct);
        System.out.println("新商品添加成功！");
        String id = newProduct.getId();
        products.put(id, newProduct);

        //更新数据库
        productDao.update(newProduct);
        ShopStudentDao shopStudentDao = new ShopStudentDao();
        ShopStudentDao.ShopStudentData data = shopStudentDao.find(student.id);
    }

    public void viewProducts() {
        System.out.println("商店商品列表：");
        int shopSize = products.size();

        //随机数组展示随机商品
        Set<String> shopSet = new HashSet<String>();
        while((shopSet.size()) < 8 && (shopSet.size()<shopSize))
        {
            String[] keys = products.keySet().toArray(new String[0]);
            Random random = new Random();
            String randomKey = keys[random.nextInt(keys.length)];
            shopSet.add(randomKey);
        }

        for (String Element : shopSet) {
            Product randomProduct = products.get(Element);
            System.out.println(randomProduct);
        }
    }

    public void viewPrice(Shop searchShop){
        System.out.println("按价格排列：");
        System.out.println("1.价格从大到小");
        System.out.println("2.价格从小到大");
        Scanner scanner = new Scanner(System.in);
        System.out.println("请选择：");
        int choice = scanner.nextInt();
        for (Product product:searchShop.products.values()) {
            System.out.println(product);
        }
    }

    public void searchProduct(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入你要查找的商品：");
        String searchName = scanner.nextLine();


    }

    public void purchaseProduct(String productId,int buyNums) {
        Product product = products.get(productId);
        if (product != null) {
            if (buyProduct(product,buyNums)) {
                int nums = product.getNumbers();
                product.setNumbers(nums-buyNums);
                // 数据库更新
                ProductDao productDao = new ProductDao();
                productDao.update(product);
            }
        } else {
            System.out.println("商品不存在！");
        }
    }

    public boolean buyProduct(Product product,int nums) {
        Scanner scanner = new Scanner(System.in);

        if( isMine(product.getId()) ){
            System.out.println("请不要自卖自销！");
            return false;
        }
        if(product.getNumbers()< nums){
            System.out.println("商品数量不足！");
            return false;
        }
        if (countPrice(product,nums) > student.getRemain()) {
            System.out.println("余额不足！");
            return false;
        }
        int times = 0;
        while(true) {
            System.out.print("请输入支付密码: ");
            int input = scanner.nextInt();
            if(input == student.getPassword()) {
                float cost = countPrice(product,nums);
                float newRemain = student.getRemain()-cost;
                student.setRemain(newRemain);;

                //更新商品数据库
                product.setNumbers(product.getNumbers()-nums);
                ProductDao productDao = new ProductDao();
                productDao.update(product);

                System.out.println("购买成功！");

                //信息整理
                Date datetime = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String formatTime = dateFormat.format(datetime);
                String ID = String.valueOf(student.getBill().size()+1);

                //添加到商店账单
                String hisProduct = "历史ID：" + ID+" 商品名称： "+product.getName()+" 商品主人： "+product.getOwner()+" 购买时间："+formatTime+
                        " 商品数量 "+ nums+" 商品单价："+product.getPrice()+" 商品折扣："+product.getDiscount()+
                        " 总价格："+cost;
                student.getBill().add(hisProduct);
                //添加到一卡通账单
                TransactionDao transactionDao = new TransactionDao();
                //扣费与上传
                String transaction = transactionDao.find(student.card);
                String transactionBuy = transaction+formatTime+",-"+cost+",购买商品;";
                transactionDao.update(transactionBuy,student.card);
                //获得money方
                //获得seller
                UserDao userDao = new UserDao();
                User uSeller = userDao.find(product.getOwner());
                ECardDao eCardDao = new ECardDao();
                ECard eSeller = new ECard(uSeller);
                //修改与上传
                newRemain = eSeller.getRemain()+cost;
                eCardDao.updateRemain(newRemain,eSeller.getCard());
                transaction = transactionDao.find(product.getOwner());
                String transactionSell = transaction + formatTime+",+"+cost+",出售商品;";
                transactionDao.update(transactionSell,student.card);
                return true;
            }
            times++;
            if(times<3)
                System.out.println("密码错误！剩余"+(3-times)+"次");
            else {
                System.out.println("多次错误，购买失败！");
                return false;
            }
        }
    }

    public boolean updateProduct() {
        Scanner scanner = new Scanner(System.in);
        viewBelongs();
        System.out.print("请输入商品ID进行更新: ");
        String updateId = scanner.nextLine();
        Product product = products.get(updateId);
        if (product == null) {
            System.out.println("商品不存在！");
            return false;
        }
        if(isMine(updateId)) {
            System.out.print("请输入新的商品名称: ");
            String newName = scanner.nextLine();
            System.out.print("请输入新的商品数量: ");
            int newNums = scanner.nextInt();
            System.out.print("请输入新的商品价格: ");
            float newPrice = scanner.nextFloat();
            float newDiscount;
            while(true) {
                System.out.print("请输入新的商品折扣(0~1): ");
                newDiscount = scanner.nextFloat();
                if((newDiscount>=0) && (newDiscount<=1))
                    break;
            }
            scanner.nextLine(); // 处理换行符

            product.setName(newName);
            product.setNumbers(newNums);
            product.setPrice(newPrice);
            product.setDiscount(newDiscount);
            //更新数据库
            ProductDao productDao = new ProductDao();
            productDao.update(product);

            System.out.println("商品信息更新成功！");
            return true;
        } else {
            System.out.println("该商品不属于您！");
            return false;
        }
    }

    public void deleteProduct() {
        Scanner scanner = new Scanner(System.in);
        viewBelongs();
        System.out.println("请输入你要删除的商品ID： ");
        String id = scanner.nextLine();
        if(isMine(id)) {
            student.getBelongs().remove(id);
            //更新数据库
            ProductDao productDao = new ProductDao();
            Product product = products.get(id);
            product.setName("该商品已失效");
            product.setNumbers(0);
            product.setDiscount(1);
            product.setPrice(0);
            productDao.update(product);
            System.out.println("删除成功！");
        } else {
            System.out.println("该商品不属于您！");
        }
    }

    public void addFavorite(String productId) {
        Product product = products.get(productId);
        if (product != null) {
            student.getFavorites().add(product);
            System.out.println("已收藏商品: " + product.getName());
        } else {
            System.out.println("商品不存在！");
        }
    }

    public void deleteFavorite(String productId){
        Product product = products.get(productId);
        if (product != null) {
            student.getFavorites().remove(product);
            System.out.println("已取消收藏商品: " + product.getName());
        } else {
            System.out.println("商品不存在！");
        }
    }

    public void viewFavorite(){
        System.out.println("收藏商品列表：");
        for (Product product : student.getFavorites()) {
            System.out.println(product);
        }
    }

    public void viewBelongs(){
        System.out.println("所属商品列表：");
        for (Product product : student.getBelongs()) {
            System.out.println(product);
        }
    }

    public void viewBill(){
        System.out.println("账单：");
        for(String his : student.getBill()){
            System.out.println(his);
        }
    }

    public void viewBalance(){
        float remain = student.getRemain();
        System.out.println("余额："+remain);
    }

    public boolean isMine(String id){
        for (Product product : student.getBelongs()) {
            if(Objects.equals(id, product.getId()))
                return true;
        }
        return false;
    }

    public void close(){
        ShopStudentDao dao = new ShopStudentDao();
        dao.update(student);
    }

}
