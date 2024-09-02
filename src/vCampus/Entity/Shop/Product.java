package vCampus.Entity.Shop;

import java.text.SimpleDateFormat;
import java.util.*;

// 商品类
public class Product {
    private String id;
    private String name;
    private float price;
    private int numbers;
    private String owner;
    private float discount;
    private Date time;
    /*
    可选功能：
    private int into;
    into表示被收藏次数，如果name == ‘NULL’并且into == 0，那么这个id就可以释放。
    */
    public Product(String id, String name, float price,int numbers,String owner) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.numbers = numbers;
        this.owner = owner;
        this.time = new Date();
        this.discount = 1;
    }

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public float getPrice() {
        return price;
    }
    public int getNumbers() {
        return numbers;
    }
    public String getOwner() {
        return owner;
    }
    public Date getTime() {
        return time;
    }
    public float getDiscount(){
        return discount;
    }

    public void setId(String id){
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setPrice(float price) {
        this.price = price;
    }
    public void setNumbers(int numbers) {
        this.numbers = numbers;
    }
    public void setTime(Date time){
        this.time = time;
    }
    public void upgradeDate(){
        Date date = new Date();
        this.time = date;
    }
    public void setDiscount(float discount){
        this.discount = discount;
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String datetime = dateFormat.format(time);
        return "ID: " + id + ", Name: " + name + ", Price: " + price*discount + ", Numbers: "+numbers+", Date: "+ datetime+", owner: "+owner;
    }
}