package com.advertiser.service.dto;

import com.advertiser.domain.Campaign;
import com.advertiser.service.mapper.CampaignMapper;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the Spot entity.
 */
public class SpotDTO implements Serializable {

    private Long id;
    private ZonedDateTime dateTime;
    private Long hourId;
    private Long campaignId;
    private Long spotInfoId;
    private String spotName;
    private Integer spotNumber;

    private CampaignDTO campaignDTO;

    public SpotDTO(){}

    public SpotDTO(ZonedDateTime dt, Long hourId, CampaignDTO campaignDTO, Long spotInfoId, Integer spotNumber){
        this.dateTime = dt;
        this.hourId = hourId;
        this.campaignId = campaignDTO.getId();
        this.campaignDTO = campaignDTO;
        this.spotInfoId = spotInfoId;
        this.spotName = campaignDTO.getNameShort();
        this.spotNumber = spotNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public ZonedDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(ZonedDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Long getHourId() {
        return hourId;
    }

    public void setHourId(Long hourId) {
        this.hourId = hourId;
    }

    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public Long getSpotInfoId() {
        return spotInfoId;
    }

    public void setSpotInfoId(Long spotInfoId) {
        this.spotInfoId = spotInfoId;
    }

    public CampaignDTO getCampaignDTO() {
        return campaignDTO;
    }

    public void setCampaignDTO(CampaignDTO campaignDTO) {
        this.campaignDTO = campaignDTO;
    }

    public String getSpotName() {
        return spotName;
    }

    public void setSpotName(String spotName) {
        this.spotName = spotName;
    }

    public Integer getSpotNumber() {
        return spotNumber;
    }

    public void setSpotNumber(Integer spotNumber) {
        this.spotNumber = spotNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SpotDTO spotDTO = (SpotDTO) o;

        if ( ! Objects.equals(id, spotDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SpotDTO{" +
            "dateTime=" + dateTime +
            ", spotName='" + spotName +
            ", spotNumber=" + spotNumber +
            '}';
    }
}
