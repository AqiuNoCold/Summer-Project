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
        int id = product.getId();
        String Id= id+"";
        products.put(Id, product);
    }

    public void addNew(){
        int size = products.size();
        Product newProduct = student.addProduct(size);
        int id = newProduct.getId();
        String Id= id+"";
        products.put(Id, newProduct);
    }

    public void viewProducts() {
        System.out.println("商店商品列表：");
        for (Product product : products.values()) {
            System.out.println(product);
        }
    }

    public void purchaseProduct(String productId,int buyNums) {
        Product product = products.get(productId);
        if (product != null) {
            if (student.buyProduct(product,buyNums)) {
                int nums = product.getNumbers();
                product.setNumbers(nums-buyNums);
                // 可以在这里进一步处理购买后的逻辑,账单
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
        int ID = Integer.parseInt(updateId);
        if(student.isMine(ID)) {
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
        int id = scanner.nextInt();
        if(student.isMine(id)) {
            student.deleteProduct(id);
            String ID = id+"";
            products.remove(ID);
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
