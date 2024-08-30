package vCampus.StuMS;

import vCampus.Dao.StuDao;
import vCampus.Entity.Student;

//import java.util.Date;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

public class StuMSServerSrv {
    private static final StuDao stuDao = new StuDao();

    /**
     * Searches for a student by card ID.
     *
     * @param cardid The card ID of the student to search for.
     * @return An Optional containing the found student, or an empty Optional if no
     *         student is found.
     */
    public static Optional<Student> searchStudent(String cardid) {
        return Optional.ofNullable(stuDao.find(cardid));
    }

    public static List<Student> searchAllStudents() {
        return stuDao.findAll();
    }

    /**
     * Adds a new student.
     *
     * @param student The student to add.
     * @return true if the student was added successfully, false otherwise.
     */
    public static boolean addStudent(Student student) {
        try {
            return stuDao.add(student);
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Modifies an existing student's information.
     *
     * @param student The student with updated information.
     * @return true if the student was updated successfully, false otherwise.
     */
    public static boolean modifyStudent(Student student) {
        try {
            return stuDao.update(student);
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes a student by card ID.
     *
     * @param cardid The card ID of the student to delete.
     * @return true if the student was deleted successfully, false otherwise.
     */
    public static boolean deleteStudent(String cardid) {
        try {
            return stuDao.delete(cardid);
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        @SuppressWarnings("deprecation")
        Date birthDate = new Date(99, 0, 1); // 1999年1月1日
        Student student = new Student(
                "123456789", // id
                "abc123", // cardId
                "Alice", // name
                "F", // gender
                birthDate, // birth
                "Engineering", // college
                "Sophomore", // grade
                "Computer Science", // major
                "alice@example.com", // email
                "123", // stage
                "Dean's List", // honor
                "None", // punish
                "A123456789123456789" // stuCode
        );
        System.out.println("deleteabc123:" + deleteStudent("abc123"));
        System.out.println("findall:" + searchAllStudents());
        System.out.println("findabc123:" + searchStudent("abc123"));
        System.out.println("addStudent:" + addStudent(student));
        System.out.println("findall:" + searchAllStudents());
        student.setCollege("123456");
        System.out.println("updateStudent:" + modifyStudent(student));
        System.out.println("findabc123:" + searchStudent("abc123"));

    }
}
