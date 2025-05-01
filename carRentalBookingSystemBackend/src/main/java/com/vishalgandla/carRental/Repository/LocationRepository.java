package com.vishalgandla.carRental.Repository;

import com.vishalgandla.carRental.Model.Location;
import com.vishalgandla.carRental.Model.Renter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {
    Page<Location> findByRenterOrderByCreatedDateDesc(Renter renter, Pageable pageable);
    @Transactional
    int deleteByRenterAndId(Renter renter, int id);
    List<Location> findByRenterAndId(Renter renter, int id);
}
