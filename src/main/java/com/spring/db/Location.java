package com.spring.db;

public class Location {
    private Long id;
    private Double latitude;
    private Double longitude;

    Location() { }

    public Location(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Location(Long id, Double latitude, Double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    Long getId() {
        return id;
    }
    void setId(Long id) { this.id = id; }

    Double getLatitude() {
        return latitude;
    }
    void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    Double getLongitude() { return longitude; }
    void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Person{" + "id=" + id + ", latitude='" + latitude + '\'' + ", longitude='" + longitude + '\'' + '}';
    }
}