package com.hilltop.service;

import com.hilltop.domain.request.HotelCreateRequestDto;
import com.hilltop.domain.response.HotelCreateResponseDto;
import com.hilltop.domain.response.HotelResponseDto;
import com.hilltop.exception.HotelServiceException;
import com.hilltop.exception.InvalidHotelException;
import com.hilltop.model.Hotel;
import com.hilltop.repository.HotelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * HotelService
 */
@Service
@Slf4j
public class HotelService {

    private final HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    /**
     * This method used to create a hotel.
     *
     * @param hotelCreateRequest hotelCreateRequest
     * @return HotelCreateResponseDto
     */
    public HotelCreateResponseDto saveHotel(HotelCreateRequestDto hotelCreateRequest) {
        try {
            var hotel = new Hotel(hotelCreateRequest);
            log.info("Successfully saved the hotel response: {}", hotelCreateRequest.toLogJson());
            hotelRepository.save(hotel);
            return new HotelCreateResponseDto(hotel);
        } catch (DataAccessException e) {
            log.error("Error saving hotel due to :{}", e.toString());
            throw new HotelServiceException("Saving hotel info into database was failed.", e);
        }
    }

    /**
     * This method used to get hotel by id.
     *
     * @param id hotel id
     * @return hotel
     */
    public Hotel getHotelById(String id) {
        try {
            Optional<Hotel> hotelOptional = hotelRepository.findById(id);
            if (hotelOptional.isPresent()) {
                log.info("Retuning hotel by id: {}", id);
                return hotelOptional.get();
            } else {
                log.error("No hotel found for id: {}", id);
                throw new InvalidHotelException("No hotel found for id: " + id);
            }
        } catch (DataAccessException e) {
            log.error("Error get hotel by id: {} due to :{}", id, e.toString());
            throw new HotelServiceException("Reading hotel info from database was failed.", e);
        }
    }

    /**
     * This method used to get all hotel list.
     *
     * @return HotelListResponseDto
     */
    public Page<Hotel> getAllHotel(Pageable pageable) {
        try {
            return hotelRepository.findAll(pageable);
        } catch (DataAccessException e) {
            log.error("Error get hotel list due to :{}", e.toString());
            throw new HotelServiceException("Reading hotel list from database was failed.", e);
        }
    }

    /**
     * This method used to update hotel.
     *
     * @param id                 hotel id
     * @param hotelCreateRequest hotelCreateRequest
     * @return Hotel
     */
    public Hotel updateHotel(String id, HotelCreateRequestDto hotelCreateRequest) {
        try {
            var hotelById = getHotelById(id);
            hotelById.update(hotelCreateRequest);
            return hotelRepository.save(hotelById);
        } catch (DataAccessException e) {
            throw new HotelServiceException("Updating hotel from database was failed.", e);
        }
    }

    /**
     * This method used to delete a hotel by id.
     *
     * @param id hotel id
     */
    public void deleteHotel(String id) {
        try {
            var hotel = getHotelById(id);
            hotelRepository.delete(hotel);
            log.info("Successfully deleted the hotel by id: {}", id);
        } catch (DataAccessException e) {
            log.error("Error deleting hotel by id: {} due to : {}", id, e.toString());
            throw new HotelServiceException("Deleting a hotel by id from database was failed.", e);
        }
    }

    /**
     * This method used to get cities.
     *
     * @return String List
     */
    public List<String> getAllCities() {
        try {
            List<Hotel> allHotels = hotelRepository.findAll();
            return allHotels.stream().map(Hotel::getCity).collect(Collectors.toList());
        } catch (DataAccessException e) {
            log.error("Error getting hotel cities");
            throw new HotelServiceException("Getting hotel cities from database was failed.", e);
        }
    }

    public List<HotelResponseDto> getHotelsByCity(String city) {
        try {
            List<Hotel> allByCity = hotelRepository.findAllByCity(city);
            return allByCity.stream().map(HotelResponseDto::new).collect(Collectors.toList());
        } catch (DataAccessException e) {
            log.error("Error getting hotels by city");
            throw new HotelServiceException("Getting hotels by city from database was failed.", e);
        }
    }
}
