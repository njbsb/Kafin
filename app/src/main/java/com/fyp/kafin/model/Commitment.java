package com.fyp.kafin.model;

public class Commitment {
    private String comID;
    private String comName;
    private float comAmount;

    public Commitment() {
    }

    public Commitment(String comID, String comName, float comAmount) {
        this.comID = comID;
        this.comName = comName;
        this.comAmount = comAmount;
    }

    public Commitment(String comName, float comAmount) {
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

    public float getComAmount() {
        return comAmount;
    }

    public void setComAmount(float comAmount) {
        this.comAmount = comAmount;
    }
}
