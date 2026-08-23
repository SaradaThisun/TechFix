package com.example.techtrack.models;

public class UsedPart {
    private String partId;
    private String partName;
    private int qty;
    private double costLKR;

    public UsedPart() {
    }

    public String getPartId() { return partId; }
    public void setPartId(String partId) { this.partId = partId; }

    public String getPartName() { return partName; }
    public void setPartName(String partName) { this.partName = partName; }

    public int getQty() { return qty; }
    public void setQty(int qty) { this.qty = qty; }

    public double getCostLKR() { return costLKR; }
    public void setCostLKR(double costLKR) { this.costLKR = costLKR; }
}