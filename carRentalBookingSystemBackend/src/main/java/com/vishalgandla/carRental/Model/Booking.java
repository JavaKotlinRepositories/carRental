package com.vishalgandla.carRental.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

import java.time.Clock;
import java.time.LocalDate;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"bookingDate", "carId"})
)
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String cardType;
    private String cardNumber;
    @Pattern(regexp = "\\d{3}", message = "CVV must be exactly 3 digits")
    private String cardCVV;
    @Pattern(regexp = "^.{0,32}$", message = "Card holder name must be at most 32 characters")
    private String cardHolderName;
    @Column(nullable = false)
    private LocalDate bookingDate;

    @ManyToOne
    @JoinColumn(name="customerId",nullable=false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "carId",nullable = false)
    private Car car;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    @PrePersist
    public void onCreate(){
        if(createdAt == null){
            createdAt = LocalDate.now(Clock.systemUTC());
        }
        if(updatedAt == null){
            updatedAt = LocalDate.now(Clock.systemUTC());
        }
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardCVV() {
        return cardCVV;
    }

    public void setCardCVV(String cardCVV) {
        this.cardCVV = cardCVV;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }
}
