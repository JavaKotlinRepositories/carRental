package com.vishalgandla.carRental.Service;

import com.vishalgandla.carRental.Dto.CarSendDto;
import com.vishalgandla.carRental.Dto.LocationSendDto;
import com.vishalgandla.carRental.Dto.LocationSendDtoCustomer;
import com.vishalgandla.carRental.Model.Car;
import com.vishalgandla.carRental.Model.Customer;
import com.vishalgandla.carRental.Model.Location;
import com.vishalgandla.carRental.Repository.CarRepository;
import com.vishalgandla.carRental.Repository.LocationRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;

@Service
public class CustomerLocationService {

    @Autowired
    LocationRepository locationRepository;
    @Autowired
    CarRepository carRepository;

    @Autowired
    S3Service s3Service;



    @Value("${s3.carRentalLocationPictures.buckentName}")
    String carRentalLocationBucket;

    @Value("${s3.carRentalCarPictures.buckentName}")
    String carRentalCarPicturesBuckent;


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



        return new ResponseEntity<>(ret, HttpStatus.OK);
    }

    public ResponseEntity<HashMap<String,Object>> customerFetchCars(HttpServletRequest req, Map<String,String> numbers) {
        HashMap<String, Object> ret = new HashMap<>();
        ret.put("message", "No cars found.");
        Integer num1 = Integer.parseInt(numbers.get("num1"));
        Integer num2 = Integer.parseInt(numbers.get("num2"));
        Integer locationId = Integer.parseInt(numbers.get("locationId"));


        Optional<Location> locations=locationRepository.findById(locationId);
        if(locations.isEmpty()) {
            ret.put("message", "No location found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ret);
        }
        Location loc=locations.get();
        int size = num2 - num1 + 1;
        int pagenum = num1 / size;
        Pageable pageable = PageRequest.of(pagenum, size);
        Page<Car> page =carRepository.findByLocationOrderByCreatedAtDesc(loc,pageable);
        List<Car> cars = page.getContent();
        List<CarSendDto> carDtos=new ArrayList<>();

        for(Car car:cars){
            CarSendDto carDto=new CarSendDto();
            carDto.setId(car.getId());
            carDto.setMake(car.getMake());
            carDto.setModel(car.getModel());
            carDto.setYear(car.getYear());
            carDto.setPricePerDay(car.getPricePerDay());
            carDto.setLocationId(locationId);
            carDto.setPhoto(s3Service.getPresignedUrl(carRentalCarPicturesBuckent,car.getPhoto()));
            carDtos.add(carDto);
        }
        ret.put("cars", carDtos);


        return ResponseEntity.ok(ret);
    }


}
