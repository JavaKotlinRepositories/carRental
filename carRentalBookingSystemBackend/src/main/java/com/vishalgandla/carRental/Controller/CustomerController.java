package com.vishalgandla.carRental.Controller;

import com.vishalgandla.carRental.Dto.CustomerDto;
import com.vishalgandla.carRental.Dto.LoginUserDto;
import com.vishalgandla.carRental.Dto.RenterDto;
import com.vishalgandla.carRental.Model.Customer;
import com.vishalgandla.carRental.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/backend")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @PostMapping("/customer/signup")
    public ResponseEntity<HashMap<String,String>> signup(@ModelAttribute CustomerDto customerDto) {
        return customerService.signup( customerDto );
    }

    @PostMapping("/customer/login")
    public ResponseEntity<HashMap<String,String>> login(@RequestBody LoginUserDto loginUserDto) {
        return customerService.login( loginUserDto );
    }
}
