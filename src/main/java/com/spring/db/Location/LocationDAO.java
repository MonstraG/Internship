package com.spring.db.Location;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@Transactional
public class LocationDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private LocationMapper locationMapper = new LocationMapper();

    public List<Location> getAllLocations() {
        String SQL_GET_ALL = "select * from locations";
        return jdbcTemplate.query(SQL_GET_ALL, locationMapper);
    }

    public List<Location> getAllLocationsByKey(String key) {
        String SQL_GET_ALL = "select * from locations where key = ?";
        return jdbcTemplate.query(SQL_GET_ALL, new Object[]{key}, locationMapper);
    }

    public void createLocation(Location location) {
        String SQL_INSERT_LOCATION = "insert into locations(key, latitude, longitude, ts) values(?,?,?,?)";
        jdbcTemplate.update(SQL_INSERT_LOCATION,
                location.getKey(), location.getLatitude(), location.getLongitude(), location.getTimestamp());
    }

    public void updateLocation(Location location) {
        String SQL_UPDATE_LOCATION = "update locations set key = ?, latitude = ?, longitude = ?, ts = ? where id = ?";
        jdbcTemplate.update(SQL_UPDATE_LOCATION,
                location.getKey(), location.getLatitude(), location.getLongitude(), location.getTimestamp(),
                location.getId());
    }

    public Location getLastLocation(String key) {
        String SQL_GET_LAST_LOCATION = "select * from locations where key = ? order by ts desc limit 1";
        return jdbcTemplate.query(SQL_GET_LAST_LOCATION, new String[]{key}, new ResultSetExtractor<Location>() {
            public Location extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                if (resultSet.next())
                    return locationMapper.mapRow(resultSet, 1);
                return null;
            }
        });
    }

    public List<Location> getLastNofLocations(String key, Long number) {
        String SQL_GET_ALL = "select * from locations where key = ? order by ts desc limit ?";
        return jdbcTemplate.query(SQL_GET_ALL, new Object[]{key, number}, locationMapper);
    }
}