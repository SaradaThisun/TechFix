package com.techfix.app.data.model;

public class Branch {
    private int id;
    private String name;
    private String address;
    private String city;
    private double latitude;
    private double longitude;
    private String phone;
    private Double distance_km; // only present when using the /nearby endpoint

    public int getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getCity() { return city; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public String getPhone() { return phone; }
    public Double getDistanceKm() { return distance_km; }
}