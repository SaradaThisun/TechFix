package com.techfix.app.data.model;

public class RepairService {
    private int id;
    private int device_category_id;
    private String name;
    private String description;
    private String price;
    private int estimated_hours;
    private String category_name;

    public int getId() { return id; }
    public int getDeviceCategoryId() { return device_category_id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getPrice() { return price; }
    public int getEstimatedHours() { return estimated_hours; }
    public String getCategoryName() { return category_name; }
}