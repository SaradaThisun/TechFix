package com.techfix.app.models;

import java.util.List;

public class Branch {

    private String id;
    private String name;            // "Colombo Branch" | "Galle Branch"
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
    private List<String> popularServices;
    private String googleMapsUrl;

    // Required empty constructor for Firebase
    public Branch() {}

    public Branch(String id, String name, String shortName, String address, String city,
                  String phone, String altPhone, String hours, String weekendHours,
                  double rating, int reviewsCount, int techniciansAvailable,
                  String sparePartsStockLevel, double latitude, double longitude,
                  List<String> popularServices, String googleMapsUrl) {
        this.id = id;
        this.name = name;
        this.shortName = shortName;
        this.address = address;
        this.city = city;
        this.phone = phone;
        this.altPhone = altPhone;
        this.hours = hours;
        this.weekendHours = weekendHours;
        this.rating = rating;
        this.reviewsCount = reviewsCount;
        this.techniciansAvailable = techniciansAvailable;
        this.sparePartsStockLevel = sparePartsStockLevel;
        this.latitude = latitude;
        this.longitude = longitude;
        this.popularServices = popularServices;
        this.googleMapsUrl = googleMapsUrl;
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

    public List<String> getPopularServices() { return popularServices; }
    public void setPopularServices(List<String> popularServices) { this.popularServices = popularServices; }

    public String getGoogleMapsUrl() { return googleMapsUrl; }
    public void setGoogleMapsUrl(String googleMapsUrl) { this.googleMapsUrl = googleMapsUrl; }
}
