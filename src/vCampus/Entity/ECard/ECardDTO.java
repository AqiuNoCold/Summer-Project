package vCampus.Entity.ECard;

import java.io.Serializable;

public class ECardDTO implements Serializable {
    private Float remain;    //账户余额
    private Integer password; //支付密码
    private String card;

    public ECardDTO(Float remain, Integer password, String card) {
        this.remain = remain;
        this.password = password;
        this.card = card;
    }

    public ECardDTO(String card) {
        remain = 0f;
        password = Integer.parseInt(card.substring(card.length() - 6));
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
