package com.hilltop.domain.request;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;

class HotelCreateRequestTestDto {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }


    @Test
    void Should_ReturnTrue_When_ValidFieldsAreProvided() {
        var sampleHotelCreateRequest = getSampleHotelCreateRequest();

        System.out.println(sampleHotelCreateRequest.toLogJson());
        assertTrue(sampleHotelCreateRequest.isRequiredAvailable());
    }
    @Test
    void Should_ReturnTrue_When_ToStringMethodReturnsValue(){
        var sampleHotelCreateRequest = getSampleHotelCreateRequest();
        String sampleHotelCreateRequestToString = getSampleHotelCreateRequestToString();
        assertTrue(sampleHotelCreateRequest.toLogJson().equals(sampleHotelCreateRequestToString));
    }


    private HotelCreateRequestDto getSampleHotelCreateRequest() {
        var hotelCreateRequestDto = new HotelCreateRequestDto();
        hotelCreateRequestDto.setName("New Hotel");
        hotelCreateRequestDto.setCity("Kalutara");
        hotelCreateRequestDto.setTelephone("0342285468");
        hotelCreateRequestDto.setEmail("email@email.com");
        hotelCreateRequestDto.setAddress("Kalutara Rd, Kalutara");
        hotelCreateRequestDto.setDescription("4 Star hotel");
        hotelCreateRequestDto.setImageUrl(new ArrayList<>());
        return hotelCreateRequestDto;
    }
    private String getSampleHotelCreateRequestToString(){
        return "{\"name\":\"New Hotel\",\"description\":\"4 Star hotel\",\"" +
                "city\":\"Kalutara\",\"address\":\"Kalutara Rd, Kalutara\",\"" +
                "telephone\":\"0342285468\",\"email\":\"email@email.com\",\"" +
                "imageUrl\":[],\"requiredAvailable\":true}";
    }
}