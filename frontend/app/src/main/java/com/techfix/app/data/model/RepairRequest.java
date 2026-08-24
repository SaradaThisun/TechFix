package com.techfix.app.data.model;

import java.util.List;

public class RepairRequest {
    private int id;
    private int user_id;
    private int branch_id;
    private String branch_name;
    private String branch_address;
    private Integer technician_id;
    private String technician_name;
    private String technician_phone;
    private int repair_service_id;
    private String service_name;
    private String price;
    private int device_category_id;
    private String category_name;
    private String device_model;
    private String issue_description;
    private String device_image_url;
    private String status;
    private String requested_date;
    private String created_at;
    private List<StatusHistory> history;

    public int getId() { return id; }
    public String getBranchName() { return branch_name; }
    public String getBranchAddress() { return branch_address; }
    public String getTechnicianName() { return technician_name; }
    public String getServiceName() { return service_name; }
    public String getPrice() { return price; }
    public String getCategoryName() { return category_name; }
    public String getDeviceModel() { return device_model; }
    public String getIssueDescription() { return issue_description; }
    public String getDeviceImageUrl() { return device_image_url; }
    public String getStatus() { return status; }
    public String getRequestedDate() { return requested_date; }
    public List<StatusHistory> getHistory() { return history; }
}