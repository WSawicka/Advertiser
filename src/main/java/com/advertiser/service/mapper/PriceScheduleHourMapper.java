package com.advertiser.service.mapper;

import com.advertiser.domain.*;
import com.advertiser.service.dto.PriceScheduleHourDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity PriceScheduleHour and its DTO PriceScheduleHourDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PriceScheduleHourMapper {

    @Mapping(source = "priceSchedule.id", target = "priceScheduleId")
    PriceScheduleHourDTO priceScheduleHourToPriceScheduleHourDTO(PriceScheduleHour priceScheduleHour);

    List<PriceScheduleHourDTO> priceScheduleHoursToPriceScheduleHourDTOs(List<PriceScheduleHour> priceScheduleHours);

    @Mapping(source = "priceScheduleId", target = "priceSchedule")
    PriceScheduleHour priceScheduleHourDTOToPriceScheduleHour(PriceScheduleHourDTO priceScheduleHourDTO);

    List<PriceScheduleHour> priceScheduleHourDTOsToPriceScheduleHours(List<PriceScheduleHourDTO> priceScheduleHourDTOs);

    default PriceSchedule priceScheduleFromId(Long id) {
        if (id == null) {
            return null;
        }
        PriceSchedule priceSchedule = new PriceSchedule();
        priceSchedule.setId(id);
        return priceSchedule;
    }
}
