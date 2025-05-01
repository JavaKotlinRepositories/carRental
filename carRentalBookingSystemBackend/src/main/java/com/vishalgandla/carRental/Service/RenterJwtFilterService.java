package com.vishalgandla.carRental.Service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

@Service
public class RenterJwtFilterService {

    // Secret key for signing JWT (store securely, use environment variables in production)
    @Value("${jwt_secret}")
    String SECRET_KEY; // Replace with your secret key

    // Expiration time for JWT in milliseconds (e.g., 1 hour)
    private static final long EXPIRATION_TIME = 3600000*24; // 1 hour

    // Create a JWT token
    public String createToken(String userId) {
        // Set the algorithm for signing the JWT
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

        // Create the token
        String token = JWT.create()
                .withSubject("Renter Authentication")
                .withClaim("userId", userId) // Include the user ID in the payload
                .withIssuedAt(new Date()) // Issued at current time
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Expiration time
                .sign(algorithm); // Sign the token with the algorithm

        return token;
    }

    // Verify the JWT token and get user ID from it
    public String verifyToken(String token) {
        try {
            // Set the algorithm for verifying the JWT
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

            // Decode and verify the JWT token
            DecodedJWT decodedJWT = JWT.require(algorithm)
                    .build()
                    .verify(token); // Verify the token

            // Extract the user ID from the JWT claims
            String userId = decodedJWT.getClaim("userId").asString();

            return userId; // Return the extracted user ID

        } catch (Exception e) {
            // If token verification fails, return null or throw an exception
            System.out.println("Invalid token or token expired");
            return null;
        }
    }
}
