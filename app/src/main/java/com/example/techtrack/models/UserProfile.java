package com.example.techtrack.models;

public class UserProfile {
    private String fullName;
    private String email;
    private String phone;
    private String branch; // "Colombo Branch" or "Galle Branch"
    private String avatarUrl;
    private boolean isAuthenticated;

    public UserProfile() {
        // Empty constructor needed for Gson (network) and Room-free SQLite mapping
    }

    public UserProfile(String fullName, String email, String phone, String branch, String avatarUrl, boolean isAuthenticated) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.branch = branch;
        this.avatarUrl = avatarUrl;
        this.isAuthenticated = isAuthenticated;
    }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public boolean isAuthenticated() { return isAuthenticated; }
    public void setAuthenticated(boolean authenticated) { isAuthenticated = authenticated; }
}
