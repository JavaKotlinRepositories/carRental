package com.vishalgandla.carRental.Service;

import com.vishalgandla.carRental.Dto.BookingDto;
import com.vishalgandla.carRental.Model.Booking;
import com.vishalgandla.carRental.Model.Car;
import com.vishalgandla.carRental.Model.Customer;
import com.vishalgandla.carRental.Model.Location;
import com.vishalgandla.carRental.Repository.BookingRepository;
import com.vishalgandla.carRental.Repository.CarRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private CarRepository  carRepository;
    @Autowired
    private S3Service s3Service;
    @Value("${s3.carRentalCarPictures.buckentName}")
    private String carPicturesBuckentName;
    public ResponseEntity<HashMap<String,Object>> createABooking(HttpServletRequest req,  BookingDto bookingDto) {
        HashMap<String,Object> response = new HashMap<>();

        try{

        Customer cust=(Customer) req.getAttribute("customer");
        if(cust==null){
            response.put("message", "Customer not found");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        Optional<Car> caropt=carRepository.findById(bookingDto.getCarId());
        if(!caropt.isPresent()) {
            response.put("status", "car does not exist");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        Booking booking = new Booking();
        Car car=caropt.get();
        booking.setBookingDate(bookingDto.getDate());
        booking.setCar(car);
        booking.setCardCVV(bookingDto.getCardCVV());
        booking.setCustomer(cust);
        booking.setCardHolderName(bookingDto.getCardHolderName());
        booking.setCardNumber(bookingDto.getCardNumber());
        booking.setCardType(bookingDto.getCardType());
         booking=bookingRepository.save(booking);
         booking.setCustomer(null);
         booking.setCar(null);
         car.setLocation(null);
         String url=s3Service.getPresignedUrl(carPicturesBuckentName,car.getPhoto());
         car.setPhoto(url);
        response.put("booking", booking);
        response.put("car", car);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    catch(Exception e){
            if(e.getMessage().contains("duplicate key value violates unique")) {
                response.put("message", "date has already been booked choose a different date");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        response.put("message", "something went wrong with your request");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    }





    public ResponseEntity<List<Booking>> getAllBookings(HttpServletRequest req, HashMap<String,Integer> map) {
        List<Booking> response = new ArrayList<>();
        try{
            Customer cust=(Customer) req.getAttribute("customer");
            if(cust==null){
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            int num1=map.get("num1");
            int num2=map.get("num2");
            int size = num2 - num1 + 1;
            int pagenum = num1 / size;

            Pageable pageable = PageRequest.of(pagenum, size);
            Page<Booking> page=bookingRepository.findByCustomerOrderByBookingDateDescIdDesc(cust,pageable);
            List<Booking> bookings = page.getContent();
            for(Booking booking:bookings){
                booking.setCustomer(null);
                Car car=booking.getCar();
                Location loc=car.getLocation();
                loc.setRenter(null);
                loc.setCarRentalPhoto(null);
                car.setLocation(loc);


                if(!booking.getCar().getPhoto().startsWith("https:")){
                    String url=s3Service.getPresignedUrl(carPicturesBuckentName,car.getPhoto());

                    car.setPhoto(url);
                }

                booking.setCar(car);
            }
            return new ResponseEntity<>(bookings, HttpStatus.OK);
        }
       catch(Exception e){
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
       }

    }
    public ResponseEntity<HashMap<String,Object>> dateDisable(HttpServletRequest req, @RequestBody HashMap<String,Integer> map) {
        HashMap<String,Object> ret=new HashMap<>();
        try{
            Integer carId=map.get("carId");
            Customer cust=(Customer) req.getAttribute("customer");
            if(cust==null){
                ret.put("message", "Customer not found");
                return new ResponseEntity<>(ret, HttpStatus.BAD_REQUEST);
            }
            List<Object> date=bookingRepository.findBookingDatesByCarId(carId);
            ret.put("ret", date);
        return new ResponseEntity<>(ret, HttpStatus.OK);
        }
        catch(Exception e){
            ret.put("message", "something went wrong with your request");
            return new ResponseEntity<>(ret, HttpStatus.BAD_REQUEST);
        }
    }


    }
