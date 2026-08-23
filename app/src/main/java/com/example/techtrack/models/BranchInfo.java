package com.example.techtrack.models;

public class BranchInfo {
    private String id;
    private String name;         // "Colombo Branch" or "Galle Branch"
    private String shortName;
    private String address;
    private String city;
    private String phone;
    private String altPhone;
    private String hours;
    private String weekendHours;
    private double rating;
    private int reviewsCount;
    private int techniciansAvailable;
    private String sparePartsStockLevel;
    private double latitude;
    private double longitude;
    private int techniciansCount;

    public BranchInfo() {
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getShortName() { return shortName; }
    public void setShortName(String shortName) { this.shortName = shortName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAltPhone() { return altPhone; }
    public void setAltPhone(String altPhone) { this.altPhone = altPhone; }

    public String getHours() { return hours; }
    public void setHours(String hours) { this.hours = hours; }

    public String getWeekendHours() { return weekendHours; }
    public void setWeekendHours(String weekendHours) { this.weekendHours = weekendHours; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public int getReviewsCount() { return reviewsCount; }
    public void setReviewsCount(int reviewsCount) { this.reviewsCount = reviewsCount; }

    public int getTechniciansAvailable() { return techniciansAvailable; }
    public void setTechniciansAvailable(int techniciansAvailable) { this.techniciansAvailable = techniciansAvailable; }

    public String getSparePartsStockLevel() { return sparePartsStockLevel; }
    public void setSparePartsStockLevel(String sparePartsStockLevel) { this.sparePartsStockLevel = sparePartsStockLevel; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public int getTechniciansCount() { return techniciansCount; }
    public void setTechniciansCount(int techniciansCount) { this.techniciansCount = techniciansCount; }
}
