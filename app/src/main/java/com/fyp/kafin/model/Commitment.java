package com.fyp.kafin.model;

import java.math.BigDecimal;

public class Commitment {
    private String comID;
    private String comName;
    private BigDecimal comAmount;

    public Commitment(String comName, BigDecimal comAmount) {
        this.comName = comName;
        this.comAmount = comAmount;
    }

    public String getComID() {
        return comID;
    }

    public String getComName() {
        return comName;
    }

    public void setComName(String comName) {
        this.comName = comName;
    }

    public BigDecimal getComAmount() {
        return comAmount;
    }

    public void setComAmount(BigDecimal comAmount) {
        this.comAmount = comAmount;
    }
}
