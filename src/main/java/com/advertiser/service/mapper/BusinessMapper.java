package com.advertiser.service.mapper;

import com.advertiser.domain.*;
import com.advertiser.service.dto.BusinessDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Business and its DTO BusinessDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BusinessMapper {

    BusinessDTO businessToBusinessDTO(Business business);

    List<BusinessDTO> businessesToBusinessDTOs(List<Business> businesses);

    @Mapping(target = "campaign", ignore = true)
    Business businessDTOToBusiness(BusinessDTO businessDTO);

    List<Business> businessDTOsToBusinesses(List<BusinessDTO> businessDTOs);
}
