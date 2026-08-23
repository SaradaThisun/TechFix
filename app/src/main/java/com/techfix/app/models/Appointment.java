package com.techfix.app.models;

public class Appointment {

    private String id;
    private String userId;
    private String deviceType;      // phone | laptop | tablet
    private String brand;
    private String model;
    private String issueType;
    private String issueDescription;
    private String branch;
    private String serviceType;     // In-Store Drop-off | Courier Pickup
    private String pickupAddress;
    private String date;
    private String timeSlot;
    private String photoUri;        // captured/attached photo URI
    private String status;          // pending | confirmed | completed | canceled
    private String createdAt;

    // Required empty constructor for Firebase
    public Appointment() {}

    public Appointment(String id, String userId, String deviceType, String brand,
                       String model, String issueType, String issueDescription,
                       String branch, String serviceType, String pickupAddress,
                       String date, String timeSlot, String photoUri,
                       String status, String createdAt) {
        this.id = id;
        this.userId = userId;
        this.deviceType = deviceType;
        this.brand = brand;
        this.model = model;
        this.issueType = issueType;
        this.issueDescription = issueDescription;
        this.branch = branch;
        this.serviceType = serviceType;
        this.pickupAddress = pickupAddress;
        this.date = date;
        this.timeSlot = timeSlot;
        this.photoUri = photoUri;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getDeviceType() { return deviceType; }
    public void setDeviceType(String deviceType) { this.deviceType = deviceType; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getIssueType() { return issueType; }
    public void setIssueType(String issueType) { this.issueType = issueType; }

    public String getIssueDescription() { return issueDescription; }
    public void setIssueDescription(String issueDescription) { this.issueDescription = issueDescription; }

    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }

    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }

    public String getPickupAddress() { return pickupAddress; }
    public void setPickupAddress(String pickupAddress) { this.pickupAddress = pickupAddress; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTimeSlot() { return timeSlot; }
    public void setTimeSlot(String timeSlot) { this.timeSlot = timeSlot; }

    public String getPhotoUri() { return photoUri; }
    public void setPhotoUri(String photoUri) { this.photoUri = photoUri; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
