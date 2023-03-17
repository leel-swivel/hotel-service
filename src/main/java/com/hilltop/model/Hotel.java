package com.hilltop.model;

import com.hilltop.domain.request.HotelCreateRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

/**
 * Hotel Entity
 */
@Entity
@Table(name = "hotel")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Hotel {

    @Transient
    private static final String HOTEL_ID_PREFIX = "hid-";

    @Id
    private String id;
    @Column(nullable = false)
    private String name;
    private String description;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    @ElementCollection
    private List<String> imageUrl;
    private String telephone;
    private String email;
    private long createdAt;
    private long updatedAt;

    public Hotel(HotelCreateRequestDto hotelCreateRequest) {
        this.id = HOTEL_ID_PREFIX + UUID.randomUUID();
        this.name = hotelCreateRequest.getName();
        this.description = hotelCreateRequest.getDescription();
        this.city = hotelCreateRequest.getCity();
        this.address = hotelCreateRequest.getAddress();
        this.telephone = hotelCreateRequest.getTelephone();
        this.email = hotelCreateRequest.getEmail();
        this.imageUrl = hotelCreateRequest.getImageUrl();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    public void update(HotelCreateRequestDto hotelCreateRequest) {
        this.name = hotelCreateRequest.getName();
        this.description = hotelCreateRequest.getDescription();
        this.city = hotelCreateRequest.getCity();
        this.address = hotelCreateRequest.getAddress();
        this.telephone = hotelCreateRequest.getTelephone();
        this.email = hotelCreateRequest.getEmail();
        this.updatedAt = System.currentTimeMillis();
        this.imageUrl = hotelCreateRequest.getImageUrl();
    }
}
