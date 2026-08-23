package com.example.techtrack.models;

public class Technician {
    private String name;
    private String role;
    private String phone;
    private String avatar;
    private double rating;

    public Technician() {
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
}