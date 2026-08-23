package com.techfix.app.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "users")
public class User {

    @PrimaryKey
    @NonNull
    private String uid;

    private String fullName;
    private String email;
    private String phone;
    private String branch;        // "Colombo Branch" | "Galle Branch"
    private String role;          // "customer" | "staff"
    private String avatarUrl;
    private boolean isAuthenticated;

    // Required empty constructor for Firebase
    public User() {}

    public User(@NonNull String uid, String fullName, String email,
                String phone, String branch, String role) {
        this.uid = uid;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.branch = branch;
        this.role = role;
        this.isAuthenticated = true;
    }

    @NonNull
    public String getUid() { return uid; }
    public void setUid(@NonNull String uid) { this.uid = uid; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public boolean isAuthenticated() { return isAuthenticated; }
    public void setAuthenticated(boolean authenticated) { isAuthenticated = authenticated; }
}
