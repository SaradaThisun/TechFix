package com.example.techtrack.models;

public class IncomingRepairRequest {
    private String id;
    private String customerName;
    private String customerPhone;
    private String customerLocation;
    private String deviceType;         // phone, laptop, tablet, desktop
    private String deviceModel;
    private String issueSummary;
    private String autoMatchBranch;
    private double autoMatchDistanceKm;
    private boolean partInStock;
    private int techniciansAvailable;
    private String urgency;            // Urgent, Standard
    private String submittedTime;
    private String status;             // Pending Dispatch, Dispatched, Transferred
    private String assignedBranch;
    private String assignedTech;

    public IncomingRepairRequest() {
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }

    public String getCustomerLocation() { return customerLocation; }
    public void setCustomerLocation(String customerLocation) { this.customerLocation = customerLocation; }

    public String getDeviceType() { return deviceType; }
    public void setDeviceType(String deviceType) { this.deviceType = deviceType; }

    public String getDeviceModel() { return deviceModel; }
    public void setDeviceModel(String deviceModel) { this.deviceModel = deviceModel; }

    public String getIssueSummary() { return issueSummary; }
    public void setIssueSummary(String issueSummary) { this.issueSummary = issueSummary; }

    public String getAutoMatchBranch() { return autoMatchBranch; }
    public void setAutoMatchBranch(String autoMatchBranch) { this.autoMatchBranch = autoMatchBranch; }

    public double getAutoMatchDistanceKm() { return autoMatchDistanceKm; }
    public void setAutoMatchDistanceKm(double autoMatchDistanceKm) { this.autoMatchDistanceKm = autoMatchDistanceKm; }

    public boolean isPartInStock() { return partInStock; }
    public void setPartInStock(boolean partInStock) { this.partInStock = partInStock; }

    public int getTechniciansAvailable() { return techniciansAvailable; }
    public void setTechniciansAvailable(int techniciansAvailable) { this.techniciansAvailable = techniciansAvailable; }

    public String getUrgency() { return urgency; }
    public void setUrgency(String urgency) { this.urgency = urgency; }

    public String getSubmittedTime() { return submittedTime; }
    public void setSubmittedTime(String submittedTime) { this.submittedTime = submittedTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAssignedBranch() { return assignedBranch; }
    public void setAssignedBranch(String assignedBranch) { this.assignedBranch = assignedBranch; }

    public String getAssignedTech() { return assignedTech; }
    public void setAssignedTech(String assignedTech) { this.assignedTech = assignedTech; }
}