package com.vishalgandla.carRental.Service;

import com.vishalgandla.carRental.Dto.BookingDto;
import com.vishalgandla.carRental.Model.Booking;
import com.vishalgandla.carRental.Model.Car;
import com.vishalgandla.carRental.Model.Customer;
import com.vishalgandla.carRental.Repository.BookingRepository;
import com.vishalgandla.carRental.Repository.CarRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
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
    private String buckentName;
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
         String url=s3Service.getPresignedUrl(buckentName,car.getPhoto());
         car.setPhoto(url);
        response.put("booking", booking);
        response.put("car", car);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    catch(Exception e){
            System.out.println("=>"+e.getMessage());
            if(e.getMessage().contains("duplicate key value violates unique")) {
                response.put("message", "date has already been booked choose a different date");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        response.put("message", "something went wrong with your request");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    }
}
