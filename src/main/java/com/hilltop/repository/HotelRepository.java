package com.hilltop.repository;

import com.hilltop.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * HotelRepository
 */
public interface HotelRepository extends JpaRepository<Hotel, String> {

    List<Hotel> findAllByCity(String city);
}
