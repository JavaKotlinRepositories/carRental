package com.vishalgandla.carRental.Repository;

import com.vishalgandla.carRental.Model.Booking;
import com.vishalgandla.carRental.Model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    Page<Booking> findByCustomerOrderByBookingDateDescIdDesc(Customer cust, Pageable pageable);
}
