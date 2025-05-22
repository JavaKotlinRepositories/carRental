package com.vishalgandla.carRental.Controller;


import com.vishalgandla.carRental.Model.Customer;
import com.vishalgandla.carRental.Service.CustomerLocationService;
import com.vishalgandla.carRental.Service.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/backend")
public class CustomerLocationController {

    @Autowired
    private CustomerLocationService customerLocationService;

    @PostMapping("/protectedcustomer/customerhome/fetchlocations")
    public ResponseEntity<HashMap<String,Object>> customerFetchLocations(HttpServletRequest req, @RequestBody Map<String,String> latlong) {
        return customerLocationService.customerFetchLocations(req, latlong);
    }

}
