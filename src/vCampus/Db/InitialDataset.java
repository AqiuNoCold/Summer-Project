package vCampus.Db;

import vCampus.Dao.ProductDao;
import vCampus.Entity.Shop.Product;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class InitialDataset {
    public static void main(String[] args) {
        DbCreator.createTables();
        String sqlFilePath = "src/vCampus/Db/sql/All_tbl.sql";
        // 连接数据库
        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            ProductDao productDao = new ProductDao();
            for(int i =0;i<=12;i++) {
                Product newProduct = new Product(i+"",i+"",1.0f,20,"987654321");
                newProduct.setDiscount(1);
                newProduct.setNewImage("src/vCampus/Shop/img/"+i+".png");
                productDao.add(newProduct);
            }

            // 读取 SQL 文件
            StringBuilder sql = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(sqlFilePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    sql.append(line);
                    // 每一条 SQL 语句应该以分号结束
                    if (line.trim().endsWith(";")) {
                        stmt.execute(sql.toString());
                        sql.setLength(0); // 清空 StringBuilder 以执行下一条语句
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("SQL 文件已成功执行!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

