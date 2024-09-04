package vCampus.Entity;

import java.io.Serializable;

public class Grade implements Serializable {
    private String id;           // 登录ID
    private String cardId;       // 一卡通号
    private String courseName;   // 课程名称
    private String courseId;     // 课程编码
    private double usual;        // 平时成绩
    private double mid;          // 期中成绩
    private double finalScore;   // 期末成绩
    private double total;        // 总成绩
    private double point;        // 学分
    private boolean isFirst;     // 首修0/重修1
    private String term;         // 学期学年

    // 构造方法
    public Grade(String id, String cardId, String courseName, String courseId, double usual, double mid, double finalScore, double total, double point, boolean isFirst, String term) {
        this.id = id;
        this.cardId = cardId;
        this.courseName = courseName;
        this.courseId = courseId;
        this.usual = usual;
        this.mid = mid;
        this.finalScore = finalScore;
        this.total = total;
        this.point = point;
        this.isFirst = isFirst;
        this.term = term;
    }

    public Grade() {

    }

    // Getter和Setter方法
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public double getUsual() {
        return usual;
    }

    public void setUsual(double usual) {
        this.usual = usual;
    }

    public double getMid() {
        return mid;
    }

    public void setMid(double mid) {
        this.mid = mid;
    }

    public double getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(double finalScore) {
        this.finalScore = finalScore;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getPoint() {
        return point;
    }

    public void setPoint(double point) {
        this.point = point;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public double getFinal() {
        return finalScore;
    }


    public void setFinal(double finalScore) {
        this.finalScore = finalScore;
    }
}
