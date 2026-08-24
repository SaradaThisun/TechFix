package com.techfix.app.data.model;

public class RepairRequestCreatedResponse {
    private int id;
    private Branch branch;
    private Technician technician;
    private String status;

    public int getId() { return id; }
    public Branch getBranch() { return branch; }
    public Technician getTechnician() { return technician; }
    public String getStatus() { return status; }
}