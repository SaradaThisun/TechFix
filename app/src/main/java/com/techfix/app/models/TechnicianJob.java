package com.techfix.app.models;

import java.util.List;

public class TechnicianJob {

    private String id;
    private String ticketNumber;
    private String deviceModel;
    private String deviceType;
    private String customerName;
    private String customerPhone;
    private String customerNotes;
    private String priority;        // Urgent | Standard
    private String currentStage;    // Diagnostic | Waiting for Parts | In Repair | Testing | Ready
    private String targetCompletion;
    private String branch;
    private String benchNumber;
    private List<String> beforeImages;
    private List<String> afterImages;
    private List<InternalNote> internalNotes;
    private List<UsedPart> usedParts;

    // Required empty constructor for Firebase
    public TechnicianJob() {}

    // ─── Inner classes ───────────────────────────────────────────────────────

    public static class InternalNote {
        public String id;
        public String timestamp;
        public String note;
        public String author;

        public InternalNote() {}

        public InternalNote(String id, String timestamp, String note, String author) {
            this.id = id;
            this.timestamp = timestamp;
            this.note = note;
            this.author = author;
        }
    }

    public static class UsedPart {
        public String partId;
        public String partName;
        public int qty;
        public long costLKR;

        public UsedPart() {}

        public UsedPart(String partId, String partName, int qty, long costLKR) {
            this.partId = partId;
            this.partName = partName;
            this.qty = qty;
            this.costLKR = costLKR;
        }
    }

    // ─── Getters / Setters ───────────────────────────────────────────────────

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTicketNumber() { return ticketNumber; }
    public void setTicketNumber(String ticketNumber) { this.ticketNumber = ticketNumber; }

    public String getDeviceModel() { return deviceModel; }
    public void setDeviceModel(String deviceModel) { this.deviceModel = deviceModel; }

    public String getDeviceType() { return deviceType; }
    public void setDeviceType(String deviceType) { this.deviceType = deviceType; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }

    public String getCustomerNotes() { return customerNotes; }
    public void setCustomerNotes(String customerNotes) { this.customerNotes = customerNotes; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getCurrentStage() { return currentStage; }
    public void setCurrentStage(String currentStage) { this.currentStage = currentStage; }

    public String getTargetCompletion() { return targetCompletion; }
    public void setTargetCompletion(String targetCompletion) { this.targetCompletion = targetCompletion; }

    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }

    public String getBenchNumber() { return benchNumber; }
    public void setBenchNumber(String benchNumber) { this.benchNumber = benchNumber; }

    public List<String> getBeforeImages() { return beforeImages; }
    public void setBeforeImages(List<String> beforeImages) { this.beforeImages = beforeImages; }

    public List<String> getAfterImages() { return afterImages; }
    public void setAfterImages(List<String> afterImages) { this.afterImages = afterImages; }

    public List<InternalNote> getInternalNotes() { return internalNotes; }
    public void setInternalNotes(List<InternalNote> internalNotes) { this.internalNotes = internalNotes; }

    public List<UsedPart> getUsedParts() { return usedParts; }
    public void setUsedParts(List<UsedPart> usedParts) { this.usedParts = usedParts; }
}
