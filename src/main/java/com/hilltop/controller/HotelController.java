package com.hilltop.controller;

import com.hilltop.configuration.Translator;
import com.hilltop.domain.request.HotelCreateRequestDto;
import com.hilltop.domain.response.CityListResponseDto;
import com.hilltop.domain.response.HotelListPageResponseDto;
import com.hilltop.domain.response.HotelListResponseDto;
import com.hilltop.domain.response.HotelResponseDto;
import com.hilltop.enums.ErrorResponseStatusType;
import com.hilltop.enums.SuccessResponseStatusType;
import com.hilltop.exception.HotelServiceException;
import com.hilltop.exception.InvalidHotelException;
import com.hilltop.model.Hotel;
import com.hilltop.service.HotelService;
import com.hilltop.wrapper.ResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.List;


/**
 * HotelController
 */
@RestController
@RequestMapping("/api/v1/hotel")
@Slf4j
@CrossOrigin
public class HotelController extends Controller {
    private final HotelService hotelService;

    public HotelController(Translator translator, HotelService hotelService) {
        super(translator);
        this.hotelService = hotelService;
    }

    /**
     * This endpoint used to save a hotel.
     *
     * @param hotelCreateRequest hotelCreateRequest
     * @return hotelSaveResponseDto
     */
    @PostMapping
    public ResponseEntity<ResponseWrapper> saveHotel(@RequestBody HotelCreateRequestDto hotelCreateRequest) {
        try {
            if (!hotelCreateRequest.isRequiredAvailable()) {
                log.error("Missing required filed to save a hotel.");
                return getErrorResponse(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS);
            }
            var hotelSaveResponseDto = hotelService.saveHotel(hotelCreateRequest);
            return getSuccessResponse(hotelSaveResponseDto, SuccessResponseStatusType.CREATE_HOTEL);
        } catch (HotelServiceException e) {
            log.error("Saving hotel was failed.", e);
            return getInternalServerError();
        }
    }

    /**
     * This endpoint used to get hotel by id.
     *
     * @param id hotel id
     * @return hotelResponseDto
     */
    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper> getHotel(@PathVariable String id) {
        try {
            var hotel = hotelService.getHotelById(id);
            var hotelResponseDto = new HotelResponseDto(hotel);
            return getSuccessResponse(hotelResponseDto, SuccessResponseStatusType.READ_HOTEL);
        } catch (InvalidHotelException e) {
            log.error("Invalid hotel id to get hotel details.");
            return getErrorResponse(ErrorResponseStatusType.INVALID_HOTEL_ID);
        } catch (HotelServiceException e) {
            log.error("Returning hotel list was failed.", e);
            return getInternalServerError();
        }
    }

    /**
     * This endpoint used to get hotel list.
     *
     * @return hotelListResponseDto
     */
    @GetMapping("/{page}/{size}")
    public ResponseEntity<ResponseWrapper> getHotelList(@Min(DEFAULT_PAGE) @PathVariable int page,
                                                        @Positive @Max(PAGE_MAX_SIZE) @PathVariable int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(DEFAULT_SORT).descending());
            Page<Hotel> allHotelPage = hotelService.getAllHotel(pageable);
            var hotelListPageResponseDto = new HotelListPageResponseDto(allHotelPage);
            return getSuccessResponse(hotelListPageResponseDto, SuccessResponseStatusType.READ_HOTEL_LIST);
        } catch (HotelServiceException e) {
            log.error("Returning hotel list was failed.", e);
            return getInternalServerError();
        }
    }

    /**
     * This endpoint used to update a hotel by id.
     *
     * @param id                 hotel id
     * @param hotelCreateRequest hotelCreateRequest
     * @return hotelResponseDto
     */
    @PutMapping("/{id}")
    public ResponseEntity<ResponseWrapper> updateHotel(@PathVariable String id,
                                                       @RequestBody HotelCreateRequestDto hotelCreateRequest) {
        try {
            if (!hotelCreateRequest.isRequiredAvailable()) {
                log.error("Missing required filed to save a hotel.");
                return getErrorResponse(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS);
            }
            var hotel = hotelService.updateHotel(id, hotelCreateRequest);
            var hotelResponseDto = new HotelResponseDto(hotel);
            return getSuccessResponse(hotelResponseDto, SuccessResponseStatusType.UPDATE_HOTEL);
        } catch (InvalidHotelException e) {
            log.error("Invalid hotel id to update hotel details.");
            return getErrorResponse(ErrorResponseStatusType.INVALID_HOTEL_ID);
        } catch (HotelServiceException e) {
            log.error("Updating hotel by id was failed.", e);
            return getInternalServerError();
        }
    }

    /**
     * This endpoint used to delete a hotel by id.
     *
     * @param id hotel id
     * @return SuccessResponseStatus
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseWrapper> deleteHotel(@PathVariable String id) {
        try {
            hotelService.deleteHotel(id);
            return getSuccessResponse(null, SuccessResponseStatusType.DELETE_HOTEL);
        } catch (InvalidHotelException e) {
            log.error("Invalid hotel id to update hotel details.");
            return getErrorResponse(ErrorResponseStatusType.INVALID_HOTEL_ID);
        } catch (HotelServiceException e) {
            log.error("Updating hotel by id was failed.", e);
            return getInternalServerError();
        }
    }

    /**
     * This endpoint used to get all cities.
     *
     * @return cityListResponseDto
     */
    @GetMapping("/cities")
    public ResponseEntity<ResponseWrapper> getHotelCites() {
        try {
            List<String> allCities = hotelService.getAllCities();
            var cityListResponseDto = new CityListResponseDto(allCities);
            return getSuccessResponse(cityListResponseDto, SuccessResponseStatusType.READ_HOTEL_CITIES);
        } catch (HotelServiceException e) {
            log.error("Getting hotel cities was failed.", e);
            return getInternalServerError();
        }
    }

    @GetMapping("city/{city}")
    public ResponseEntity<ResponseWrapper> getHotelsByCity(@PathVariable String city) {
        try {
            var hotelsByCity = hotelService.getHotelsByCity(city);
            var hotelListResponseDto = new HotelListResponseDto(hotelsByCity);
            return getSuccessResponse(hotelListResponseDto, SuccessResponseStatusType.READ_HOTELS_BY_CITY);
        } catch (HotelServiceException e) {
            log.error("Getting hotel cities was failed.", e);
            return getInternalServerError();
        }
    }
}
