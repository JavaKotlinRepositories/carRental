package com.vishalgandla.carRental.Configuration;

import com.vishalgandla.carRental.Model.Customer;
import com.vishalgandla.carRental.Model.Renter;
import com.vishalgandla.carRental.Repository.CustomerRepository;
import com.vishalgandla.carRental.Repository.RenterRepository;
import com.vishalgandla.carRental.Service.CustomerJwtFilterService;
import com.vishalgandla.carRental.Service.RenterJwtFilterService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Service
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    RenterRepository renterRepository;


    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    RenterJwtFilterService renterJwtFilterService;

    @Autowired
    CustomerJwtFilterService customerJwtFilterService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        if(path.equals("/backend/renter/signup") ||   path.equals("/backend/renter/login") || path.equals("/backend/customer/login") || path.equals("/backend/customer/signup")) {
            filterChain.doFilter(request, response);
            return;
        }
        if(path.startsWith("/backend/protectedcustomer")){
            String token=request.getHeader("Authorization");
            if(token!=null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                String id=customerJwtFilterService.verifyToken(token);
                if(id!=null) {
                    Optional<Customer> cust=customerRepository.findById(Integer.parseInt(id));
                    if(cust.isPresent() && SecurityContextHolder.getContext().getAuthentication()==null) {
                        Customer authcustomer=cust.get();
                        CustomerDetails custDetails=new CustomerDetails(authcustomer);
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                custDetails,null, custDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        request.setAttribute("customer", authcustomer);
                    }
                }
            }
            filterChain.doFilter(request, response);
            return;
        }
        else{
            String token=request.getHeader("Authorization");
            if(token!=null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                String id=renterJwtFilterService.verifyToken(token);
                if(id!=null) {
                    Optional<Renter> renter=renterRepository.findById(Long.parseLong(id));
                    if(renter.isPresent() && SecurityContextHolder.getContext().getAuthentication()==null) {
                        Renter authrenter=renter.get();
                        RenterDetails renterDet=new RenterDetails(authrenter);
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                renterDet,null, renterDet.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        request.setAttribute("renter", authrenter);
                    }
                }
            }
            filterChain.doFilter(request, response);



        }

    }

}
