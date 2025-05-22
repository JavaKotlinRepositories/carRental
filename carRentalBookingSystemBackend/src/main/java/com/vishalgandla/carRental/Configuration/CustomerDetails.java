package com.vishalgandla.carRental.Configuration;

import com.vishalgandla.carRental.Model.Customer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CustomerDetails implements UserDetails {
    Customer customer;
    CustomerDetails(Customer customer) {
        this.customer = customer;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_CUSTOMER"));

    }

    @Override
    public String getPassword() {
        return customer.getPassword();
    }

    @Override
    public String getUsername() {
        return customer.getEmail();
    }
}
