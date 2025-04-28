package com.vishalgandla.carRental.Dto;

import org.springframework.web.multipart.MultipartFile;

public class RenterDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private MultipartFile profilePicture; // For file upload

    // Getters and Setters (or use Lombok for @Data)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public MultipartFile getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(MultipartFile profilePicture) {
        this.profilePicture = profilePicture;
    }
}
