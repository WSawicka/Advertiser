package com.advertiser.service.mapper;

import com.advertiser.domain.*;
import com.advertiser.service.dto.PriceScheduleDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity PriceSchedule and its DTO PriceScheduleDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PriceScheduleMapper {

    PriceScheduleDTO priceScheduleToPriceScheduleDTO(PriceSchedule priceSchedule);

    List<PriceScheduleDTO> priceSchedulesToPriceScheduleDTOs(List<PriceSchedule> priceSchedules);

    @Mapping(target = "priceScheduleHours", ignore = true)
    @Mapping(target = "campaigns", ignore = true)
    PriceSchedule priceScheduleDTOToPriceSchedule(PriceScheduleDTO priceScheduleDTO);

    List<PriceSchedule> priceScheduleDTOsToPriceSchedules(List<PriceScheduleDTO> priceScheduleDTOs);
}
