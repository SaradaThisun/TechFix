package com.techfix.app.models;

public class Technician {

    private String id;
    private String name;
    private String role;
    private String phone;
    private String avatarUrl;
    private float rating;
    private String branch;
    private String benchNumber;
    private boolean available;

    // Required empty constructor for Firebase
    public Technician() {}

    public Technician(String id, String name, String role, String phone,
                      String avatarUrl, float rating, String branch,
                      String benchNumber, boolean available) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.phone = phone;
        this.avatarUrl = avatarUrl;
        this.rating = rating;
        this.branch = branch;
        this.benchNumber = benchNumber;
        this.available = available;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public float getRating() { return rating; }
    public void setRating(float rating) { this.rating = rating; }

    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }

    public String getBenchNumber() { return benchNumber; }
    public void setBenchNumber(String benchNumber) { this.benchNumber = benchNumber; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}
