package com.spring.db;

import java.sql.Timestamp;

public class Location {
    private Long id;
    private Double latitude;
    private Double longitude;
    private Timestamp timestamp;

    Location() { }

    public Location(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Location(Long id, Double latitude, Double longitude, Timestamp timestamp) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
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

    Timestamp getTimestamp() { return timestamp; }
    void setTimestamp(Timestamp timestamp) { this.timestamp = timestamp; }

    @Override
    public String toString() {
        return "Person{" + "id=" + id + ", latitude='" + latitude + '\'' + ", longitude='" + longitude + '\'' +
                ", timestamp='" + timestamp + '\'' + '}';
    }

    /**
     * Checks if 2 locations are within 5 meters of each other.
     * @param loc1 first location
     * @param loc2 second location
     * @return true if distance between them less than 5 meters.
     */
    public boolean locationsClose(Location loc1, Location loc2) {
        return distance(loc1.getLatitude(), loc1.getLongitude(), loc2.getLatitude(), loc2.getLongitude()) < 5.0;
    }

    /**
     * Calculates distance in meters between 2 locations given their latitude and longitude values.
     * @param lat1 latitude of 1st location
     * @param lng1 longitude of 1st location
     * @param lat2 latitude of 2st location
     * @param lng2 longitude of 2st location
     * @return distance in meters.
     */
    private double distance(double lat1, double lng1, double lat2, double lng2) {

        double earthRadius = 6371000; //meters

        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);

        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);

        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return earthRadius * c;
    }

    /**
     * Returns average location of two given.
     * @param loc1 first location
     * @param loc2 second location
     * @return new location object with average latitude and longitude.
     */
    public Location getAverageLocation(Location loc1, Location loc2) {
        return new Location((loc1.getLatitude() + loc2.getLatitude()) / 2,
                (loc1.getLongitude() + loc2.getLongitude()) / 2);
    }
}