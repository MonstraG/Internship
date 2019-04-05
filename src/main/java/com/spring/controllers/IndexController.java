package com.spring.controllers;

import com.google.gson.Gson;
import com.spring.db.Key.Key;
import com.spring.db.Key.KeyDAO;
import com.spring.db.Location.Location;
import com.spring.db.Location.LocationDAO;
import com.spring.db.User.UserDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Controller
@RequestMapping("/*")
class IndexController {

    @Autowired
    LocationDAO locationDAO;
    @Autowired
    KeyDAO keyDAO;
    @Autowired
    UserDAO userDAO;

    /**
     * Handles POST requests on /location
     */
    @RequestMapping(value = "/location", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity locationPost(@RequestBody Location payload, @RequestHeader Map<String, String> header) {
        Location location;
        String key = header.get("key");
        try {
            location = new Location(key, payload.getLatitude(), payload.getLongitude()); //adds timestamp.
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not find latitude or longitude data.");
        }

        //getting last location from table
        Location oldLocation;
        try { oldLocation = locationDAO.getLastLocation(location); }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Following exception was thrown when trying to get last item from table:\n" + e.toString());
        }

        if ((oldLocation != null) && location.needToMigrate(oldLocation)) {
            //updating last location
            Location updatedLocation = oldLocation.getAverageLocation(location);
            try {
                locationDAO.updateLocation(updatedLocation);
            }
            catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Following exception was thrown when trying to update table item:\n" + e.toString());
            }
        } else {
            //inserting new location into the table
            try {
                locationDAO.createLocation(location);
                updateEveryone(location);
            }
            catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Following exception was thrown when trying to add item to the table:\n" + e.toString());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @RequestMapping(value = "/location/{key}", method = RequestMethod.GET)
    @ResponseBody
    public List<Location> locationGet(@PathVariable String key) {
        return locationDAO.getAllLocationsByKey(key);
    }

    @RequestMapping(value = "/location", method = RequestMethod.GET)
    @ResponseBody
    public List<Location> locationGet() {
        return locationDAO.getAllLocations();
    }

    /**
     * Handles POST requests on /install:
     */
    @RequestMapping(value = "/install", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity installKey(@RequestBody Key key) {
        if (key.getUsername() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username was not provided.");
        }

        if (key.getKey() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Key was not provided.");
        }

        try {
            if (userDAO.userExists(key.getUsername())) {
                keyDAO.createKey(key);
            }
            else
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Provided user does not exist.");
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Following exception was thrown when trying to add item to the table:\n" + e.toString());
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @RequestMapping(value = "/ui/index", method = RequestMethod.GET)
    public ModelAndView uiIndexGet() {
        return new ModelAndView("ui/index");
    }

    @RequestMapping(value = "/login")
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    @RequestMapping(value = "/username", method = RequestMethod.GET, produces="text/plain")
    @ResponseBody
    public String currentUserName(Authentication authentication) {
        return authentication.getName();
    }

    @RequestMapping(value = "/keys/{username}", method = RequestMethod.GET)
    @ResponseBody
    public List<Key> keysGet(@PathVariable String username) {
        return keyDAO.getAllKeysByUsername(username);
    }

    @Autowired
    SimpMessagingTemplate template;

    public void updateEveryone(Location location) {
        this.template.convertAndSend("/location-updates/" + location.getKey(), location.toJSON());
    }
}



