package com.vishalgandla.carRental.Configuration;

import com.vishalgandla.carRental.Model.Renter;
import com.vishalgandla.carRental.Repository.RenterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class RenterDetailService implements UserDetailsService {
    @Autowired
    RenterRepository repo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Renter renter=repo.findByEmail(email);
        System.out.println(email);
        if(renter==null)
            throw new UsernameNotFoundException(email);
        return new RenterDetails(renter);
    }
}
