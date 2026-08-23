package com.techfix.app.models;

public class PartTransferLog {

    private String id;
    private String partId;
    private String partName;
    private String fromBranch;
    private String toBranch;
    private int quantity;
    private String reason;
    private String timestamp;
    private String status;      // In Transit | Delivered | Pending Approval

    // Required empty constructor for Firebase
    public PartTransferLog() {}

    public PartTransferLog(String id, String partId, String partName,
                           String fromBranch, String toBranch, int quantity,
                           String reason, String timestamp, String status) {
        this.id = id;
        this.partId = partId;
        this.partName = partName;
        this.fromBranch = fromBranch;
        this.toBranch = toBranch;
        this.quantity = quantity;
        this.reason = reason;
        this.timestamp = timestamp;
        this.status = status;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPartId() { return partId; }
    public void setPartId(String partId) { this.partId = partId; }

    public String getPartName() { return partName; }
    public void setPartName(String partName) { this.partName = partName; }

    public String getFromBranch() { return fromBranch; }
    public void setFromBranch(String fromBranch) { this.fromBranch = fromBranch; }

    public String getToBranch() { return toBranch; }
    public void setToBranch(String toBranch) { this.toBranch = toBranch; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
