package vCampus.Entity;

import java.util.Date;

public class Student {
    private String id;
    private String cardId;  // 9 characters, primary key
    private String name;     // Up to 10 characters
    private String gender;   // 1 character, 'M' or 'F'
    private Date birth;
    private String college;
    private String grade;
    private String major;    // Up to 20 characters
    private String email;    // Must follow pattern '%@%.com'
    private String stage;    // 3 characters
    private String honor;    // Text field
    private String punish;   // Text field
    private String stuCode;  // 19 characters

    // Constructor
    public Student(String id, String cardId, String name, String gender, Date birth, String college, String grade,
                   String major, String email, String stage, String honor, String punish, String stuCode) {
        this.id = id;
        this.cardId = cardId;
        this.name = name;
        this.gender = gender;
        this.birth = birth;
        this.college = college;
        this.grade = grade;
        this.major = major;
        this.email = email;
        this.stage = stage;
        this.honor = honor;
        this.punish = punish;
        this.stuCode = stuCode;
    }

    // Getters and Setters
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public java.sql.Date getBirth() {
        return (java.sql.Date) birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getHonor() {
        return honor;
    }

    public void setHonor(String honor) {
        this.honor = honor;
    }

    public String getPunish() {
        return punish;
    }

    public void setPunish(String punish) {
        this.punish = punish;
    }

    public String getStuCode() {
        return stuCode;
    }

    public void setStuCode(String stuCode) {
        this.stuCode = stuCode;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", cardId='" + cardId + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", birth=" + birth +
                ", college='" + college + '\'' +
                ", grade='" + grade + '\'' +
                ", major='" + major + '\'' +
                ", email='" + email + '\'' +
                ", stage='" + stage + '\'' +
                ", honor='" + honor + '\'' +
                ", punish='" + punish + '\'' +
                ", stuCode='" + stuCode + '\'' +
                '}';
    }
}
