package com.hilltop.service;

import com.hilltop.domain.request.HotelCreateRequestDto;
import com.hilltop.exception.HotelServiceException;
import com.hilltop.exception.InvalidHotelException;
import com.hilltop.model.Hotel;
import com.hilltop.repository.HotelRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

class HotelServiceTest {
    private static final String HOTEL_ID = "hid-92be0c67-3810-47c2-9e28-615f81efad6a";

    private HotelService hotelService;
    @Mock
    private HotelRepository hotelRepository;

    @BeforeEach
    void setUp() {
        initMocks(this);
        hotelService = new HotelService(hotelRepository);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void Should_SaveHotel() {
        var hotelCreateRequestDto = getHotelCreateRequestDto();
        hotelService.saveHotel(hotelCreateRequestDto);
        verify(hotelRepository, times(1)).save(any(Hotel.class));
    }

    @Test
    void Should_ThrowException_When_SavingHotel() {
        var hotelCreateRequestDto = getHotelCreateRequestDto();
        when(hotelRepository.save(any(Hotel.class))).thenThrow(new DataAccessException("ERROR") {
        });
        HotelServiceException hotelServiceException = assertThrows(HotelServiceException.class, () ->
                hotelService.saveHotel(hotelCreateRequestDto));
        assertEquals("Saving hotel info into database was failed.", hotelServiceException.getMessage());
    }

    @Test
    void Should_ReturnHotel_When_HotelIdProvided() {
        var hotelCreateRequestDto = getHotelCreateRequestDto();
        var hotel = getHotel(hotelCreateRequestDto);
        when(hotelRepository.findById(HOTEL_ID)).thenReturn(Optional.of(hotel));
        assertEquals(hotel, hotelService.getHotelById(HOTEL_ID));
    }

    @Test
    void Should_ThrowInvalidHotelException_When_InvalidHotelIdProvided() {
        when(hotelRepository.findById(HOTEL_ID)).thenReturn(Optional.empty());
        var invalidHotelException = assertThrows(InvalidHotelException.class, () -> hotelService.getHotelById(HOTEL_ID));
        assertEquals("No hotel found for id: " + HOTEL_ID, invalidHotelException.getMessage());
    }

    @Test
    void Should_ThrowException_When_GettingHotelById() {
        when(hotelRepository.findById(HOTEL_ID)).thenThrow(new DataAccessException("ERROR") {
        });
        HotelServiceException hotelServiceException = assertThrows(HotelServiceException.class, () ->
                hotelService.getHotelById(HOTEL_ID));
        assertEquals("Reading hotel info from database was failed.", hotelServiceException.getMessage());
    }

    @Test
    void Should_DeleteHotel() {
        var hotelCreateRequestDto = getHotelCreateRequestDto();
        var hotel = getHotel(hotelCreateRequestDto);
        when(hotelRepository.findById(HOTEL_ID)).thenReturn(Optional.of(hotel));
        hotelService.deleteHotel(HOTEL_ID);
        verify(hotelRepository, times(1)).delete(hotel);
    }

    @Test
    void Should_ReturnHotelCityList() {
        HotelCreateRequestDto hotelCreateRequestDto = getHotelCreateRequestDto();
        Hotel hotel = getHotel(hotelCreateRequestDto);
        List<Hotel> hotelList = new ArrayList<>();
        hotelList.add(hotel);
        when(hotelRepository.findAll()).thenReturn(hotelList);
        hotelService.getAllCities();
        assertEquals(1, (hotelService.getAllCities()).size());
    }


    @Test
    void Should_ThrowException_When_DeletingHotelById() {
        var hotelCreateRequestDto = getHotelCreateRequestDto();
        var hotel = getHotel(hotelCreateRequestDto);
        when(hotelRepository.findById(HOTEL_ID)).thenReturn(Optional.of(hotel));
        doThrow(new DataAccessException("ERROR") {
        }).when(hotelRepository).delete(hotel);
        HotelServiceException hotelServiceException = assertThrows(HotelServiceException.class, () ->
                hotelService.deleteHotel(HOTEL_ID));
        assertEquals("Deleting a hotel by id from database was failed.", hotelServiceException.getMessage());
    }


    @Test
    void Should_ThrowHotelServiceException_When_InvalidHotelIdProvided() {
        when(hotelRepository.findAll()).thenThrow(new DataAccessException("ERROR") {
        });
        HotelServiceException hotelServiceException = assertThrows(HotelServiceException.class, () ->
                hotelService.getAllCities());
        assertEquals("Getting hotel cities from database was failed.", hotelServiceException.getMessage());
    }


    @Test
    void Should_ReturnHotelResponseDtoListWhenCityIsProvided() {
        HotelCreateRequestDto hotelCreateRequestDto = getHotelCreateRequestDto();
        Hotel hotel = getHotel(hotelCreateRequestDto);
        hotel.setId(HOTEL_ID);
        List<Hotel> hotelList = new ArrayList<>();
        hotelList.add(hotel);
        when(hotelRepository.findAllByCity(any())).thenReturn(hotelList);
        assertEquals(HOTEL_ID, hotelService.getHotelsByCity(any()).get(0).getId());
    }

    @Test
    void Should_ThrowHotelServiceException_When_GetHotelsByCity() {
        when(hotelRepository.findAllByCity(any())).thenThrow(new DataAccessException("ERROR") {
        });
        HotelServiceException hotelServiceException = assertThrows(HotelServiceException.class, () ->
                hotelService.getHotelsByCity(any()));
        assertEquals("Getting hotels by city from database was failed.", hotelServiceException.getMessage());
    }


    @Test
    void Should_AllHotels_WhenProvidedValidPageable() {
        Pageable pageable = PageRequest.of(0, 2, Sort.by("updatedAt").descending());
        hotelService.getAllHotel(pageable);
        verify(hotelRepository, times(1)).findAll(pageable);
    }

    @Test
    void Should_ThrowHotelServiceException_When_GettingAllHotel() {
        Pageable pageable = PageRequest.of(0, 2, Sort.by("updatedAt").descending());
        when(hotelRepository.findAll(pageable)).thenThrow(new DataAccessException("ERROR") {
        });
        HotelServiceException hotelServiceException = assertThrows(HotelServiceException.class, () ->
                hotelService.getAllHotel(pageable));
        assertEquals("Reading hotel list from database was failed.", hotelServiceException.getMessage());
    }


    @Test
    void Should_UpdateHotel() {
        var hotelCreateRequestDto = getHotelCreateRequestDto();
        Hotel hotel = getHotel(hotelCreateRequestDto);
        when(hotelRepository.findById(HOTEL_ID)).thenReturn(Optional.of(hotel));
        hotelService.updateHotel(HOTEL_ID, hotelCreateRequestDto);
        verify(hotelRepository, times(1)).save(any(Hotel.class));
    }


    @Test
    void Should_ThrowException_When_UpdatingHotel() {
        var hotelCreateRequestDto = getHotelCreateRequestDto();
        Hotel hotel = getHotel(hotelCreateRequestDto);
        when(hotelRepository.findById(HOTEL_ID)).thenReturn(Optional.of(hotel));
        when(hotelRepository.save(any(Hotel.class))).thenThrow(new DataAccessException("ERROR") {
        });
        HotelServiceException hotelServiceException = assertThrows(HotelServiceException.class, () ->
                hotelService.updateHotel(HOTEL_ID,hotelCreateRequestDto));
        assertEquals("Updating hotel from database was failed.", hotelServiceException.getMessage());
    }


    private HotelCreateRequestDto getHotelCreateRequestDto() {
        HotelCreateRequestDto hotelCreateRequestDto = new HotelCreateRequestDto();
        hotelCreateRequestDto.setName("Hilton");
        hotelCreateRequestDto.setDescription("3-Star hotel.");
        hotelCreateRequestDto.setCity("Colombo");
        hotelCreateRequestDto.setTelephone("011215487");
        hotelCreateRequestDto.setAddress("Galle Rd, Colombo.");
        hotelCreateRequestDto.setEmail("info@hilton.com");
        return hotelCreateRequestDto;
    }

    private Hotel getHotel(HotelCreateRequestDto hotelCreateRequestDto) {
        var hotel = new Hotel(hotelCreateRequestDto);
        hotel.setId(HOTEL_ID);
        return hotel;
    }
}