package com.vishalgandla.carRental.Dto;

import java.time.LocalDate;

public class LocationSendDto {
    private int id;
    private String name;
    private String city;
    private String state;
    private String country;
    private Float latitude;
    private Float longitude;
    private String carRentalPhoto;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private int carRentalId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public String getCarRentalPhoto() {
        return carRentalPhoto;
    }

    public void setCarRentalPhoto(String carRentalPhoto) {
        this.carRentalPhoto = carRentalPhoto;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public int getCarRentalId() {
        return carRentalId;
    }

    public void setCarRentalId(int carRentalId) {
        this.carRentalId = carRentalId;
    }
}
