package com.vishalgandla.carRental.Service;

import com.vishalgandla.carRental.Dto.LocationDto;
import com.vishalgandla.carRental.Dto.LocationSendDto;
import com.vishalgandla.carRental.Model.Location;
import com.vishalgandla.carRental.Model.Renter;
import com.vishalgandla.carRental.Repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private S3Service s3Service;
    @Value("${s3.carRentalLocationPictures.buckentName}")
    private String locationPictureBuckent;
    @Autowired
    private RandomService randomService;
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
        String photoRandomGeneratedName=randomService.generateRandomString(16)+".jpg";
        savedLocation.setCarRentalPhoto(photoRandomGeneratedName);
        savedLocation.setRenter(renter);
        savedLocation.setName(locationDto.getName());
        s3Service.uploadFile(locationPictureBuckent,photoRandomGeneratedName,locationDto.getCarRentalPhoto());
        locationRepository.save(savedLocation);
        ret.put("message", "location created successfully");
        return true;
    }

    public List<LocationSendDto>  getAllLocations(Renter renter, int num1, int num2) {
        System.out.println(num1+" "+num2);
        Pageable pageable = PageRequest.of(num1, num2); // e.g., page 0, size 10
        Page<Location> page = locationRepository.findByRenterOrderByCreatedDateDesc(renter, pageable);
        List<Location> locations = page.getContent();
        List<LocationSendDto> ret= new ArrayList<>();
        for (Location location : locations) {
            LocationSendDto temp=new LocationSendDto();
            temp.setId(location.getId());temp.setName(location.getName());
            temp.setCity(location.getCity());temp.setState(location.getState());
            temp.setCountry(location.getCountry());temp.setCarRentalPhoto(s3Service.getPresignedUrl(locationPictureBuckent,location.getCarRentalPhoto()));
            temp.setLatitude(location.getLatitude());temp.setLongitude(location.getLongitude());
            temp.setCarRentalId(location.getRenter().getId());temp.setCreatedAt(location.getCreatedDate());
            temp.setUpdatedAt(location.getCreatedDate());
            ret.add(temp);
        }
        return ret;
    }

    @Transactional
    public int deleteLocation(Renter renter, Integer id) {
        List<Location> l=locationRepository.findByRenterAndId(renter,id);
        for(Location location:l){
        s3Service.deleteFile(locationPictureBuckent,location.getCarRentalPhoto());
        }
        return  locationRepository.deleteByRenterAndId(renter,id);
    }

}
