package com.spring.controllers;

import com.google.gson.Gson;
import com.spring.db.Key.Key;
import com.spring.db.Key.KeyDAO;
import com.spring.db.Location.Location;
import com.spring.db.Location.LocationDAO;
import com.spring.db.User.UserDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/*")
class IndexController {

    @Autowired
    LocationDAO locationDAO;
    @Autowired
    KeyDAO keyDAO;
    @Autowired
    UserDAO userDAO;

    private Gson gson = new Gson();

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
            try { locationDAO.createLocation(location); }
            catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Following exception was thrown when trying to add item to the table:\n" + e.toString());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @RequestMapping(value = "/location/*", method = RequestMethod.GET)
    @ResponseBody
    public String locationGet(HttpServletRequest request) {
        return gson.toJson(locationDAO.getAllLocationsByKey(request.getRequestURI().split("/location/")[1]));
    }

    //TODO: ADD SOMETHING TO SHOW ALL BY USER.

    @RequestMapping(value = "/location", method = RequestMethod.GET)
    @ResponseBody
    public String locationGet() {
        return gson.toJson(locationDAO.getAllLocations());
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

    @RequestMapping(value = "/username", method = RequestMethod.GET)
    @ResponseBody
    public String currentUserName(Authentication authentication) {
        return new Gson().toJson(authentication.getName());
    }

    @RequestMapping(value = "/keys", method = RequestMethod.POST)
    @ResponseBody
    public String keysGet(@RequestBody String username) {
        return gson.toJson(keyDAO.getAllKeysByUsername(username));
    }}

