package com.vishalgandla.carRental.Repository;

import com.vishalgandla.carRental.Model.Renter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RenterRepository extends JpaRepository<Renter,Long> {
    Renter findByEmail(String email);
}
