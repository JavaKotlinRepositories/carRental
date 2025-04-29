package com.vishalgandla.carRental.Service;

import com.vishalgandla.carRental.Dto.LoginUserDto;
import com.vishalgandla.carRental.Dto.RenterDto;
import com.vishalgandla.carRental.Model.Renter;
import com.vishalgandla.carRental.Repository.RenterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class RenterService {

    @Autowired
    RenterRepository renterRepository;
    @Autowired
    RenterJwtFilterService renterJwtFilterService;
    @Autowired
    S3Service s3Service;
    @Autowired
    RandomService randomService;

    @Value("${s3.carRentalProfilePictures.bucketName}")
    private String bucketName;
    BCryptPasswordEncoder bCryptPasswordEncoder;
    RenterService(){
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder(12);
    }


    public void signup(HashMap<String,String> returnValue, RenterDto renterDto){
        try{
            if(renterDto.getFirstName()==null || renterDto.getLastName()==null || renterDto.getEmail()==null|| renterDto.getPassword()==null || renterDto.getProfilePicture()==null || renterDto.getAddress()==null){
                returnValue.put("message","provide all the fields");
                return;
            }
            Renter existing=renterRepository.findByEmail(renterDto.getEmail());
            if(existing!=null){
                returnValue.put("message","Email Already Exists");
                return;
            }
            String randomFileName=randomService.generateRandomString(16);
            Renter renter = new Renter();
            renter.setFirstName(renterDto.getFirstName());
            renter.setLastName(renterDto.getLastName());
            renter.setEmail(renterDto.getEmail());
            renter.setPassword(bCryptPasswordEncoder.encode(renterDto.getPassword()));
            renter.setProfilePicture(randomFileName+".jpg");
            renter.setAddress(renterDto.getAddress());
            s3Service.uploadFile(bucketName,randomFileName+".jpg",renterDto.getProfilePicture());
            renter=renterRepository.save(renter);
            String profilepicture=s3Service.getPresignedUrl(bucketName,randomFileName+".jpg");
            returnValue.put("email",renterDto.getEmail());
            returnValue.put("token",renterJwtFilterService.createToken(""+renter.getId()));
            returnValue.put("profilePicture",profilepicture);
        }
        catch(Exception e){
            returnValue.put("message","something went wrong with server");
        }
    }



    public boolean login(HashMap<String,String> returnValue, LoginUserDto loginUserDto) {
        try{
            if(loginUserDto.getEmail()==null || loginUserDto.getPassword()==null){
                returnValue.put("message","provide all the fields");
                return false;
            }
            Renter existingRenter=renterRepository.findByEmail(loginUserDto.getEmail());
            if(existingRenter==null){
                returnValue.put("message","Email does not exist");
                return false;
            }
            boolean match=bCryptPasswordEncoder.matches(loginUserDto.getPassword(), existingRenter.getPassword());
            if(!match){
                returnValue.put("message","incorrect password");
                return false;
            }
            String profilepicture=s3Service.getPresignedUrl(bucketName,existingRenter.getProfilePicture());
            returnValue.put("email",existingRenter.getEmail());
            returnValue.put("token",renterJwtFilterService.createToken(""+existingRenter.getId()));
            returnValue.put("profilePicture",profilepicture);
            return true;
        }
        catch(Exception e){
            returnValue.put("message","something went wrong with server");
            return false;
        }
    }
}

