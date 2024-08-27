package vCampus.Entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class ShopStudent extends User {
    //private String card;

    private List<Product> favorites = new ArrayList<>();
    private List<Product> belongs = new ArrayList<>();
    private List<Product> bill = new ArrayList<>();

    //private float remain;
    //private boolean lost;
    //private String password;

    public ShopStudent(String id, String pwd, Integer age, Boolean gender, String role, String email, String card,Float remain,Integer password,Boolean lost){
        super(id, pwd, age, gender, role, email, card, remain, password, lost);
        /*
        this.remain = balance;
        this.lost = false;
        this.password = "123456";
         */
    }

    public List<Product> getFavorites() {
        return favorites;
    }
    public List<Product> getBelongs(){
        return belongs;
    }
    public List<Product> getBill() {
        return bill;
    }
    //public boolean getLost(){return lost;}

    public boolean buyProduct(Product product,int nums) {
        Scanner scanner = new Scanner(System.in);

        if(product.getNumbers()< nums){
            System.out.println("商品数量不足！");
            return false;
        }
        if (product.countPrice()*nums > remain) {
            System.out.println("余额不足！");
            return false;
        }
        int times = 0;
        while(true) {
            System.out.print("请输入支付密码: ");
            int input = scanner.nextInt();
            if(input == password) {
                remain -= product.getPrice()*nums;
                System.out.println("购买成功！");
                Date datetime = new Date();
                int ID = bill.size()+1;
                String strId = ID+"";
                float cost = product.countPrice();
                Product hisProduct = new Product(strId, product.getName(), product.getPrice(),nums, product.getOwner());
                bill.add(hisProduct);
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

    public Product addProduct(int size){
        Scanner scanner = new Scanner(System.in);

        //System.out.print("请输入商品ID: ");
        String newId = size+1+"";
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
        String owner = card;
        Product newProduct = new Product(newId,newName,newPrice,newNums,owner);
        newProduct.setDiscount(newDiscount);
        belongs.add(newProduct);
        System.out.println("新商品添加成功！");

        return newProduct;
    }

    public void updateProduct(Product product, String name, int nums,float price,float discount) {
        product.setName(name);
        product.setNumbers(nums);
        product.setPrice(price);
        product.setDiscount(discount);
        System.out.println("商品信息更新成功！");
    }

    public void deleteProduct(String id){
        int Index = 0;
        for (Product product : belongs) {
            if (id.equals(product.getId())) {
                belongs.remove(Index);
                break;
            }
            Index++;

        }
    }

    public void addFavorite(Product product) {
        favorites.add(product);
        System.out.println("已收藏商品: " + product.getName());
    }

    public void viewFavorite(){
        System.out.println("收藏商品列表：");
        for (Product product : favorites) {
            System.out.println(product);
        }
    }

    public void viewBelongs(){
        System.out.println("所属商品列表：");
        for (Product product : belongs) {
            System.out.println(product);
        }
    }

    public void viewBill(){
        System.out.println("收藏商品列表：");
        for (Product his : bill) {
            System.out.println(his+", 折扣: "+his.getDiscount()+", 总花费:"+his.countPrice()* his.getNumbers());
        }
    }

    public boolean isMine(String id){
        for (Product product : belongs) {
            if(id.equals(product.getId()))
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Student{name='" + card + "', balance=" + remain + '}';
    }
}
