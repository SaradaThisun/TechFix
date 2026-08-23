package com.example.techtrack.models;

public class HistoryItem {
    private String id;
    private String referenceId;
    private String deviceName;
    private String deviceType;      // phone, laptop, tablet
    private String repairDate;
    private String serviceSummary;
    private String branch;
    private double totalCostLKR;
    private String status;          // Completed, Canceled
    private String warrantyUntil;
    private String invoiceNumber;

    public HistoryItem() {
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getReferenceId() { return referenceId; }
    public void setReferenceId(String referenceId) { this.referenceId = referenceId; }

    public String getDeviceName() { return deviceName; }
    public void setDeviceName(String deviceName) { this.deviceName = deviceName; }

    public String getDeviceType() { return deviceType; }
    public void setDeviceType(String deviceType) { this.deviceType = deviceType; }

    public String getRepairDate() { return repairDate; }
    public void setRepairDate(String repairDate) { this.repairDate = repairDate; }

    public String getServiceSummary() { return serviceSummary; }
    public void setServiceSummary(String serviceSummary) { this.serviceSummary = serviceSummary; }

    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }

    public double getTotalCostLKR() { return totalCostLKR; }
    public void setTotalCostLKR(double totalCostLKR) { this.totalCostLKR = totalCostLKR; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getWarrantyUntil() { return warrantyUntil; }
    public void setWarrantyUntil(String warrantyUntil) { this.warrantyUntil = warrantyUntil; }

    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }
}