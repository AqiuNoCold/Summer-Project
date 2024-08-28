package vCampus.Entity;

public class Course {
    private String courseId;       // 课程编号
    private String courseName;     // 课程名称
    private String teacherName;    // 教师名称（一卡通号）
    private int time;              // 一周几次
    private int startTime;         // 第几节开始
    private int endTime;           // 第几节结束
    private String status;         // 选修课还是必修课
    private int nowNum;            // 现在有几人选课
    private int maxNum;            // 课程容量
    private String classroom;      // 上课教室
    private int num;               // 开课数量

    // 默认构造函数
    public Course() {}

    // 带参数的构造函数
    public Course(String courseId, String courseName, String teacherName, int time, int startTime, int endTime, String status, int nowNum, int maxNum, String classroom, int num) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.teacherName = teacherName;
        this.time = time;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.nowNum = nowNum;
        this.maxNum = maxNum;
        this.classroom = classroom;
        this.num = num;
    }

    // Getter 和 Setter 方法
    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getNowNum() {
        return nowNum;
    }

    public void setNowNum(int nowNum) {
        this.nowNum = nowNum;
    }

    public int getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseId='" + courseId + '\'' +
                ", courseName='" + courseName + '\'' +
                ", teacherName='" + teacherName + '\'' +
                ", time=" + time +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", status='" + status + '\'' +
                ", nowNum=" + nowNum +
                ", maxNum=" + maxNum +
                ", classroom='" + classroom + '\'' +
                ", num=" + num +
                '}';
    }
}
