package com.advertiser.service.mapper;

import com.advertiser.domain.*;
import com.advertiser.service.dto.DayDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Day and its DTO DayDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DayMapper {

    @Mapping(source = "week.id", target = "weekId")
    DayDTO dayToDayDTO(Day day);

    List<DayDTO> daysToDayDTOs(List<Day> days);

    @Mapping(target = "hours", ignore = true)
    @Mapping(source = "weekId", target = "week")
    Day dayDTOToDay(DayDTO dayDTO);

    List<Day> dayDTOsToDays(List<DayDTO> dayDTOs);

    default Week weekFromId(Long id) {
        if (id == null) {
            return null;
        }
        Week week = new Week();
        week.setId(id);
        return week;
    }
}
