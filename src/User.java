public class User {
    // 属性定义
    public String id;       // 表管理项
    public String pwd;      // 密码
    public Integer age;     // 年龄
    public Boolean gender;   // 性别
    public String role;     // 角色
    public String email;    // 邮箱
    public String card;     // 一卡通号、账号

    // 构造函数
    public User(String id, String pwd, Integer age, Boolean gender, String role, String email, String card) {
        setId(id);
        setPwd(pwd);
        setAge(age);
        setGender(gender);
        setRole(role);
        setEmail(email);
        setCard(card);
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

    public void setCard(String card) {
        if (card.length() != 9) {
            throw new IllegalArgumentException("一卡通号必须是9个字符");
        }
        this.card = card;
    }

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
                '}';
    }
}


