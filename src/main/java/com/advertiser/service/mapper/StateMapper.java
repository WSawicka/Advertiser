package com.advertiser.service.mapper;

import com.advertiser.domain.*;
import com.advertiser.service.dto.StateDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity State and its DTO StateDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StateMapper {

    StateDTO stateToStateDTO(State state);

    List<StateDTO> statesToStateDTOs(List<State> states);

    @Mapping(target = "campaign", ignore = true)
    State stateDTOToState(StateDTO stateDTO);

    List<State> stateDTOsToStates(List<StateDTO> stateDTOs);
}
