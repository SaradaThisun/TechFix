package com.example.techtrack.models;

public class StatusLogEntry {
    private String id;
    private String title;
    private String description;
    private String timestamp;
    private String technicianName;
    private String statusType;   // info, diagnostic, progress, success, alert

    public StatusLogEntry() {
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public String getTechnicianName() { return technicianName; }
    public void setTechnicianName(String technicianName) { this.technicianName = technicianName; }

    public String getStatusType() { return statusType; }
    public void setStatusType(String statusType) { this.statusType = statusType; }
}