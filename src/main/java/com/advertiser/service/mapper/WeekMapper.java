package com.advertiser.service.mapper;

import com.advertiser.domain.*;
import com.advertiser.service.dto.WeekDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Week and its DTO WeekDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface WeekMapper {

    WeekDTO weekToWeekDTO(Week week);

    List<WeekDTO> weeksToWeekDTOs(List<Week> weeks);

    @Mapping(target = "days", ignore = true)
    Week weekDTOToWeek(WeekDTO weekDTO);

    List<Week> weekDTOsToWeeks(List<WeekDTO> weekDTOs);
}
