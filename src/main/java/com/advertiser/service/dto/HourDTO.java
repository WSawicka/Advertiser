package com.advertiser.service.dto;

import com.advertiser.domain.Campaign;
import com.advertiser.domain.Spot;
import com.advertiser.service.mapper.CampaignMapper;
import com.advertiser.service.mapper.SpotMapper;

import java.io.Serializable;
import java.util.*;


/**
 * A DTO for the Hour entity.
 */
public class HourDTO implements Serializable {

    private Long id;
    private Integer number;
    private Long dayId;
    private Set<SpotDTO> spots = new HashSet<>();

    public HourDTO(){}

    public HourDTO(Integer number){
        this.number = number;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Long getDayId() {
        return dayId;
    }

    public void setDayId(Long dayId) {
        this.dayId = dayId;
    }

    public Set<SpotDTO> getSpots() {
        return spots;
    }

    public void setSpots(Set<SpotDTO> spots) {
        this.spots = spots;
    }

    public void setSpotsDTO(Set<Spot> spots, SpotMapper spotMapper, CampaignMapper campaignMapper) {
        for(Spot spot : spots){
            SpotDTO spotDTO = spotMapper.spotToSpotDTO(spot);
            if (campaignMapper != null) {
                Campaign campaign = spot.getCampaign();
                spotDTO.setCampaignDTO(campaignMapper.campaignToCampaignDTO(campaign));
            }
            this.spots.add(spotDTO);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        HourDTO hourDTO = (HourDTO) o;

        if ( ! Objects.equals(id, hourDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "HourDTO{" +
            "id=" + id +
            ", number='" + number + "'" +
            '}';
    }
}
