package com.vishalgandla.carRental.Controller;

import com.vishalgandla.carRental.Dto.LoginUserDto;
import com.vishalgandla.carRental.Dto.RenterDto;
import com.vishalgandla.carRental.Service.RenterService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;


@RestController
@RequestMapping("/backend")
public class RenterController {

    @Autowired
    private RenterService renterService;


    @GetMapping("/renter")
    public String home() {
        return "Hello World";
    }

    @PostMapping("/renter/login")
    public ResponseEntity<HashMap<String,String>> login(@RequestBody LoginUserDto loginUserDto) {
        HashMap<String,String> returnValue = new HashMap<>();
        boolean loggedin=renterService.login(returnValue,loginUserDto);
        if(loggedin) {
            return new ResponseEntity<>(returnValue, HttpStatus.OK);
        }
        else{
            return ResponseEntity.status(401).body(returnValue);
        }
    }

    @PostMapping("/renter/signup")
    public HashMap<String,String> signup(@ModelAttribute RenterDto renterDto) {
        HashMap<String,String> returnValue = new HashMap<>();
        renterService.signup(returnValue,renterDto);
        return returnValue;
    }
}
