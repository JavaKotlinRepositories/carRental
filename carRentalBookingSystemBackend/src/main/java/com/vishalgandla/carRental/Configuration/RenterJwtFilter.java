package com.vishalgandla.carRental.Configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.annotations.Filter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Service
public class RenterJwtFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        if(path.equals("/backend/renter/signup") ||   path.equals("/backend/renter/login")) {
            filterChain.doFilter(request, response);
            return;
        }
        String token=request.getHeader("Authorization");
        System.out.println("token==="+" "+token);

        if(token!=null && token.startsWith("Bearer ")) {
            System.out.println(token);
        }

        filterChain.doFilter(request, response);
    }
}
