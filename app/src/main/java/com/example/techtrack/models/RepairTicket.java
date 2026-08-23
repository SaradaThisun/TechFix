package com.example.techtrack.models;

import java.util.List;

public class RepairTicket {
    private String id;
    private String deviceModel;
    private String deviceType;        // phone, laptop, tablet, desktop
    private String category;
    private String issue;
    private String status;            // Request Received, Assigned to Technician, Repair in Progress, Ready for Pickup, Completed, Canceled
    private int currentStepIndex;
    private int progressPercent;
    private String branch;
    private Technician technician;
    private String estimatedCompletion;
    private String createdAt;
    private double serviceFeeLKR;
    private double partsFeeLKR;
    private double taxDiscountLKR;
    private double totalCostLKR;
    private boolean isPaid;
    private List<TrackingStep> timelineSteps;
    private List<StatusLogEntry> statusLogs;
    private String customerNotes;

    public RepairTicket() {
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

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

    public Technician getTechnician() { return technician; }
    public void setTechnician(Technician technician) { this.technician = technician; }

    public String getEstimatedCompletion() { return estimatedCompletion; }
    public void setEstimatedCompletion(String estimatedCompletion) { this.estimatedCompletion = estimatedCompletion; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public double getServiceFeeLKR() { return serviceFeeLKR; }
    public void setServiceFeeLKR(double serviceFeeLKR) { this.serviceFeeLKR = serviceFeeLKR; }

    public double getPartsFeeLKR() { return partsFeeLKR; }
    public void setPartsFeeLKR(double partsFeeLKR) { this.partsFeeLKR = partsFeeLKR; }

    public double getTaxDiscountLKR() { return taxDiscountLKR; }
    public void setTaxDiscountLKR(double taxDiscountLKR) { this.taxDiscountLKR = taxDiscountLKR; }

    public double getTotalCostLKR() { return totalCostLKR; }
    public void setTotalCostLKR(double totalCostLKR) { this.totalCostLKR = totalCostLKR; }

    public boolean isPaid() { return isPaid; }
    public void setPaid(boolean paid) { isPaid = paid; }

    public List<TrackingStep> getTimelineSteps() { return timelineSteps; }
    public void setTimelineSteps(List<TrackingStep> timelineSteps) { this.timelineSteps = timelineSteps; }

    public List<StatusLogEntry> getStatusLogs() { return statusLogs; }
    public void setStatusLogs(List<StatusLogEntry> statusLogs) { this.statusLogs = statusLogs; }

    public String getCustomerNotes() { return customerNotes; }
    public void setCustomerNotes(String customerNotes) { this.customerNotes = customerNotes; }
}