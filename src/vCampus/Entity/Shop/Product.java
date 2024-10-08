package vCampus.Entity.Shop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.Serializable;

// 商品类
public class Product implements Serializable  {
    private String id;
    private String name;
    private float price;
    private int numbers;
    private String owner;
    private float discount;
    private Date time;
    private byte[] image;
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
        this.image = null;
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
    public byte[] getImage(){
        return image;
    }
    public void getPicture(){
        if (image == null) {
            System.out.println("Image data is null");
            return;
        }
        try (FileOutputStream fileOutputStream = new FileOutputStream(new File("src/vCampus/Shop/Img/"+this.id+".png"));) {
            fileOutputStream.write(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    public void setNewImage(String imagePath){

        try (FileInputStream fileInputStream = new FileInputStream(new File(imagePath));) {
            image = new byte[fileInputStream.available()];
            fileInputStream.read(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setImage(Blob blob) throws SQLException {
        image = blob.getBytes(1, (int) blob.length());
    }

    public void setImage2(byte[] compressedImage) throws SQLException {
        image = compressedImage;
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String datetime = dateFormat.format(time);
        return "ID: " + id + ", Name: " + name + ", Price: " + price*discount + ", Numbers: "+numbers+", Date: "+ datetime+", owner: "+owner;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id); // Compare based on unique id
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); // Hash based on unique id
    }
}