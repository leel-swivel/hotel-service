package com.hilltop.domain.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * HotelCreateRequestDto
 */
@Getter
@Setter
public class HotelCreateRequestDto extends RequestDto {

    private String name;
    private String description;
    private String city;
    private String address;
    private String telephone;
    private String email;
    private List<String> imageUrl;


    @Override
    public String toLogJson() {
        return toJson();
    }

    @Override
    public boolean isRequiredAvailable() {
        return isNonEmpty(name) && isNonEmpty(city) && isNonEmpty(telephone) && isNonEmpty(address);
    }
}
