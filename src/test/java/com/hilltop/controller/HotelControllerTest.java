package com.hilltop.controller;

import com.hilltop.configuration.Translator;
import com.hilltop.domain.request.HotelCreateRequestDto;
import com.hilltop.exception.HotelServiceException;
import com.hilltop.exception.InvalidHotelException;
import com.hilltop.model.Hotel;
import com.hilltop.service.HotelService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class HotelControllerTest {

    private static final String CREATE_HOTEL_URL = "/api/v1/hotel";
    private static final String GET_HOTEL_CITIES_URL = "/api/v1/hotel/cities";
    private static final String GET_HOTEL_BY_CITY_URL = "/api/v1/hotel/city/{city}";
    private static final String UPDATE_HOTEL_URL = "/api/v1/hotel/{id}";
    private static final String GET_HOTEL_BY_ID_URL = "/api/v1/hotel/{id}";
    private static final String GET_HOTEL_LIST = "/api/v1/hotel?page=0&size=10";
    private static final String HOTEL_ID = "hid-1235-1458-1785";
    private static final String SUCCESS_STATUS = "SUCCESS";
    private static final String CITY = "Kalutara";
    private static final int PAGE_NO = 0;
    private static final int SIZE = 1;

    @Mock
    private HotelService hotelService;
    @Mock
    private Translator translator;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        initMocks(this);
        HotelController hotelController = new HotelController(translator, hotelService);
        mockMvc = MockMvcBuilders.standaloneSetup(hotelController).build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void Should_ReturnOk_When_CreatingAHotel() throws Exception {
        HotelCreateRequestDto hotelCreateRequestDto = getHotelCreateRequestDto();
        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_HOTEL_URL)
                        .content(hotelCreateRequestDto.toLogJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void Should_ReturnBadRequest_When_CreatingAHotelWithoutRequiredFields() throws Exception {
        HotelCreateRequestDto hotelCreateRequestDto = getHotelCreateRequestDto();
        hotelCreateRequestDto.setName(null);
        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_HOTEL_URL)
                        .content(hotelCreateRequestDto.toLogJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void Should_ReturnInternalServerError_When_CreatingAHotel() throws Exception {
        HotelCreateRequestDto hotelCreateRequestDto = getHotelCreateRequestDto();
        doThrow(new HotelServiceException("ERROR")).when(hotelService).saveHotel(any());
        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_HOTEL_URL)
                        .content(hotelCreateRequestDto.toLogJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void Should_ReturnOk_When_ValidHotelIdIsProvided() throws Exception {
        String url = GET_HOTEL_BY_ID_URL.replace("{id}", HOTEL_ID);
        Hotel hotel = generateHotel();
        when(hotelService.getHotelById(HOTEL_ID)).thenReturn(hotel);
        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(status().isOk());
    }

    @Test
    void Should_ReturnOk_When_DeletingHotel() throws Exception {
        String url = GET_HOTEL_BY_ID_URL.replace("{id}", HOTEL_ID);
        Hotel hotel = generateHotel();
        when(hotelService.getHotelById(HOTEL_ID)).thenReturn(hotel);
        mockMvc.perform(MockMvcRequestBuilders.delete(url))
                .andExpect(status().isOk());
    }

    @Test
    void Should_ReturnBadRequest_When_DeletingAHotel() throws Exception {
        String url = GET_HOTEL_BY_ID_URL.replace("{id}", HOTEL_ID);
        doThrow(new InvalidHotelException("ERROR")).when(hotelService).deleteHotel(HOTEL_ID);
        mockMvc.perform(MockMvcRequestBuilders.delete(url))
                .andExpect(status().isBadRequest());
    }

    @Test
    void Should_ReturnInternalServerError_When_DeletingAHotel() throws Exception {
        String url = GET_HOTEL_BY_ID_URL.replace("{id}", HOTEL_ID);
        doThrow(new HotelServiceException("ERROR")).when(hotelService).deleteHotel(HOTEL_ID);
        mockMvc.perform(MockMvcRequestBuilders.delete(url))
                .andExpect(status().isInternalServerError());
    }


    @Test
    void Should_ReturnBadRequest_When_InvalidHotelIdIsProvided() throws Exception {
        String url = GET_HOTEL_BY_ID_URL.replace("{id}", HOTEL_ID);
        doThrow(new InvalidHotelException("ERROR")).when(hotelService).getHotelById(HOTEL_ID);
        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(status().isBadRequest());
    }

    @Test
    void Should_ReturnInternalServerError_When_GetHotelByIdIsFailed() throws Exception {
        String url = GET_HOTEL_BY_ID_URL.replace("{id}", HOTEL_ID);
        doThrow(new HotelServiceException("ERROR")).when(hotelService).getHotelById(HOTEL_ID);
        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void Should_ReturnOk_When_ValidPageAndSizeProvidedForGetAllHotelList() throws Exception {
        Page<Hotel> hotelPage = getHotelPage();
        when(hotelService.getAllHotel(any())).thenReturn(hotelPage);
        mockMvc.perform(MockMvcRequestBuilders.get(GET_HOTEL_LIST))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(SUCCESS_STATUS));
    }

    @Test
    void Should_ReturnInternalServerError_When_GetAllHotelIsFailed() throws Exception {
        doThrow(new HotelServiceException("ERROR")).when(hotelService).getAllHotel(any());
        mockMvc.perform(MockMvcRequestBuilders.get(GET_HOTEL_LIST))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void Should_ReturnBadRequest_When_UpdatingAHotelWithoutRequiredFields() throws Exception {
        String url = UPDATE_HOTEL_URL.replace("{id}", HOTEL_ID);
        HotelCreateRequestDto hotelCreateRequestDto = getHotelCreateRequestDto();
        hotelCreateRequestDto.setName(null);
        mockMvc.perform(MockMvcRequestBuilders.put(url)
                        .content(hotelCreateRequestDto.toLogJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void Should_ReturnBadRequest_When_UpdateHotelIsFailed() throws Exception {
        String url = UPDATE_HOTEL_URL.replace("{id}", HOTEL_ID);

        System.out.println(url);
        HotelCreateRequestDto hotelCreateRequestDto = getHotelCreateRequestDto();

        doThrow(new InvalidHotelException("ERROR")).when(hotelService).updateHotel(HOTEL_ID, hotelCreateRequestDto);
        mockMvc.perform(MockMvcRequestBuilders.put(url))
                .andExpect(status().isBadRequest());
    }

    @Test
    void Should_ReturnOk_When_GetHotelCitesRequested() throws Exception {
        List<String> sampleCities = getSampleCities();
        when(hotelService.getAllCities()).thenReturn(sampleCities);
        mockMvc.perform(MockMvcRequestBuilders.get(GET_HOTEL_CITIES_URL)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void Should_ReturnInternalServerError_When_GetHotelCitiesIsFailed() throws Exception {
        doThrow(new HotelServiceException("ERROR")).when(hotelService).getAllCities();
        mockMvc.perform(MockMvcRequestBuilders.get(GET_HOTEL_CITIES_URL))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void Should_ReturnOk_When_GetHotelByCityRequested() throws Exception {
        String url = GET_HOTEL_BY_CITY_URL.replace("{city}", CITY);
        when(hotelService.getHotelsByCity(CITY)).thenReturn(anyList());
        mockMvc.perform(MockMvcRequestBuilders.get(url)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void Should_ReturnInternalServerError_When_GetHotelByCityIsFailed() throws Exception {
        String url = GET_HOTEL_BY_CITY_URL.replace("{city}", CITY);
        doThrow(new HotelServiceException("ERROR")).when(hotelService).getHotelsByCity(CITY);
        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(status().isInternalServerError());
    }

    private HotelCreateRequestDto getHotelCreateRequestDto() {
        HotelCreateRequestDto hotelCreateRequestDto = new HotelCreateRequestDto();
        hotelCreateRequestDto.setName("Hilton");
        hotelCreateRequestDto.setDescription("3-Star hotel.");
        hotelCreateRequestDto.setCity("Colombo");
        hotelCreateRequestDto.setTelephone("011215487");
        hotelCreateRequestDto.setAddress("Galle Rd, Colombo.");
        hotelCreateRequestDto.setEmail("info@hilton.com");
        hotelCreateRequestDto.setImageUrl(new ArrayList<>());
        return hotelCreateRequestDto;
    }

    private Hotel generateHotel() {
        return new Hotel(getHotelCreateRequestDto());
    }

    private Page<Hotel> getHotelPage() {
        List<Hotel> hotels = new ArrayList<>();
        Hotel hotel = generateHotel();
        hotels.add(hotel);
        return new PageImpl<>(hotels);
    }

    private List<String> getSampleCities() {
        ArrayList<String> cities = new ArrayList<>();
        cities.add("Colombo");
        cities.add("Gampaha");
        return cities;
    }
}