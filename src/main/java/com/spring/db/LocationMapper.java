package com.spring.db;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LocationMapper implements RowMapper<Location> {

    public Location mapRow(ResultSet resultSet, int i) throws SQLException {
        Location Location = new Location();
        Location.setId(resultSet.getLong("id"));
        Location.setLatitude(resultSet.getDouble("latitude"));
        Location.setLongitude(resultSet.getDouble("longitude"));
        Location.setTimestamp(resultSet.getTimestamp("ts"));
        return Location;
    }
}