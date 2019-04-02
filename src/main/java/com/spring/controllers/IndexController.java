package com.spring.controllers;

import com.google.gson.Gson;
import com.spring.db.Key.Key;
import com.spring.db.Key.KeyDAO;
import com.spring.db.Location.Location;
import com.spring.db.Location.LocationDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import sun.text.normalizer.NormalizerBase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;

@Controller
@RequestMapping("/*")
class IndexController {

    @Autowired
    LocationDAO locationDAO;
    @Autowired
    KeyDAO keyDAO;
    private Gson gson = new Gson();

    /**
     * Handles POST requests on /location
     * @param payload payload of POST request
     * @return response (with set body) which will be sent to requester.
     */
    @RequestMapping(value = "/location", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity locationPost(@RequestBody Location payload) {
        Location location;
        try {
            location = new Location(payload.getLatitude(), payload.getLongitude()); //needed to add timestamp
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not find latitude or longitude data.");
        }

        //getting last location from table
        Location oldLocation;
        try { oldLocation = locationDAO.getLastLocation(); }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Following exception was thrown when trying to get last item from table:\n" + e.toString());
        }

        if ((oldLocation != null) && location.needToMigrate(oldLocation)) {
            //updating last location
            Location updatedLocation = oldLocation.getAverageLocation(location);
            try { locationDAO.updateLocation(updatedLocation); }
            catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Following exception was thrown when trying to update table item:\n" + e.toString());
            }
        } else {
            //inserting new location into the table
            try { locationDAO.createLocationAutoId(location); }
            catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Following exception was thrown when trying to add item to the table:\n" + e.toString());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    /**
     * Handles GET requests on /location:
     * returns full locations table as JSON (via Gson) body in response.
     * @return response with JSON of locations table.
     */
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
        if (key.getKey() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Key was not received.");
        }

        try { keyDAO.createKey(key); }
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
}

