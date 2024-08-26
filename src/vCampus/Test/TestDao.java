package vCampus.Test;

import vCampus.Dao.UserDao;
import vCampus.Entity.User;

public class TestDao {
    public static void main(String[] args) {
        UserDao userDao = new UserDao();

        // Create a User object
        User user = new User("user123", "password", 25, true, "ST", "user123@example.com", "123456789");

        // Test add method
        boolean isAdded = userDao.add(user);
        System.out.println("User added: " + isAdded);

        // Test find method
        User foundUser = userDao.find("user123");
        System.out.println("User found: " + foundUser);

        // Update user details
        if (foundUser != null) {
            foundUser.setEmail("newemail@example.com");
            boolean isUpdated = userDao.update(foundUser);
            System.out.println("User updated: " + isUpdated);

            // Test find method again to see the updated details
            User updatedUser = userDao.find("user123");
            System.out.println("Updated user: " + updatedUser);
        }

        // Test delete method
        boolean isDeleted = userDao.delete("user123");
        System.out.println("User deleted: " + isDeleted);

        // Test find method after deletion
        User deletedUser = userDao.find("user123");
        System.out.println("User after deletion: " + deletedUser);
    }
}
