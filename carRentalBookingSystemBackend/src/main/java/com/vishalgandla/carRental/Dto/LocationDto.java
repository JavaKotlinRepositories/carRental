package com.vishalgandla.carRental.Dto;

import org.springframework.web.multipart.MultipartFile;

public class LocationDto {
    private String name;
    private String city;
    private String state;
    private String country;
    private String latitude;
    private String longitude;
    private MultipartFile carRentalPhoto;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public MultipartFile getCarRentalPhoto() {
        return carRentalPhoto;
    }

    public void setCarRentalPhoto(MultipartFile carRentalPhoto) {
        this.carRentalPhoto = carRentalPhoto;
    }
}
