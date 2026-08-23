package com.techfix.app.models;

import java.util.List;

public class RepairService {

    private String id;
    private String title;
    private String category;        // Mobile | Computers | Screen Replace | Battery | Audio/Port | Board Level
    private String deviceType;      // mobile | computer | tablet
    private long priceLKR;
    private String estimatedTime;
    private boolean popular;
    private int warrantyDays;
    private String description;
    private List<String> sampleImages;

    // Required empty constructor for Firebase / Gson
    public RepairService() {}

    public RepairService(String id, String title, String category, String deviceType,
                         long priceLKR, String estimatedTime, boolean popular,
                         int warrantyDays, String description, List<String> sampleImages) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.deviceType = deviceType;
        this.priceLKR = priceLKR;
        this.estimatedTime = estimatedTime;
        this.popular = popular;
        this.warrantyDays = warrantyDays;
        this.description = description;
        this.sampleImages = sampleImages;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDeviceType() { return deviceType; }
    public void setDeviceType(String deviceType) { this.deviceType = deviceType; }

    public long getPriceLKR() { return priceLKR; }
    public void setPriceLKR(long priceLKR) { this.priceLKR = priceLKR; }

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
