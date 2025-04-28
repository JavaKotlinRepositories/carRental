package com.vishalgandla.carRental.Configuration;

import com.vishalgandla.carRental.Model.Renter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class RenterDetails implements UserDetails {
    Renter renter;
    RenterDetails(Renter renter){
        this.renter = renter;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_RENTER"));
    }

    @Override
    public String getPassword() {
        return renter.getPassword();
    }

    @Override
    public String getUsername() {
        return renter.getEmail();
    }
}
