package com.advertiser.service.mapper;

import com.advertiser.domain.*;
import com.advertiser.service.dto.HourDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Hour and its DTO HourDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface HourMapper {

    @Mapping(source = "day.id", target = "dayId")
    HourDTO hourToHourDTO(Hour hour);

    List<HourDTO> hoursToHourDTOs(List<Hour> hours);

    @Mapping(target = "spots", ignore = true)
    @Mapping(source = "dayId", target = "day")
    Hour hourDTOToHour(HourDTO hourDTO);

    List<Hour> hourDTOsToHours(List<HourDTO> hourDTOs);

    default Day dayFromId(Long id) {
        if (id == null) {
            return null;
        }
        Day day = new Day();
        day.setId(id);
        return day;
    }
}
