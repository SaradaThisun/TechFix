package com.techfix.app.models;

public class PartMovementLog {
    private String id;
    private String partId;
    private String partName;
    private String type; // ADD | RESTOCK | USE | TRANSFER
    private int quantity;
    private String branch;
    private String staffName;
    private String timestamp;

    public PartMovementLog() {}

    public PartMovementLog(String id, String partId, String partName, String type, 
                           int quantity, String branch, String staffName, String timestamp) {
        this.id = id;
        this.partId = partId;
        this.partName = partName;
        this.type = type;
        this.quantity = quantity;
        this.branch = branch;
        this.staffName = staffName;
        this.timestamp = timestamp;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPartId() { return partId; }
    public void setPartId(String partId) { this.partId = partId; }
    public String getPartName() { return partName; }
    public void setPartName(String partName) { this.partName = partName; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }
    public String getStaffName() { return staffName; }
    public void setStaffName(String staffName) { this.staffName = staffName; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}
