package com.spring.db;

import java.sql.Timestamp;

public class Location {
    private Long id;
    private Double latitude;
    private Double longitude;
    private Timestamp timestamp;

    Location() { }

    public Location(Double latitude, Double longitude) {
        if (latitude == null || longitude == null) {
            throw new IllegalArgumentException("Couldn't find location data in request"); 
        }
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    public Location(Long id, Double latitude, Double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    public Location(Long id, Double latitude, Double longitude, Timestamp timestamp) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    @Override
    public String toString() {
        return "Person{" + "id=" + id + ", latitude='" + latitude + '\'' + ", longitude='" + longitude + '\'' +
                ", timestamp='" + timestamp + '\'' + '}';
    }

    /**
     * Checks if this location and new one are within 5 meters and 5 minutes of each other.
     * @param location second location
     * @return true if distance between them less than 5 meters and 5 minutes.
     */
    public boolean needToMigrate(Location location) {
        return distance(this.getLatitude(), this.getLongitude(), location.getLatitude(), location.getLongitude()) < 5.0
                && (timestampDifference(this.getTimestamp(), location.getTimestamp()) < 5);
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
        return Math.sqrt((lat1 - lat2) * (lat1 - lat2) + (lng1 - lng2) * (lng1 - lng2));
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
        return new Location(this.getId(), (this.getLatitude() + secondLocation.getLatitude()) / 2,
                (this.getLongitude() + secondLocation.getLongitude()) / 2);
    }
}