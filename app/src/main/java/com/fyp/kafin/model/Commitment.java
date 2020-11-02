package com.fyp.kafin.model;

import java.math.BigDecimal;

public class Commitment {
    private String comID;
    private String comName;
    private Double comAmount;

    public Commitment(String comName, Double comAmount) {
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

    public Double getComAmount() {
        return comAmount;
    }

    public void setComAmount(Double comAmount) {
        this.comAmount = comAmount;
    }
}
