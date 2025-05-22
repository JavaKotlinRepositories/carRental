package com.vishalgandla.carRental.Service;

import com.vishalgandla.carRental.Dto.LocationSendDto;
import com.vishalgandla.carRental.Dto.LocationSendDtoCustomer;
import com.vishalgandla.carRental.Model.Customer;
import com.vishalgandla.carRental.Model.Location;
import com.vishalgandla.carRental.Repository.LocationRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerLocationService {

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    S3Service s3Service;

    @Value("${s3.carRentalLocationPictures.buckentName}")
    String carRentalLocationBucket;

    public ResponseEntity<HashMap<String,Object>> customerFetchLocations(HttpServletRequest req, Map<String,String> latlong) {
        HashMap<String, Object> ret = new HashMap<>();

        try {
            double latitude = Double.parseDouble(latlong.get("latitude"));
            double longitude = Double.parseDouble(latlong.get("longitude"));

            System.out.println("Latitude: " + latitude);
            System.out.println("Longitude: " + longitude);

            // You can customize this based on how your repository works
            List<Location> nearbyLocations = locationRepository.findClosestLocations(latitude, longitude,3);



            if (nearbyLocations.size() == 0) {
                ret.put("message", "No nearby locations found.");
            } else {
                List<LocationSendDtoCustomer> locationSendDtos = new ArrayList<>();
                for (Location location : nearbyLocations) {
                    String url=s3Service.getPresignedUrl(carRentalLocationBucket,location.getCarRentalPhoto());
                    LocationSendDtoCustomer locationSendDto = new LocationSendDtoCustomer();
                    locationSendDto.setLatitude(location.getLatitude());
                    locationSendDto.setLongitude(location.getLongitude());
                    locationSendDto.setId(location.getId());
                    locationSendDto.setName(location.getName());
                    locationSendDto.setCity(location.getCity());
                    locationSendDto.setCarRentalPhoto(url);
                    locationSendDto.setState(location.getState());
                    locationSendDto.setCountry(location.getCountry());


                    final int R = 6371; // Earth's radius in kilometers

                    double lat2 = location.getLatitude();
                    double lon2 = location.getLongitude();

                    double dLat = Math.toRadians(lat2 - latitude);
                    double dLon = Math.toRadians(lon2 - longitude);

                    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                            Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(lat2)) *
                                    Math.sin(dLon / 2) * Math.sin(dLon / 2);

                    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                    float distance = (float) (R * c); // in kilometers
                    locationSendDto.setDistancesquared(distance * distance);
                    locationSendDto.setDistance(distance);
                    locationSendDtos.add(locationSendDto);

                }
                ret.put("results", locationSendDtos);
            }

        } catch (Exception e) {
            ret.put("error", "Invalid latitude/longitude or database error.");
        }

        Customer cust = (Customer) req.getAttribute("customer");
        // You can use 'cust' for logging or personalization if needed

        return new ResponseEntity<>(ret, HttpStatus.OK);
    }

}
