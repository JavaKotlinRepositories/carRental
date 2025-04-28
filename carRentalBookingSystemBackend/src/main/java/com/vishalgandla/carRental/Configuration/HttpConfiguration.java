package com.vishalgandla.carRental.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.security.AuthProvider;

@Configuration
@EnableWebSecurity
public class HttpConfiguration {
    @Autowired
    RenterDetailService renterDetailService;

    @Autowired
    RenterJwtFilter renterJwtFilter;

    @Bean
    SecurityFilterChain httpConfig(HttpSecurity http) throws Exception {
        http.csrf(customizer->customizer.disable());
        http.formLogin(customizer->customizer.disable());
        http.httpBasic(Customizer->Customizer.disable());
        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests.requestMatchers("/backend/renter/login","/backend/renter/signup").permitAll().anyRequest().authenticated());
        http.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(renterJwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

//    @Bean
//    AuthenticationProvider authProvider() {
//    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//    authProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
//    authProvider.setUserDetailsService(renterDetailService);
//    return authProvider;
//    }
}
