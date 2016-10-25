package com.advertiser.service.mapper;

import com.advertiser.domain.*;
import com.advertiser.service.dto.SpotInfoDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity SpotInfo and its DTO SpotInfoDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SpotInfoMapper {

    @Mapping(source = "campaign.id", target = "campaignId")
    SpotInfoDTO spotInfoToSpotInfoDTO(SpotInfo spotInfo);

    List<SpotInfoDTO> spotInfosToSpotInfoDTOs(List<SpotInfo> spotInfos);

    @Mapping(target = "spots", ignore = true)
    @Mapping(source = "campaignId", target = "campaign")
    SpotInfo spotInfoDTOToSpotInfo(SpotInfoDTO spotInfoDTO);

    List<SpotInfo> spotInfoDTOsToSpotInfos(List<SpotInfoDTO> spotInfoDTOs);

    default Campaign campaignFromId(Long id) {
        if (id == null) {
            return null;
        }
        Campaign campaign = new Campaign();
        campaign.setId(id);
        return campaign;
    }
}
