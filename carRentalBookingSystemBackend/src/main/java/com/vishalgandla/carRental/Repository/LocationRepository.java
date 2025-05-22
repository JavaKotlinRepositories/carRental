package com.vishalgandla.carRental.Repository;

import com.vishalgandla.carRental.Model.Location;
import com.vishalgandla.carRental.Model.Renter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {
    Page<Location> findByRenterOrderByCreatedDateDesc(Renter renter, Pageable pageable);
    @Transactional
    int deleteByRenterAndId(Renter renter, int id);
    List<Location> findByRenterAndId(Renter renter, int id);


    @Query(value = """
        SELECT *, 
        (6371 * acos(
            cos(radians(:lat)) * cos(radians(latitude)) * 
            cos(radians(longitude) - radians(:lon)) + 
            sin(radians(:lat)) * sin(radians(latitude))
        )) AS distance 
        FROM Location 
        ORDER BY distance 
        LIMIT :limit
        """, nativeQuery = true)
    List<Location> findClosestLocations(
            @Param("lat") double latitude,
            @Param("lon") double longitude,
            @Param("limit") int limit
    );


}
