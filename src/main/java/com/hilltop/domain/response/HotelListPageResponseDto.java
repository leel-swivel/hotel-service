package com.hilltop.domain.response;

import com.hilltop.model.Hotel;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class HotelListPageResponseDto extends PageResponseDto {

    private final List<HotelResponseDto> hotelResponseDto;

    public HotelListPageResponseDto(Page<Hotel> page) {
        super(page);
        this.hotelResponseDto = generateHotelResponseList(page);
    }

    private List<HotelResponseDto> generateHotelResponseList(Page<Hotel> hotelPage) {
        return hotelPage.getContent().stream().map(HotelResponseDto::new).collect(Collectors.toList());
    }

}
