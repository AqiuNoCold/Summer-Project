package vCampus.StuMS;

import vCampus.Dao.StuDao;
import vCampus.Dao.GradeDao;
import vCampus.Entity.Student;
import vCampus.Entity.Grade;


//import java.util.Date;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

public class StuMSServerSrv {
    private static final StuDao stuDao = new StuDao();
    private static final GradeDao gradeDao = new GradeDao();
    /**
     * Searches for a student by card ID.
     *
     * @param cardid The card ID of the student to search for.
     * @return An Optional containing the found student, or an empty Optional if no student is found.
     */
    public static Student studentFindInfo(String cardid) {
        return stuDao.find(cardid);
    }
    public static List<Grade> studentFindGrade(String cardid){
        return gradeDao.findAllByCardId(cardid);
    }



    public static List<Student> teacherFindAllInfo(){
        return stuDao.findAll();
    }

    public static boolean teacherAddInfo(Student student) {
        try {
            return stuDao.add(student);
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            return false;
        }
    }

    public static boolean teacherModifyInfo(Student student) {
        try {
            return stuDao.update(student);
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            return false;
        }
    }

    public static boolean teacherDeleteInfo(String cardid) {
        try {
            return stuDao.delete(cardid);
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            return false;
        }
    }

    public static boolean teacherModifyGrade(Grade grade){
        return gradeDao.setGrade(grade);
    }

    public static boolean teacherDeleteGrade(String cardid, String courseId,boolean isFirst){
        return gradeDao.deleteGrade(cardid, courseId,isFirst);
    }

    public static List<Grade> teacherFindAllGrade(){
        return gradeDao.findAll();
    }




//    public static boolean addGrade(Grade grade){
//        if(gradeDao.find(grade.getId())==null) {
//            return gradeDao.add(grade);
//        }
//        try {
//            return gradeDao.update(grade);
//        } catch (Exception e) {
//            // Log the exception
//            e.printStackTrace();
//            return false;
//        }
//    }

//    public static boolean modifyGrade(Grade grade){
//        try {
//            return gradeDao.update(grade);
//        } catch (Exception e) {
//            // Log the exception
//            e.printStackTrace();
//            return false;
//        }
//    }


//    public static List<Grade> searchALLGradesbyCourseId(String cardid){
//        return gradeDao.findAllByCourseId(cardid);
//    }

//    public static List<Grade> searchGrade(String cardid,String courseId){
//        return gradeDao.findGrade(cardid,courseId);
//    }



    public static void main(String[] args) {
        //test code
        Student student = new Student();
        System.out.println(student);
        Student student1 = new Student(student.getCardId(),"abc1234","Alice","F",new Date(99, 0, 1),"Engineering","Sophomore","Computer Science","alice@example.com","123","Dean's List","None","A123456789123456789");
        System.out.println(student1);
        System.out.println(teacherAddInfo(student1));
        System.out.println(studentFindInfo("abc1234"));
        System.out.println(teacherFindAllInfo());
        student1.setCollege("123456");
        System.out.println(teacherModifyInfo(student1));
        System.out.println(studentFindInfo("abc1234"));
        System.out.println(teacherDeleteInfo("abc1234"));
        System.out.println(studentFindGrade("123456789"));
//        System.out.println(searchALLGradesbyCourseId("111"));
//        System.out.println(searchGrade("123456789","111"));
    }

//    public static void main(String[] args) {
//        Date birthDate = new Date(99, 0, 1); // 1999年1月1日
//        Student student = new Student(
//                "123456789",       // id
//                "abc123",      // cardId
//                "Alice",       // name
//                "F",      // gender
//                birthDate,     // birth
//                "Engineering", // college
//                "Sophomore",   // grade
//                "Computer Science", // major
//                "alice@example.com", // email
//                "123",      // stage
//                "Dean's List", // honor
//                "None",        // punish
//                "A123456789123456789"      // stuCode
//        );
//        System.out.println("deleteabc123:"+deleteStudent("abc123"));
//        System.out.println("findall:"+searchAllStudents());
//        System.out.println("findabc123:"+searchStudent("abc123"));
//        System.out.println("addStudent:"+addStudent(student));
//        System.out.println("findall:"+searchAllStudents());
//        student.setCollege("123456");
//        System.out.println("updateStudent:"+modifyStudent(student));
//        System.out.println("findabc123:"+searchStudent("abc123"));
////        Grade grade = new Grade();
////        grade=gradeDao.find("1");
////        System.out.println(grade.getFinalScore());
//    }


}




//    public static boolean deleteGrade(String studentId,String courseId){
//        try {
//            return gradeDao.delete(id);
//        } catch (Exception e) {
//            // Log the exception
//            e.printStackTrace();
//            return false;
//        }
//    }