package com.techfix.app.data.model;

public class StatusHistory {
    private int id;
    private int repair_request_id;
    private String status;
    private String notes;
    private String updated_at;

    public String getStatus() { return status; }
    public String getNotes() { return notes; }
    public String getUpdatedAt() { return updated_at; }
}