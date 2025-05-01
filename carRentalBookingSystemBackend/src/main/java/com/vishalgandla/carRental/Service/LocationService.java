package com.vishalgandla.carRental.Service;

import com.vishalgandla.carRental.Dto.LocationDto;
import com.vishalgandla.carRental.Model.Location;
import com.vishalgandla.carRental.Model.Renter;
import com.vishalgandla.carRental.Repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;
    public boolean createALocation(HashMap<String,String> ret, Renter renter, LocationDto locationDto) {
        if(locationDto.getName() == null || locationDto.getCity()==null || locationDto.getState()==null || locationDto.getCountry()==null || locationDto.getLongitude()==null || locationDto.getLatitude()==null || locationDto.getCarRentalPhoto()==null){
            ret.put("status", "please fill all the necessary fields");
            return false;
        }
        Location savedLocation = new Location();
        savedLocation.setLatitude(Float.parseFloat(locationDto.getLatitude()));
        savedLocation.setLongitude(Float.parseFloat(locationDto.getLongitude()));
        savedLocation.setCity(locationDto.getCity());
        savedLocation.setState(locationDto.getState());
        savedLocation.setCountry(locationDto.getCountry());
        savedLocation.setCarRentalPhoto(locationDto.getCarRentalPhoto().getName());
        savedLocation.setRenter(renter);
        savedLocation.setName(locationDto.getName());
        locationRepository.save(savedLocation);
        ret.put("message", "location created successfully");
        return true;
    }

    public void getAllLocations(ArrayList<Location> ret, Renter renter, int num1, int num2) {

    }
}
