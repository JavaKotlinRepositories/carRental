package com.vishalgandla.carRental.Controller;

import com.vishalgandla.carRental.Dto.LocationDto;
import com.vishalgandla.carRental.Model.Location;
import com.vishalgandla.carRental.Model.Renter;
import com.vishalgandla.carRental.Service.LocationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/backend/protected/renterhome")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @PostMapping("/createlocation")
    public ResponseEntity<HashMap<String,String>> createLocation(HttpServletRequest request,@ModelAttribute LocationDto locationDto) {
        HashMap<String,String> ret;
        try{
            ret=new HashMap<>();
            Renter renter=(Renter) request.getAttribute("renter");
            if(renter==null)
            {
                ret.put("status","please send a valid token to create a location");
                return ResponseEntity.status(403).body(ret);
            }
            else{
                boolean locationcreated=locationService.createALocation(ret,renter,locationDto);
                if(locationcreated){
                    return ResponseEntity.status(200).body(ret);
                }
                else{
                    return ResponseEntity.status(403).body(ret);
                }
            }
        }
        catch(Exception e){
            ret=new HashMap<>();
            ret.put("message","Error creating location");
            ret.put("status",e.getMessage());
            return ResponseEntity.status(403).body(ret);
        }
    }
    @PostMapping("/getalllocations")
    public ResponseEntity<List<Location>> getAllLocations(HttpServletRequest request,@RequestBody Map<String, Integer> numbers) {
        try{
            ArrayList<Location> ret=new ArrayList<>();
            int num1 = numbers.get("num1");
            int num2 = numbers.get("num2");
            Renter renter=(Renter) request.getAttribute("renter");
            if(renter==null)
            {
                return ResponseEntity.status(403).body(new ArrayList<Location>());
            }
            locationService.getAllLocations(ret,renter,num1,num2);
            return ResponseEntity.status(200).body(ret);
        }
        catch (Exception e){
            return ResponseEntity.status(403).body(new ArrayList<Location>());
        }

    }
}

