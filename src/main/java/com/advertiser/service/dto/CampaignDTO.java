package com.advertiser.service.dto;

import com.advertiser.domain.Spot;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the Campaign entity.
 */
public class CampaignDTO implements Serializable {

    private Long id;
    private String name;
    private String nameShort;
    private String product;
    private Integer spotAmount;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;

    private Long campaignStateId;
    private Long businessId;
    private Set<Spot> spots = new HashSet<>();

    private Set<PriceScheduleDTO> priceSchedules = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getNameShort() {
        return nameShort;
    }

    public void setNameShort(String nameShort) {
        this.nameShort = nameShort;
    }
    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }
    public Integer getSpotAmount() {
        return spotAmount;
    }

    public void setSpotAmount(Integer spotAmount) {
        this.spotAmount = spotAmount;
    }
    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }
    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public Long getCampaignStateId() {
        return campaignStateId;
    }

    public void setCampaignStateId(Long stateId) {
        this.campaignStateId = stateId;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public Set<PriceScheduleDTO> getPriceSchedules() {
        return priceSchedules;
    }

    public void setPriceSchedules(Set<PriceScheduleDTO> priceSchedules) {
        this.priceSchedules = priceSchedules;
    }

    public Set<Spot> getSpots() {
        return spots;
    }

    public void setSpots(Set<Spot> spots) {
        this.spots = spots;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CampaignDTO campaignDTO = (CampaignDTO) o;

        if ( ! Objects.equals(id, campaignDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CampaignDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", nameShort='" + nameShort + "'" +
            ", product='" + product + "'" +
            ", spotAmount='" + spotAmount + "'" +
            ", startDate='" + startDate + "'" +
            ", endDate='" + endDate + "'" +
            '}';
    }
}
