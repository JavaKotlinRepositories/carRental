package com.vishalgandla.carRental.Controller;


import com.vishalgandla.carRental.Dto.CarDto;
import com.vishalgandla.carRental.Dto.LocationDto;
import com.vishalgandla.carRental.Model.Location;
import com.vishalgandla.carRental.Model.Renter;
import com.vishalgandla.carRental.Service.EachLocationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/backend/protected/rentereachlocationcars")
public class EachLocationController {

    @Autowired
    EachLocationService eachLocationService;

    @PostMapping("/createlocationcars")
    public ResponseEntity<HashMap<String,String>> createLocationCars(HttpServletRequest request, @ModelAttribute CarDto carDto) {
    return eachLocationService.createLocationCars(request, carDto);
    }

    @PostMapping("/getLocationcars")
    public ResponseEntity<HashMap<String,Object>> getLocationCars(HttpServletRequest request, @RequestBody Map<String, Integer> numbers) {


        return eachLocationService.getLocationCars(request, numbers);
    }


}
