package vCampus.Entity;

import java.util.*;

// 商店类
public class Shop {
    private Map<String, Product> products = new HashMap<>();
    private ShopStudent student;

    public Shop(ShopStudent student) {
        this.student = student;
    }

    public void addProduct(Product product) {
        String id = product.getId();
        products.put(id, product);
    }

    public void addNew(){
        int size = products.size();
        Product newProduct = student.addProduct(size);
        String id = newProduct.getId();
        products.put(id, newProduct);
    }

    public void viewProducts() {
        System.out.println("商店商品列表：");
        int shopSize = products.size();

        //随机数组
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
            if (student.buyProduct(product,buyNums)) {
                int nums = product.getNumbers();
                product.setNumbers(nums-buyNums);
                // 购买之后余额打给卖家
            }
        } else {
            System.out.println("商品不存在！");
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
        if(student.isMine(updateId)) {
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
            scanner.nextLine(); // 处理换行符
            student.updateProduct(product, newName, newNums, newPrice, newDiscount);
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
        if(student.isMine(id)) {
            student.deleteProduct(id);
            products.remove(id);
            System.out.println("删除成功！");
        } else {
            System.out.println("该商品不属于您！");
        }
    }

    public void addFavorite(String productId) {
        Product product = products.get(productId);
        if (product != null) {
            student.addFavorite(product);
        } else {
            System.out.println("商品不存在！");
        }
    }

    public void viewFavorite(){
        student.viewFavorite();
    }

    public void viewBelongs(){
        student.viewBelongs();
    }

    public void viewBill(){
        student.viewBill();
    }

    public void viewBalance(){
        float remain = student.getRemain();
        System.out.println("余额："+remain);
    }
}
