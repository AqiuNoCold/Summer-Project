package vCampus.Test;

import vCampus.Dao.ShopStudentDao;
import vCampus.Entity.ShopStudent;
import vCampus.Entity.Product;

public class TestDao {
    public static void main(String[] args) {
        // Create a ShopStudentDao instance
        ShopStudentDao shopStudentDao = new ShopStudentDao();

        // Create some Product instances
        Product product1 = new Product("p1", "Product1", 10.0F, 5, "shop123");
        Product product2 = new Product("p2", "Product2", 20.0F, 10, "shop123");

        // Create a ShopStudent object with initial data
        ShopStudent shopStudent = new ShopStudent(
                "user123", "password", 25, true, "ST", "user123@example.com", "123456789",
                1.23F, 122, false
        );
        shopStudent.getFavorites().add(product1);
        shopStudent.getFavorites().add(product2);
        shopStudent.getBelongs().add(product2);
        shopStudent.getBill().add(product1);

        // Test add method
        boolean isAdded = shopStudentDao.add(shopStudent);
        System.out.println("ShopStudent added: " + isAdded);

        // Test find method
        ShopStudentDao.ShopStudentData foundData = shopStudentDao.find("user123");
        System.out.println("ShopStudent found: " + foundData);

        // Update ShopStudent details
        if (foundData != null) {
            // Creating a new ShopStudent object with updated details
            ShopStudent updatedShopStudent = new ShopStudent(
                    "user123", "newpassword", 26, false, "TC", "newuser@example.com", "987654321",
                    2.34F, 321, true
            );
            updatedShopStudent.getFavorites().add(product2);
            updatedShopStudent.getBelongs().add(product1);
            updatedShopStudent.getBill().add(product2);
            boolean isUpdated = shopStudentDao.update(updatedShopStudent);
            System.out.println("ShopStudent updated: " + isUpdated);

            // Test find method again to see the updated details
            ShopStudentDao.ShopStudentData updatedData = shopStudentDao.find("user123");
            System.out.println("Updated ShopStudent: " + updatedData);
        }

        // Test delete method
        boolean isDeleted = shopStudentDao.delete("user123");
        System.out.println("ShopStudent deleted: " + isDeleted);

        // Test find method after deletion
        ShopStudentDao.ShopStudentData deletedData = shopStudentDao.find("user123");
        System.out.println("ShopStudent after deletion: " + deletedData);
    }
}
