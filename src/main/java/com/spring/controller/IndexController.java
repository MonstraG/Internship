package com.spring.controller;

import com.google.gson.Gson;
import com.spring.db.Location;
import com.spring.db.LocationDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/*")
class IndexController {

    @Autowired
    LocationDAO locationDAO;

    /**
     * Handles POST requests on /location:
     * tries to add new item to database if payload of the request looks like
     * {
     *     latitude: Double,
     *     longitude: Double
     * }
     * or returns exception as response body if something went wrong.
     * @param payload payload of POST request
     * @return response (with set body) which will be sent to requester.
     */
    @RequestMapping(value = "/location", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity locationPost(@RequestBody Map<String, String> payload) {
        Location location;

        //creating location object
        try {
            location = new Location(Double.parseDouble(payload.get("latitude")),
                    Double.parseDouble(payload.get("longitude")));
        } catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Following exception was thrown when trying to create new Location:\n" + e.toString());
        }

        //inserting it into the table
        try {
            locationDAO.createLocationAutoId(location);
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Following exception was thrown when trying to add item to the table:\n" + e.toString());
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
     * Responds with 404 for everything else.
     */
    @RequestMapping(value = "/")
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public void notFound() { }
}

