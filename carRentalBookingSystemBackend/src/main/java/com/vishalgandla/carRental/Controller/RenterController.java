package com.vishalgandla.carRental.Controller;

import com.vishalgandla.carRental.Dto.RenterDto;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/backend")
public class RenterController {

    @GetMapping("/renter")
    public String home() {
        return "Hello World";
    }

    @GetMapping("/renter/login")
    public String login() {
        System.out.println("login");
        return "login";
    }

    @GetMapping("/renter/signup")
    public String signup(@ModelAttribute RenterDto renterDto) {

        System.out.println("First Name: " + renterDto.getFirstName());
        System.out.println("Last Name: " + renterDto.getLastName());
        System.out.println("Email: " + renterDto.getEmail());
        System.out.println("Password: " + renterDto.getPassword());
        System.out.println("Profile Picture Name: " + renterDto.getProfilePicture().getOriginalFilename());


        return "signup";
    }
}
