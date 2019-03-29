package com.spring.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class LocationDAOImpl implements LocationDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Location getLocationById(Long id) {
        String SQL_FIND_Location = "select * from location where id = ?";
        return jdbcTemplate.queryForObject(SQL_FIND_Location, new Object[] {id}, new LocationMapper());
    }

    public List<Location> getAllLocations() {
        String SQL_GET_ALL = "select * from location";
        return jdbcTemplate.query(SQL_GET_ALL, new LocationMapper());
    }

    public boolean createLocation(Location location) {
        String SQL_INSERT_Location = "insert into location(id, latitude, longitude) values(?,?,?)";
        return jdbcTemplate.update(SQL_INSERT_Location, location.getId(), location.getLatitude(), location.getLongitude()) > 0;
    }

    public boolean createLocationAutoId(Location location) {
        String SQL_INSERT_Location = "insert into location(latitude, longitude) values(?,?)";
        return jdbcTemplate.update(SQL_INSERT_Location, location.getLatitude(), location.getLongitude()) > 0;
    }

    public boolean updateLocation(Location location) {
        String SQL_UPDATE_Location = "update location set latitude = ?, longitude = ? where id = ?";
        return jdbcTemplate.update(SQL_UPDATE_Location, location.getLatitude(), location.getLongitude(), location.getId()) > 0;
    }

    public boolean deleteLocation(Location location) {
        String SQL_DELETE_Location = "delete from location where id = ?";
        return jdbcTemplate.update(SQL_DELETE_Location, location.getId()) > 0;
    }
}