package com.vishalgandla.carRental.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class HttpConfiguration {
//    @Autowired
//    RenterDetailService renterDetailService;

    @Autowired
    JwtFilter jwtFilter;
    @Value("${cors_link}")
    String corsLink;

    @Bean
    SecurityFilterChain httpConfig(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults());
        http.csrf(customizer->customizer.disable());
        http.formLogin(customizer->customizer.disable());
        http.httpBasic(Customizer->Customizer.disable());
        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests.requestMatchers("/backend/renter/login","/backend/renter/signup","/backend/customer/signup","/backend/customer/login").permitAll().anyRequest().authenticated());
        http.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        configuration.setAllowedOrigins(Arrays.asList(corsLink));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type","Cache-Control"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
//    @Bean
//    AuthenticationProvider authProvider() {
//    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//    authProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
//    authProvider.setUserDetailsService(renterDetailService);
//    return authProvider;
//    }

//    @Bean
//    public AuthenticationManager authenticationManager() {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(renterDetailService);
//        authProvider.setPasswordEncoder(new BCryptPasswordEncoder(12));
//        return new ProviderManager(List.of(authProvider));
//    }

}
