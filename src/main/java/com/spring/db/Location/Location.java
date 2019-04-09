package com.spring.db.Location;

import java.sql.Timestamp;

public class Location {
    private Long id;
    private String key;
    private Double latitude;
    private Double longitude;
    private Timestamp timestamp;

    Location() { }

    public Location(String key, Double latitude, Double longitude) {
        if (key == null || latitude == null || longitude == null) {
            throw new IllegalArgumentException("Couldn't find location data in request");
        }
        this.key = key;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    public Location(Long id, String key, Double latitude, Double longitude) {
        if (id == null || key == null || latitude == null || longitude == null) {
            throw new IllegalArgumentException("Couldn't find location data in request");
        }
        this.id = id;
        this.key = key;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    public Location(Long id, String key, Double latitude, Double longitude, Timestamp timestamp) {
        if (key == null || latitude == null || longitude == null) {
            throw new IllegalArgumentException("Couldn't find location data in request");
        }
        this.id = id;
        this.key = key;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    public  Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Timestamp getTimestamp() { return timestamp; }
    public void setTimestamp(Timestamp timestamp) { this.timestamp = timestamp; }

    public String toJSON() {
        return "{" + "\"key\":\"" + key + "\", \"latitude\":" + latitude + ", \"longitude\":" + longitude +'}';
    }

    /**
     * Checks if this location and new one are within 5 meters and 5 minutes of each other.
     * @param location second location
     * @return true if distance between them less than 5 meters and 5 minutes.
     */
    public boolean needToMigrate(Location location) {
        return this.getKey().equals(location.getKey()) && //same key
                distance(this.getLatitude(), this.getLongitude(), location.getLatitude(), location.getLongitude()) < 5.0 //close distance-wise
                && (timestampDifference(this.getTimestamp(), location.getTimestamp()) < 5); //close time-wise
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
        int earthRadiusMeters = 6378100;

        double dLat = Math.toRadians(lat2-lat1);
        double dLon = Math.toRadians(lng2-lng1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return earthRadiusMeters * c;
    }

    /**
     * Finds difference in minutes between 2 timestamps.
     * @param currentTime first timestamp
     * @param oldTime second timestamp
     * @return difference in minutes
     */
    private static long timestampDifference(Timestamp currentTime, Timestamp oldTime)
    {
        long diff = Math.abs(currentTime.getTime() - oldTime.getTime());
        return diff / (60000);
    }

    /**
     * Returns average location of this and new location
     * @param secondLocation second location
     * @return new location object with average latitude and longitude.
     */
    public Location getAverageLocation(Location secondLocation) {
        return new Location(this.getId(), this.getKey(), (this.getLatitude() + secondLocation.getLatitude()) / 2,
                (this.getLongitude() + secondLocation.getLongitude()) / 2);
    }

    public double bearingTo(Location location) {
        double dLon = (location.longitude - this.longitude);
        double y = Math.sin(dLon) * Math.cos(location.longitude);
        double x = Math.cos(this.latitude) * Math.sin(location.longitude) -
                Math.sin(this.latitude) * Math.cos(location.longitude) * Math.cos(dLon);
        double bearing = Math.atan2(y, x);

        bearing = Math.toDegrees(bearing);
        bearing = (bearing + 360) % 360;
        bearing = 360 - bearing; // count degrees counter-clockwise - remove to make clockwise

        return bearing;
    }
}