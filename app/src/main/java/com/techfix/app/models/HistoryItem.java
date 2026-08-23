package com.techfix.app.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "history")
public class HistoryItem {

    @PrimaryKey
    @NonNull
    private String id;

    private String referenceId;
    private String deviceName;
    private String deviceType;      // phone | laptop | tablet
    private String repairDate;
    private String serviceSummary;
    private String branch;
    private long totalCostLKR;
    private String status;          // Completed | Canceled
    private String warrantyUntil;
    private String invoiceNumber;
    private String userId;

    // Required empty constructor for Room / Firebase
    public HistoryItem() {}

    public HistoryItem(@NonNull String id, String referenceId, String deviceName,
                       String deviceType, String repairDate, String serviceSummary,
                       String branch, long totalCostLKR, String status,
                       String warrantyUntil, String invoiceNumber, String userId) {
        this.id = id;
        this.referenceId = referenceId;
        this.deviceName = deviceName;
        this.deviceType = deviceType;
        this.repairDate = repairDate;
        this.serviceSummary = serviceSummary;
        this.branch = branch;
        this.totalCostLKR = totalCostLKR;
        this.status = status;
        this.warrantyUntil = warrantyUntil;
        this.invoiceNumber = invoiceNumber;
        this.userId = userId;
    }

    @NonNull
    public String getId() { return id; }
    public void setId(@NonNull String id) { this.id = id; }

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

    public long getTotalCostLKR() { return totalCostLKR; }
    public void setTotalCostLKR(long totalCostLKR) { this.totalCostLKR = totalCostLKR; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getWarrantyUntil() { return warrantyUntil; }
    public void setWarrantyUntil(String warrantyUntil) { this.warrantyUntil = warrantyUntil; }

    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
}
