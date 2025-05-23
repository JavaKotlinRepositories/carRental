package com.vishalgandla.carRental.Repository;

import com.vishalgandla.carRental.Model.Booking;
import com.vishalgandla.carRental.Model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    Page<Booking> findByCustomerOrderByBookingDateDescIdDesc(Customer cust, Pageable pageable);
    @Query("SELECT b.bookingDate FROM Booking b WHERE b.car.id = :id ORDER BY b.bookingDate DESC, b.id DESC")
    List<Object> findBookingDatesByCarId(@Param("id") Integer id);

}
