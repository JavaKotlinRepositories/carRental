package com.vishalgandla.carRental.Model;

import jakarta.persistence.*;

@Entity
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private String city;
    private String state;
    private String country;
    private Float latitude;
    private Float longitude;
    private String carRentalPhoto;
    @ManyToOne
    @JoinColumn(name = "carRentalId",nullable = false)
    private Renter renter;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Renter getRenter() {
        return renter;
    }

    public void setRenter(Renter renter) {
        this.renter = renter;
    }

    public String getCarRentalPhoto() {
        return carRentalPhoto;
    }

    public void setCarRentalPhoto(String carRentalPhoto) {
        this.carRentalPhoto = carRentalPhoto;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
