package com.example.techtrack.models;

import java.util.List;

public class RepairServiceItem {
    private String id;
    private String title;
    private String category;
    private String deviceType;
    private double priceLKR;
    private String estimatedTime;
    private boolean popular;
    private int warrantyDays;
    private String description;
    private List<String> sampleImages;

    public RepairServiceItem() {
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDeviceType() { return deviceType; }
    public void setDeviceType(String deviceType) { this.deviceType = deviceType; }

    public double getPriceLKR() { return priceLKR; }
    public void setPriceLKR(double priceLKR) { this.priceLKR = priceLKR; }

    public String getEstimatedTime() { return estimatedTime; }
    public void setEstimatedTime(String estimatedTime) { this.estimatedTime = estimatedTime; }

    public boolean isPopular() { return popular; }
    public void setPopular(boolean popular) { this.popular = popular; }

    public int getWarrantyDays() { return warrantyDays; }
    public void setWarrantyDays(int warrantyDays) { this.warrantyDays = warrantyDays; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<String> getSampleImages() { return sampleImages; }
    public void setSampleImages(List<String> sampleImages) { this.sampleImages = sampleImages; }
}