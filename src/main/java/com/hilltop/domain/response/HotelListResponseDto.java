package com.hilltop.domain.response;

import com.hilltop.model.Hotel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * HotelListResponseDto
 */
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class HotelListResponseDto extends ResponseDto {

    private List<HotelResponseDto> hotelList;
}
