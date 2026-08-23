package com.techfix.app.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import androidx.annotation.NonNull;
import com.techfix.app.database.Converters;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "repair_tickets")
@TypeConverters(Converters.class)
public class RepairTicket {

    @PrimaryKey
    @NonNull
    private String id;

    private String deviceModel;
    private String deviceType;      // phone | laptop | tablet | desktop
    private String category;
    private String issue;
    private String status;          // Request Received | Assigned to Technician | Repair in Progress | Ready for Pickup | Completed | Canceled
    private int currentStepIndex;
    private int progressPercent;
    private String branch;
    private String technicianName;
    private String technicianRole;
    private String technicianPhone;
    private String technicianAvatar;
    private float technicianRating;
    private String estimatedCompletion;
    private String createdAt;
    private long serviceFeeLKR;
    private long partsFeeLKR;
    private long taxDiscountLKR;
    private long totalCostLKR;
    private boolean isPaid;
    private String customerName;
    private String customerPhone;
    private String customerNotes;
    private String devicePhoto;
    private String userId = "";          // owner's Firebase UID

    // Stored as JSON strings via Room TypeConverters
    private List<TimelineStep> timelineSteps = new ArrayList<>();
    private List<StatusLogEntry> statusLogs = new ArrayList<>();

    // Required empty constructor for Firebase
    public RepairTicket() {}

    @NonNull
    public String getId() { return id; }
    public void setId(@NonNull String id) { this.id = id; }

    public String getDeviceModel() { return deviceModel; }
    public void setDeviceModel(String deviceModel) { this.deviceModel = deviceModel; }

    public String getDeviceType() { return deviceType; }
    public void setDeviceType(String deviceType) { this.deviceType = deviceType; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getIssue() { return issue; }
    public void setIssue(String issue) { this.issue = issue; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getCurrentStepIndex() { return currentStepIndex; }
    public void setCurrentStepIndex(int currentStepIndex) { this.currentStepIndex = currentStepIndex; }

    public int getProgressPercent() { return progressPercent; }
    public void setProgressPercent(int progressPercent) { this.progressPercent = progressPercent; }

    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }

    public String getTechnicianName() { return technicianName; }
    public void setTechnicianName(String technicianName) { this.technicianName = technicianName; }

    public String getTechnicianRole() { return technicianRole; }
    public void setTechnicianRole(String technicianRole) { this.technicianRole = technicianRole; }

    public String getTechnicianPhone() { return technicianPhone; }
    public void setTechnicianPhone(String technicianPhone) { this.technicianPhone = technicianPhone; }

    public String getTechnicianAvatar() { return technicianAvatar; }
    public void setTechnicianAvatar(String technicianAvatar) { this.technicianAvatar = technicianAvatar; }

    public float getTechnicianRating() { return technicianRating; }
    public void setTechnicianRating(float technicianRating) { this.technicianRating = technicianRating; }

    public String getEstimatedCompletion() { return estimatedCompletion; }
    public void setEstimatedCompletion(String estimatedCompletion) { this.estimatedCompletion = estimatedCompletion; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public long getServiceFeeLKR() { return serviceFeeLKR; }
    public void setServiceFeeLKR(long serviceFeeLKR) { this.serviceFeeLKR = serviceFeeLKR; }

    public long getPartsFeeLKR() { return partsFeeLKR; }
    public void setPartsFeeLKR(long partsFeeLKR) { this.partsFeeLKR = partsFeeLKR; }

    public long getTaxDiscountLKR() { return taxDiscountLKR; }
    public void setTaxDiscountLKR(long taxDiscountLKR) { this.taxDiscountLKR = taxDiscountLKR; }

    public long getTotalCostLKR() { return totalCostLKR; }
    public void setTotalCostLKR(long totalCostLKR) { this.totalCostLKR = totalCostLKR; }

    @com.google.firebase.database.PropertyName("isPaid")
    public boolean isPaid() { return isPaid; }
    @com.google.firebase.database.PropertyName("isPaid")
    public void setPaid(boolean paid) { isPaid = paid; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }

    public String getCustomerNotes() { return customerNotes; }
    public void setCustomerNotes(String customerNotes) { this.customerNotes = customerNotes; }

    public String getDevicePhoto() { return devicePhoto; }
    public void setDevicePhoto(String devicePhoto) { this.devicePhoto = devicePhoto; }

    @com.google.firebase.database.PropertyName("userId")
    public String getUserId() { return userId; }
    @com.google.firebase.database.PropertyName("userId")
    public void setUserId(String userId) { this.userId = userId; }

    public List<TimelineStep> getTimelineSteps() { return timelineSteps; }
    public void setTimelineSteps(List<TimelineStep> timelineSteps) { this.timelineSteps = timelineSteps; }

    public List<StatusLogEntry> getStatusLogs() { return statusLogs; }
    public void setStatusLogs(List<StatusLogEntry> statusLogs) { this.statusLogs = statusLogs; }

    // ─── Inner classes ───────────────────────────────────────────────────────

    public static class TimelineStep {
        public int stepNumber;
        public String title;
        public String description;
        public String timestamp;
        public boolean isCompleted;
        public boolean isCurrent;

        public TimelineStep() {}

        public TimelineStep(int stepNumber, String title, String description,
                            String timestamp, boolean isCompleted, boolean isCurrent) {
            this.stepNumber = stepNumber;
            this.title = title;
            this.description = description;
            this.timestamp = timestamp;
            this.isCompleted = isCompleted;
            this.isCurrent = isCurrent;
        }
    }

    public static class StatusLogEntry {
        public String id;
        public String title;
        public String description;
        public String timestamp;
        public String technicianName;
        public String statusType;   // info | diagnostic | progress | success | alert

        public StatusLogEntry() {}

        public StatusLogEntry(String id, String title, String description,
                              String timestamp, String technicianName, String statusType) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.timestamp = timestamp;
            this.technicianName = technicianName;
            this.statusType = statusType;
        }
    }
}
