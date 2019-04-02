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

    public Location getLocationById(Long id) {
        String SQL_FIND_LOCATION = "select * from locations where id = ?";
        return jdbcTemplate.queryForObject(SQL_FIND_LOCATION, new Object[]{id}, locationMapper);
    }

    public List<Location> getAllLocations() {
        String SQL_GET_ALL = "select * from locations";
        return jdbcTemplate.query(SQL_GET_ALL, locationMapper);
    }

    public boolean createLocation(Location location) {
        String SQL_INSERT_LOCATION = "insert into locations(id, latitude, longitude, ts) values(?,?,?,?)";
        return jdbcTemplate.update(SQL_INSERT_LOCATION, location.getId(), location.getLatitude(), location.getLongitude(), location.getTimestamp()) > 0;
    }

    public boolean createLocationAutoId(Location location) {
        String SQL_INSERT_LOCATION = "insert into locations(latitude, longitude) values(?,?)";
        return jdbcTemplate.update(SQL_INSERT_LOCATION, location.getLatitude(), location.getLongitude()) > 0;
    }

    public boolean updateLocation(Location location) {
        String SQL_UPDATE_LOCATION = "update locations set latitude = ?, longitude = ? where id = ?";
        return jdbcTemplate.update(SQL_UPDATE_LOCATION, location.getLatitude(), location.getLongitude(), location.getId()) > 0;
    }

    public boolean deleteLocation(Location location) {
        String SQL_DELETE_LOCATION = "delete from locations where id = ?";
        return jdbcTemplate.update(SQL_DELETE_LOCATION, location.getId()) > 0;
    }

    public Location getLastLocation() {
        String SQL_GET_LAST_LOCATION = "select * from locations order by ts desc nulls last limit 1";
        return jdbcTemplate.query(SQL_GET_LAST_LOCATION, new ResultSetExtractor<Location>() {
            public Location extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                if (resultSet.next())
                    return locationMapper.mapRow(resultSet, 1);
                return null;
            }
        });
    }
}