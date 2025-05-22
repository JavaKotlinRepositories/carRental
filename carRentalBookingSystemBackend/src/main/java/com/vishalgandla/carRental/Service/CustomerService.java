package com.vishalgandla.carRental.Service;

import com.vishalgandla.carRental.Dto.CustomerDto;
import com.vishalgandla.carRental.Dto.LoginUserDto;
import com.vishalgandla.carRental.Model.Customer;
import com.vishalgandla.carRental.Repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;

@Service
public class CustomerService {
    BCryptPasswordEncoder encoder ;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    S3Service s3Service;
    @Autowired
    RandomService randomService;
    @Autowired
    CustomerJwtFilterService customerJwtFilterService;
    @Value("${s3.carRentalCustomerPictures.buckentName}")
    private String customerProfilePictureBucket;

    CustomerService(){
        encoder = new BCryptPasswordEncoder(12);
    }
    public ResponseEntity<HashMap<String,String>> signup(CustomerDto customerDto) {
    HashMap<String,String> ret = new HashMap<>();
    try{
        Customer existing=customerRepository.findByEmail(customerDto.getEmail());
        if(existing!=null){
            ret.put("message", "Email already in use");
            return new ResponseEntity<>(ret, HttpStatus.CONFLICT);
        }
        String profilepicname=randomService.generateRandomString(16)+".jpg";
        s3Service.uploadFile(customerProfilePictureBucket,profilepicname,customerDto.getProfilePicture());
        Customer cust=new Customer();
        cust.setFirstName(customerDto.getFirstName());
        cust.setLastName(customerDto.getLastName());
        cust.setEmail(customerDto.getEmail());
        cust.setPhoneNumber(customerDto.getPhoneNumber());
        cust.setAddress(customerDto.getAddress());
        cust.setPassword(encoder.encode(customerDto.getPassword()));
        cust.setProfilePicture(profilepicname);
        cust=customerRepository.save(cust);
        String url=s3Service.getPresignedUrl(customerProfilePictureBucket,profilepicname);
        ret.put("email", cust.getEmail());
        ret.put("profilePicture",url);
        ret.put("token", customerJwtFilterService.createToken(""+cust.getId()));
        System.out.println(customerDto.getFirstName());
        return ResponseEntity.ok(ret);
    }
    catch(Exception e){
        e.printStackTrace();
        ret.put("status", "something went wrong with you request");
        return ResponseEntity.status(500).body(ret);
    }
    }


    public ResponseEntity<HashMap<String,String>> login( LoginUserDto loginUserDto) {
    HashMap<String,String> ret = new HashMap<>();
    try{
        Customer cust=customerRepository.findByEmail(loginUserDto.getEmail());
        if(cust==null){
            ret.put("message", "customer doest not exist");
            return ResponseEntity.status(403).body(ret);
        }
        boolean ismatch=encoder.matches(loginUserDto.getPassword(),cust.getPassword());
        if(!ismatch){
            ret.put("message", "authentication failed");
            return ResponseEntity.status(403).body(ret);
        }
        String url=s3Service.getPresignedUrl(customerProfilePictureBucket,cust.getProfilePicture());
        ret.put("email", cust.getEmail());
        ret.put("token", customerJwtFilterService.createToken(""+cust.getId()));
        ret.put("profilePicture",url );
        return new ResponseEntity<>(ret, HttpStatus.OK);
    }
    catch(Exception e){
        e.printStackTrace();
        ret.put("message", "something went wrong with you request");
        return ResponseEntity.status(500).body(ret);
    }
    }

}
