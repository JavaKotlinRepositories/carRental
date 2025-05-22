package com.vishalgandla.carRental.Dto;

import java.time.LocalDate;

public class LocationSendDtoCustomer {
    private int id;
    private String name;
    private String city;
    private String state;
    private String country;
    private Float latitude;
    private Float longitude;
    private String carRentalPhoto;
    private Float distancesquared;
    private Float distance;

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

    public Float getDistancesquared() {
        return distancesquared;
    }

    public void setDistancesquared(Float distancesquared) {
        this.distancesquared = distancesquared;
    }

    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }
}
