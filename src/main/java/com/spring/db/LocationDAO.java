package com.spring.db;

import java.util.List;

public interface LocationDAO {
    Location getLocationById(Long id);

    List<Location> getAllLocations();

    boolean createLocation(Location location);

    boolean updateLocation(Location location);

    boolean deleteLocation(Location location);

    boolean createLocationAutoId(Location location);

}
