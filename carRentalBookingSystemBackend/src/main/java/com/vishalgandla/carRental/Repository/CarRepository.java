package com.vishalgandla.carRental.Repository;


import com.vishalgandla.carRental.Model.Car;
import com.vishalgandla.carRental.Model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface CarRepository extends JpaRepository<Car, Integer> {
    Page<Car> findByLocationOrderByCreatedAtDesc(Location location, Pageable pageable);

}
