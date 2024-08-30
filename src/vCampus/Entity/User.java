package vCampus.Entity;

import java.util.ArrayList;

public class User {
    // 属性定义
    protected String id;       // 表管理项
    protected String pwd;      // 密码
    protected Integer age;     // 年龄
    protected Boolean gender;   // 性别
    protected String role;     // 角色
    protected String email;    // 邮箱
    protected String card;     // 一卡通号、账号
    protected Float remain;    //账户余额
    protected Integer password; //支付密码
    protected Boolean lost;    //账户冻结情况
//    protected ArrayList<String> courses;

    // 构造函数
    public User(String id, String pwd, Integer age, Boolean gender, String role, String email, String card,Float remain,Integer password,Boolean lost) {//, ArrayList<String> courses
        setId(id);
        setPwd(pwd);
        setAge(age);
        setGender(gender);
        setRole(role);
        setEmail(email);
        setCard(card);
        setRemain(remain);
        setPassword(password);
        setLost(lost);
//        setCourses(courses);
    }

    // id的getter和setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id; // 可以根据需要添加更多验证
    }

    // pwd的getter和setter
    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        if (pwd.length() < 6 || pwd.length() > 16) {
            throw new IllegalArgumentException("密码必须是6到16个字符");
        }
        this.pwd = pwd;
    }

    // age的getter和setter
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        if (age <= 0) {
            throw new IllegalArgumentException("年龄必须大于0");
        }
        this.age = age;
    }

    // gender的getter和setter
    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender; // 0表示女性，1表示男性
    }

    // role的getter和setter
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        if (!role.matches("ST|TC|AD")) {
            throw new IllegalArgumentException("角色必须是ST, TC或AD");
        }
        this.role = role;
    }

    // email的getter和setter
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (!email.matches(".*@.*\\.com")) {
            throw new IllegalArgumentException("邮箱格式不正确");
        }
        this.email = email;
    }

    // card的getter和setter
    public String getCard() {
        return card;
    }

//    public ArrayList<String> getCourses() {
//        return courses;
//    }

    public void setCard(String card) {
        if (card.length() != 9) {
            throw new IllegalArgumentException("一卡通号必须是9个字符");
        }
        this.card = card;
    }

    // remain的getter和setter
    public float getRemain() {
        return remain;
    }

    public void setRemain(Float remain) {
        this.remain = remain; // 可以根据需要添加更多验证
    }

    // password的getter和setter
    public Integer getPassword() {
        return password;
    }

    public void setPassword(Integer password) {
        this.password = password; // 可以根据需要添加更多验证
    }

    // lost的getter和setter
    public Boolean getLost() {
        return lost;
    }

    public void setLost(Boolean lost) {
        this.lost = lost; // 可以根据需要添加更多验证
    }

//    public void setCourses(ArrayList<String> courses) {
//        this.courses = courses;
//    }

//    public void addCourse(String course) {
//        if (courses == null) {
//            courses = new ArrayList<>();
//        }
//        courses.add(course);
//    }

    // 重写toString方法以便于输出用户信息
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", pwd='" + pwd + '\'' +
                ", age=" + age +
                ", gender=" + (gender ? "男" : "女") +
                ", role='" + role + '\'' +
                ", email='" + email + '\'' +
                ", card='" + card + '\'' +
                ", remain=" + remain +
                ", password='" + password + '\'' +
                ", lost=" + (lost ? "正常" : "冻结") +
//                ", courses=" + courses +
                '}';
    }
}


