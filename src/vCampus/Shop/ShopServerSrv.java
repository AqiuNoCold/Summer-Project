package vCampus.Shop;

import vCampus.Dao.*;
import vCampus.ECard.ECardServerSrv;
import vCampus.Entity.ECard.ECard;
import vCampus.Entity.Shop.*;
import vCampus.Entity.*;

import java.text.SimpleDateFormat;
import java.util.*;
import Pages.Utils.ImageUtils;


// 商店类
public class ShopServerSrv {
    public static ShopStudent initialShopStudent(User user){
        return new ShopStudent(user);
    }

    public static float countPrice(Product product,int nums){
        //计算总价格,1为单价
        return product.getPrice()*product.getDiscount()*nums;
    }

    public static void addProduct(Product product,ShopStudent student) {
        //给商店添加已有商品
        String id = product.getId();
        student.products.put(id, product);
    }

    public static void initialShop(int nums,ShopStudent student){
        ProductDao productDao = new ProductDao();
        int totalNums = productDao.getRecordCount("tblproduct");
        student.products.clear();

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
            if(product != null)
                shopSet.add(product.getId());
        }

        for(Product product : student.getBelongs()){
            if(product != null)
                shopSet.add(product.getId());
        }

        for (String Element : shopSet) {
            Product randomProduct = productDao.find(Element);
            randomProduct.getPicture();
            student.products.put(Element,randomProduct);
        }

    }

    public static boolean addNew(ShopStudent student,Product newProduct){
        ProductDao productDao = new ProductDao();
        //更新数据库
        productDao.add(newProduct);
        student.getBelongs().add(newProduct);
        student.products.put(newProduct.getId(), newProduct);
        ShopStudentDao shopStudentDao = new ShopStudentDao();
        shopStudentDao.update(student);
        //校验
        Product checkProduct = productDao.find(newProduct.getId());
        return checkProduct != null;
    }

    public static String getRecordCount(String tablename){
        ProductDao productDao = new ProductDao();
        return String.valueOf(productDao.getRecordCount("tblproduct"));
    }

    public static void viewProducts(ShopStudent student) {
        System.out.println("商店商品列表：");
        int shopSize = student.products.size();

        //随机数组展示随机商品
        Set<String> shopSet = new HashSet<String>();
        while((shopSet.size()) < 8 && (shopSet.size()<shopSize))
        {
            String[] keys = student.products.keySet().toArray(new String[0]);
            Random random = new Random();
            String randomKey = keys[random.nextInt(keys.length)];
            shopSet.add(randomKey);
        }

        for (String Element : shopSet) {
            Product randomProduct = student.products.get(Element);
            System.out.println(randomProduct);
        }
    }

    public static boolean searchProduct(ShopStudent student,String searchName){
        Scanner scanner = new Scanner(System.in);
        ProductDao productDao =new ProductDao();
        String ids = productDao.searchProduct(searchName);

        if(!Objects.equals(ids, "")) {
            student.products.clear();
            String[] productIds = ids.split(",");
            for (String id : productIds) {
                Product product = productDao.find(id);
                addProduct(product, student);
            }
            return true;
        }else{
            //System.out.println("未查找到需要的商品");
            return false;
        }
    }

    public static int purchaseProduct(String productId,int buyNums,int password,ShopStudent student) {
        Product product = student.products.get(productId);
        return buyProduct(product,buyNums,password,student);
    }

    public static int buyProduct(Product product,int nums,int password,ShopStudent student) {
        if(student.getLost())
            return 3;
        if (countPrice(product, nums) > student.getRemain()) {
            System.out.println("余额不足！");
            return 1;
        }
        if (password == student.getPassword()) {
            float cost = countPrice(product, nums);
            float newRemain = student.getRemain() - cost;
            student.setRemain(newRemain);
            //更新商品数据库
            product.setNumbers(product.getNumbers() - nums);
            ProductDao productDao = new ProductDao();
            productDao.update(product);

            //System.out.println("购买成功！");

            //信息整理
            Date datetime = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String formatTime = dateFormat.format(datetime);
            String ID = String.valueOf(student.getBill().size()+1);


            //添加到商店账单
            ShopStudentDao shopStudentDao = new ShopStudentDao();
            String hisProduct = "历史ID：" + ID + " 商品名称： " + product.getName() + " 商品主人： " + product.getOwner() + " 购买时间：" + formatTime +
                    " 商品数量: " + nums + " 商品单价：" + product.getPrice() + " 商品折扣：" + product.getDiscount() +
                    " 总价格：" + cost+" ;";
            student.getBill().add(hisProduct);
            shopStudentDao.update(student);

            //添加到一卡通账单(服务端使用addTransaction E)
            TransactionDao transactionDao = new TransactionDao();
            //扣费与上传
            UserDao userDao = new UserDao();
            ECardDao eCardDao = new ECardDao();

            ECard eBuyer = new ECard(student);
            eCardDao.updateRemain(newRemain, eBuyer.getCard());
            ECardServerSrv.addTransaction(student.getCard(),-cost,"购买商品");

            //获得money方
            //获得seller
            User uSeller = userDao.find(product.getOwner());
            ECard eSeller = new ECard(uSeller);
            //修改与上传
            newRemain = eSeller.getRemain() + cost;
            eCardDao.updateRemain(newRemain, eSeller.getCard());
            ECardServerSrv.addTransaction(eSeller.getCard(),cost,"出售商品");
            return 0;
        }
        else{
            return 2;
        }
    }

    public static boolean updateProduct(ShopStudent student,Product updateProduct) {
            //更新数据库
            ProductDao productDao = new ProductDao();
            productDao.update(updateProduct);
            String updateId = updateProduct.getId();
        if (student.products.containsKey(updateId)) {
            student.products.put(updateId, updateProduct);
            return true;
        } else {
            // 产品 ID 不存在于学生的产品列表中
            return false;
        }
    }

    public static boolean deleteProduct(ShopStudent student,Product deleteProduct) {
        String id = deleteProduct.getId();
        //更新数据库
        if (student.products.containsKey(id)) {
            ProductDao productDao = new ProductDao();
            Product product = student.products.get(id);
            product.setName("该商品已失效");
            product.setNumbers(0);
            product.setDiscount(1);
            product.setPrice(0);
            String projectRoot = System.getProperty("user.dir");
            product.setNewImage(projectRoot+"/vCampus/Shop/img/noimage.png");

            int index = 0;
            for (Product productB : student.getBelongs()) {
                String deleteId = productB.getId();
                if (Objects.equals(deleteId, id)) {
                    student.getBelongs().remove(index);
                    break;
                }
                index++;
            }
            index = 0;
            for (Product productF : student.getFavorites()) {
                String deleteId = productF.getId();
                if (Objects.equals(deleteId, id)) {
                    student.getFavorites().remove(index);
                    break;
                }
                index++;
            }

            student.products.remove(id);
            productDao.update(product);

            System.out.println("删除成功！");
            ShopStudentDao dao = new ShopStudentDao();
            dao.update(student);
            return true;
        } else {
            // 产品 ID 不存在于学生的产品列表中
            return false;
        }
    }

    public static void changeFavorites(String productId,ShopStudent student,boolean is){
        Product product = student.products.get(productId);
        ShopStudentDao shopStudentDao = new ShopStudentDao();
        if (is) {
            student.getFavorites().remove(product);
            shopStudentDao.update(student);
            //System.out.println("已收藏商品: " + product.getName());
        } else {
            student.getFavorites().add(product);
            shopStudentDao.update(student);
        }
    }

    public static boolean isMine(String id,ShopStudent student){
        for (Product product : student.getBelongs()) {
            if(Objects.equals(id, product.getId()))
                return true;
        }
        return false;
    }

    public static void close(ShopStudent student){
        ShopStudentDao dao = new ShopStudentDao();
        dao.update(student);
    }

}
