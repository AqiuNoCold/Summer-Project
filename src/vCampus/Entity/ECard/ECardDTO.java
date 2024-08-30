package vCampus.Entity.ECard;

public class ECardDTO {
    private Float remain;    //账户余额
    private Integer password; //支付密码
    private String card;

    public ECardDTO(Float remain, Integer password, String card) {
        this.remain = remain;
        this.password = password;
        this.card = card;
    }

    public Float getRemain() {
        return remain;
    }

    public void setRemain(Float remain) {
        this.remain = remain;
    }

    public Integer getPassword() {
        return password;
    }

    public void setPassword(Integer password) {
        this.password = password;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }
}
