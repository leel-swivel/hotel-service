package com.hilltop.repository;

import com.hilltop.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * HotelRepository
 */
public interface HotelRepository extends JpaRepository<Hotel, String> {

    /**
     * This method used to get all hotels by city.
     *
     * @param city city
     * @return List of hotel
     */
    List<Hotel> findAllByCity(String city);
}
