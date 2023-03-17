package com.hilltop.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CityListResponseDto extends ResponseDto {
    private Set<String> cities;

    public CityListResponseDto(List<String> cities) {
        this.cities = new HashSet<>(cities);
    }
}
