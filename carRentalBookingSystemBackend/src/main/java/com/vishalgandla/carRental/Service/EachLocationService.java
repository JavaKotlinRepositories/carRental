package com.vishalgandla.carRental.Service;

import com.vishalgandla.carRental.Dto.CarDto;
import com.vishalgandla.carRental.Dto.CarSendDto;
import com.vishalgandla.carRental.Model.Car;
import com.vishalgandla.carRental.Model.Location;
import com.vishalgandla.carRental.Model.Renter;
import com.vishalgandla.carRental.Repository.CarRepository;
import com.vishalgandla.carRental.Repository.LocationRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EachLocationService {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private S3Service s3Service;

    @Value("${s3.carRentalCarPictures.buckentName}")
    private String carRentalCarPicturesBuckent;
    @Autowired
    private RandomService randomService;

    public ResponseEntity<HashMap<String,String>> createLocationCars(HttpServletRequest request, CarDto carDto) {
        HashMap<String,String> ret = new HashMap<>();

        try{
            Renter renter=(Renter) request.getAttribute("renter");
            if(renter==null)
            {
                ret.put("message","please send a valid token to create a car");
                return ResponseEntity.status(403).body(ret);
            }
            else {
                List<Location> locations=locationRepository.findByRenterAndId(renter,carDto.getLocationId());

                if(locations.size()==0){
                    ret.put("message","please send a valid location");
                    return ResponseEntity.status(403).body(ret);
                }
                Location location=locations.get(0);
                String photoRandomGeneratedName=randomService.generateRandomString(16)+".jpg";
                s3Service.uploadFile(carRentalCarPicturesBuckent,photoRandomGeneratedName,carDto.getPhoto());
                Car car=new Car();
                car.setLocation(location);
                car.setMake(carDto.getMake());
                car.setModel(carDto.getModel());
                car.setYear(carDto.getYear());
                car.setPricePerDay(carDto.getPricePerDay());
                car.setPhoto(photoRandomGeneratedName);
                car=carRepository.save(car);
                ret.put("id", String.valueOf(car.getId()));
                ret.put("locationId", String.valueOf(car.getLocation().getId()));
                ret.put("make", car.getMake());
                ret.put("model", car.getModel());
                ret.put("year", String.valueOf(car.getYear()));
                ret.put("pricePerDay", String.valueOf(car.getPricePerDay()));
                ret.put("photo", s3Service.getPresignedUrl(carRentalCarPicturesBuckent,photoRandomGeneratedName));
                ret.put("createdAt", car.getCreatedAt().toString());
                ret.put("updatedAt", car.getUpdatedAt().toString());
                return ResponseEntity.status(200).body(ret);
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            ret.put("message","something went wrong with the request");
            return ResponseEntity.status(500).body(ret);
        }

    }

    public ResponseEntity<HashMap<String, Object>> getLocationCars(HttpServletRequest request, Map<String, Integer> numbers) {
        HashMap<String,Object> ret = new HashMap<>();
        try{
            Renter renter=(Renter) request.getAttribute("renter");
            if(renter==null)
            {
                ret.put("message","please send a valid token to create a car");
                return ResponseEntity.status(403).body(ret);
            }
            int num1 = numbers.get("num1");
            int num2 = numbers.get("num2");
            int locationId = numbers.get("locationId");
            System.out.println(num1+" "+num2+" "+locationId);
            ret.put("locationId", locationId);
            List<Location> locations=locationRepository.findByRenterAndId(renter,locationId);
            int size = num2 - num1 + 1;
            int pagenum = num1 / size;
            Pageable pageable = PageRequest.of(pagenum, size);
            Page<Car> page =carRepository.findByLocationOrderByCreatedAtDesc(locations.get(0),pageable);
            List<Car> cars = page.getContent();
            List<CarSendDto> carDtos=new ArrayList<>();
            System.out.println(cars.size());
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
            return ResponseEntity.status(200).body(ret);
        }
        catch(Exception e){
            ret.put("message","something went wrong with the request");
            return ResponseEntity.status(500).body(ret);
        }
    }



}
