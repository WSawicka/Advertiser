package com.advertiser.service.mapper;

import com.advertiser.domain.*;
import com.advertiser.service.dto.CampaignDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Campaign and its DTO CampaignDTO.
 */
@Mapper(componentModel = "spring", uses = {PriceScheduleMapper.class, })
public interface CampaignMapper {

    @Mapping(source = "campaignState.id", target = "campaignStateId")
    @Mapping(source = "business.id", target = "businessId")
    @Mapping(target = "priceSchedule", ignore = true)
    CampaignDTO campaignToCampaignDTO(Campaign campaign);

    List<CampaignDTO> campaignsToCampaignDTOs(List<Campaign> campaigns);

    @Mapping(source = "campaignStateId", target = "campaignState")
    @Mapping(source = "businessId", target = "business")
    @Mapping(target = "spots", ignore = true)
    @Mapping(target = "spotInfos", ignore = true)
    @Mapping(target = "reports", ignore = true)
    Campaign campaignDTOToCampaign(CampaignDTO campaignDTO);

    List<Campaign> campaignDTOsToCampaigns(List<CampaignDTO> campaignDTOs);

    default State stateFromId(Long id) {
        if (id == null) {
            return null;
        }
        State state = new State();
        state.setId(id);
        return state;
    }

    default Business businessFromId(Long id) {
        if (id == null) {
            return null;
        }
        Business business = new Business();
        business.setId(id);
        return business;
    }

    default PriceSchedule priceScheduleFromId(Long id) {
        if (id == null) {
            return null;
        }
        PriceSchedule priceSchedule = new PriceSchedule();
        priceSchedule.setId(id);
        return priceSchedule;
    }
}
