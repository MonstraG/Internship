package com.spring.controller;

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

@Controller
@RequestMapping("/*")
class IndexController {

    @Autowired
    LocationDAO locationDAO;
    @Autowired
    KeyDAO keyDAO;

    /**
     * Handles POST requests on /location
     * @param payload payload of POST request
     * @return response (with set body) which will be sent to requester.
     */
    @RequestMapping(value = "/location", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity locationPost(@RequestBody Location payload) {
        Location location;
        Location oldLocation;
        //creating location object
        try { location = new Location(payload.getLatitude(), payload.getLongitude()); } //FIXME
        catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Following exception was thrown when trying to create new Location:\n" + e.toString());
        }

        //getting last location from table
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
     * returns full locations table as JSON (via GSON) body in response.
     * @return response with JSON of locations table.
     */
    @RequestMapping(value = "/location", method = RequestMethod.GET)
    @ResponseBody
    public String locationGet() {
        return new Gson().toJson(locationDAO.getAllLocations());
    }

    /**
     * Handles POST requests on /install:
     */
    @RequestMapping(value = "/install", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity installKey(@RequestBody Key payload) {
        Key key;
        //creating key object
        try { key = new Key(payload.getKey()); } //FIXME
        catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Following exception was thrown when trying to create new Location:\n" + e.toString());
        }

        try { keyDAO.createKey(key); }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Following exception was thrown when trying to add item to the table:\n" + e.toString());
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }


    /**
     * Responds with 404 for everything else.
     */
    @RequestMapping(value = "/")
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public void notFound() { }
}

