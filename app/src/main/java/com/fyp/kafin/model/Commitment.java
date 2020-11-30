package com.fyp.kafin.model;

public class Commitment {
    private String comID;
    private String comName;
    private String comAmount;

    public Commitment(String comName, String comAmount) {
        this.comName = comName;
        this.comAmount = comAmount;
    }

    public void setComID(String comID) {
        this.comID = comID;
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

    public String getComAmount() {
        return comAmount;
    }

    public void setComAmount(String comAmount) {
        this.comAmount = comAmount;
    }
}
