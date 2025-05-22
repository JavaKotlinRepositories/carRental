package com.vishalgandla.carRental.Controller;


import com.vishalgandla.carRental.Dto.BookingDto;
import com.vishalgandla.carRental.Model.Booking;
import com.vishalgandla.carRental.Service.BookingService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/backend")
public class BookingController {

    @Autowired
    private BookingService bookingService;


    @PostMapping("/protectedcustomer/booking/createabooking")
    public ResponseEntity<HashMap<String,Object>> createABooking(HttpServletRequest req, @RequestBody BookingDto bookingDto) {

        return bookingService.createABooking(req, bookingDto);
    }

    @PostMapping("/protectedcustomer/booking/getallbookings")
    public ResponseEntity<List<Booking>> getAllBookings(HttpServletRequest req, @RequestBody HashMap<String,Integer> map) {
        return bookingService.getAllBookings(req,map);
    }

}
