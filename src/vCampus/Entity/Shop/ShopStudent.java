package vCampus.Entity.Shop;


import java.text.SimpleDateFormat;
import java.util.*;
import vCampus.Dao.*;
import vCampus.Entity.ECard.*;
import vCampus.Entity.Shop.*;
import vCampus.Entity.*;





public class ShopStudent extends ECard {

    private List<Product> favorites;
    private List<Product> belongs;
    private List<String> bill;
    public Map<String, Product> products = new HashMap<>();

    public ShopStudent(User user){
        super(user);
        //初始化商店用户
        ShopStudentDao dao = new ShopStudentDao();
        ShopStudentDao.ShopStudentData data = dao.find(id);
        turnFavorites(data.getFavorites());
        turnBelongs(data.getBelongs());
        turnBill(data.getBill());
    }

    public List<Product> getFavorites() {
        return favorites;
    }
    public List<Product> getBelongs(){
        return belongs;
    }
    public List<String> getBill() {
        return bill;
    }

    /*
    用于数据库操作，存放productid
     */
    public String getFavoritesId() {
        List<String> favoritesId = new ArrayList<>();
        for (Product p : favorites) {
            favoritesId.add(p.getId());
        }
        return String.join(",", favoritesId);
    }

    public String getBelongsId() {
        List<String> belongsId = new ArrayList<>();
        for (Product p : belongs) {
            belongsId.add(p.getId());
        }
        return String.join(",", belongsId);
    }

    public String getBillId() {
        StringBuilder Bill= new StringBuilder();
        for (String p : bill) {
            Bill.append(p);
        }
        return String.join(";", Bill.toString());
    }

    public void turnFavorites(String favoritesId){
        String[] productIds = favoritesId.split(",");
        ProductDao dao = new ProductDao();
        for(String id : productIds) {
            Product product = dao.find(id);
            favorites.add(product);
        }
    }

    public void turnBelongs(String belongsId){
        String[] productIds = belongsId.split(",");
        ProductDao dao = new ProductDao();
        for(String id : productIds) {
            Product product = dao.find(id);
            belongs.add(product);
        }
    }

    public void turnBill(String billId){
        String[] billIds = billId.split(";");
        bill.addAll(Arrays.asList(billIds));
    }

    public void setFavorites(List<Product> favorites){
        this.favorites = favorites;
    }

    public void setBelongss(List<Product> belongs){
        this.belongs = belongs;
    }

    public void setBill(List<String> bill){
        this.bill = bill;
    }


    @Override
    public String toString() {
        return "Student{name='" + card + "', balance=" + remain + '}';
    }
}
