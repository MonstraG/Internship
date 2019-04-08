package com.spring.config;

import com.spring.db.Location.Location;
import com.spring.db.Location.LocationDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Random;
/**
 * Config for Spring MVC and Thymeleaf
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.spring"})
@EnableScheduling
public class WebMVCConfig implements WebMvcConfigurer {
    @Autowired
    LocationDAO locationDAO;

    private Location oldLoc = new Location("debug", 0.0, 1.0);
    private Location veryOldLoc = new Location("debug", 0.0, 0.0);

    @Autowired
    SimpMessagingTemplate template;

    public void updateEveryone(Location location) {
        this.template.convertAndSend("/location-updates/" + location.getKey(), location.toJSON());
    }

    @Bean
    @Scheduled(fixedRate=5000)
    public void sendRandomLocations() {
        Location loc = getNewRandomLocation();
        locationDAO.createLocation(loc);
        oldLoc = loc;
        updateEveryone(loc);

    }

    private Location getNewRandomLocation() {
        Random rand = new Random();
        double r = rand.nextDouble() * 2;
        double bearing = angleFromCoordinate(veryOldLoc.getLatitude(), veryOldLoc.getLongitude(), oldLoc.getLatitude(), oldLoc.getLongitude());
        double angle = rand.nextDouble() + bearing;
        return new Location("debug",
                oldLoc.getLatitude() + Math.sqrt(r) * Math.cos(angle),
                oldLoc.getLongitude() + Math.sqrt(r) * Math.sin(angle));
    }

    private double angleFromCoordinate(double lat1, double long1, double lat2, double long2) {
        double dLon = (long2 - long1);
        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(dLon);
        double bearing = Math.atan2(y, x);

        bearing = Math.toDegrees(bearing);
        bearing = (bearing + 360) % 360;
        bearing = 360 - bearing; // count degrees counter-clockwise - remove to make clockwise

        return bearing;
    }
}
