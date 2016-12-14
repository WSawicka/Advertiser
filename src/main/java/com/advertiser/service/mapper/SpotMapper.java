package com.advertiser.service.mapper;

import com.advertiser.domain.*;
import com.advertiser.service.dto.SpotDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Spot and its DTO SpotDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SpotMapper {

    @Mapping(source = "hour.id", target = "hourId")
    @Mapping(source = "campaign.id", target = "campaignId")
    @Mapping(source = "spotInfo.id", target = "spotInfoId")
    SpotDTO spotToSpotDTO(Spot spot);
    SpotDTO spotToSpotDTO(Spot spot, CampaignMapper cm);

    List<SpotDTO> spotsToSpotDTOs(List<Spot> spots);
    List<SpotDTO> spotsToSpotDTOs(List<Spot> spots, CampaignMapper cm);

    @Mapping(source = "hourId", target = "hour")
    @Mapping(source = "campaignId", target = "campaign")
    @Mapping(source = "spotInfoId", target = "spotInfo")
    Spot spotDTOToSpot(SpotDTO spotDTO);

    List<Spot> spotDTOsToSpots(List<SpotDTO> spotDTOs);

    default Hour hourFromId(Long id) {
        if (id == null) {
            return null;
        }
        Hour hour = new Hour();
        hour.setId(id);
        return hour;
    }

    default Campaign campaignFromId(Long id) {
        if (id == null) {
            return null;
        }
        Campaign campaign = new Campaign();
        campaign.setId(id);
        return campaign;
    }

    default SpotInfo spotInfoFromId(Long id) {
        if (id == null) {
            return null;
        }
        SpotInfo spotInfo = new SpotInfo();
        spotInfo.setId(id);
        return spotInfo;
    }
}
